/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import static MyTracker.TrackerThread.tracker;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author szhc
 */
public class TrajectoryPanel extends MyPanel{
    public TrajectoryPanel() {
        initComponents();
    }
    
    private void initComponents() {
//        this.setLayout(new GridBagLayout());
        this.setLayout(null);
        settingPoints_Panel = new JPanel();
        readingPoints_Panel = new JPanel();
        result_Panel = new JPanel();
        curveRot_Panel = new JPanel();
        curve_Panel = new JPanel();
        
        change_Button = new JButton("修改数据");
        startMeasure_Button = new JButton("启动测量");
        stopMeasure_Button = new JButton("停止测量");
        
        APp_Label = new JLabel("ATp：");
        APp_Value = new JLabel("0.000");
        APa_Label = new JLabel("ATa：");
        APa_Value = new JLabel("0.000");
        APb_Label = new JLabel("ATb：");
        APb_Value = new JLabel("0.000");
        APc_Label = new JLabel("ATc：");
        APc_Value = new JLabel("0.000");
        RPp_Label = new JLabel("RTp：");
        RPp_Value = new JLabel("0.000");
        RPa_Label = new JLabel("RTa：");
        RPa_Value = new JLabel("0.000");
        RPb_Label = new JLabel("RTb：");
        RPb_Value = new JLabel("0.000");
        RPc_Label = new JLabel("RTc：");
        RPc_Value = new JLabel("0.000");
        APp_Value.setBorder(BorderFactory.createEtchedBorder());
        APp_Value.setPreferredSize(new Dimension(80,22));
        APa_Value.setBorder(BorderFactory.createEtchedBorder());
        APa_Label.setPreferredSize(new Dimension(40,22));
        APa_Value.setPreferredSize(new Dimension(80,22));
        APb_Value.setBorder(BorderFactory.createEtchedBorder());
        APb_Label.setPreferredSize(new Dimension(40,22));
        APb_Value.setPreferredSize(new Dimension(80,22));
        APc_Value.setBorder(BorderFactory.createEtchedBorder());
        APc_Label.setPreferredSize(new Dimension(40,22));
        APc_Value.setPreferredSize(new Dimension(80,22));
        RPp_Value.setBorder(BorderFactory.createEtchedBorder());
        RPp_Value.setPreferredSize(new Dimension(80,22));
        RPa_Value.setBorder(BorderFactory.createEtchedBorder());
        RPa_Label.setPreferredSize(new Dimension(40,22));
        RPa_Value.setPreferredSize(new Dimension(80,22));
        RPb_Value.setBorder(BorderFactory.createEtchedBorder());
        RPb_Label.setPreferredSize(new Dimension(40,22));
        RPb_Value.setPreferredSize(new Dimension(80,22));
        RPc_Value.setBorder(BorderFactory.createEtchedBorder());
        RPc_Label.setPreferredSize(new Dimension(40,22));
        RPc_Value.setPreferredSize(new Dimension(80,22));
        JPanel ARP_Panel = new JPanel();
        ARP_Panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridx = 0;c.gridy = 0;
        ARP_Panel.add(APp_Label,c);
        c.gridx = 1;c.gridy = 0;
        ARP_Panel.add(APp_Value,c);
        c.gridx = 0;c.gridy = 1;
        ARP_Panel.add(APa_Label,c);
        c.gridx = 1;c.gridy = 1;
        ARP_Panel.add(APa_Value,c);
        c.gridx = 2;c.gridy = 1;
        ARP_Panel.add(APb_Label,c);
        c.gridx = 3;c.gridy = 1;
        ARP_Panel.add(APb_Value,c);
        c.gridx = 4;c.gridy = 1;
        ARP_Panel.add(APc_Label,c);
        c.gridx = 5;c.gridy = 1;
        ARP_Panel.add(APc_Value,c);
        c.gridx = 0;c.gridy = 2;
        ARP_Panel.add(RPp_Label,c);
        c.gridx = 1;c.gridy = 2;
        ARP_Panel.add(RPp_Value,c);
        c.gridx = 0;c.gridy = 3;
        ARP_Panel.add(RPa_Label,c);
        c.gridx = 1;c.gridy = 3;
        ARP_Panel.add(RPa_Value,c);
        c.gridx = 2;c.gridy = 3;
        ARP_Panel.add(RPb_Label,c);
        c.gridx = 3;c.gridy = 3;
        ARP_Panel.add(RPb_Value,c);
        c.gridx = 4;c.gridy = 3;
        ARP_Panel.add(RPc_Label,c);
        c.gridx = 5;c.gridy = 3;
        ARP_Panel.add(RPc_Value,c);
        result_Panel.add(ARP_Panel);
        
        chgDialog = new ChangeDialog(this);
        
        pointsList = new ArrayList<>();

        
        settingPoints_Panel.setBorder(BorderFactory.createTitledBorder("轨迹测量"));//直线端点
        readingPoints_Panel.setBorder(BorderFactory.createTitledBorder("测量点位"));
        result_Panel.setBorder(BorderFactory.createTitledBorder("测量结果"));
//        curveRot_Panel.setBorder(BorderFactory.createTitledBorder("观测偏差"));
//        curve_Panel.setBorder(BorderFactory.createTitledBorder("轨迹曲线"));
        curve_Panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        curveRot_Panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        

        
        settingPoints_Panel.setLayout(new BorderLayout());
        readingPoints_Panel.setLayout(new BorderLayout());
        
        settingPoints_Panel.setBounds(0, 0, 650, 150);
        result_Panel.setBounds(670, 0, 650, 150);
        curve_Panel.setBounds(0, 150, 650, 400);
        curveRot_Panel.setBounds(670, 150, 650, 400);
        this.add(settingPoints_Panel);
        this.add(result_Panel);   
        this.add(curveRot_Panel);
        this.add(curve_Panel);
        
        String[] settingColumn = {"点位","坐标X","坐标Y","坐标Z"};
        Object[][] settingModelData = new Object[2][4];
        for(int i = 0;i < settingModelData.length;++i) {
            for(int j = 0;j < settingModelData[0].length;++j) {
                if(i == 0 && j == 0) settingModelData[i][j] = "起点";
                else if(i == 1 && j == 0) settingModelData[i][j] = "终点";
                else settingModelData[i][j] = 0.0;
            }
        }
        settingPoints_Table = new JTable(settingModelData,settingColumn);
        DefaultTableModel settingTableModel = new DefaultTableModel(settingModelData.length,settingModelData[0].length) {
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
            @Override
            public String getColumnName(int column) {
                return settingColumn[column];
            }
            @Override
            public int getColumnCount() {
                return settingColumn.length;
            }
            @Override
            public int getRowCount() {
                return settingModelData.length;
            }
            @Override
            public Object getValueAt(int row,int column) {
                return settingModelData[row][column];
            }
            @Override
            public void setValueAt(Object obj,int row,int column) {
                settingModelData[row][column] = obj;
                fireTableCellUpdated(row,column);
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return settingModelData[0][column].getClass();
            }
        };
        settingPoints_Table.setModel(settingTableModel);
        TableColumn column = null;
        int columns = settingPoints_Table.getColumnCount();
        for(int i = 0;i < columns;++i) {
            column = settingPoints_Table.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
        DefaultTableCellRenderer settingPoints_TCR = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable talbe,Object value,boolean isSelected,
                    boolean hasFocus,int row,int column) {
                    setBackground(Color.WHITE);
                return super.getTableCellRendererComponent(talbe, value, isSelected, hasFocus, row, column);
            }
        };
        for(int i = 0;i < columns;++i) {
            settingPoints_Table.getColumn(settingPoints_Table.getColumnName(i)).setCellRenderer(settingPoints_TCR);
        }
        settingPoints_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = settingPoints_Table.getTableHeader();
        header.setFont(new Font("楷体",Font.PLAIN,14));
        header.setEnabled(false);
        settingPoints_Table.setRowHeight(20);
        settingPoints_Table.setShowGrid(true);
        settingPoints_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        settingPoints_ScrollPane = new JScrollPane(settingPoints_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        settingPoints_ScrollPane.setViewportView(settingPoints_Table);
        settingPoints_Panel.add(settingPoints_ScrollPane);
        JPanel button_Panel = new JPanel();
        button_Panel.add(change_Button);
        button_Panel.add(startMeasure_Button);
        button_Panel.add(stopMeasure_Button);
        settingPoints_Panel.add(button_Panel,BorderLayout.SOUTH);
        change_Button.setEnabled(false);
        startMeasure_Button.setEnabled(false);
        stopMeasure_Button.setEnabled(false);
        
        trajCurve = new TrajectoryCurve("轨迹曲线","");
        curve_Panel.add(trajCurve);
        trajCurve.SetChart3DSize(new Dimension(640,390));
        trajCurveRotation = new TrajectoryCurve("转换后曲线","");
        curveRot_Panel.add(trajCurveRotation);
        trajCurveRotation.SetChart3DSize(new Dimension(640,390));

        change_Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                change_ButtonActionPerformed(ae);
            }
        });
        startMeasure_Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                startMeasure_ButtonActionPerformed(ae);
            }
        });
        stopMeasure_Button.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent ae) {
               stopMeasure_ButtonActionPerformed(ae);
           }
        });
    }
    
    @Override
    public Object[] GetTableRowValue(){
        Object[] value = new Object[6];
        int row = settingPoints_Table.getSelectedRow();
        for(int i = 1;i < 7;++i) {
            value[i-1] = settingPoints_Table.getModel().getValueAt(row, i);
        }
        return value;
    }
    @Override
    public void SetTableValue(final Object[] values) {
        int row = settingPoints_Table.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 1;i < 7;++i) {
            double v = Double.parseDouble(values[i-1].toString());
            settingPoints_Table.setValueAt(df.format(v), row, i);
        }
    }
    
    private void change_ButtonActionPerformed(ActionEvent ae) {
        int row = settingPoints_Table.getSelectedRow();
        if(settingPoints_Table.isRowSelected(row)) {
            chgDialog.setVisible(true);
        }
    }
    private void startMeasure_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
            return;
        }
        if(trackerThread.GetCoordTransformFlag() == false) {
            JOptionPane.showMessageDialog(this, "请先进行自动标定或点击手动计算完成标定！");
            return;
        }
        trackerThread.ProcessChanged(CURRENTPROCESS.TRAJECTORY_PAGE_CONTINUE_PROCESS);
        trackerThread.StartBackgroundCalibration();
        trajCurve.ClearLineTrajectory();
        trajCurveRotation.ClearLineTrajectory();

    }
    
    private void stopMeasure_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
            return;
        }
        trackerThread.ProcessChanged(CURRENTPROCESS.TRAJECTORY_PAGE_STOPMEASURE_PROCESS);
    }
    
    public void SetPointsValue(int times,Object[] value) {
        if(times >= 10) {
            JOptionPane.showMessageDialog(this, "运行次数过多！");
            return;
        }
        GeneralPair<Integer ,Object[]> p = new GeneralPair(times,value);
        pointsList.add(p);
    }
    
    public Object[] GetPointsValue() {
        int cnt = 0;
        List tempList = new ArrayList<>();
        Object[] tempObj;
        Object[] ret = new Object[10*3];
        for(int i = 0;i < pointsList.size();++i) {
            GeneralPair p = (GeneralPair<Integer,Object[]>)pointsList.get(i);
            if(Integer.parseInt(p.getFirst().toString()) == 0) {
               ++cnt; 
               tempList.add(p);
            }
        }
        int pointsCnt = 0;
        double skip = cnt / 10.0;
        for(int i = 0;i < cnt;) {
            GeneralPair p = (GeneralPair<Integer,Object[]>)tempList.get(pointsCnt);
            tempObj = (Object[])p.getSecond();
            ret[pointsCnt*3 + 0] = tempObj[0];
            ret[pointsCnt*3 + 1] = tempObj[1];
            ret[pointsCnt*3 + 2] = tempObj[2];
            i+=skip;
            ++pointsCnt;
            if(pointsCnt >= 10) {
                break;
            }
        }
        return ret;
    }
    
    public void DispResult(double[] values) {
        double maxError = 0.0;
        double minError = 0.0;
        double aveError = 0.0;
        double average = 0.0;
        for(int i = 0;i < values.length;++i) {
            if(values[i] > maxError) {
                maxError = values[i];
            }
            if(values[i] < minError) {
                minError = values[i];
            }
            aveError += values[i];
        }
        if(values.length > 0) {
            aveError /= values.length;
            for(int i = 0;i < values.length;++i) {
                average += Math.pow(values[i] - aveError,2);
            }
            average /= values.length;
            aveError = Math.sqrt(average);
        }
        DecimalFormat df = new DecimalFormat("0.000");
        String v = df.format(maxError);
        //maxError_Value.setText(v);
        v = df.format(minError);
        //minError_Value.setText(v);
        v = df.format(aveError);
       // aveError_Value.setText(v);
    }
    
    public Object[] GetResult() {
        Object[] ret = new Object[3];
       // ret[0] = maxError_Value.getText();
       // ret[1] = minError_Value.getText();
       // ret[2] = aveError_Value.getText();
        return ret;
    }
    public void SetResult(Object[] values) {
        if(values.length >= 3) {
           // maxError_Value.setText(String.valueOf(values[0]));
           // minError_Value.setText(String.valueOf(values[1]));
           // aveError_Value.setText(String.valueOf(values[2]));
        }
    }
    
    public void SetValueAll(final Object[][] values) {
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 0;i < 2;++i) {
            for(int j = 1;j < 4;++j) {
                double v = Double.parseDouble(values[i][j-1].toString());
                settingPoints_Table.setValueAt(df.format(v), i, j);
            }
        }
    }
    
    public Object getValueAt(int i,int j) {
        return settingPoints_Table.getModel().getValueAt(i, j);
    }
    
    public void ProcessEnable() {
        change_Button.setEnabled(true);
        startMeasure_Button.setEnabled(true);
    }
    
    public void AddCurve(List values) {
//        double[] values = new double[300];
//        for(int i = 0;i < 298;++i) {
//            values[i] = values[i+1] = values[i+2] = i;
//        }
        double[] data = new double[values.size()];
        for(int i = 0;i < values.size();++i) {
            data[i] = Double.parseDouble(values.get(i).toString());
        }
        System.out.println("p size"+values.size());
        trajCurve.AddLineTrajectory(data);
    }
    
    public void AddCurveRotation(List values) {
        double[] data = new double[values.size()];
        for(int i = 0;i < values.size();++i) {
            data[i] = Double.parseDouble(values.get(i).toString());
        }
        trajCurveRotation.AddLineTrajectory(data);
    }
    public void RepeatDiff_RTp(double diff) {
        DecimalFormat df = new DecimalFormat("0.000");
        RPp_Value.setText(df.format(diff));
    }
    public void RepeatDiff_ATp(double diff) {
        DecimalFormat df = new DecimalFormat("0.000");
        APp_Value.setText(df.format(diff));
    }
    
    private JPanel settingPoints_Panel;
    private JPanel readingPoints_Panel;
    private JPanel result_Panel;
    private JPanel curveRot_Panel;
    private JPanel curve_Panel;
    private JScrollPane settingPoints_ScrollPane;
    private JScrollPane readingPoints_ScrollPane;
    private JScrollPane result_ScrollPane;
    
    private JLabel APp_Label;
    private JLabel APp_Value;
    private JLabel APa_Label;
    private JLabel APa_Value;
    private JLabel APb_Label;
    private JLabel APb_Value;
    private JLabel APc_Label;
    private JLabel APc_Value;
    private JLabel RPp_Label;
    private JLabel RPp_Value;
    private JLabel RPa_Label;
    private JLabel RPa_Value;
    private JLabel RPb_Label;
    private JLabel RPb_Value;
    private JLabel RPc_Label;
    private JLabel RPc_Value;
    
    private JTable settingPoints_Table;
    private JTable readingPoints_Table;
    private JTable result_Table;
    private JButton change_Button;
    private JButton startMeasure_Button;
    private JButton stopMeasure_Button;
    
    private ChangeDialog chgDialog;
    
    private TrackerThread trackerThread;
    List pointsList;
    
    TrajectoryCurve trajCurve;
    TrajectoryCurve trajCurveRotation;
}
