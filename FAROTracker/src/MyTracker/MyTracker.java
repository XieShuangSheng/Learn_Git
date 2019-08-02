/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import smx.tracker.*;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.WritableCell;
import jxl.write.Label;
import jxl.write.WriteException;

public class MyTracker extends javax.swing.JFrame
{
    public MyTracker() {
        long startTime = System.currentTimeMillis();
        long startTime_ns = System.nanoTime();
        initComponents();
        long endTime_ns = System.nanoTime();
        long endTime = System.currentTimeMillis();
        System.out.println("StartUpTime(ms):" + (endTime - startTime));
        System.out.println("StartUpTime(ns):" + (endTime_ns - startTime_ns));
    }
    
    private void initComponents() {
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        int screenWidth = toolKit.getScreenSize().width;
        int screenHeight = toolKit.getScreenSize().height;
        System.out.println("Width:" + screenWidth + "Height:" + screenHeight);
        menuBar = new JMenuBar();
        selectMode_Menu = new JMenu("模号");
        newMode_MenuItem = new JMenuItem("另存模号");
        saveMode_MenuItem = new JMenuItem("保存模号");
        selectMode_MenuItem = new JMenuItem("载入模号");
        leadoutResult_MenuItem = new JMenuItem("导出结果");
        leadinPoints_MenuItem = new JMenuItem("导入点位");
        
        connect_Menu = new JMenu("链接");
        connect_MenuItem = new JMenuItem("链接");
        disconnect_MenuItem = new JMenuItem("断开链接");
        
        application_Menu = new JMenu("工具");
        startupCheck_MenuItem = new JMenuItem("启动预热");
        exeCmdSeq_MenuItem = new JMenuItem("执行命令序列");
        trackerPanel_MenuItem = new JMenuItem("控制面板");
        compIT_MenuItem = new JMenuItem("补偿设定");
        
        measurement_Menu = new JMenu("测量");
        background_MenuItem = new JMenuItem("后台测量");
        foreground_MenuItem = new JMenuItem("前台测量");
        foreAndBackground_MenuItem = new JMenuItem("前台 & 后台测量");
        
        about_Menu = new JMenu("关于");
        versionInfo_MenuItem = new JMenuItem("版本信息");
        register_MenuItem = new JMenuItem("软件注册");
        

        
        menuBar.add(selectMode_Menu);
        menuBar.add(connect_Menu);
        menuBar.add(application_Menu);
//        menuBar.add(measurement_Menu);
        menuBar.add(about_Menu);
        
        selectMode_Menu.setEnabled(false);
        selectMode_Menu.add(selectMode_MenuItem);
        selectMode_Menu.add(saveMode_MenuItem);
        selectMode_Menu.add(newMode_MenuItem);
        selectMode_Menu.add(leadoutResult_MenuItem);
        selectMode_Menu.add(leadinPoints_MenuItem);
        connect_Menu.add(connect_MenuItem);
        connect_Menu.add(disconnect_MenuItem);
        application_Menu.add(startupCheck_MenuItem);
        application_Menu.add(exeCmdSeq_MenuItem);
        application_Menu.add(trackerPanel_MenuItem);
        application_Menu.add(compIT_MenuItem);
        about_Menu.add(versionInfo_MenuItem);
        about_Menu.add(register_MenuItem);
        
        measurement_Menu.add(foreground_MenuItem);
        measurement_Menu.add(background_MenuItem);
        measurement_Menu.add(foreAndBackground_MenuItem);
        setJMenuBar(menuBar);
        
        main_LayeredPane = new JLayeredPane();
        main_LayeredPane.setPreferredSize(new Dimension(screenWidth,screenHeight-168));
        
        typeSel_Panel = new JPanel();
        sixDOF_Button = new JButton("6轴机器人");
        Icon sixDOF_Icon = new ImageIcon("Res/6axisRobot.png");
        sixDOF_Button.setIcon(sixDOF_Icon);
        scara_Button = new JButton("Scara");
        sixDOF_Button.setPreferredSize(new Dimension(130,50));
        scara_Button.setPreferredSize(new Dimension(100,50));
        typeSel_Panel.add(sixDOF_Button);
        typeSel_Panel.add(scara_Button);
        typeSel_Panel.setBorder(etchedBorder);
        typeSel_Panel.setOpaque(true);
        typeSel_Panel.setBounds(0, 0, screenWidth, screenHeight-168);
        main_LayeredPane.add(typeSel_Panel,10,0);
        
        main_Panel = new JTabbedPane();
        main_Panel.setBorder(etchedBorder);
        main_Panel.setOpaque(true);
        main_Panel.setBounds(0, 0, screenWidth, screenHeight-168);
        
        checkPanel = new CheckPanel();
        posePanel = new PosePanel();
        trajectoryPanel = new TrajectoryPanel();
        stabilityPanel = new StabilityPanel();
        multiDirPanel = new MultiDirectionPanel();
        poseDriftPanel = new PoseDriftPanel();
        interChangePanel = new InterchangeabilityPanel();
        distancePanel = new DistancePanel();
        repeatTrajPanel = new RepeatTrajectoryPanel();
        cornerPanel = new CornerPanel();
        trajSpeedPanel = new TrajectorySpeedPanel();
        minTimeLocalPanel = new MinTimeLocalPanel();
        staticCompPanel = new StaticCompliancePanel();
        swingErrorPanel = new SwingErrorPanel();

        
//        ImageIcon check_Image = new ImageIcon("Res/6axisRobot.png");
        
//        main_Panel.addTab("校准", check_Image,checkPanel);
//        main_Panel.addTab("位姿", posePanel);
//        main_Panel.addTab("多方向位姿",multiDirPanel);
//        main_Panel.addTab("距离",distancePanel);
//        main_Panel.addTab("稳定性", stabilityPanel);
//        main_Panel.addTab("位姿特性漂移",poseDriftPanel);
//        main_Panel.addTab("互换性",interChangePanel);       
//        main_Panel.addTab("轨迹",trajectoryPanel);
//        main_Panel.addTab("重复定向轨迹",repeatTrajPanel);
//        main_Panel.addTab("拐角",cornerPanel);
//        main_Panel.addTab("轨迹速度特性",trajSpeedPanel);
//        main_Panel.addTab("最小稳定时间",minTimeLocalPanel);
//        main_Panel.addTab("静态柔顺性",staticCompPanel);
//        main_Panel.addTab("摆动偏差",swingErrorPanel);
        main_Panel.add(checkPanel,0);
        main_Panel.add(posePanel,1);
        main_Panel.add(multiDirPanel,2);
        main_Panel.add(distancePanel,3);
        main_Panel.add(stabilityPanel,4);
        main_Panel.add(poseDriftPanel,5);
        main_Panel.add(interChangePanel,6);       
        main_Panel.add(trajectoryPanel,7);
        main_Panel.add(repeatTrajPanel,8);
        main_Panel.add(cornerPanel,9);
        main_Panel.add(trajSpeedPanel,10);
        main_Panel.add(minTimeLocalPanel,11);
        main_Panel.add(staticCompPanel,12);
        main_Panel.add(swingErrorPanel,13);

        
        JLabel check_Label = new JLabel("校准",SwingConstants.CENTER);
        JLabel pose_Label = new JLabel("位姿",SwingConstants.CENTER);
        JLabel multiDir_Label = new JLabel("多方向位姿",SwingConstants.CENTER);
        JLabel distance_Label = new JLabel("距离",SwingConstants.CENTER);
        JLabel stability_Label = new JLabel("稳定性",SwingConstants.CENTER);
        JLabel poseDrift_Label = new JLabel("位姿特性漂移",SwingConstants.CENTER);
        JLabel interChange_Label = new JLabel("互换性",SwingConstants.CENTER);
        JLabel trajectory_Label = new JLabel("轨迹",SwingConstants.CENTER);
        JLabel repeatTraj_Label = new JLabel("重复定向轨迹",SwingConstants.CENTER);
        JLabel corner_Label = new JLabel("拐角偏差",SwingConstants.CENTER);
        JLabel trajSpeed_Label = new JLabel("轨迹速度特性",SwingConstants.CENTER);
        JLabel minTimeLocal_Label = new JLabel("最小定位时间",SwingConstants.CENTER);
        JLabel staticComp_Label = new JLabel("静态柔顺性",SwingConstants.CENTER);
        JLabel swingError_Label = new JLabel("摆动偏差",SwingConstants.CENTER);

        
        Dimension dim = new Dimension(65,20);
        Dimension dim_large = new Dimension(90,20);
        Font f = new Font("楷体",Font.BOLD,14);
        check_Label.setPreferredSize(dim);check_Label.setFont(f);check_Label.setForeground(Color.BLACK);
        pose_Label.setPreferredSize(dim);pose_Label.setFont(f);pose_Label.setForeground(Color.BLACK);
        multiDir_Label.setPreferredSize(dim_large);multiDir_Label.setFont(f);multiDir_Label.setForeground(Color.BLACK);
        distance_Label.setPreferredSize(dim);distance_Label.setFont(f);distance_Label.setForeground(Color.BLACK);
        stability_Label.setPreferredSize(dim);stability_Label.setFont(f);stability_Label.setForeground(Color.BLACK);
        poseDrift_Label.setPreferredSize(dim_large);poseDrift_Label.setFont(f);poseDrift_Label.setForeground(Color.BLACK);
        interChange_Label.setPreferredSize(dim);interChange_Label.setFont(f);interChange_Label.setForeground(Color.BLACK);
        trajectory_Label.setPreferredSize(dim);trajectory_Label.setFont(f);trajectory_Label.setForeground(Color.BLACK);
        repeatTraj_Label.setPreferredSize(dim_large);repeatTraj_Label.setFont(f);repeatTraj_Label.setForeground(Color.BLACK);
        corner_Label.setPreferredSize(dim);corner_Label.setFont(f);corner_Label.setForeground(Color.BLACK);
        trajSpeed_Label.setPreferredSize(dim_large);trajSpeed_Label.setFont(f);trajSpeed_Label.setForeground(Color.BLACK);
        minTimeLocal_Label.setPreferredSize(dim_large);minTimeLocal_Label.setFont(f);minTimeLocal_Label.setForeground(Color.BLACK);
        staticComp_Label.setPreferredSize(dim_large);staticComp_Label.setFont(f);staticComp_Label.setForeground(Color.BLACK);
        swingError_Label.setPreferredSize(dim);swingError_Label.setFont(f);swingError_Label.setForeground(Color.BLACK);

        main_Panel.setTabComponentAt(0, check_Label);
        main_Panel.setTabComponentAt(1, pose_Label);
        main_Panel.setTabComponentAt(2, multiDir_Label);
        main_Panel.setTabComponentAt(3, distance_Label);
        main_Panel.setTabComponentAt(4, stability_Label);
        main_Panel.setTabComponentAt(5, poseDrift_Label);
        main_Panel.setTabComponentAt(6, interChange_Label);
        main_Panel.setTabComponentAt(7, trajectory_Label);
        main_Panel.setTabComponentAt(8, repeatTraj_Label);
        main_Panel.setTabComponentAt(9, corner_Label);
        main_Panel.setTabComponentAt(10, trajSpeed_Label);
        main_Panel.setTabComponentAt(11, minTimeLocal_Label);
        main_Panel.setTabComponentAt(12, staticComp_Label);
        main_Panel.setTabComponentAt(13, swingError_Label);

        main_LayeredPane.add(main_Panel,10,1);
        
        scara_Panel = new JTabbedPane();
        scara_Panel.setBorder(etchedBorder);
        scara_Panel.setOpaque(true);
        scara_Panel.setBounds(0, 0, screenWidth, screenHeight-168);
        
        checkPanelScara = new CheckPanelScara();
        posePanelScara = new PosePanelScara();
//        trajectoryPanel = new TrajectoryPanel();
        scara_Panel.addTab("校准", checkPanelScara);
        scara_Panel.addTab("位姿", posePanelScara);
//        scara_Panel.addTab("轨迹",trajectoryPanel);

        main_LayeredPane.add(scara_Panel,10,2);
        this.add(main_LayeredPane);
        
        alarm_Panel = new JPanel();
        alarmInfo_Label = new JLabel("");
        alarmInfo_Label.setForeground(Color.RED);
        alarm_Panel.add(alarmInfo_Label);
        alarm_Panel.setBorder(etchedBorder);
        alarm_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        alarm_Panel.setPreferredSize(new Dimension(screenWidth,30));
        this.add(alarm_Panel);
        
        label_Panel = new JPanel();
        runMode_Label = new JLabel("");
        runMode_Label.setBorder(etchedBorder);
        runMode_Label.setPreferredSize(new Dimension(200,20));
        cmdSeq_Label = new JLabel("命令");
        cmdSeq_Label.setBorder(etchedBorder);
        cmdSeq_Label.setPreferredSize(new Dimension(200,20));
        connectStatus_Label = new JLabel("未连接");
        connectStatus_Label.setBorder(etchedBorder);
        connectStatus_Label.setPreferredSize(new Dimension(100,20));
        label_Panel.add(runMode_Label);
        label_Panel.add(connectStatus_Label);
        label_Panel.add(cmdSeq_Label);       
        label_Panel.setPreferredSize(new Dimension(screenWidth,30));
        label_Panel.setBorder(etchedBorder);
        label_Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(label_Panel);
        
        aboutDialog = new AboutDialog();
        fileDialog = new JFileChooser("./model");
        
//        register = new Register();
        
        newMode_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                newMode_MenuItemActionPerformed(ae);
            }
        });
        saveMode_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveMode_MenuItemActionPerformed(ae);
            }
        });
        selectMode_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                selectMode_MenuItemActionPerformed(ae);
            }
        });
        leadoutResult_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                leadoutResultActionPerformed(ae);
            }
        });
        leadinPoints_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                leadinPointsActionPerformed(ae);
            }
        });
        
        connect_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                connect_MenuItemActionPerformed(ae);
            }
        });
        disconnect_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                disconnect_MenuItemActionPerformed(ae);
            }
        });
        startupCheck_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                startupCheck_MenuItemActionPerformed(ae);
            }
        });
        exeCmdSeq_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                exeCmdSeq_MenuItemActionPerformed(ae);
            }
        });
        trackerPanel_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                trackerPanel_MenuItemActionPerformed(ae);
            }
        });
        compIT_MenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                compIT_MenuItemActionPerformed(ae);
            }
        });
        foreground_MenuItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent ae) {
               foreground_MenuItemActionPerformed(ae);
           }
        });
        versionInfo_MenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                versionInfo_MenuItemActionPerformed(ae);
            }
        });
        register_MenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                register_MenuItemActionPerformed(ae);
            }
        });
        sixDOF_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                sixDOF_ButtonActionPerformed(ae);
            }
        });
        scara_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                scara_ButtonActionPerformed(ae);
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {//窗口获得焦点
                CheckRegister();
            }
        });
        
        
        setTitle("机器人结构激光标定软件V1.0.3");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image img = toolKit.getImage("Res/Tracker.png");
        this.setIconImage(img);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        pack();
    }
    
    private void CheckRegister() {
        if(disp == false) {
            disp = true;
        }
        else {
            return;
        }
        if(register == null) {
            register = TrackerThread.GetRegister();
        }
        boolean flag = register.ReadValues();
        if(!flag) {
            checkRegister = false;
            JOptionPane.showMessageDialog(this, "请注册！");
        }
        else {
            checkRegister = true;
        }
    }
    
    private void connect_MenuItemActionPerformed(ActionEvent ae) {
        trackerThread.Connect();
    }
    
    private void disconnect_MenuItemActionPerformed(ActionEvent ae) {
        trackerThread.Disconnect();
    }
    
    private void startupCheck_MenuItemActionPerformed(ActionEvent ae) {
        trackerThread.StartupCheck();
    }
    
    private void exeCmdSeq_MenuItemActionPerformed(ActionEvent ae) {
        trackerThread.ExeCmdSeq();
    }
    
    private void trackerPanel_MenuItemActionPerformed(ActionEvent at) {
        trackerThread.TrackerPanel();
    }
    
    private void compIT_MenuItemActionPerformed(ActionEvent ae) {
        trackerThread.CompIT();
    }
    
    private void foreground_MenuItemActionPerformed(ActionEvent ae) {
        trackerThread.ForegroundMeasure();
    }
    
    
    private void newMode_MenuItemActionPerformed(ActionEvent ae) {
        File file;
        String fileName;
        BufferedWriter bw = null;
        int ret = fileDialog.showSaveDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) {
            file = fileDialog.getSelectedFile();
            fileName = fileDialog.getName(file);
            if(fileName == null || fileName.trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "文件名为空！");
            }
            if(file.isFile()) {
                fileName = file.getName();
            }
