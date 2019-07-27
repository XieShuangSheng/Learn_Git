/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

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
        
        maxError_Label = new JLabel("最大偏差：");
        minError_Label = new JLabel("最小偏差：");
        aveError_Label = new JLabel("有效偏差：");
        maxError_Value = new JLabel("0.000");
        minError_Value = new JLabel("0.000");
        aveError_Value = new JLabel("0.000");
        
        chgDialog = new ChangeDialog(this);
        
        pointsList = new ArrayList<>();
        
//        for(int i = 0;i <10;++i) {
//            for(int j = 0;j < 200;++j) {
//                Object[] v = new Object[3];
//                v[0] = 1*1000 + j;
//                v[1] = 1*2000 + j;
//                v[2] = 1*3000 + j;
//                SetPointsValue(i,v);
//            }
//        }
//        
//        Object[] ret = GetPointsValue();
//        for(int i = 0;i < ret.length;++i) {
//            System.out.println(ret[i]);
//        }
        
        settingPoints_Panel.setBorder(BorderFactory.createTitledBorder("轨迹测量"));//直线端点
        readingPoints_Panel.setBorder(BorderFactory.createTitledBorder("测量点位"));
        result_Panel.setBorder(BorderFactory.createTitledBorder("测量结果"));
//        curveRot_Panel.setBorder(BorderFactory.createTitledBorder("观测偏差"));
//        curve_Panel.setBorder(BorderFactory.createTitledBorder("轨迹曲线"));
        curve_Panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        curveRot_Panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        
        Dimension dLabel = new Dimension(60,20);
        Dimension dValue = new Dimension(80,20);
        maxError_Value.setBorder(BorderFactory.createEtchedBorder());
        minError_Value.setBorder(BorderFactory.createEtchedBorder());
        aveError_Value.setBorder(BorderFactory.createEtchedBorder());
        maxError_Label.setPreferredSize(dLabel);
        minError_Label.setPreferredSize(dLabel);
        aveError_Label.setPreferredSize(dLabel);
        maxError_Value.setPreferredSize(dValue);
        minError_Value.setPreferredSize(dValue);
        aveError_Value.setPreferredSize(dValue);
        result_Panel.setLayout(new FlowLayout());
        result_Panel.add(maxError_Label);
        result_Panel.add(maxError_Value);
        result_Panel.add(minError_Label);
        result_Panel.add(minError_Value);
        result_Panel.add(aveError_Label);
        result_Panel.add(aveError_Value);
        
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
        
