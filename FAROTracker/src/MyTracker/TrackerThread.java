/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.Color;
import smx.tracker.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
//import MyTracker.Register;

/**
 *
 * @author xss
 */
public class TrackerThread implements Runnable {
    public enum CURRENTEVENT {//当前机器状态的标志
        NONE_EVENT,//无
        CONNECT_EVENT,//连接
        DISCONNECT_EVENT,//断开
        STARTUPCHECK_EVENT,//启动检查start up check
        EXECMDSEQ_EVENT,//初始化
        TRACKERPANEL_EVENT,//Tracker Pad
        COMPIT_EVENT,//CompIT
        SINGLEREAD_EVENT,//手动读取
        CONTINUEREAD_EVENT,//连续读取
        FOREGROUND_EVENT,//前台模式
        BACKGROUND_EVENT,//后台模式
        FORE_BACKGROUND_EVENT,//
        STARTCALIBRATE_EVENT,//开始标定/位姿/摆动偏差检测
        STARTCALIBRATE_STEP1_EVENT,//
        STARTCALIBRATE_STEP2_EVENT,//结束标定
        STARTCALIBRATE_POSE_EVENT,//位姿检测完成
        STARTCALIBRATE_SWINGERROR_EVENT,//摆动偏差检测完成
        STARTBACKGROUND_CAL_EVENT,//开始轨迹测试
        STARTBACKGROUND_STEP1_EVENT,//
        STARTBACKGROUND_STEP2_EVENT,//结束轨迹测试

        
        
        START_SEARCH_TARGET_EVENT, //Search target搜寻目标
        MOVETO_TARGET_EVENT, //Move to the target position移动到目标位置
    }
    
    Thread t;
    MyTracker frame;
    CheckPanel checkPanel;
    CheckPanelScara checkPanelScara;
    PosePanel posePanel;
    SwingErrorPanel swingerrorPanel;
    CoordTransform coordTransPanel;
    PosePanelScara posePanelScara;
    TrajectoryPanel trajPanel;
//    CalibrationResult calResult;
//    private Timer timer;
    
    long startTime;
    long endTime;
    double TotalTime[] = new double[3];
    
    
    private Timer timerBknd;
    public static Tracker tracker;
    public static Calibration cal;
    public static Register register;
    private double[] ratio_cal = new double[6];
    private double[] ratio_Set = new double[6];
    private double[] zero_cal = new double[6];
//    private double[] alpha_cal = new double[6];
    private double[] tool_cal = new double[6];
    private double[] diff_post = new double[50];
    private double[] diff_prev = new double[50];
    private double[] diff_post_scara = new double[30];
    private double[] diff_prev_scara = new double[30];
    private double[] diff_traj = new double[50];
    private double[] param_cal = new double[6];
    private int[] dir = new int[6];
    private double[] points = new double[450];
    private double[] outputs = new double[150];
    private boolean[] couple_en = new boolean[3]; //是否耦合
    private double[] couple_cal = new double[3]; //耦合比
    private int cycleTime = 1;
    private int currPoint = 0;//标记当前正在执行的行数
    private int number = 1; //标记手动测量时重复读取了多少次
    private int times = 0; //标记后台测量时重复运动了多少次
    private int pointCnt = 0;
    private boolean moving = false;
    private boolean auto2manual = false;
    private double[][] repeatDiff = new double[150][3];//位姿测试数据存储
    private double[][] repeatSE = new double [60][3];//摆动偏差测试数据存储
    private double[][] Result_DataSE = new double [3][9];//摆动偏差测试结果数据存储
    private int stopJudgement = 0; //运动停止判别
    private double[] rotation = new double[5]; //轨迹点位显示变换
    private double[] base = new double[16]; //坐标变换
    private boolean base_gen = false; //是否进行了坐标变换
    private double[][] dataBuf = new double[5][3]; //临时存储连续读取点位用
    List curveData = new ArrayList<>();
    
    public CURRENTEVENT currentEvent = CURRENTEVENT.NONE_EVENT; //State machine 
    private CURRENTPROCESS currentProcess = CURRENTPROCESS.NONE_PROCESS; //Calculate process 
    
    public TrackerThread(MyTracker frame) {
        t = new Thread(this,"Tracker Thread");
        System.out.println("Tracker Thread starting.");
        t.start();
        this.frame = frame;
        this.checkPanel = frame.getCheckPanel();
        this.coordTransPanel = this.checkPanel.getCoordTransPanel();
        this.checkPanelScara = frame.getCheckPanelScara();
        this.posePanel = frame.getPosePanel();
        this.posePanelScara = frame.getPosePanelScara();
        this.trajPanel = frame.getTrajPanel();
        this.swingerrorPanel = frame.getSwingErrorPanel();
        cal = new Calibration();
        register = new Register(cal);
    }
    
    @Override
    public void run() {
        while(true) {            
            switch(currentEvent) {
                case CONNECT_EVENT:
                    ClearCurrEvent();
                    try{
                        tracker = new Tracker("TrackerCypher");
//                        tracker=new Tracker("TrackerKeystoneSim");
//                        tracker = new Tracker("TrackerKeystone");
                        tracker.setBlocking(true);
                        
//                        tracker.connect("VantageS-Tracker-WiFi_7235","user","");
                        tracker.connect("128.128.128.100","user","");
                        if(tracker.connected()) {
                            frame.SetConnectStatusString("已连接");
                            frame.SetAlarmString("");
                        }
                        tracker.setBlocking(false);
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                        frame.SetAlarmString("连接异常！");
                    }
                    break;
                case DISCONNECT_EVENT:
                    ClearCurrEvent();
                    if(tracker == null) {
                        break;
                    }
                    try{
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("已断开!");
                            break;
                        }
                        StopContinueMeasurement();
                        tracker.setBlocking(true);
                        tracker.disconnect();
                        tracker.setBlocking(false);
                        frame.SetConnectStatusString("已断开");
                    }
                    catch(TrackerException e) {
                        frame.SetAlarmString("断开连接时发生异常！");
                    }
                    break;
                case STARTUPCHECK_EVENT:
                    ClearCurrEvent();
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        break;
                    }
                    try{
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("请先连接激光跟踪仪!");
                            break;
                        }
                        tracker.setBlocking(true);
                        tracker.startApplicationFrame("StartupChecks","");
                        tracker.setBlocking(false);
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
                    break;
                case EXECMDSEQ_EVENT:
                    ClearCurrEvent();
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        break;
                    }
                    try{
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("请先连接激光跟踪仪!");
                            break;
                        }
                        tracker.setBlocking(true);
                        boolean state = tracker.initialized();
                        if(state == false)
                        {
                            tracker.initialize();
                        }
                        frame.SetCmdSeqString("初始化完成");

                        state = tracker.motorsOn();
                        if(state == false)
                        {
                            tracker.changeMotorState(true);
                        }
                        frame.SetCmdSeqString("电机已使能");

                        state = tracker.trackingOn();
                        if(state == false)
                        {
                            tracker.changeTrackingState(true);
                        }
                        frame.SetCmdSeqString("跟踪已使能");

                        state = tracker.targetLocationValid();
                        if(state == false)
                        {
                            frame.SetAlarmString("距离不当");
                        }

                        state = tracker.targetPresent();
                        if(state == false)
                        {
                            frame.SetAlarmString("靶球不在范围内");
                        }

                        tracker.home(true);
                        frame.SetCmdSeqString("激光跟踪仪返航完成");