//            runMode_Label.setText(fileName);
            if(GetCheckRegister() == true)
            {
                if(typeSel == TYPESEL.SIXDOF_ROBOT) {
                    checkPanel.ProcessEnable();
                    posePanel.ProcessEnable();
                    trajectoryPanel.ProcessEnable();
                }
                else if(typeSel == TYPESEL.SCARA_ROBOT) {
                    checkPanelScara.ProcessEnable();
                    posePanelScara.ProcessEnable();
//                    trajectoryPanel.ProcessEnable();
                }
            }
            file = fileDialog.getCurrentDirectory();
            filePath = file.getPath() + java.io.File.separator + fileName;
            file = new File(filePath);            
            if(file.exists()) {
                JOptionPane.showMessageDialog(this, "文件已存在！");
                return;
            }

            //写excel
            try {
                //创建工作簿
                WritableWorkbook workbook = Workbook.createWorkbook(file);
                if(typeSel == TYPESEL.SIXDOF_ROBOT) {
                    //创建sheet
                    WritableSheet sheet_coord = workbook.createSheet("坐标", 0);
                    WritableSheet sheet_pose = workbook.createSheet("位姿", 1);
                    WritableSheet sheet_route = workbook.createSheet("轨迹", 2);
                    WritableSheet sheet_struct = workbook.createSheet("结构", 3);
                    WritableSheet sheet_length = workbook.createSheet("长度",4);
                    WritableSheet sheet_result = workbook.createSheet("结果",5);
                    String[] title = {"J1","J2","J3","J4","J5","J6","X","Y","Z"};
                    String[] title_route = {"X","Y","Z"};
                    String[] title_struct = {"","J1","J2","J3","J4","J5","J6"};
                    String[] title_length = {"","L1ecc","L23","L24","L34a","L34b","L56"};
                    Label label_coord = null,label_pose = null,label_route = null,label_struct = null,label_length = null,label_result = null;
                    String str = null;
                    for(int i = 0;i < title.length;++i) {
                        label_coord = new Label(i,0,title[i]);
                        label_pose = new Label(i,0,title[i]);
                        sheet_coord.addCell(label_coord);
                        sheet_pose.addCell(label_pose);
                    }
                    for(int i = 0;i < title_route.length;++i) {
                        label_route = new Label(i,0,title[i]);
                        sheet_route.addCell(label_route);
                    }
                    for(int i = 0;i < title_struct.length;++i) {
                        label_struct = new Label(i,0,title_struct[i]);
                        sheet_struct.addCell(label_struct);
                    }
                    for(int i = 0;i < title_length.length;++i) {
                        label_length = new Label(i,0,title_length[i]);
                        sheet_length.addCell(label_length);
                    }
                    label_struct = new Label(0,1,"轴向");
                    sheet_struct.addCell(label_struct);
                    label_struct = new Label(0,2,"零位");
                    sheet_struct.addCell(label_struct);
                    label_struct = new Label(0,3,"减速比");
                    sheet_struct.addCell(label_struct);
                    for(int i = 1;i <= 50;++i) {
                        for(int j = 1;j <= 9;++j) {
                            Object obj = checkPanel.getValueAt(i-1, j);
                            str = obj.toString();
                            label_coord = new Label(j-1,i,str);
                            sheet_coord.addCell(label_coord);
                        }
                    }

                    for(int i = 1;i <= 5;++i) {
                        for(int j = 1;j <= 9;++j) {
                            Object obj = posePanel.getValueAt(i-1, j);
                            str = obj.toString();
                            label_pose = new Label(j-1,i,str);
                            sheet_pose.addCell(label_pose);
                        }
                    }

                    for(int i = 1;i <= 2;++i) {
                        for(int j = 1;j <= 3;++j) {
                            Object obj = trajectoryPanel.getValueAt(i-1, j);
                            str = obj.toString();
                            label_route = new Label(j-1,i,str);
                            sheet_route.addCell(label_route);
                        }
                    }

                    Object[] obj = checkPanel.GetStructParam();
                    for(int i = 0;i < 3;++i) {
                        for(int j = 0;j < 6;++j) {
                            label_struct = new Label(j+1,i+1,obj[i*6 + j].toString());
                            sheet_struct.addCell(label_struct);
                        }
                    }
                    for(int i = 18;i < obj.length;++i) {
                        label_length = new Label(i-18 + 1,1,obj[i].toString());
                        sheet_length.addCell(label_length);
                    }
                    label_length = new Label(0,1,"长度");
                    sheet_length.addCell(label_length);

                    Object[] resultObj = checkPanel.GetResultParams();
                    Object[] repeatDiff = posePanel.GetRepeatDiff();
                    Object[] trajResult = trajectoryPanel.GetResult();
                    int length = resultObj.length+repeatDiff.length+trajResult.length;

                    for(int i = 0;i < length;++i) {
                        if(i < resultObj.length) {
                            label_result = new Label(0,i,String.valueOf(resultObj[i]));
                        }
                        else if(i < resultObj.length+repeatDiff.length) {
                            label_result = new Label(0,i,String.valueOf(repeatDiff[i - resultObj.length]));
                        }
                        else if(i < resultObj.length+repeatDiff.length+trajResult.length) {
                            label_result = new Label(0,i,String.valueOf(trajResult[i-resultObj.length - repeatDiff.length]));
                        }
                        sheet_result.addCell(label_result);
                    }
                }
                else if(typeSel == TYPESEL.SCARA_ROBOT) {
                    //创建sheet
                    WritableSheet sheet_coord = workbook.createSheet("坐标", 0);
                    WritableSheet sheet_pose = workbook.createSheet("位姿", 1);
                    WritableSheet sheet_route = workbook.createSheet("轨迹", 2);
                    WritableSheet sheet_struct = workbook.createSheet("结构", 3);
                    WritableSheet sheet_length = workbook.createSheet("长度",4);
                    WritableSheet sheet_result = workbook.createSheet("结果",5);
                    String[] title = {"J1","J2","J3","J4","X","Y","Z"};
                    String[] title_route = {"X","Y","Z"};
                    String[] title_struct = {"","J1","J2","J3","J4"};
                    String[] title_length = {"","L1ecc","L23","L24","L34a"};
                    Label label_coord = null,label_pose = null,label_route = null,label_struct = null,label_length = null,label_result = null;
                    String str = null;
                    for(int i = 0;i < title.length;++i) {
                        label_coord = new Label(i,0,title[i]);
                        label_pose = new Label(i,0,title[i]);
                        sheet_coord.addCell(label_coord);
                        sheet_pose.addCell(label_pose);
                    }
                    for(int i = 0;i < title_route.length;++i) {
                        label_route = new Label(i,0,title[i]);
                        sheet_route.addCell(label_route);
                    }
                    for(int i = 0;i < title_struct.length;++i) {
                        label_struct = new Label(i,0,title_struct[i]);
                        sheet_struct.addCell(label_struct);
                    }
                    for(int i = 0;i < title_length.length;++i) {
                        label_length = new Label(i,0,title_length[i]);
                        sheet_length.addCell(label_length);
                    }
                    label_struct = new Label(0,1,"轴向");
                    sheet_struct.addCell(label_struct);
                    label_struct = new Label(0,2,"零位");
                    sheet_struct.addCell(label_struct);
                    label_struct = new Label(0,3,"减速比");
                    sheet_struct.addCell(label_struct);
                    for(int i = 1;i <= 30;++i) {
                        for(int j = 1;j <= 7;++j) {
                            Object obj = checkPanelScara.getValueAt(i-1, j);
                            str = obj.toString();
                            label_coord = new Label(j-1,i,str);
                            sheet_coord.addCell(label_coord);
                        }
                    }

                    for(int i = 1;i <= 10;++i) {
                        for(int j = 1;j <= 7;++j) {
                            Object obj = posePanelScara.getValueAt(i-1, j);
                            str = obj.toString();
                            label_pose = new Label(j-1,i,str);
                            sheet_pose.addCell(label_pose);
                        }
                    }

//                    for(int i = 1;i <= 2;++i) {
//                        for(int j = 1;j <= 6;++j) {
//                            Object obj = trajectoryPanel.getValueAt(i-1, j);
//                            str = obj.toString();
//                            label_route = new Label(j-1,i,str);
//                            sheet_route.addCell(label_route);
//                        }
//                    }

                    Object[] obj = checkPanelScara.GetStructParam();
                    for(int i = 0;i < 3;++i) {
                        for(int j = 0;j < 4;++j) {
                            label_struct = new Label(j+1,i+1,obj[i*4 + j].toString());
                            sheet_struct.addCell(label_struct);
                        }
                    }
                    for(int i = 12;i < obj.length;++i) {
                        label_length = new Label(i-12 + 1,1,obj[i].toString());
                        sheet_length.addCell(label_length);
                    }
                    label_length = new Label(0,1,"长度");
                    sheet_length.addCell(label_length);

                    Object[] resultObj = checkPanelScara.GetResultParams();
                    for(int i = 0;i < resultObj.length;++i) {
                        label_result = new Label(0,i,String.valueOf(resultObj[i]));
                        sheet_result.addCell(label_result);
                    }
                }
                
                workbook.write();
                workbook.close();
            }
            catch(IOException | WriteException e) {
                System.out.println(e);
            }
        }
    }
    private void saveMode_MenuItemActionPerformed(ActionEvent ae) {
        if(filePath == null) {
            return;
        }
        int ret = JOptionPane.showConfirmDialog(this, "确定要保存修改到模板模号吗？","提示",JOptionPane.OK_CANCEL_OPTION);
        if(ret == JOptionPane.CANCEL_OPTION) {
            System.out.println("Cancel");
            return;
        }
        File file = new File(filePath);
        System.out.println(filePath);
        //写excel
        try {
            //创建工作簿
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            if(typeSel == TYPESEL.SIXDOF_ROBOT) {
                //创建sheet
                WritableSheet sheet_coord = workbook.createSheet("坐标", 0);
                WritableSheet sheet_pose = workbook.createSheet("位姿", 1);
                WritableSheet sheet_route = workbook.createSheet("轨迹", 2);
                WritableSheet sheet_struct = workbook.createSheet("结构", 3);
                WritableSheet sheet_length = workbook.createSheet("长度",4);
                WritableSheet sheet_result = workbook.createSheet("结果",5);
                String[] title = {"J1","J2","J3","J4","J5","J6","X","Y","Z"};
                String[] title_route = {"X","Y","Z"};
                String[] title_struct = {"","J1","J2","J3","J4","J5","J6"};
                String[] title_length = {"","L1ecc","L23","L24","L34a","L34b","L56"};
                Label label_coord = null,label_pose = null,label_route = null,label_struct = null,label_length = null,label_result = null;
                String str = null;
                for(int i = 0;i < title.length;++i) {
                    label_coord = new Label(i,0,title[i]);
                    label_pose = new Label(i,0,title[i]);
                    sheet_coord.addCell(label_coord);
                    sheet_pose.addCell(label_pose);
                }
                for(int i = 0;i < title_route.length;++i) {
                    label_route = new Label(i,0,title[i]);
                    sheet_route.addCell(label_route);
                }
                for(int i = 0;i < title_struct.length;++i) {
                    label_struct = new Label(i,0,title_struct[i]);
                    sheet_struct.addCell(label_struct);
                }
                for(int i = 0;i < title_length.length;++i) {
                    label_length = new Label(i,0,title_length[i]);
                    sheet_length.addCell(label_length);
                }
                label_struct = new Label(0,1,"轴向");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,2,"零位");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,3,"减速比");
                sheet_struct.addCell(label_struct);
                for(int i = 1;i <= 50;++i) {
                    for(int j = 1;j <= 9;++j) {
                        Object obj = checkPanel.getValueAt(i-1, j);
                        str = obj.toString();
                        label_coord = new Label(j-1,i,str);
                        sheet_coord.addCell(label_coord);
                    }
                }

                for(int i = 1;i <= 5;++i) {
                    for(int j = 1;j <= 9;++j) {
                        Object obj = posePanel.getValueAt(i-1, j);
                        str = obj.toString();
                        label_pose = new Label(j-1,i,str);
                        sheet_pose.addCell(label_pose);
                    }
                }

                for(int i = 1;i <= 2;++i) {
                    for(int j = 1;j <= 3;++j) {
                        Object obj = trajectoryPanel.getValueAt(i-1, j);
                        str = obj.toString();
                        label_route = new Label(j-1,i,str);
                        sheet_route.addCell(label_route);
                    }
                }

                Object[] obj = checkPanel.GetStructParam();
                for(int i = 0;i < 3;++i) {
                    for(int j = 0;j < 6;++j) {
                        label_struct = new Label(j+1,i+1,obj[i*6 + j].toString());
                        sheet_struct.addCell(label_struct);
                    }
                }
                for(int i = 18;i < obj.length;++i) {
                    label_length = new Label(i-18 + 1,1,obj[i].toString());
                    sheet_length.addCell(label_length);
                }
                label_length = new Label(0,1,"长度");
                sheet_length.addCell(label_length);

                Object[] resultObj = checkPanel.GetResultParams();
                Object[] repeatDiff = posePanel.GetRepeatDiff();
                Object[] trajResult = trajectoryPanel.GetResult();
                int length = resultObj.length+repeatDiff.length+trajResult.length;
                
                for(int i = 0;i < length;++i) {
                    if(i < resultObj.length) {
                        label_result = new Label(0,i,String.valueOf(resultObj[i]));
                    }
                    else if(i < resultObj.length+repeatDiff.length) {
                        label_result = new Label(0,i,String.valueOf(repeatDiff[i - resultObj.length]));
                    }
                    else if(i < resultObj.length+repeatDiff.length+trajResult.length) {
                        label_result = new Label(0,i,String.valueOf(trajResult[i-resultObj.length - repeatDiff.length]));
                    }
                    sheet_result.addCell(label_result);
                }
            }
            else if(typeSel == TYPESEL.SCARA_ROBOT) {
                //创建sheet
                WritableSheet sheet_coord = workbook.createSheet("坐标", 0);
                WritableSheet sheet_pose = workbook.createSheet("位姿", 1);
                WritableSheet sheet_route = workbook.createSheet("轨迹", 2);
                WritableSheet sheet_struct = workbook.createSheet("结构", 3);
                WritableSheet sheet_length = workbook.createSheet("长度",4);
                WritableSheet sheet_result = workbook.createSheet("结果",5);
                String[] title = {"J1","J2","J3","J4","X","Y","Z"};
                String[] title_route = {"X","Y","Z"};
                String[] title_struct = {"","J1","J2","J3","J4"};
                String[] title_length = {"","L12","L23"};
                Label label_coord = null,label_pose = null,label_route = null,label_struct = null,label_length = null,label_result = null;
                String str = null;
                for(int i = 0;i < title.length;++i) {
                    label_coord = new Label(i,0,title[i]);
                    label_pose = new Label(i,0,title[i]);
                    sheet_coord.addCell(label_coord);
                    sheet_pose.addCell(label_pose);
                }
                for(int i = 0;i < title_route.length;++i) {
                    label_route = new Label(i,0,title[i]);
                    sheet_route.addCell(label_route);
                }
                for(int i = 0;i < title_struct.length;++i) {
                    label_struct = new Label(i,0,title_struct[i]);
                    sheet_struct.addCell(label_struct);
                }
                for(int i = 0;i < title_length.length;++i) {
                    label_length = new Label(i,0,title_length[i]);
                    sheet_length.addCell(label_length);
                }
                label_struct = new Label(0,1,"轴向");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,2,"零位");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,3,"减速比");
                sheet_struct.addCell(label_struct);
                for(int i = 1;i <= 30;++i) {
                    for(int j = 1;j <= 7;++j) {
                        Object obj = checkPanelScara.getValueAt(i-1, j);
                        str = obj.toString();
                        label_coord = new Label(j-1,i,str);
                        sheet_coord.addCell(label_coord);
                    }
                }

                for(int i = 1;i <= 10;++i) {
                    for(int j = 1;j <= 7;++j) {
                        Object obj = posePanelScara.getValueAt(i-1, j);
                        str = obj.toString();
                        label_pose = new Label(j-1,i,str);
                        sheet_pose.addCell(label_pose);
                    }
                }