//        String[] readingColumn = {"点位","坐标X","坐标Y","坐标Z"};
//        Object[][] readingModelData = new Object[20][4];
//        for(int i = 0;i < readingModelData.length;++i) {
//            for(int j = 0;j < readingModelData[0].length;++j) {
//                if(j == 0) readingModelData[i][j] = i+1;
//                else readingModelData[i][j] = 0.0;
//            }
//        }
//        readingPoints_Table = new JTable(readingModelData,readingColumn);
//        readingPoints_Panel.add(readingPoints_Table);
//        DefaultTableModel readingTableModel = new DefaultTableModel(readingModelData.length,readingModelData[0].length) {
//            @Override
//            public boolean isCellEditable(int row,int column) {
//                return false;
//            }
//            @Override
//            public String getColumnName(int column) {
//                return readingColumn[column];
//            }
//            @Override
//            public int getColumnCount() {
//                return readingColumn.length;
//            }
//            @Override
//            public int getRowCount() {
//                return readingModelData.length;
//            }
//            @Override
//            public Object getValueAt(int row,int column) {
//                return readingModelData[row][column];
//            }
//            @Override
//            public void setValueAt(Object obj,int row,int column) {
//                readingModelData[row][column] = obj;
//                fireTableCellUpdated(row,column);
//            }
//            @Override
//            public Class<?> getColumnClass(int column) {
//                return readingModelData[0][column].getClass();
//            }
//        };
//        readingPoints_Table.setModel(readingTableModel);
//        columns = readingPoints_Table.getColumnCount();
//        for(int i = 0;i < columns;++i) {
//            column = readingPoints_Table.getColumnModel().getColumn(i);
//            column.setPreferredWidth(80);
//        }
//        DefaultTableCellRenderer readingPoints_TCR = new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable talbe,Object value,boolean isSelected,
//                    boolean hasFocus,int row,int column) {
//                if(row % 2 == 0) {
//                    setBackground(new Color(203,203,203));
//                }
//                else {
//                    setBackground(Color.WHITE);
//                }
//                return super.getTableCellRendererComponent(talbe, value, isSelected, hasFocus, row, column);
//            }
//        };
//        for(int i = 0;i < columns;++i) {
//            readingPoints_Table.getColumn(readingPoints_Table.getColumnName(i)).setCellRenderer(readingPoints_TCR);
//        }
//        readingPoints_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        JTableHeader reading_header = readingPoints_Table.getTableHeader();
//        reading_header.setFont(new Font("楷体",Font.PLAIN,14));
//        readingPoints_Table.setRowHeight(20);
//        readingPoints_Table.setShowGrid(true);
//        readingPoints_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        readingPoints_ScrollPane = new JScrollPane(readingPoints_Table,
//                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
//                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        readingPoints_ScrollPane.setViewportView(readingPoints_Table);
//        
//        readingPoints_Panel.add(readingPoints_ScrollPane);
//        readingPoints_Panel.add(startMeasure_Button,BorderLayout.SOUTH);
        



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
        trackerThread.ProcessChanged(CURRENTPROCESS.TRAJECTORY_PAGE_CONTINUE_PROCESS);
        trackerThread.StartBackgroundCalibration();
        trajCurve.ClearLineTrajectory();
        trajCurveRotation.ClearLineTrajectory();
        maxError_Value.setText("0.0000");
        minError_Value.setText("0.0000");
        aveError_Value.setText("0.0000");
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
        Object[] ret = new Object[50*3];
        for(int i = 0;i < pointsList.size();++i) {
            GeneralPair p = (GeneralPair<Integer,Object[]>)pointsList.get(i);
            if(Integer.parseInt(p.getFirst().toString()) == 0) {
               ++cnt; 
               tempList.add(p);
            }
        }
        int pointsCnt = 0;
        double skip = cnt / 50.0;
        for(int i = 0;i < cnt;) {
            GeneralPair p = (GeneralPair<Integer,Object[]>)tempList.get(pointsCnt);
            tempObj = (Object[])p.getSecond();
            ret[pointsCnt*3 + 0] = tempObj[0];
            ret[pointsCnt*3 + 1] = tempObj[1];
            ret[pointsCnt*3 + 2] = tempObj[2];
            i+=skip;
            ++pointsCnt;
            if(pointsCnt >= 50) {
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
        maxError_Value.setText(v);
        v = df.format(minError);
        minError_Value.setText(v);
        v = df.format(aveError);
        aveError_Value.setText(v);
    }
    
    public Object[] GetResult() {
        Object[] ret = new Object[3];
        ret[0] = maxError_Value.getText();
        ret[1] = minError_Value.getText();
        ret[2] = aveError_Value.getText();
        return ret;
    }
    public void SetResult(Object[] values) {
        if(values.length >= 3) {
            maxError_Value.setText(String.valueOf(values[0]));
            minError_Value.setText(String.valueOf(values[1]));
            aveError_Value.setText(String.valueOf(values[2]));
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
        trajCurve.AddLineTrajectory(data);
    }
    
    public void AddCurveRotation(List values) {
        double[] data = new double[values.size()];
        for(int i = 0;i < values.size();++i) {
            data[i] = Double.parseDouble(values.get(i).toString());
        }
        trajCurveRotation.AddLineTrajectory(data);
    }
    
    private JPanel settingPoints_Panel;
    private JPanel readingPoints_Panel;
    private JPanel result_Panel;
    private JPanel curveRot_Panel;
    private JPanel curve_Panel;
    private JScrollPane settingPoints_ScrollPane;
    private JScrollPane readingPoints_ScrollPane;
    private JScrollPane result_ScrollPane;
    private JLabel maxError_Label;
    private JLabel minError_Label;
    private JLabel aveError_Label;
    private JLabel maxError_Value;
    private JLabel minError_Value;
    private JLabel aveError_Value;
    
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