                        tracker.setBlocking(false);
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
                    break;
                case TRACKERPANEL_EVENT:
                    ClearCurrEvent();
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        break;
                    }
                    try{
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("请先连接激光跟踪仪!");
                            break;
                        }
                        tracker.setBlocking(true);
                        tracker.startApplicationFrame("TrackerPad","");
                        tracker.setBlocking(false);
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
                    break;
                case COMPIT_EVENT:
                    ClearCurrEvent();
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        break;
                    }
                    try{
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("请先连接激光跟踪仪!");
                            break;
                        }
                        tracker.setBlocking(true);
                        tracker.startApplicationFrame("CompIT","");
                        tracker.setBlocking(false);
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
                    break;
                case FOREGROUND_EVENT:
                    break;
                case SINGLEREAD_EVENT:
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                        break;
                    }
                    if(auto2manual) {
                        if(GetCurrProcess() == CURRENTPROCESS.TURN_MANUAL_PROCESS) {
                            auto2manual = false;
                            ClearCurrEvent();
                            ProcessChanged(CURRENTPROCESS.CHECK_PAGE_SINGLEREAD_PROCESS);
                            SingleMeasurement();
                            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                        }
                    }
                    else {
                        ClearCurrEvent();
                        SingleMeasurement();
                        ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                    }
                    break;
                case BACKGROUND_EVENT:
                    ClearCurrEvent();
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        break;
                    }
                    try{
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("请先连接激光跟踪仪!");
                            break;
                        }
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
                    break;
                case FORE_BACKGROUND_EVENT:
                    ClearCurrEvent();
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        break;
                    }
                    try{
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("请先连接激光跟踪仪!");
                            break;
                        }
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
                    break;
                case STARTCALIBRATE_EVENT:
                    ClearCurrEvent();
                    currPoint = 0;
                    times = 0;
                    pointCnt = 0;
                    moving = false;
                    if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS) {
                        checkPanel.SetSelectionRow(currPoint);
                        frame.SetCmdSeqString("开始标定，请稍候···");
                    }
                    if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS) {
                        posePanel.SetSelectionRow(currPoint);
                        frame.SetCmdSeqString("开始位姿检测，请稍候···");
                    }
                    
                    if(GetCurrProcess() == CURRENTPROCESS.SWINGERROR_PAGE_CONTINUE_PROCESS){
                        swingerrorPanel.SetSelectionRow(currPoint);
                        frame.SetCmdSeqString("开始摆动偏差检测，请稍等···");
                    }

                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                        break;
                    }
                    try {
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("激光跟踪仪未连接!");
                            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                            break;
                        }
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
//                    checkPanel.SetMainTableEnable(false);
                    StartContinueMeasurement();
                    SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_STEP1_EVENT);
                    break;
                case STARTCALIBRATE_STEP1_EVENT:
                    ClearCurrEvent();
                    if(frame.GetTypeSel() == TYPESEL.SIXDOF_ROBOT) {
                        ContinueMeasurement();
                    }
                    else if(frame.GetTypeSel() == TYPESEL.SCARA_ROBOT) {
                        ContinueMeasurementScara();
                    }
                    break;
                case STARTCALIBRATE_STEP2_EVENT:
                    ClearCurrEvent();
                    if(frame.GetTypeSel() == TYPESEL.SIXDOF_ROBOT) {
                        CalculationProgress(checkPanel);
                    }
                    else if(frame.GetTypeSel() == TYPESEL.SCARA_ROBOT) {
                        CalculationProgress(checkPanelScara);
                    }
                    frame.SetCmdSeqString("标定完成");
                    frame.saveCaledBuffer();
                    break;
                case STARTCALIBRATE_POSE_EVENT:
                    ClearCurrEvent();
                    frame.SetCmdSeqString("位姿检测完成");
                    ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                    break;
                case STARTCALIBRATE_SWINGERROR_EVENT:
                    ClearCurrEvent(); 
                    frame.SetCmdSeqString("摆动偏差检测完成");
                    swingerrorPanel.SetSpeed_TextField.setEditable(true);
                    swingerrorPanel.SetSpeed_TextField.setBackground(Color.white);
                    ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                    break;
                case STARTBACKGROUND_CAL_EVENT:
                    ClearCurrEvent();
                    currPoint = 0;
                    times = 0;
                    pointCnt = 0;
                    moving = false;
                    if(tracker == null) {
                        frame.SetAlarmString("请先连接激光跟踪仪!");
                        ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                        break;
                    }
                    try {
                        if(tracker.connected() == false) {
                            frame.SetAlarmString("激光跟踪仪未连接!");
                            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                            break;
                        }
                    }
                    catch(TrackerException e) {
                        System.out.println(e);
                    }
                    frame.SetCmdSeqString("开始轨迹测试");
                    StartContinueMeasurement();
                    SetCurrEvent(CURRENTEVENT.STARTBACKGROUND_STEP1_EVENT);
                    break;
                case STARTBACKGROUND_STEP1_EVENT:
                    ClearCurrEvent();
                    ContinueBkndMeasurement();
                    break;
                case STARTBACKGROUND_STEP2_EVENT:
                    ClearCurrEvent();
                    Object[] trajVal = trajPanel.GetPointsValue();
                    double [] trajPoints = new double[50*3];
                    for(int i = 0;i < trajVal.length;++i) {
                        trajPoints[i] = Double.parseDouble(trajVal[i].toString());
                    }
                    cal.DLL_CheckLineError(trajPoints, 48, diff_traj, 2);
                    trajPanel.DispResult(diff_traj);
                    frame.SetCmdSeqString("轨迹测试完成");
                    ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
                    break;
                case START_SEARCH_TARGET_EVENT:
                    break;
                case MOVETO_TARGET_EVENT:
                    break;
                default:break;
            }
        }
    }
    
    private void SetCurrEvent(CURRENTEVENT e) {
        currentEvent = e;
    }
    private CURRENTEVENT GetCurrEvent() {
        return currentEvent;
    }
    private void ClearCurrEvent() {
        currentEvent = CURRENTEVENT.NONE_EVENT;
    }
    private void SingleMeasurement() {
        int nEventRate=1;
        MeasurePointData mData;
        try{
            if(tracker.connected() == false) {
                frame.SetAlarmString("请先连接激光跟踪仪!");
                return;
            }
            NullStartTrigger startTrigger = new NullStartTrigger();
            IntervalTrigger continueTrigger = new IntervalTrigger(1.0);
            AverageFilter filter = new AverageFilter();
            MeasureCfg cfg = new MeasureCfg(1000,filter,startTrigger,continueTrigger);

            tracker.setBlocking(true);
            tracker.setMeasureEventRate(nEventRate);
            tracker.startMeasurePoint(cfg);
            if(nEventRate == 1) {
                mData = tracker.readMeasurePointData();
                if(mData != null) {//极坐标转换直角坐标
                    String az = String.valueOf(mData.azimuth()) + ": "; 
                    String ze = String.valueOf(mData.zenith()) + ": ";
                    String dist = String.valueOf(mData.distance());
                    double dist_VecZ = mData.distance() * Math.cos(mData.zenith());
                    double dist_VecXY = mData.distance() * Math.sin(mData.zenith());
                    double dist_VecX = dist_VecXY * Math.cos(mData.azimuth());
                    double dist_VecY = dist_VecXY * Math.sin(mData.azimuth());
                    String str_VecX = String.valueOf(dist_VecX) + ": ";
                    String str_VecY = String.valueOf(dist_VecY) + ": ";
                    String str_VecZ = String.valueOf(dist_VecZ);
                    System.out.println(az+ze+dist);
                    System.out.println(str_VecX + str_VecY + str_VecZ);
                    Object[] values = new Object[3];
                    values[0] = dist_VecX*1000;
                    values[1] = dist_VecY*1000;
                    values[2] = dist_VecZ*1000;
                    SingleMeasurement_Process(values);
                }
            }
            else if(nEventRate == 10) {
                MeasurePointData[] dataArray = tracker.readMeasurePointData(nEventRate);
                int numElements = dataArray.length;
                double[] az = new double[numElements];
                double[] ze = new double[numElements];
                double[] dist = new double[numElements];
                int[] status = new int[numElements];

                if(numElements>0) {
                    for(int i = 0;i < numElements;++i) {
                        mData = dataArray[i];
                        if(mData != null) {
                            az[i] = mData.azimuth();
                            ze[i] = mData.zenith();
                            dist[i] = mData.distance();
                            status[i] = mData.status();
                            String str = String.valueOf(az[i]) + ": " + String.valueOf(ze[i]) + ": " + String.valueOf(dist[i]) + ": " + String.valueOf(status[i]);
                            System.out.println(str);
                        }
                    }
                }
            }
            tracker.stopMeasurePoint();
            tracker.setBlocking(false);
        }
        catch(TrackerException e) {
            System.out.println(e);
        }
    }
    
    private void SingleMeasurement_Process(Object[] values) {
        if(frame.GetTypeSel() == TYPESEL.SIXDOF_ROBOT) {//六轴机器人
            if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_SINGLEREAD_PROCESS) {
                checkPanel.SetTrackerValue(values);
                currPoint = checkPanel.GetSelectionRow();//获取当前行数
                if(currPoint < 49) {
                    currPoint = checkPanel.GetSelectionRow() + 1;//行数加一
                }
                checkPanel.SetSelectionRow(currPoint);
            }
            if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_SINGLEREAD_PROCESS) {
                posePanel.SetTrackerValue(values);
                currPoint = posePanel.GetSelectionRow();
                if(currPoint < 4) {
                    currPoint = posePanel.GetSelectionRow() + 1;
                }
                posePanel.SetSelectionRow(currPoint);
            }
            if(GetCurrProcess() == CURRENTPROCESS.SWINGERROR_PAGE_SINGLEREAD_PROCESS) {
                if(swingerrorPanel != null) {
                    swingerrorPanel.SetTrackerValue(values);
                }
                currPoint = swingerrorPanel.GetSelectionRow();
                if(currPoint < 19) {
                   
                        currPoint = swingerrorPanel.GetSelectionRow() + 1;
                        swingerrorPanel.SetSelectionRow(currPoint);
                }
                else{
                    if((number < 3)){
                        swingerrorPanel.SetSelectionRow(0);
                        swingerrorPanel.Repeat_Data_Loops(++number);
                    }
                }
                System.out.println("currPoint:" + currPoint);   
            }
            if(GetCurrProcess() == CURRENTPROCESS.COORDTRANS_SINGLEREAD_PROCESS) {
                coordTransPanel.SetTrackerValue(values);
            }
        }
        else if(frame.GetTypeSel() == TYPESEL.SCARA_ROBOT) {//scara机器人
            if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_SINGLEREAD_PROCESS) {
                checkPanelScara.SetTrackerValue(values);
                currPoint = checkPanelScara.GetSelectionRow();
                if(currPoint < 29) {
                    currPoint = checkPanelScara.GetSelectionRow() + 1;
                }
                checkPanelScara.SetSelectionRow(currPoint);
            }
        }
    }
    
    private void StartContinueMeasurement() {
        int nEventRate = 1;
        MeasurePointData mData;
        try {
            if(tracker.connected() == false) {
                frame.SetAlarmString("激光仪未连接!");
                return;
            }
            NullStartTrigger startTrigger = new NullStartTrigger();
            AverageFilter filter = new AverageFilter();
            if(GetCurrProcess() == CURRENTPROCESS.TRAJECTORY_PAGE_CONTINUE_PROCESS) {
                IntervalTrigger continueTrigger = new IntervalTrigger(0.005);
                MeasureCfg cfg = new MeasureCfg(5,filter,startTrigger,continueTrigger);

                tracker.setBkndMeasureEventRate(nEventRate);
                tracker.setBkndMeasureBlocking(true);
                tracker.startBkndMeasurePoint(cfg);
            }
            else {
                IntervalTrigger continueTrigger = new IntervalTrigger(0.1);
                MeasureCfg cfg = new MeasureCfg(100,filter,startTrigger,continueTrigger);

                tracker.setBlocking(true);
                tracker.setMeasureEventRate(nEventRate);
                tracker.startMeasurePoint(cfg);
            }
            
            if(nEventRate == 1) {
                if(GetCurrProcess() == CURRENTPROCESS.TRAJECTORY_PAGE_CONTINUE_PROCESS) {
                    mData = tracker.readBkndMeasurePointData();
                }
                else {
                    mData = tracker.readMeasurePointData();
                }
                
                if(mData != null) {
                    double dist_VecZ = mData.distance() * Math.cos(mData.zenith());
                    double dist_VecXY = mData.distance() * Math.sin(mData.zenith());
                    double dist_VecX = dist_VecXY * Math.cos(mData.azimuth());
                    double dist_VecY = dist_VecXY * Math.sin(mData.azimuth());
                    Object[] values = new Object[3];
                    values[0] = dist_VecX * 1000;
                    values[1] = dist_VecY * 1000;
                    values[2] = dist_VecZ * 1000;
                    StartContinueMeasurement_Process(values);
                }
            }
        }
        catch(TrackerException e) {
            System.out.println(e);
        }
    }
    
    private void StartContinueMeasurement_Process(Object[] values) {
        if(frame.GetTypeSel() == TYPESEL.SIXDOF_ROBOT) {
            if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS) {
                checkPanel.SetTrackerValue(values);
            }
            if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS) {
                posePanel.SetTrackerValue(values);
                posePanel.AddPointsDisplay(values);

                repeatDiff[times*5 + currPoint][0] = Double.parseDouble(String.valueOf(values[0]));
                repeatDiff[times*5 + currPoint][1] = Double.parseDouble(String.valueOf(values[1]));
                repeatDiff[times*5 + currPoint][2] = Double.parseDouble(String.valueOf(values[2]));

                System.out.println("repeatDiff:" + repeatDiff[times*10 + currPoint][0] + ":" +
                                        repeatDiff[times*10 + currPoint][1] + ":" +
                                        repeatDiff[times*10 + currPoint][2]);
            }
            if(GetCurrProcess() == CURRENTPROCESS.TRAJECTORY_PAGE_CONTINUE_PROCESS) {
                trajPanel.SetPointsValue(times, values);
            }
            
            if(GetCurrProcess() == CURRENTPROCESS.SWINGERROR_PAGE_CONTINUE_PROCESS) {
                swingerrorPanel.SetTrackerValue(values);
                repeatSE[times*20 + currPoint][0] = Double.parseDouble(String.valueOf(values[0]));
                repeatSE[times*20 + currPoint][1] = Double.parseDouble(String.valueOf(values[1]));
                repeatSE[times*20 + currPoint][2] = Double.parseDouble(String.valueOf(values[2]));

                System.out.println("repeatSE:" + repeatSE[times*20 + currPoint][0] + ":" +
                                        repeatDiff[times*20 + currPoint][1] + ":" +
                                        repeatDiff[times*20 + currPoint][2]);
            }
            
        }
        else if(frame.GetTypeSel() == TYPESEL.SCARA_ROBOT) {
            if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS) {
                checkPanelScara.SetTrackerValue(values);
            }
        }
        ++currPoint;
    }
    
    private void ContinueMeasurement() {
        stopJudgement = 0;

        while(true) {
            try {
                if(tracker.connected() == false) {
                    frame.SetAlarmString("激光仪未连接!");
                    return;
                }
                int nEventRate = 1;
                MeasurePointData mData;
                if(nEventRate == 1) {
                    mData = tracker.readMeasurePointData();
                    if(mData != null) {
                        double dist_VecZ = mData.distance() * Math.cos(mData.zenith());
                        double dist_VecXY = mData.distance() * Math.sin(mData.zenith());
                        double dist_VecX = dist_VecXY * Math.cos(mData.azimuth());
                        double dist_VecY = dist_VecXY * Math.sin(mData.azimuth());

                        Object[] values = new Object[3];
                        values[0] = dist_VecX * 1000;
                        values[1] = dist_VecY * 1000;
                        values[2] = dist_VecZ * 1000;
                        int ret=ContinueMeasurement_Process(values);
                        if(ret == 1) 
                            break;
                    }
                }
            }
            catch(TrackerException e) {
                System.out.println(e);
            }
        }
    }
    
    private int ContinueMeasurement_Process(Object[] v) {
        try {
            Thread.sleep(1); //如果没有sleep那么其他线程更改了CURRENTPROCESS，这里的判断也不会生效
            CURRENTPROCESS ret = ProgressManager();
            if(ret == CURRENTPROCESS.TURN_MANUAL_PROCESS) return 1;
            if(ret == CURRENTPROCESS.POSE_PAGE_BREAK_AUTO_PROCESS) return 1;
            if(ret == CURRENTPROCESS.SWINGERROR_PAGE_BREAK_AUTO_PROCESS)return 1;
            if(ret == CURRENTPROCESS.DISCONNECT_PROCESS) return 1;
            if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS) {
                if(currPoint != checkPanel.GetSelectionRow()) {
                    checkPanel.SetSelectionRow(currPoint);
                }
            }
            if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS) {
                if(currPoint != posePanel.GetSelectionRow()) {
                    posePanel.SetSelectionRow(currPoint);
                }
            }
            if(GetCurrProcess() == CURRENTPROCESS.SWINGERROR_PAGE_CONTINUE_PROCESS) {
                if(currPoint != swingerrorPanel.GetSelectionRow()) {
                    swingerrorPanel.SetSelectionRow(currPoint);
                }
            }
        }
        catch(InterruptedException e) {
            System.out.println(e);
        }
        
        dataBuf[pointCnt][0] = Double.parseDouble(String.valueOf(v[0]));
        dataBuf[pointCnt][1] = Double.parseDouble(String.valueOf(v[1]));
        dataBuf[pointCnt][2] = Double.parseDouble(String.valueOf(v[2]));
        ++pointCnt;
        if(pointCnt >= dataBuf.length) {
            pointCnt = 0;
            double temp_X = 0.0,temp_Y = 0.0,temp_Z = 0.0;
            for(int i = 0;i < dataBuf.length;++i) {
                temp_X += dataBuf[i][0];
                temp_Y += dataBuf[i][1];
                temp_Z += dataBuf[i][2];
            }
            double average_X = temp_X / dataBuf.length;
            double average_Y = temp_Y / dataBuf.length;
            double average_Z = temp_Z / dataBuf.length;
            double length;
            double average_length = 0.0;
            for(int i = 0;i < dataBuf.length;++i) {
                length = Math.pow(dataBuf[i][0]-average_X, 2) + Math.pow(dataBuf[i][1]-average_Y, 2) +
                        Math.pow(dataBuf[i][2]-average_Z, 2);
                average_length += length;                                                  
            }
            average_length = Math.sqrt(average_length/dataBuf.length);

            if(average_length <= 0.1) {
                ++stopJudgement;
                if(stopJudgement >= 2) {
                    stopJudgement = 0;
                }
                else {
                    return 0;//continue
                }
                if(moving == true) {
                    Object[] values = new Object[3];
                    values[0] = dataBuf[dataBuf.length-1][0];//average_X;                                           ???????
                    values[1] = dataBuf[dataBuf.length-1][1];//average_Y;                                          
                    values[2] = dataBuf[dataBuf.length-1][2];//average_Z;                                           
                    ++currPoint;
                    if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS) {
                        checkPanel.SetTrackerValue(values);
                        if(currPoint >= 50) {
                            currPoint = 0;
                            StopContinueMeasurement();
                            return 1;
                        }
                    }
                    if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS) {
                        posePanel.SetTrackerValue(values);
                        posePanel.AddPointsDisplay(values);
                        posePanel.SetSelectionRow_Result(times);
                        repeatDiff[times*5 + currPoint-1][0] = dataBuf[dataBuf.length-1][0];                        
                        repeatDiff[times*5 + currPoint-1][1] = dataBuf[dataBuf.length-1][1];
                        repeatDiff[times*5 + currPoint-1][2] = dataBuf[dataBuf.length-1][2];
                        System.out.println("dataBuf:" + dataBuf[dataBuf.length-1][0] + ":" +
                                dataBuf[dataBuf.length-1][1] + ":" +
                                dataBuf[dataBuf.length-1][2]);
                        System.out.println("repeatDiff:" + repeatDiff[times*5 + currPoint-1][0] + ":" +
                                repeatDiff[times*5 + currPoint-1][1] + ":" +
                                repeatDiff[times*5 + currPoint-1][2]);
                        if(currPoint >= 5) {
                            currPoint = 0;

                            if(frame.GetTypeSel() == TYPESEL.SIXDOF_ROBOT) {
                                PoseCheckProgress(posePanel);
                            }
                            else if(frame.GetTypeSel() == TYPESEL.SCARA_ROBOT) {
                                PoseCheckProgress(posePanelScara);
                            }
                            ++times;
                        }
                        if(times >= 30) {
                            times = 0;
                            StopContinueMeasurement();

                            //位置重复性RPl = lbar + 3St
                            //lbar = sum(lj)/n,其中j=1~n
                            //lj = sqrt((xj-xbar)^2+(yj-ybar)^2+(zj-zbar)^2)
                            //xbar,ybar,zbar为坐标的平均值
                            //St=sqrt(sum(lj-lbar)^2/(n-1))
                            double[] RPl = new double[5];
                            double[] St = new double[5];
                            double[] lbar = new double[5];
                            double[] xbar = new double[30];
                            double[] ybar = new double[30];
                            double[] zbar = new double[30];
                            double[][] lj = new double[5][30];

                            //第i个点的第j次实验
                            for(int i = 0;i < 5;++i) {
                                for(int j = 0;j < 30;++j) {
                                    xbar[i] += repeatDiff[j*5 + i][0];
                                    ybar[i] += repeatDiff[j*5 + i][1];
                                    zbar[i] += repeatDiff[j*5 + i][2];
                                }
                            }
                            //求每个点的重心
                            for(int i = 0;i < 5;++i) {
                                xbar[i] /= 30;
                                ybar[i] /= 30;
                                zbar[i] /= 30;
                            }
                            //第i个点的第j次实验
                            for(int i = 0;i < 5;++i) {
                                for(int j = 0;j < 30;++j) {
                                    lj[i][j] = Math.pow(repeatDiff[j*5 + i][0]-xbar[i], 2) + 
                                            Math.pow(repeatDiff[j*5 + i][1]-ybar[i], 2) + 
                                            Math.pow(repeatDiff[j*5 + i][2]-zbar[i], 2);
                                    lj[i][j] = Math.sqrt(lj[i][j]);
                                    lbar[i] += lj[i][j];
                                }
                                lbar[i] /= 30;
                            }

                            for(int i = 0;i < 5;++i) {
                                for(int j = 0;j < 30;++j) {
                                    St[i] += Math.pow(lj[i][j]-lbar[i], 2);
                                }
                            }
                            for(int i = 0;i < 5;++i) {
                                St[i] /= 30-1;
                                St[i] = Math.sqrt(St[i]);
                            }
                            double aveRPl = 0.0;
                            for(int i = 0;i < 5;++i) {
                                RPl[i] = lbar[i] + 3*St[i];
                                aveRPl += RPl[i];
                            }

                            aveRPl /= 5;
                            posePanel.RepeatDiffDisp(aveRPl);
                            return 1;
                        }
                    }   
                    //摆动偏差检测处理
                    if(GetCurrProcess() == CURRENTPROCESS.SWINGERROR_PAGE_CONTINUE_PROCESS) {
                        swingerrorPanel.SetTrackerValue(values);
                        if(currPoint == 1){
                            startTime = System.currentTimeMillis();
                        }
                        repeatSE[times*20 + currPoint-1][0] = dataBuf[dataBuf.length-1][0];                        
                        repeatSE[times*20 + currPoint-1][1] = dataBuf[dataBuf.length-1][1];
                        repeatSE[times*20 + currPoint-1][2] = dataBuf[dataBuf.length-1][2];
                        System.out.println("dataBuf:" + dataBuf[dataBuf.length-1][0] + ":" +
                                dataBuf[dataBuf.length-1][1] + ":" +
                                dataBuf[dataBuf.length-1][2]);
                        System.out.println("repeatSE:" + repeatSE[times*20 + currPoint-1][0] + ":" +
                                repeatSE[times*20 + currPoint-1][1] + ":" +
                                repeatSE[times*20 + currPoint-1][2]);
                        if(currPoint >= 20) {
                            currPoint = 0;
                            endTime = System.currentTimeMillis();
                            TotalTime[times] = endTime - startTime;
                            ++times;
                            if(times < 3){
                                 swingerrorPanel.Repeat_Data_Loops(times + 1);
                            }         
                        }
                        if(times >= 3){
                            times = 0;
                            StopContinueMeasurement();
                            
                            //摆幅误差WS计算——WS = ( Sa - Sc ) / Sc * 100%
                            double data_WS;
                            
                            //指令摆幅Sc
                            double Sc = 0.0;
                            double[] Previous_distance = new double[21];
                            double[] Next_distance = new double[21];
                            double[] diff_distance = new double[30];
                            double[] dist_ave = new double[3];
                            int row = swingerrorPanel.GetPointsTableRow();
                            int column = swingerrorPanel.GetPointsTableColumn();
                            System.out.println("row:" + row);
                            System.out.println("column:" + column);
                            double[][] directive;
                            directive = swingerrorPanel.GetTableValue(); 
                            for(int i = 0; i < row; i++){
                                for(int j = 0; j < column - 1; j++){
                                    System.out.println("directive[" + i +"]" + "[" + j + "]:" + directive[i][j]);
                                }
                            }
                            for(int i = 0; i < 3; i++){
                                int k = 0;
                                for(int j = 0; j < row; j++){
                                    if(j % 2 == 0){
                                        Previous_distance[k] = Math.sqrt(Math.pow(directive[j][0],2) + Math.pow(directive[j][2],2));
                                        System.out.println("Previous_distance:" + Previous_distance[k]);
                                        ++k;  
                                    }
                                    if(j % 2 !=0){
                                        Next_distance[k] = Math.sqrt(Math.pow(directive[j][0],2) + Math.pow(directive[j][2],2));
                                        System.out.println("Next_distance" +  Next_distance[k]);
                                        ++k;                                    
                                    }                   
                                }
                                for(int h = 0; h < 10; h++){
                                    diff_distance[i*10+h] = Math.abs(Previous_distance[h*2] - Next_distance[h*2+1]);
                                    dist_ave[i] += diff_distance[i*10+h];
                                }
                                dist_ave[i] /= 10;
                                Sc += dist_ave[i];
                                Result_DataSE[i][0] = dist_ave[i];
                            }
                            Sc /= 3;
                            
                             //测到的摆幅平均值Sa
                            double Sa = 0.0;
                            double[] Front_distance = new double[21];
                            double[] After_distance = new double[21];
                            double[] distance_diff = new double[30];
                            double[] ave_dist = new double[3];
                            for(int i = 0; i < 3; i++){
                                int g = 0;
                                for(int j = 0; j < row; j++){
                                    if(j % 2 == 0){
                                        Front_distance[g] = Math.sqrt(Math.pow(repeatSE[i*20+j][0], 2) + Math.pow(repeatSE[i*20+j][2],2));
                                        System.out.println("Front_distance:" + Front_distance[g]);
                                        ++g;      
                                    }
                                    if(j % 2 !=0){
                                        After_distance[g] = Math.sqrt(Math.pow(repeatSE[i*20+j][0], 2) + Math.pow(repeatSE[i*20+j][2],2));
                                        System.out.println("After_distance:" + After_distance[g]);
                                        ++g;   
                                    }                   
                                }
                                for(int h = 0; h < 10; h++){
                                    distance_diff[i*10+h] = Math.abs(Front_distance[h*2] - After_distance[h*2+1]);
                                    ave_dist[i] += distance_diff[i*10+h];
                                }
                                ave_dist[i] /= 10;
                                System.out.println(" ave_dist[i]:" +  ave_dist[i]);
                                Sa += ave_dist[i];
                                Result_DataSE[i][1] = ave_dist[i];
                                System.out.println("实际摆幅Result_DataSE[i][1]:" + Result_DataSE[i][1]);
                            }
                            Sa /= 3;
                            data_WS = (Sa - Sc) / Sc * 100;   
                            swingerrorPanel.Repeat_Data_WS(data_WS);
                            
                            
                            //摆频误差WF计算——WF = ( Fa - Fc ) / Fc * 100%
                            double data_WF;
                            //WDc = 指令摆动距离
                            double[][] data_WDc = new double[2][3];
                            double distance_WDc; 

                            data_WDc[0][1] = 0 - directive[0][1]; 
                            data_WDc[1][1] = 0 - directive[19][1]; 
                            distance_WDc = Math.sqrt(Math.pow(data_WDc[0][1] - data_WDc[1][1], 2));     
                            for(int i = 0; i < 3; i++){
                                Result_DataSE[i][2] =  distance_WDc;
                            }
                            //WDa = 平均的实际摆动距离
                            double[][] data_WDa = new double[6][3];
                            double distance_WDa = 0.0; 
                            for(int i = 0; i < 3; i++){
                                data_WDa[i][1] = 0 - repeatSE[i][1];
                                data_WDa[i+3][1] = 0 - repeatSE[i*20+19][1]; 
                                distance_WDa += Math.sqrt(Math.pow(data_WDa[i][1] - data_WDa[i+3][1], 2)); 
                                Result_DataSE[i][3] = Math.sqrt(Math.pow(data_WDa[i][1] - data_WDa[i+3][1], 2)); 
                            }
                            distance_WDa /= 3; 
                            //WVc = 指令摆动速度
                            double speed_WVc = swingerrorPanel.GetSpeedValue();  
                            for(int i = 0; i < 3; i++){
                                Result_DataSE[i][4] = speed_WVc;
                            }
                            //WVa = 实际摆动速度   
                            double speed_WVa = 0.0;
                            for(int i = 0; i < 3; i++){
                                Result_DataSE[i][5] = Result_DataSE[i][3] / TotalTime[i] / 1000;
                                speed_WVa += Result_DataSE[i][5];
                                System.out.println("实际摆动时间TotalTime[i]：" + TotalTime[i]);
                                System.out.println("实际摆动速度Result_DataSE[i][5]:" + Result_DataSE[i][5]);
                            }
                            speed_WVa /= 3;
                            System.out.println("speed_WVa:" + speed_WVa);
                            //Fc = 10 * [WVc / (10 -  WDc)]
                            double Fc = 10 * (speed_WVc / (10 -  distance_WDc));
                            for(int i = 0; i < 3; i++){
                                Result_DataSE[i][6] = Fc;
                            }
                            //Fa = 10 * [WVa / (10 -  WDa)]
                            double Fa = 10 * (speed_WVa / (10 -  distance_WDa));
                            for(int i = 0; i < 3; i++){
                                Result_DataSE[i][7] = Fa;       
                            }
                            
                            
                            data_WF = (Fa - Fc) / Fc * 100;
                            swingerrorPanel.Repeat_Data_WF(data_WF);
                            System.out.println("输出检测结果");
                            swingerrorPanel.SetResultTableValue(Result_DataSE);
                            System.out.println("输出检测结果完毕");
                            
                            return 1;
                        }
                    }       
                    moving = false;
                }
            }
            else {
                moving = true;
            }
        }
        return 0;
    }
    
    private void ContinueMeasurementScara() {
        while(true) {
            try {
                if(tracker.connected() == false) {
                    frame.SetAlarmString("激光仪未连接!");
                    return;
                }
                int nEventRate = 1;
                MeasurePointData mData;
                if(nEventRate == 1) {
                    mData = tracker.readMeasurePointData();
                    if(mData != null) {
                        double dist_VecZ = mData.distance() * Math.cos(mData.zenith());
                        double dist_VecXY = mData.distance() * Math.sin(mData.zenith());
                        double dist_VecX = dist_VecXY * Math.cos(mData.azimuth());
                        double dist_VecY = dist_VecXY * Math.sin(mData.azimuth());
                        
                        Object[] values = new Object[3];
                        values[0] = dist_VecX * 1000;
                        values[1] = dist_VecY * 1000;
                        values[2] = dist_VecZ * 1000;
                        int ret=ContinueMeasurementScara_Process(values);
                        if(ret == 1) break;                     
                    }
                }
            }
            catch(TrackerException e) {
                System.out.println(e);
            }
        }
    }
    
    private int ContinueMeasurementScara_Process(Object[] v) {
        try {
            Thread.sleep(1); //如果没有sleep那么其他线程更改了CURRENTPROCESS，这里的判断也不会生效
            CURRENTPROCESS ret = ProgressManager();
            if(ret == CURRENTPROCESS.TURN_MANUAL_PROCESS) return 1;
            if(ret == CURRENTPROCESS.POSE_PAGE_BREAK_AUTO_PROCESS) return 1;
            if(ret == CURRENTPROCESS.DISCONNECT_PROCESS) return 1;
            if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS) {
                if(currPoint != checkPanelScara.GetSelectionRow()) {
                    checkPanelScara.SetSelectionRow(currPoint);
                }
            }
            if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS) {
                if(currPoint != posePanelScara.GetSelectionRow()) {
                    posePanelScara.SetSelectionRow(currPoint);
                }
            }
        }
        catch(InterruptedException e) {
            System.out.println(e);
        }
        
        dataBuf[pointCnt][0] = Double.parseDouble(String.valueOf(v[0]));
        dataBuf[pointCnt][1] = Double.parseDouble(String.valueOf(v[1]));
        dataBuf[pointCnt][2] = Double.parseDouble(String.valueOf(v[2]));
        ++pointCnt;
        if(pointCnt >= dataBuf.length) {
            pointCnt = 0;
            double temp_X = 0.0,temp_Y = 0.0,temp_Z = 0.0;
            double tempPow_X = 0.0,tempPow_Y = 0.0,tempPow_Z = 0.0;
            for(int i = 0;i < dataBuf.length;++i) {
                temp_X += dataBuf[i][0];
                temp_Y += dataBuf[i][1];
                temp_Z += dataBuf[i][2];
            }
            double average_X = temp_X / dataBuf.length;
            double average_Y = temp_Y / dataBuf.length;
            double average_Z = temp_Z / dataBuf.length;
            for(int i = 0;i < dataBuf.length;++i) {
                tempPow_X += Math.pow(dataBuf[i][0]-average_X, 2);
                tempPow_Y += Math.pow(dataBuf[i][1]-average_Y, 2);
                tempPow_Z += Math.pow(dataBuf[i][2]-average_Z, 2);
            }
            double r_X = Math.sqrt(tempPow_X / dataBuf.length);
            double r_Y = Math.sqrt(tempPow_Y / dataBuf.length);
            double r_Z = Math.sqrt(tempPow_Z / dataBuf.length);

            if(r_X <= 0.1 && r_Y <= 0.1 && r_Z <= 0.1) {
                if(moving == true) {
                    Object[] values = new Object[3];
                    values[0] = dataBuf[dataBuf.length-1][0];//average_X;
                    values[1] = dataBuf[dataBuf.length-1][1];//average_Y;
                    values[2] = dataBuf[dataBuf.length-1][2];//average_Z;
                    ++currPoint;
                    if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS) {
                        checkPanelScara.SetTrackerValue(values);
                        if(currPoint >= 50) {
                            currPoint = 0;
                            StopContinueMeasurement();
                            return 1;
                        }
                    }
                    if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS) {
                        posePanelScara.SetTrackerValue(values);
                        posePanelScara.AddPointsDisplay(values);
                        if(currPoint >= 10) {
                            currPoint = 0;
                            StopContinueMeasurement();
                            return 1;
                        }
                    }
                    moving = false;
                }
            }
            else {
                moving = true;
            }
        }
        return 0;
    }
    
    private void ContinueBkndMeasurement() {
        timerBknd = new Timer();
        timerBknd.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(tracker.connected() == false) {
                        frame.SetAlarmString("激光仪未连接!");
                        return;
                    }
                    if(GetCurrProcess() == CURRENTPROCESS.TRAJECTORY_PAGE_STOPMEASURE_PROCESS) {
                        StopContinueMeasurement();
                    }
                    int nEventRate = 1;
                    MeasurePointData mData;
                    if(nEventRate == 1) {
                        mData = tracker.readBkndMeasurePointData();
                        if(mData != null) {
                            double dist_VecZ = mData.distance() * Math.cos(mData.zenith());
                            double dist_VecXY = mData.distance() * Math.sin(mData.zenith());
                            double dist_VecX = dist_VecXY * Math.cos(mData.azimuth());
                            double dist_VecY = dist_VecXY * Math.sin(mData.azimuth());
                            Object[] values = new Object[3];
                            values[0] = dist_VecX * 1000;
                            values[1] = dist_VecY * 1000;
                            values[2] = dist_VecZ * 1000;
                            ContinueBkndMeasurement_Process(values);
                        }    
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
            }
        }, 0, 10);
    }
    
    private void ContinueBkndMeasurement_Process(Object[] values) {
        dataBuf[pointCnt][0] = Double.parseDouble(String.valueOf(values[0]));
        dataBuf[pointCnt][1] = Double.parseDouble(String.valueOf(values[1]));
        dataBuf[pointCnt][2] = Double.parseDouble(String.valueOf(values[2]));

        ++pointCnt;
        if(pointCnt == 3) {
            pointCnt = 0;
            double average_X = (dataBuf[0][0] + dataBuf[1][0] + dataBuf[2][0]) / 3.0;
            double average_Y = (dataBuf[0][1] + dataBuf[1][1] + dataBuf[2][1]) / 3.0;
            double average_Z = (dataBuf[0][2] + dataBuf[1][2] + dataBuf[2][2]) / 3.0;
            double pow_X = Math.pow((dataBuf[0][0] - average_X), 2) + Math.pow((dataBuf[1][0] - average_X), 2) +
                    Math.pow((dataBuf[2][0] - average_X), 2);
            double r_X = Math.sqrt(pow_X / 3.0);
            double pow_Y = Math.pow((dataBuf[0][1] - average_Y), 2) + Math.pow((dataBuf[1][1] - average_Y), 2) + 
                    Math.pow((dataBuf[2][1] - average_Y), 2);
            double r_Y = Math.sqrt(pow_Y / 3.0);
            double pow_Z = Math.pow((dataBuf[0][2] - average_Z), 2) + Math.pow((dataBuf[1][2] - average_Z), 2) + 
                    Math.pow((dataBuf[2][2] - average_Z), 2);
            double r_Z = Math.sqrt(pow_Z / 3.0);

            if(r_X <= 0.1 && r_Y <= 0.1 && r_Z <= 0.1) {
                if(moving == true) {
                    moving = false;
                    if(currPoint >= 50) {
                        currPoint = 0;

                        double[] before = new double[3];
                        double[] after = new double[3];
                        List curveRot = new ArrayList<>();
                        if(times == 0) {
                            List ret = LineDesign(curveData);
                            trajPanel.AddCurve(ret);

                            for(int i = 0;i < ret.size();i+=3) {
                                before[i % 3] = Double.parseDouble(ret.get(i).toString());
                                before[i % 3 + 1] = Double.parseDouble(ret.get(i + 1).toString());
                                before[i % 3 + 2] = Double.parseDouble(ret.get(i + 2).toString());
                                cal.DLL_CalcPointAfterRotationZY(after, before, rotation);
                                curveRot.add(after[0]);
                                curveRot.add(after[1]);
                                curveRot.add(after[2]);
                            }
                            trajPanel.AddCurveRotation(curveRot);
                        }
                        trajPanel.AddCurve(curveData);
                        for(int i = 0;i < curveData.size();i+=3) {
                            before[i % 3] = Double.parseDouble(curveData.get(i).toString());
                            before[i % 3 + 1] = Double.parseDouble(curveData.get(i + 1).toString());
                            before[i % 3 + 2] = Double.parseDouble(curveData.get(i + 2).toString());
                            cal.DLL_CalcPointAfterRotationZY(after, before, rotation);
                            curveRot.add(after[0]);
                            curveRot.add(after[1]);
                            curveRot.add(after[2]);
                        }
                        trajPanel.AddCurveRotation(curveRot);

                        curveData.clear();

                        times++;
                        if(times >= 10) {
                            StopContinueMeasurement();
                        }
                    }
                }
            }
            else {
                moving = true;
            }
            //30ms记录一个点位，最后从所有点位中取出50个点位给校验库使用
            if(moving == true) {
                Object[] obj = new Object[3];
                obj[0] = dataBuf[pointCnt][0];
                obj[1] = dataBuf[pointCnt][1];
                obj[2] = dataBuf[pointCnt][2];
                trajPanel.SetPointsValue(times, obj);

                curveData.add(dataBuf[pointCnt][0]);
                curveData.add(dataBuf[pointCnt][1]);
                curveData.add(dataBuf[pointCnt][2]);

                ++currPoint;
            }
        }
    }
    
    private void StopContinueMeasurement() {
        if(timerBknd != null) {
            timerBknd.cancel();
        }
        
        if(null != GetCurrProcess()) switch (GetCurrProcess()) {
            case CHECK_PAGE_CONTINUE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopMeasurePoint();
                        tracker.setBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_STEP2_EVENT);
                break;
            case POSE_PAGE_CONTINUE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopMeasurePoint();
                        tracker.setBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_POSE_EVENT);
                break;
            case SWINGERROR_PAGE_CONTINUE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopMeasurePoint();
                        tracker.setBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_SWINGERROR_EVENT);
                break;
            case TRAJECTORY_PAGE_CONTINUE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopBkndMeasurePoint();
                        tracker.setBkndMeasureBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.STARTBACKGROUND_STEP2_EVENT);
                break;
            case TRAJECTORY_PAGE_STOPMEASURE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopBkndMeasurePoint();
                        tracker.setBkndMeasureBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.NONE_EVENT);
                break;
            default:
                break;
        }
    }
    //校准计算，包含6轴机器人和scara
    private void CalculationProgress(Object obj) {
        if(obj instanceof CheckPanel) {
            CheckPanel tempPanel = (CheckPanel)obj;
            Object[] param = tempPanel.GetStructParam();
            for(int i = 0;i < param.length;++i) {
                if(i < 6) {dir[i] = Integer.parseInt(param[i].toString());System.out.println(dir[i]);}
                else if(i < 12) {zero_cal[i - 6] = Double.parseDouble(param[i].toString());System.out.println(zero_cal[i - 6]);}
                else if(i < 18) {ratio_Set[i - 12] = Double.parseDouble(param[i].toString());System.out.println(ratio_Set[i - 12]);}
                else if(i < 24) {param_cal[i - 18] = Double.parseDouble(param[i].toString());System.out.println(param_cal[i - 18]);}
                else if(i == 24) {
                    int v = Integer.parseInt(param[i].toString());
                    String num = Integer.toBinaryString(v);
                    if(num.length() == 2) num = "0" + num;
                    if(num.length() == 1) num = "00" + num;
                    boolean state = num.charAt(2) == '1';
                    couple_en[0] = state;
                    state = num.charAt(1) == '1';
                    couple_en[1] = state;
                    state = num.charAt(0) == '1';
                    couple_en[2] = state;
                    System.out.println(couple_en[0] + ":" + couple_en[1] + ":" + couple_en[2]);
                }
                else {
                    couple_cal[i-25] = Double.parseDouble(param[i].toString());
                    if(couple_cal[i-25] != 0.0) {
                        couple_cal[i-25] = 1.0 / couple_cal[i-25];
                    }
                }
            }

            Object[][] ret;
            ret = tempPanel.GetTableValue();
            for(int i = 0;i < ret.length;++i) {
                for(int j = 0; j < ret[0].length-1;++j) {
                    double val = Double.parseDouble(ret[i][j].toString());
                    points[i*9 + j] = val;
                }
            }
            tempPanel.DisplayCalTips(true);
//            couple_cal[0] = 0.0;
//            couple_cal[1] = 0.0;
//            couple_cal[2] = 1 / 9.753281197404144;
            for(int i = 0;i < ratio_cal.length;++i) {
                ratio_cal[i] = ratio_Set[i];
            }
            cal.DLL_CalibrationS6(points, zero_cal, ratio_cal, tool_cal, diff_post, diff_prev, param_cal, 
                    dir,couple_en,couple_cal,0.00001,base);

            System.out.println(couple_cal[0] + ":"+ couple_cal[1] + ":"+ 1.0/couple_cal[2]);

            String str_ratio = String.format("Ratio:%f %f %f %f %f %f",
                ratio_cal[0],ratio_cal[1],ratio_cal[2],ratio_cal[3],ratio_cal[4],ratio_cal[5]);
            String str_zero = String.format("Zero:%f %f %f %f %f %f",
                    zero_cal[0],zero_cal[1],zero_cal[2],zero_cal[3],zero_cal[4],zero_cal[5]);
            String str_tool = String.format("Tool:%f %f %f",
                    tool_cal[0],tool_cal[1],tool_cal[2]);
            String str_diff = String.format("Diff:%f %f %f",
                    diff_post[0],diff_post[1],diff_post[2]);
            String str_param = String.format("Param:%f %f %f %f %f %f",
                    param_cal[0],param_cal[1],param_cal[2],param_cal[3],param_cal[4],param_cal[5]);
            String str_couple = String.format("couple:%f %f %f",
                    couple_cal[0],couple_cal[1],1.0 / couple_cal[2]);

            System.out.println(str_ratio);
            System.out.println(str_zero);
            System.out.println(str_param);
            System.out.println(cycleTime);
            System.out.println(str_tool);
            System.out.println(str_diff);
            System.out.println(str_couple);

            Object[] err = new Object[6];
            double max_diff = 0.0;
            double ave_diff = 0.0;
            double min_diff = 10000.0;
            double pow_sum = 0.0;

            for (int i = 0; i < 50; i++) {
                    if (diff_prev[i] > max_diff) {
                            max_diff = diff_prev[i];
                    }
                    if (diff_prev[i] < min_diff) {
                            min_diff = diff_prev[i];
                    }
                    ave_diff += diff_prev[i];
            }
            ave_diff /= 50;
            for(int i = 0;i < 50;++i) {
                pow_sum += Math.pow(diff_prev[i]-ave_diff, 2);
            }
            pow_sum /= 50.0;
            err[0] = max_diff;
            err[1] = ave_diff;//min_diff
            err[2] = Math.sqrt(pow_sum);

            max_diff = 0.0;
            min_diff = 10000.0;
            ave_diff = 0.0;
            pow_sum = 0.0;
            for (int i = 0; i < 50; i++) {
                    if (diff_post[i] > max_diff) {
                            max_diff = diff_post[i];
                    }
                    if (diff_post[i] < min_diff) {
                            min_diff = diff_post[i];
                    }
                    ave_diff += diff_post[i];
            }
            ave_diff /= 50;
            for(int i = 0;i < 50;++i) {
                pow_sum += Math.pow(diff_post[i] - ave_diff, 2);
            }
            pow_sum /= 50.0;
            err[3] = max_diff;
            err[4] = ave_diff;//min_diff
            err[5] = Math.sqrt(pow_sum);

            Object[] values = new Object[30];
            for(int i = 0;i < values.length;++i) {
                if(i < 6) values[i] = zero_cal[i];
                else if(i < 12) values[i] = ratio_cal[i-6];
                else if(i < 18) values[i] = ratio_Set[i-12] / ratio_cal[i-12];
                else if(i < 24) values[i] = param_cal[i-18];
                else values[i] = err[i-24];
            }
            tempPanel.DispResult(values);
            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
            tempPanel.DisplayCalTips(false);
            tempPanel.ErrorDispEnable();
            tempPanel.GetErrorDisp().RefreshChartData(diff_prev, diff_post);
            SetCoordTransformFlag(true);
            
            double[][] worldPos = CoordinatorTrans();
            posePanel.CoordTrans(worldPos);
        }
        else if(obj instanceof CheckPanelScara) {
            CheckPanelScara tempPanel = (CheckPanelScara)obj;
            Object[] param = tempPanel.GetStructParam();
            for(int i = 0;i < param.length;++i) {
                if(i < 4) {dir[i] = Integer.parseInt(param[i].toString());System.out.println(dir[i]);}
                else if(i < 8) {zero_cal[i - 4] = Double.parseDouble(param[i].toString());System.out.println(zero_cal[i - 4]);}
                else if(i < 12) {ratio_Set[i - 8] = Double.parseDouble(param[i].toString());System.out.println(ratio_Set[i - 8]);}
                else if(i < 16) {param_cal[i - 12] = Double.parseDouble(param[i].toString());System.out.println(param_cal[i - 12]);}
                else if(i == 20) {
                    int v = Integer.parseInt(param[i].toString());
                    String num = Integer.toBinaryString(v);
                    if(num.length() == 2) num = "0" + num;
                    if(num.length() == 1) num = "00" + num;
                    boolean state = num.charAt(2) == '1';
                    couple_en[0] = state;
                    state = num.charAt(1) == '1';
                    couple_en[1] = state;
                    state = num.charAt(0) == '1';
                    couple_en[2] = state;
                    System.out.println(couple_en[0] + ":" + couple_en[1] + ":" + couple_en[2]);
                }
            }

            Object[][] ret;
            ret = tempPanel.GetTableValue();
            for(int i = 0;i < ret.length;++i) {
                for(int j = 0; j < ret[0].length-1;++j) {
                    double val = Double.parseDouble(ret[i][j].toString());
                    points[i*7 + j] = val;
                }
            }
            tempPanel.DisplayCalTips(true);
            couple_cal[0] = 0.0;
            couple_cal[1] = 0.0;
            couple_cal[2] = 0.0;
            cal.DLL_CalibrationScara(points, zero_cal, ratio_cal, tool_cal, diff_post_scara, diff_prev_scara, param_cal, couple_cal);

            Object[] err = new Object[6];
            double max_diff = 0.0;
            double ave_diff = 0.0;
            double min_diff = 10000.0;
            double pow_sum = 0.0;

            for (int i = 0; i < 30; i++) {
                    if (diff_prev_scara[i] > max_diff) {
                            max_diff = diff_prev_scara[i];
                    }
                    if (diff_prev_scara[i] < min_diff) {
                            min_diff = diff_prev_scara[i];
                    }
                    ave_diff += diff_prev_scara[i];
            }
            ave_diff /= 30;
            for(int i = 0;i < 30;++i) {
                pow_sum += Math.pow(diff_prev_scara[i]-ave_diff, 2);
            }
            pow_sum /= 30.0;
            err[0] = max_diff;
            err[1] = ave_diff;
            err[2] = Math.sqrt(pow_sum);

            max_diff = 0.0;
            min_diff = 10000.0;
            ave_diff = 0.0;
            pow_sum = 0.0;
            for (int i = 0; i < 30; i++) {
                    if (diff_post_scara[i] > max_diff) {
                            max_diff = diff_post_scara[i];
                    }
                    if (diff_post_scara[i] < min_diff) {
                            min_diff = diff_post_scara[i];
                    }
                    ave_diff += diff_post_scara[i];
            }
            ave_diff /= 30;
            for(int i = 0;i < 30;++i) {
                pow_sum += Math.pow(diff_post_scara[i]-ave_diff, 2);
            }
            pow_sum /= 30.0;
            err[3] = max_diff;
            err[4] = ave_diff;
            err[5] = Math.sqrt(pow_sum);

            Object[] values = new Object[20];
            for(int i = 0;i < values.length;++i) {
                if(i < 4) values[i] = zero_cal[i];
                else if(i < 8) values[i] = ratio_cal[i-4];
                else if(i < 12) values[i] = ratio_Set[i-8] / ratio_cal[i-8];
                else if(i < 16) values[i] = param_cal[i-12];
                else values[i] = err[i-16];
            }
            tempPanel.DispResult(values);
            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
            tempPanel.DisplayCalTips(false);
            tempPanel.ErrorDispEnable();
            tempPanel.GetErrorDisp().RefreshChartData(diff_prev_scara, diff_post_scara);
        }
    }
    //位姿检测处理，包含6轴机器人和scara
    private void PoseCheckProgress(Object obj) {
        Object[][] data;
        if(obj instanceof PosePanel) {
            data = posePanel.GetTableValue();
            for(int i = 0;i < 5;++i) {
                for(int j = 0; j < 9;++j) {
                    double val = Double.parseDouble(data[i][j].toString());
                    points[i*9 + j] = val;
                }
            }

            Object[] v = checkPanel.GetStructParam();
            for(int i = 0;i < v.length;++i) {
                if(i < 6) dir[i] = Integer.parseInt(v[i].toString());
                else if(i < 12) zero_cal[i - 6] = Double.parseDouble(v[i].toString());
                else if(i < 18) ratio_Set[i - 12] = Double.parseDouble(v[i].toString());
                else if(i < 24) param_cal[i - 18] = Double.parseDouble(v[i].toString());
                else if(i == 24) {
                    int value = Integer.parseInt(v[i].toString());
                    String num = Integer.toBinaryString(value);
                    if(num.length() == 2) num = "0" + num;
                    if(num.length() == 1) num = "00" + num;

                    boolean state = num.charAt(2) == '1';
                    couple_en[0] = state;
                    state = num.charAt(1) == '1';
                    couple_en[1] = state;
                    state = num.charAt(0) == '1';
                    couple_en[2] = state;
                    System.out.println(num);
                }
                else {
                    couple_cal[i-25] = Double.parseDouble(v[i].toString());
                }
            }

            int calNumber = 5;

            boolean retValue = cal.DLL_CheckPosePosError(points, outputs, calNumber, tool_cal, diff_prev, param_cal, 
                    dir,couple_en,couple_cal,zero_cal,base);

            for(int i = 0;i < calNumber+5;++i) {
                System.out.println("Pose_Diff:"+diff_prev[i]);
            }
            if(retValue) {
                Object[] pose_data = new Object[4];
                double max_d = 0;
                double ave_d = 0;
                double min_d = 10000.0;

                for (int i = 0; i < calNumber; i++) {
                        if (diff_prev[i] > max_d) {
                                max_d = diff_prev[i];
                        }
                        if (diff_prev[i] < min_d) {
                                min_d = diff_prev[i];
                        }
                        System.out.println("Outputs:" + outputs[i*3] + " " + outputs[i*3+1] + " " + outputs[i*3+2]);
                        ave_d += diff_prev[i];
                }
                ave_d /= calNumber;
                pose_data[2] = ave_d; //平均偏差
                double total = 0.0;
                for(int i = 0;i < calNumber;++i) {
                    total += Math.pow(diff_prev[i] - ave_d,2);
                }
                total /= calNumber;
                ave_d = Math.sqrt(total);

                pose_data[0] = times;
                pose_data[1] = max_d;
                pose_data[3] = ave_d; //有效偏差
                posePanel.DispResult(pose_data);
            }
            else {
                JOptionPane.showMessageDialog(frame, "位姿检测失败！");
            }
        }
        else if(obj instanceof PosePanelScara) {
            data = posePanelScara.GetTableValue();
            for(int i = 0;i < 10;++i) {
                for(int j = 0; j < 7;++j) {
                    double val = Double.parseDouble(data[i][j].toString());
                    points[i*7 + j] = val;
                }
            }

            Object[] v = checkPanelScara.GetStructParam();
            for(int i = 0;i < v.length;++i) {
                if(i < 4) dir[i] = Integer.parseInt(v[i].toString());
                else if(i < 8) zero_cal[i - 4] = Double.parseDouble(v[i].toString());
                else if(i < 12) ratio_Set[i - 8] = Double.parseDouble(v[i].toString());
                else if(i < 16) param_cal[i - 12] = Double.parseDouble(v[i].toString());
                else if(i == 20) {
                    int value = Integer.parseInt(v[i].toString());
                    String num = Integer.toBinaryString(value);
                    if(num.length() == 2) num = "0" + num;
                    if(num.length() == 1) num = "00" + num;

                    boolean state = num.charAt(2) == '1';
                    couple_en[0] = state;
                    state = num.charAt(1) == '1';
                    couple_en[1] = state;
                    state = num.charAt(0) == '1';
                    couple_en[2] = state;
                    System.out.println(num);
                }
                else {
                    couple_cal[i-21] = Double.parseDouble(v[i].toString());
                }
            }

            int calNumber = 10;

            boolean retValue = cal.DLL_CheckPosePosErrorScara(points, outputs, calNumber, tool_cal, diff_prev, param_cal, couple_cal);

            for(int i = 0;i < calNumber;++i) {
                System.out.println("Pose_Diff:"+diff_prev[i]);
            }
            if(retValue) {
                Object[][] pose_data = new Object[1][4];
                double max_d = 0;
                double ave_d = 0;
                double min_d = 10000.0;

                for (int i = 0; i < calNumber - 5; i++) {
                        if (diff_prev[i] > max_d) {
                                max_d = diff_prev[i];
                        }
                        if (diff_prev[i] < min_d) {
                                min_d = diff_prev[i];
                        }
                        System.out.println("Outputs:" + outputs[i*3] + " " + outputs[i*3+1] + " " + outputs[i*3+2]);
                        ave_d += diff_prev[i];
                }
                ave_d /= (calNumber - 5);
                double total = 0.0;
                for(int i = 0;i < calNumber - 5;++i) {
                    total += Math.pow(diff_prev[i] - ave_d,2);
                }
                total /= (calNumber - 5);
                ave_d = Math.sqrt(total);

                pose_data[0][0] = max_d;
                pose_data[0][1] = min_d;
                pose_data[0][2] = ave_d;
                pose_data[0][3] = !(max_d >= 5.0 || ave_d >=2.0); //判断通过与否的条件
                posePanelScara.DispResult(pose_data);
            }
            else {
                JOptionPane.showMessageDialog(frame, "位姿检测失败！");
            }
        }
    }
    
    
    private CURRENTPROCESS ProgressManager() {
        if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_SINGLEREAD_PROCESS ||
                GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_SINGLEREAD_PROCESS) {
            if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_SINGLEREAD_PROCESS) {
                checkPanel.SetSelectionRow(currPoint);
            }
            if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_SINGLEREAD_PROCESS) {
                posePanel.SetSelectionRow(currPoint);
            }
            ProcessChanged(CURRENTPROCESS.TURN_MANUAL_PROCESS);
            try {
                tracker.stopMeasurePoint();
                tracker.setBlocking(false);
            }
            catch(TrackerException e) {
                System.out.println(e);
            }
            return CURRENTPROCESS.TURN_MANUAL_PROCESS;
        }
        if(GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_BREAK_AUTO_PROCESS) {
            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
            posePanel.SetSelectionRow_Result(0);
            try {
                tracker.stopMeasurePoint();
                tracker.setBlocking(false);
            }
            catch(TrackerException e) {
                System.out.println(e);
            }
            return CURRENTPROCESS.POSE_PAGE_BREAK_AUTO_PROCESS;
        }
        if(GetCurrProcess() == CURRENTPROCESS.SWINGERROR_PAGE_BREAK_AUTO_PROCESS) {
            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
            swingerrorPanel.SetSelectionRow(0);
            try {
                tracker.stopMeasurePoint();
                tracker.setBlocking(false);
            }
            catch(TrackerException e) {
                System.out.println(e);
            }
            return CURRENTPROCESS.SWINGERROR_PAGE_BREAK_AUTO_PROCESS;
        }
        if(GetCurrProcess() == CURRENTPROCESS.DISCONNECT_PROCESS) {
            ProcessChanged(CURRENTPROCESS.NONE_PROCESS);
            try {
                tracker.stopMeasurePoint();
                tracker.setBlocking(false);
            }
            catch(TrackerException e) {
                System.out.println(e);
            }
            return CURRENTPROCESS.DISCONNECT_PROCESS;
        }
        return CURRENTPROCESS.NONE_PROCESS;
    }
    
    private List LineDesign(List values) {
        List ret = new ArrayList<>();
        double[] start = new double[3];
        double[] end = new double[3];
        int length = values.size();
        start[0] = Double.parseDouble(values.get(0).toString());
        start[1] = Double.parseDouble(values.get(1).toString());
        start[2] = Double.parseDouble(values.get(2).toString());
        end[0] = Double.parseDouble(values.get(length-3).toString());
        end[1] = Double.parseDouble(values.get(length-2).toString());
        end[2] = Double.parseDouble(values.get(length-1).toString());
        
        double alpha = (end[0] - start[0]) / length;
        double beta = (end[1] - start[1]) / length;
        double gamma = (end[2] - start[2]) / length;
        double dltX,dltY,dltZ;
        for(int i = 0;i < length;++i) {
            dltX = start[0] + i*alpha;
            dltY = start[1] + i*beta;
            dltZ = start[2] + i*gamma;
            if(alpha > 0.0 && dltX > end[0]) dltX = end[0];
            if(alpha < 0.0 && dltX < end[0]) dltX = end[0];
            if(beta > 0.0 && dltY > end[1]) dltY = end[1];
            if(beta < 0.0 && dltY < end[1]) dltY = end[1];
            if(gamma > 0.0 && dltZ > end[2]) dltZ = end[2];
            if(gamma < 0.0 && dltZ < end[2]) dltZ = end[2];
            ret.add(dltX);
            ret.add(dltY);
            ret.add(dltZ);
        }
        ret.add(end[0]);
        ret.add(end[1]);
        ret.add(end[2]);
        cal.DLL_CalcRotationZY(rotation, start, end);
        for(int i = 0;i < rotation.length;++i) {
            System.out.println(rotation[i]);
        }
        return ret;
    }
    
    public void MoveTo(double[] coord,boolean isRelative) {
        if(tracker == null) return;
        try {
            tracker.move(coord[0] / 100.0, coord[1] / 100.0, coord[2] / 100.0, true,isRelative);
        }
        catch(TrackerException e) {
            System.out.println(e);
        }
    }
    
    public void SearchTarget() {
        if(tracker == null) return;
        try {
            tracker.search(0.2,5000); //radius 20 cm,5000ms
        }
        catch(TrackerException e) {
            System.out.println(e);
        }
    }

    public void Connect() {
        SetCurrEvent(CURRENTEVENT.CONNECT_EVENT);
    }
    public void Disconnect() {
        if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS ||
                GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS) {
            ProcessChanged(CURRENTPROCESS.DISCONNECT_PROCESS);
        }
        SetCurrEvent(CURRENTEVENT.DISCONNECT_EVENT);
    }
    public void StartupCheck() {
        SetCurrEvent(CURRENTEVENT.STARTUPCHECK_EVENT);
    }
    public void ExeCmdSeq() {
        SetCurrEvent(CURRENTEVENT.EXECMDSEQ_EVENT);
    }
    public void TrackerPanel() {
        SetCurrEvent(CURRENTEVENT.TRACKERPANEL_EVENT);
    }
    public void CompIT() {
        SetCurrEvent(CURRENTEVENT.COMPIT_EVENT);
    }
    public void SingleReadMeasure() {
        if(GetCurrProcess() == CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS ||
                GetCurrProcess() == CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS ||
                GetCurrProcess() == CURRENTPROCESS.SWINGERROR_PAGE_CONTINUE_PROCESS) {
            auto2manual = true;
        }
        else {
            auto2manual = false;
        }
        SetCurrEvent(CURRENTEVENT.SINGLEREAD_EVENT);
    }
    public void ForegroundMeasure() {
        SetCurrEvent(CURRENTEVENT.FOREGROUND_EVENT);
    }
    public void BackgroundMeasure() {
        SetCurrEvent(CURRENTEVENT.BACKGROUND_EVENT);
    }
    public void ForeAndBackgroundMeasure() {
        SetCurrEvent(CURRENTEVENT.FORE_BACKGROUND_EVENT);
    }
    public void StartCalibration() {
        SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_EVENT);
    }
    public void StartCalibrationAlone() {
        SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_STEP2_EVENT);
    }
    public void StartBackgroundCalibration() {
        SetCurrEvent(CURRENTEVENT.STARTBACKGROUND_CAL_EVENT);
    }
    public void ProcessChanged(CURRENTPROCESS p) {
        currentProcess = p;
    }
    public CURRENTPROCESS GetCurrProcess() {
        return currentProcess;
    }
    public void ExitMeasurement() {
        if(timerBknd != null) {
            timerBknd.cancel();
        }
        
        if(null != GetCurrProcess()) switch (GetCurrProcess()) {
            case CHECK_PAGE_CONTINUE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopMeasurePoint();
                        tracker.disconnect();
                        tracker.setBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_STEP2_EVENT);
                break;
            case POSE_PAGE_CONTINUE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopMeasurePoint();
                        tracker.disconnect();
                        tracker.setBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.STARTCALIBRATE_POSE_EVENT);
                break;
            case TRAJECTORY_PAGE_CONTINUE_PROCESS:
                try {
                    if(tracker != null) {
                        tracker.stopBkndMeasurePoint();
                        tracker.disconnect();
                        tracker.setBkndMeasureBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                SetCurrEvent(CURRENTEVENT.STARTBACKGROUND_STEP2_EVENT);
                break;
            default:
                try {
                    if(tracker != null) {
                        tracker.disconnect();
                        tracker.setBlocking(false);
                    }
                }
                catch(TrackerException e) {
                    System.out.println(e);
                }
                break;
        }
    }
    
    public static Register GetRegister() {
        return register;
    }
    
    public void SetCoordTransformFlag(boolean flag) {
        base_gen = flag;
    }
    public boolean GetCoordTransformFlag() {
        return base_gen;
    }
    
    public double[] GetBase() {
        if(base_gen == false) {
            for(int i = 0;i < base.length;++i) {
                base[i] = 0.0;
            }
        }
        return base;
    }
    public double[] GetTool() {
        double[] tool = new double[6];
        if(base_gen == false) {
            for(int i = 0;i < tool.length;++i) {
                tool[i] = 0.0;
            }
        }
        else {
            for(int i = 0;i < tool.length;++i) {
                tool[i] = tool_cal[i];
            }
        }
        return tool;
    }
    public double[] GetRatio() {
        double[] ratio = new double[6];
        if(base_gen == false) {
            for(int i = 0;i < ratio.length;++i) {
                ratio[i] = 0.0;
            }
        }
        
        for(int i = 0;i < 6;++i) {
            ratio[i] = 1.0/ratio_Set[i];// / ratio_cal[i];
        }
        return ratio;
    }
    public double[] GetZero() {
        double[] zero = new double[6];
        if(base_gen == false) {
            for(int i = 0;i < zero.length;++i) {
                zero[i] = 0.0;
            }
        }
        Object [] obj = checkPanel.GetStructParam();
        for(int i = 0;i < zero.length;++i) {
            zero[i] = Double.parseDouble(String.valueOf(obj[i+6])) + zero_cal[i];
        }
        return zero;
    }
    private double[][] CoordinatorTrans() {
        if(base_gen == false) {
            return null;
        }
        //Transform the joint angle to world coordinator
        Object[][] vObj = posePanel.GetTableValue();
        double[][] theta = new double[5][6];
        double[][] worldPos = new double[vObj.length][6];
        for(int i = 0;i < theta.length;++i) {
            for(int j = 0;j < theta[0].length;++j) {
                theta[i][j] = Double.parseDouble(String.valueOf(vObj[i][j]));
            }
        }
        double[] ratio = GetRatio();
        double[] zero = GetZero();
        double[] tool = GetTool();
        double [][] DHParam = checkPanel.GetDHParam();
        double[] d = DHParam[1];
        double[] a = DHParam[2];
        double[] alpha = DHParam[3];
        for(int i = 0;i < theta.length;++i) {
//            cal.DLL_Joints2Base(worldPos[i], theta[i], d, a, alpha, ratio, zero, tool);
            cal.DLL_Joints2Base(worldPos[i], alpha, a, theta[i], d, ratio, zero, tool);
            System.out.println("WorldPos:"+worldPos[i][0] + " " + worldPos[i][1] + " " + worldPos[i][2] + " " + worldPos[i][3] + " " + worldPos[i][4] + " " + worldPos[i][5]);
        }
        return worldPos;
    }
    
    public double[] PointCoordinatorTrans() {
        if(base_gen == false) {
            return null;
        }
        //Transform the joint angle to world coordinator
        Object[] vObj = posePanel.GetTableRowValue();
        double[] theta = new double[6];
        double[] worldPos = new double[6];
        for(int i = 0;i < theta.length;++i) {
            theta[i] = Double.parseDouble(String.valueOf(vObj[i]));
        }
        double[] ratio = GetRatio();
        double[] zero = GetZero();
        double[] tool = GetTool();
        double [][] DHParam = checkPanel.GetDHParam();
        double[] d = DHParam[1];
        double[] a = DHParam[2];
        double[] alpha = DHParam[3];
        cal.DLL_Joints2Base(worldPos, theta, d, a, alpha, ratio, zero, tool);
        System.out.println("WorldPos:"+worldPos[0] + " " + worldPos[1] + " " + worldPos[2] + " " + worldPos[3] + " " + worldPos[4] + " " + worldPos[5]);
        return worldPos;
    }
    
}