//
//                for(int i = 1;i <= 2;++i) {
//                    for(int j = 1;j <= 6;++j) {
//                        Object obj = trajectoryPanel.getValueAt(i-1, j);
//                        str = obj.toString();
//                        label_route = new Label(j-1,i,str);
//                        sheet_route.addCell(label_route);
//                    }
//                }

                Object[] obj = checkPanelScara.GetStructParam();
                for(int i = 0;i < 3;++i) {
                    for(int j = 0;j < 4;++j) {
                        label_struct = new Label(j+1,i+1,obj[i*4 + j].toString());
                        sheet_struct.addCell(label_struct);
                    }
                }
                for(int i = 12;i < obj.length;++i) {
                    label_length = new Label(i-12 + 1,1,obj[i].toString());
                    sheet_length.addCell(label_length);
                }
                label_length = new Label(0,1,"长度");
                sheet_length.addCell(label_length);

                Object[] resultObj = checkPanelScara.GetResultParams();
                for(int i = 0;i < resultObj.length;++i) {
                    label_result = new Label(0,i,String.valueOf(resultObj[i]));
                    sheet_result.addCell(label_result);
                }
            }
            
            workbook.write();
            workbook.close();
        }
        catch(IOException | WriteException e) {
            System.out.println(e);
        }
    }
    public void saveCaledBuffer() {
        String fileName = runMode_Label.getText();
        fileName = fileName.replaceAll(".xls", "");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
        String date = df.format(new Date());
        File file = new File("Temp" + java.io.File.separator + fileName + date + ".xls");
        System.out.println(file);
        //写excel
        try {
            //创建工作簿
            WritableWorkbook workbook = Workbook.createWorkbook(file);
           if(typeSel == TYPESEL.SIXDOF_ROBOT) {
                //创建sheet
                WritableSheet sheet_coord = workbook.createSheet("坐标", 0);
                WritableSheet sheet_pose = workbook.createSheet("位姿", 1);
                WritableSheet sheet_route = workbook.createSheet("轨迹", 2);
                WritableSheet sheet_struct = workbook.createSheet("结构", 3);
                WritableSheet sheet_length = workbook.createSheet("长度",4);
                WritableSheet sheet_result = workbook.createSheet("结果",5);
                String[] title = {"J1","J2","J3","J4","J5","J6","X","Y","Z"};
                String[] title_route = {"X","Y","Z"};
                String[] title_struct = {"","J1","J2","J3","J4","J5","J6"};
                String[] title_length = {"","L1ecc","L23","L24","L34a","L34b","L56"};
                Label label_coord = null,label_pose = null,label_route = null,label_struct = null,label_length = null,label_result = null;
                String str = null;
                for(int i = 0;i < title.length;++i) {
                    label_coord = new Label(i,0,title[i]);
                    label_pose = new Label(i,0,title[i]);
                    sheet_coord.addCell(label_coord);
                    sheet_pose.addCell(label_pose);
                }
                for(int i = 0;i < title_route.length;++i) {
                    label_route = new Label(i,0,title[i]);
                    sheet_route.addCell(label_route);
                }
                for(int i = 0;i < title_struct.length;++i) {
                    label_struct = new Label(i,0,title_struct[i]);
                    sheet_struct.addCell(label_struct);
                }
                for(int i = 0;i < title_length.length;++i) {
                    label_length = new Label(i,0,title_length[i]);
                    sheet_length.addCell(label_length);
                }
                label_struct = new Label(0,1,"轴向");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,2,"零位");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,3,"减速比");
                sheet_struct.addCell(label_struct);
                for(int i = 1;i <= 50;++i) {
                    for(int j = 1;j <= 9;++j) {
                        Object obj = checkPanel.getValueAt(i-1, j);
                        str = obj.toString();
                        label_coord = new Label(j-1,i,str);
                        sheet_coord.addCell(label_coord);
                    }
                }

                for(int i = 1;i <= 5;++i) {
                    for(int j = 1;j <= 9;++j) {
                        Object obj = posePanel.getValueAt(i-1, j);
                        str = obj.toString();
                        label_pose = new Label(j-1,i,str);
                        sheet_pose.addCell(label_pose);
                    }
                }

                for(int i = 1;i <= 2;++i) {
                    for(int j = 1;j <= 3;++j) {
                        Object obj = trajectoryPanel.getValueAt(i-1, j);
                        str = obj.toString();
                        label_route = new Label(j-1,i,str);
                        sheet_route.addCell(label_route);
                    }
                }

                Object[] obj = checkPanel.GetStructParam();
                for(int i = 0;i < 3;++i) {
                    for(int j = 0;j < 6;++j) {
                        label_struct = new Label(j+1,i+1,obj[i*6 + j].toString());
                        sheet_struct.addCell(label_struct);
                    }
                }
                for(int i = 18;i < obj.length;++i) {
                    label_length = new Label(i-18 + 1,1,obj[i].toString());
                    sheet_length.addCell(label_length);
                }
                label_length = new Label(0,1,"长度");
                sheet_length.addCell(label_length);

                Object[] resultObj = checkPanel.GetResultParams();
                Object[] repeatDiff = posePanel.GetRepeatDiff();
                Object[] trajResult = trajectoryPanel.GetResult();
                int length = resultObj.length+repeatDiff.length+trajResult.length;
                
                for(int i = 0;i < length;++i) {
                    if(i < resultObj.length) {
                        label_result = new Label(0,i,String.valueOf(resultObj[i]));
                    }
                    else if(i < resultObj.length+repeatDiff.length) {
                        label_result = new Label(0,i,String.valueOf(repeatDiff[i - resultObj.length]));
                    }
                    else if(i < resultObj.length+repeatDiff.length+trajResult.length) {
                        label_result = new Label(0,i,String.valueOf(trajResult[i-resultObj.length - repeatDiff.length]));
                    }
                    sheet_result.addCell(label_result);
                }
           }
           else if(typeSel == TYPESEL.SCARA_ROBOT) {
                //创建sheet
                WritableSheet sheet_coord = workbook.createSheet("坐标", 0);
                WritableSheet sheet_pose = workbook.createSheet("位姿", 1);
                WritableSheet sheet_route = workbook.createSheet("轨迹", 2);
                WritableSheet sheet_struct = workbook.createSheet("结构", 3);
                WritableSheet sheet_length = workbook.createSheet("长度",4);
                WritableSheet sheet_result = workbook.createSheet("结果",5);
                String[] title = {"J1","J2","J3","J4","X","Y","Z"};
                String[] title_route = {"X","Y","Z"};
                String[] title_struct = {"","J1","J2","J3","J4"};
                String[] title_length = {"","L12","L23"};
                Label label_coord = null,label_pose = null,label_route = null,label_struct = null,label_length = null,label_result = null;
                String str = null;
                for(int i = 0;i < title.length;++i) {
                    label_coord = new Label(i,0,title[i]);
                    label_pose = new Label(i,0,title[i]);
                    sheet_coord.addCell(label_coord);
                    sheet_pose.addCell(label_pose);
                }
                for(int i = 0;i < title_route.length;++i) {
                    label_route = new Label(i,0,title[i]);
                    sheet_route.addCell(label_route);
                }
                for(int i = 0;i < title_struct.length;++i) {
                    label_struct = new Label(i,0,title_struct[i]);
                    sheet_struct.addCell(label_struct);
                }
                for(int i = 0;i < title_length.length;++i) {
                    label_length = new Label(i,0,title_length[i]);
                    sheet_length.addCell(label_length);
                }
                label_struct = new Label(0,1,"轴向");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,2,"零位");
                sheet_struct.addCell(label_struct);
                label_struct = new Label(0,3,"减速比");
                sheet_struct.addCell(label_struct);
                for(int i = 1;i <= 30;++i) {
                    for(int j = 1;j <= 7;++j) {
                        Object obj = checkPanelScara.getValueAt(i-1, j);
                        str = obj.toString();
                        label_coord = new Label(j-1,i,str);
                        sheet_coord.addCell(label_coord);
                    }
                }

                for(int i = 1;i <= 10;++i) {
                    for(int j = 1;j <= 7;++j) {
                        Object obj = posePanelScara.getValueAt(i-1, j);
                        str = obj.toString();
                        label_pose = new Label(j-1,i,str);
                        sheet_pose.addCell(label_pose);
                    }
                }
//
//                for(int i = 1;i <= 2;++i) {
//                    for(int j = 1;j <= 6;++j) {
//                        Object obj = trajectoryPanel.getValueAt(i-1, j);
//                        str = obj.toString();
//                        label_route = new Label(j-1,i,str);
//                        sheet_route.addCell(label_route);
//                    }
//                }

                Object[] obj = checkPanelScara.GetStructParam();
                for(int i = 0;i < 3;++i) {
                    for(int j = 0;j < 4;++j) {
                        label_struct = new Label(j+1,i+1,obj[i*4 + j].toString());
                        sheet_struct.addCell(label_struct);
                    }
                }
                for(int i = 12;i < obj.length;++i) {
                    label_length = new Label(i-12 + 1,1,obj[i].toString());
                    sheet_length.addCell(label_length);
                }
                label_length = new Label(0,1,"长度");
                sheet_length.addCell(label_length);

                Object[] resultObj = checkPanelScara.GetResultParams();
                for(int i = 0;i < resultObj.length;++i) {
                    label_result = new Label(0,i,String.valueOf(resultObj[i]));
                    sheet_result.addCell(label_result);
                }
           }
            
            workbook.write();
            workbook.close();
        }
        catch(IOException | WriteException e) {
            System.out.println(e);
        }
    }
    private void selectMode_MenuItemActionPerformed(ActionEvent at) {
        File file;
        String fileName;
        BufferedReader br = null;
        int ret = fileDialog.showOpenDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) {
            file = fileDialog.getSelectedFile();
            fileName = file.getName();
            runMode_Label.setText(fileName);
            if(fileName == null || fileName.trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "文件名为空！");
            }
            if(file.isFile() && file.exists()) {
                fileName = file.getName();
                file = fileDialog.getCurrentDirectory();
                filePath = file.getPath() + java.io.File.separator + fileName;
                file = new File(filePath);     

                //read excel
                try {
                    Workbook book = Workbook.getWorkbook(file);
                    Sheet sheet_data = book.getSheet(0);        //坐标  
                    Sheet sheet_pose = book.getSheet(1);        //位姿
                    Sheet sheet_traj = book.getSheet(2);        //轨迹
                    Sheet sheet_Struct = book.getSheet(3);      //结构  
                    Sheet sheet_Length = book.getSheet(4);      //长度
                    Sheet sheet_result = book.getSheet(5);      //结果
                    Sheet sheet_minTime = book.getSheet(6);     //最小定位时间
                    Sheet sheet_swingerror = book.getSheet(7);  //摆动偏差
                    
                    
                    int rowCnt_data = sheet_data.getRows(); //坐标的行数
                    int columnCnt_data = sheet_data.getColumns();//坐标的列数
                    
                    if(typeSel == TYPESEL.SIXDOF_ROBOT) {
                        String[][] data = new String[51][9];
                        for(int i = 1;i < rowCnt_data;++i) {
                            for(int j = 0;j < columnCnt_data;++j) {
                                data[i-1][j] = sheet_data.getCell(j,i).getContents();
                            }
                        }
                        checkPanel.SetValueAll(data);
                        
                        String[][] structParam = new String[6][4];
                        int rowCnt_Struct = sheet_Struct.getRows();
                        int columnCnt_Struct = sheet_Struct.getColumns();
                        for(int i = 1;i < rowCnt_Struct;++i) {
                            for(int j = 1;j < columnCnt_Struct;++j) {
                                structParam[j-1][i] = sheet_Struct.getCell(j, i).getContents();
                            }
                        }

                        String[] lengthParam = new String[sheet_Length.getColumns()-1];
                        for(int j = 1;j < sheet_Length.getColumns();++j) {
                            lengthParam[j-1] = sheet_Length.getCell(j, 1).getContents();
                        }
                        checkPanel.SetStructParam(structParam, lengthParam);

                        String[][] poseData = new String[6][9];
//                        int rowCnt_Pose = sheet_pose.getRows();
//                        int columnCnt_Pose = sheet_pose.getColumns();
                        for(int i = 1;i < poseData.length;++i) {
                            for(int j = 0;j < poseData[0].length;++j) {
                                poseData[i-1][j] = sheet_pose.getCell(j, i).getContents();
                            }
                        }
                        posePanel.SetValueAll(poseData);

                        String[][] trajData = new String[3][3];
//                        int rowCnt_Traj = sheet_traj.getRows();
//                        int columnCnt_Traj = sheet_traj.getColumns();
                        for(int i = 1;i < trajData.length;++i) {
                            for(int j = 0;j < trajData[0].length;++j) {
                                trajData[i-1][j] = sheet_traj.getCell(j,i).getContents();
                            }
                        }
                        trajectoryPanel.SetValueAll(trajData);
                        
                        String[][] minTimeData = new String[9][6];
                        int rowCnt_minTime = sheet_minTime.getRows();   
                        int columnCnt_minTime = sheet_minTime.getColumns();                    
                        for(int i = 1;i < rowCnt_minTime;++i) {
                            for(int j = 0;j < columnCnt_minTime;++j) {
                                minTimeData[i-1][j] = sheet_minTime.getCell(j,i).getContents();
                            }
                        }   
                        minTimeLocalPanel.SetValueAll(minTimeData);
                        
                        String[][] swingerror = new String[21][6];
                        int rowCnt_swingerror = sheet_swingerror.getRows();
                        int columnCnt_swingerror = sheet_swingerror.getColumns();
                        for(int i = 1;i < rowCnt_swingerror;++i) {
                            for(int j = 0;j < columnCnt_swingerror;++j) {
                                swingerror[i-1][j] = sheet_swingerror.getCell(j,i).getContents();
                            }
                        }   
                        swingErrorPanel.SetValueAll(swingerror);
                           
                        if(GetCheckRegister() == true)
                        {
                            checkPanel.ProcessEnable();
                            posePanel.ProcessEnable();
                            trajectoryPanel.ProcessEnable();
                            swingErrorPanel.ProcessEnable();
                            minTimeLocalPanel.ProcessEnable();
                            
                        }

                        Object[] resultObj = checkPanel.GetResultParams();
                        Object[] repeatDiff = posePanel.GetRepeatDiff();
                        Object[] trajResult = trajectoryPanel.GetResult();
                        for(int i = 0;i < sheet_result.getRows();++i) {
                            if(i < resultObj.length) {
                                resultObj[i] = sheet_result.getCell(0,i).getContents();
                            }
                            else if(i < resultObj.length+repeatDiff.length) {
                                repeatDiff[i-resultObj.length] = sheet_result.getCell(0,i).getContents();
                            }
                            else if(i < resultObj.length+repeatDiff.length+trajResult.length) {
                                trajResult[i-resultObj.length-repeatDiff.length] = sheet_result.getCell(0,i).getContents();
                            }
                        }
                        checkPanel.DispResult(resultObj);
                        posePanel.SetRepeatDiff(repeatDiff);
                        trajectoryPanel.SetResult(trajResult);

                    }
                    else if(typeSel == TYPESEL.SCARA_ROBOT) {
                        String[][] data = new String[31][7];
                        for(int i = 1;i < rowCnt_data;++i) {
                            for(int j = 0;j < columnCnt_data;++j) {
                                data[i-1][j] = sheet_data.getCell(j,i).getContents();
                            }
                        }
                        checkPanelScara.SetValueAll(data);

                        String[][] structParam = new String[4][4];
                        int rowCnt_Struct = sheet_Struct.getRows();
                        int columnCnt_Struct = sheet_Struct.getColumns();
                        for(int i = 1;i < rowCnt_Struct;++i) {
                            for(int j = 1;j < columnCnt_Struct;++j) {
                                structParam[j-1][i] = sheet_Struct.getCell(j, i).getContents();
                            }
                        }

                        String[] lengthParam = new String[sheet_Length.getColumns()-1];
                        for(int j = 1;j < sheet_Length.getColumns();++j) {
                            lengthParam[j-1] = sheet_Length.getCell(j, 1).getContents();
                        }
                        checkPanelScara.SetStructParam(structParam, lengthParam);

                        String[][] poseData = new String[11][7];
                        int rowCnt_Pose = sheet_pose.getRows();
                        int columnCnt_Pose = sheet_pose.getColumns();
                        for(int i = 1;i < rowCnt_Pose;++i) {
                            for(int j = 0;j < columnCnt_Pose;++j) {
                                poseData[i-1][j] = sheet_pose.getCell(j, i).getContents();
                            }
                        }
                        posePanelScara.SetValueAll(poseData);
//
//                        String[][] trajData = new String[3][6];
//                        int rowCnt_Traj = sheet_traj.getRows();
//                        int columnCnt_Traj = sheet_traj.getColumns();
//                        for(int i = 1;i < rowCnt_Traj;++i) {
//                            for(int j = 0;j < columnCnt_Traj;++j) {
//                                trajData[i-1][j] = sheet_traj.getCell(j,i).getContents();
//                            }
//                        }
//                        trajectoryPanel.SetValueAll(trajData);

                        if(GetCheckRegister() == true)
                        {
                            checkPanelScara.ProcessEnable();
                            posePanelScara.ProcessEnable();
//                            trajectoryPanel.ProcessEnable();
                        }

                        Object[] resultObj = checkPanelScara.GetResultParams();
                        Object[] repeatDiff = posePanelScara.GetRepeatDiff();
                        for(int i = 0;i < sheet_result.getRows();++i) {
                            if(i < resultObj.length) {
                                resultObj[i] = sheet_result.getCell(0,i).getContents();
                            }
                            else if(i < resultObj.length+repeatDiff.length) {
                                repeatDiff[i-resultObj.length] = sheet_result.getCell(0,i).getContents();
                            }
                        }
                        checkPanelScara.DispResult(resultObj);
                        posePanelScara.SetRepeatDiff(repeatDiff);
                    }
                    
                    book.close();
                }
                catch(IOException | BiffException e) {
                    System.out.println(e);
                }
            }
                 
        }
    }
    
    private void leadoutResultActionPerformed(ActionEvent ae) {
        File file;
        String fileName;
        Object[] caledStruct;
        JFileChooser saveDialog = new JFileChooser("./result");
        FileNameExtensionFilter ff = new FileNameExtensionFilter("dat文件","dat");
        saveDialog.setFileFilter(ff);
        int ret = saveDialog.showSaveDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) {
            file = saveDialog.getSelectedFile();
            fileName = file.getName();
            if(file.exists()) {
                int result = JOptionPane.showConfirmDialog(this, "是否覆盖？", "提示", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    file.delete();
                    file = saveDialog.getCurrentDirectory();
                    String fileP = file.getPath() + java.io.File.separator + fileName;
                    if(fileP.endsWith(".dat") == false) {
                        fileP += ".dat";
                    }
                    file = new File(fileP);
                    caledStruct = checkPanel.LeadoutStructParam();
                    try {
                        try (FileWriter bf = new FileWriter(file)) {
                            for(int i = 0;i < caledStruct.length;++i) {
                                bf.write(String.valueOf(caledStruct[i]));
                                bf.write('\n');
                            }
                        }
                    }
                    catch(IOException e) {
                        System.out.println(e);
                    }
                }
                else if(result == JOptionPane.NO_OPTION) {
                    
                }
            }
            else {
                file = saveDialog.getCurrentDirectory();
                String fileP = file.getPath() + java.io.File.separator + fileName;
                if(fileP.endsWith(".dat") == false) {
                    fileP += ".dat";
                }
                file = new File(fileP);
                caledStruct = checkPanel.LeadoutStructParam();
                try {
                    try(FileWriter bf = new FileWriter(file)) {
                        for(int i = 0;i < caledStruct.length;++i) {
                            bf.write(String.valueOf(caledStruct[i]));
                            bf.write('\n');
                        }
                        bf.close();
                    }
                }
                catch(IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
    
    private void leadinPointsActionPerformed(ActionEvent ae) {
        File file;
        JFileChooser openDialog = new JFileChooser("./Result");
        FileNameExtensionFilter ff = new FileNameExtensionFilter("car文件","car");
        openDialog.setFileFilter(ff);
        int ret = openDialog.showOpenDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) {
            file = openDialog.getSelectedFile();
            if(file.exists()) {
                try {
                    String str;
                    String[][] dataBuff = new String[50][6];
                    int index = 0;
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader brd = new BufferedReader(isr);
                    while((str = brd.readLine()) != null) {
                        String tempStr = str.replace(";", "");
                        String[] buf = tempStr.split(",");
                        if(buf.length != 6) {
                            JOptionPane.showMessageDialog(this, "文件格式错误!", "警告",JOptionPane.CLOSED_OPTION);
                            brd.close();
                            isr.close();
                            fis.close();
                            return;
                        }
                        else {
                            dataBuff[index] = buf;
                            ++index;
                        }
                    }
                    if(index != 50) {
                        JOptionPane.showMessageDialog(this,"文件格式错误！","警告",JOptionPane.CLOSED_OPTION);
                        brd.close();
                        isr.close();
                        fis.close();
                        return;
                    }
                    
                    if(filePath == null) {
                        JOptionPane.showMessageDialog(this, "请先载入模号！", "警告",JOptionPane.CANCEL_OPTION);
                    }
                    else {
                        File modeFile = new File(filePath);
                        try {
                            Workbook book = Workbook.getWorkbook(modeFile);
                            WritableWorkbook writeBook = Workbook.createWorkbook(modeFile, book);
                            WritableSheet sheet_data = writeBook.getSheet(0);
                            
                            
                            int rowCnt_data = sheet_data.getRows();
                            int columnCnt_data = sheet_data.getColumns();
                            if(typeSel == TYPESEL.SIXDOF_ROBOT) {
                                //Change excel data
                                for(int i = 1;i < rowCnt_data;++i) {
                                    for(int j = 0;j < columnCnt_data - 3;++j) {
                                        WritableCell cell = sheet_data.getWritableCell(j, i); //Get cell data
                                        jxl.format.CellFormat cf = cell.getCellFormat(); //Get cell format
                                        //Change cell data
                                        Label lb = new Label(j,i,dataBuff[i-1][j]);
                                        lb.setCellFormat(cf);
                                        sheet_data.addCell(lb);
                                    }
                                }
                                //Update check panel display
                                String[][] data = new String[51][9];
                                for(int i = 1;i < rowCnt_data;++i) {
                                    for(int j = 0;j < columnCnt_data;++j) {
                                        data[i-1][j] = sheet_data.getCell(j,i).getContents();
                                    }
                                }
                                checkPanel.SetValueAll(data);
                            }
                            writeBook.write();
                            writeBook.close();
                        }
                        catch(BiffException | WriteException e) {
                            System.out.println(e);
                        }
                    }
                    
                    brd.close();
                    isr.close();
                    fis.close();
                }
                catch(IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
    
    private void versionInfo_MenuItemActionPerformed(ActionEvent ae) {
        aboutDialog.setVisible(true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
        System.out.println(df.format(new Date()));
    }
    
    private void register_MenuItemActionPerformed(ActionEvent ae) {
        if(licenseDialog == null) {
            register = TrackerThread.GetRegister();
            licenseDialog = new LicenseDialog(register);
        }
        licenseDialog.setVisible(true);
    }
    private void sixDOF_ButtonActionPerformed(ActionEvent ae) {
        typeSel_Panel.setVisible(false);
        scara_Panel.setVisible(false);
        selectMode_Menu.setEnabled(true);
        typeSel = TYPESEL.SIXDOF_ROBOT;
    }
    private void scara_ButtonActionPerformed(ActionEvent ae) {
        typeSel_Panel.setVisible(false);
        main_Panel.setVisible(false);
        selectMode_Menu.setEnabled(true);
        typeSel = TYPESEL.SCARA_ROBOT;
    }
    
    public void SetModeLabelString(String str) {
        runMode_Label.setText(str);
    }
    public void SetConnectStatusString(String str) {
        connectStatus_Label.setText(str);
    }
    public void SetCmdSeqString(String str) {
        cmdSeq_Label.setText(str);
    }
    public void SetAlarmString(String str) {
        String str_tmp = "报警：" + str;
        if(!"".equals(str)) {
            alarmInfo_Label.setText(str_tmp);
        }
        else {
            alarmInfo_Label.setText("");
        }
    }
    
    public CheckPanel getCheckPanel() {
        return checkPanel;
    }
    public CheckPanelScara getCheckPanelScara() {
        return checkPanelScara;
    }
    public PosePanel getPosePanel() {
        return posePanel;
    }
    public PosePanelScara getPosePanelScara() {
        return posePanelScara;
    }
    public TrajectoryPanel getTrajPanel() {
        return trajectoryPanel;
    }
    public SwingErrorPanel getSwingErrorPanel() {
        return swingErrorPanel;
    }
    public MinTimeLocalPanel getMinTimeLocalPanel() {
        return minTimeLocalPanel;
    }
    
    public static TrackerThread getTrackerThread() {
        return trackerThread;
    }
    
    public boolean GetCheckRegister() {
        return checkRegister;
    }
    
    public TYPESEL GetTypeSel() {
        return typeSel;
    }
    
    public static void main(String[] args) {
        try{
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyTracker.class.getName()).log(java.util.logging.Level.SEVERE,null,ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                MyTracker frame = new MyTracker();
                trackerThread = new TrackerThread(frame);
                shutDown = new ShutdownProcess(trackerThread);
                frame.setVisible(true);
            }
        });
    }
    
    private JMenuBar menuBar;
    
    private JMenu selectMode_Menu;
    private JMenuItem newMode_MenuItem;
    private JMenuItem saveMode_MenuItem;
    private JMenuItem selectMode_MenuItem;
    private JMenuItem leadoutResult_MenuItem;
    private JMenuItem leadinPoints_MenuItem;
    
    private JMenu connect_Menu;
    private JMenuItem connect_MenuItem;
    private JMenuItem disconnect_MenuItem;
    
    private JMenu application_Menu;   
    private JMenuItem startupCheck_MenuItem;
    private JMenuItem exeCmdSeq_MenuItem;
    private JMenuItem trackerPanel_MenuItem;
    private JMenuItem compIT_MenuItem;
    
    private JMenu measurement_Menu;
    private JMenuItem foreground_MenuItem;
    private JMenuItem background_MenuItem;
    private JMenuItem foreAndBackground_MenuItem;
    
    private JMenu about_Menu;
    private JMenuItem versionInfo_MenuItem;
    private JMenuItem register_MenuItem;
    

    
    private JToolBar toolBar;
    
    private JPanel label_Panel;
    private JLabel runMode_Label;
    private JLabel cmdSeq_Label;
    private JLabel connectStatus_Label;
    
    private JLayeredPane main_LayeredPane;
    private JPanel typeSel_Panel;
    private JTabbedPane main_Panel;
    private JTabbedPane scara_Panel;
    
    private JPanel alarm_Panel;
    private JLabel alarmInfo_Label;
    
    private JButton sixDOF_Button;
    private JButton scara_Button;
    
    private JFileChooser fileDialog;
    
    private static TrackerThread trackerThread;
    private static ShutdownProcess shutDown;
    
    private CheckPanel checkPanel;
    private PosePanel posePanel;
    private TrajectoryPanel trajectoryPanel;
    private StabilityPanel stabilityPanel;
    private MultiDirectionPanel multiDirPanel;
    private PoseDriftPanel poseDriftPanel;
    private InterchangeabilityPanel interChangePanel;
    private DistancePanel distancePanel;
    private RepeatTrajectoryPanel repeatTrajPanel;
    private CornerPanel cornerPanel;
    private TrajectorySpeedPanel trajSpeedPanel;
    private MinTimeLocalPanel minTimeLocalPanel;
    private StaticCompliancePanel staticCompPanel;
    private SwingErrorPanel swingErrorPanel;

    
    private CheckPanelScara checkPanelScara;
    private PosePanelScara posePanelScara;
    private TrajectoryPanel trajectoryPanelScara;
    
    private AboutDialog aboutDialog;
    private LicenseDialog licenseDialog;
    
    private String filePath;
    private Register register;
    private boolean checkRegister = false;
    private boolean disp = false;
    
    private TYPESEL typeSel;
    
}
