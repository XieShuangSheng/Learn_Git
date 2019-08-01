/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author szhc
 */
public class MinTimeLocalPanel extends MyPanel{
    public MinTimeLocalPanel() {
        initComponents();
    }
    private void initComponents() {
        minTimeLocal_Panel = new JPanel();
        minTimeResult_Panel = new JPanel();
        minTime19Table_Panel = new JPanel();
        minTime20Table_Panel = new JPanel();
        minTimeLoops_Panel = new JPanel();
        
        ImageIcon Table19_img;
        ImageIcon Table20_img;
        ImageIcon PointsLoops_img;
        Table19_img = new ImageIcon("Res/minTime19.png");
        Table20_img = new ImageIcon("Res/minTime20.png");
        PointsLoops_img = new ImageIcon("Res/minTimeLoops.png");
        Table19_Label = new JLabel(Table19_img);
        Table20_Label = new JLabel(Table20_img);
        PointsLoops_Label = new JLabel(PointsLoops_img);
        
        minTimeLocal_Panel.setBorder(BorderFactory.createTitledBorder("最小定位时间"));
        minTimeResult_Panel.setBorder(BorderFactory.createTitledBorder("测试结果"));
        minTime19Table_Panel.setBorder(BorderFactory.createTitledBorder("表19  最小定位时间的试验位姿与距离"));
        minTime20Table_Panel.setBorder(BorderFactory.createTitledBorder("表20  最小定位时间的试验条件"));
        minTimeLoops_Panel.setBorder(BorderFactory.createTitledBorder("点位循环图示"));

        this.setLayout(null);
        minTimeLocal_Panel.setBounds(0,0,550,400);
        minTimeResult_Panel.setBounds(0,400,800,400);
        minTime19Table_Panel.setBounds(550,0,900,170);
        minTime20Table_Panel.setBounds(550,170,900,230);
        minTimeLoops_Panel.setBounds(800,400,600,400);
                
        this.add(minTimeLocal_Panel);
        this.add(minTimeResult_Panel);
        this.add(minTime19Table_Panel);
        this.add(minTime20Table_Panel);
        this.add(minTimeLoops_Panel);
        
        minTime19Table_Panel.add(Table19_Label);
        minTime20Table_Panel.add(Table20_Label);
        minTimeLoops_Panel.add(PointsLoops_Label);

        
        //点位表格设置
        String[] PointsColumnNames = {"点位","指令坐标X","指令坐标Y","指令坐标Z","实际坐标X","实际坐标Y","实际坐标Z"};
        Object[][] PointsmodelData = new Object[8][7];
        for(int i = 0;i < PointsmodelData.length;++i) {
            for(int j = 0;j < PointsmodelData[0].length;++j) {
                if(j == 0) {
                    if(i == 0){
                        PointsmodelData[i][j] = "P" + String.valueOf(i);
                    }
                    else
                        PointsmodelData[i][j] = "P1+" + String.valueOf(i);
                }
                else {
                    PointsmodelData[i][j] = 0.0;
                }
            }
        }
        points_Table = new JTable(PointsmodelData,PointsColumnNames);
        minTimeLocal_Panel.add(points_Table);
        DefaultTableModel PointsTableModel = new DefaultTableModel(PointsmodelData.length,PointsmodelData[0].length) {
            // 以下为继承自AbstractTableModle的方法，可以自定义 
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;//editable[column];
            }
            //得到列名
            @Override
            public String getColumnName(int column) {
                return PointsColumnNames[column];
            }
            //得到列数
            @Override
            public int getColumnCount() {
                return PointsColumnNames.length;
            }
            //得到行数
            @Override
            public int getRowCount() {
                return PointsmodelData.length;
            }
            //得到数据对应的对象
            @Override
            public Object getValueAt(int rowIndex,int columnIndex) {
                return PointsmodelData[rowIndex][columnIndex];
            }
            
            //设置对应的对象
            @Override
            public void setValueAt(Object object,int rowIndex,int columnIndex) {
                PointsmodelData[rowIndex][columnIndex] = object;
                //通知监听器数据单元数据已经改变
                fireTableCellUpdated(rowIndex,columnIndex);
            }
            
            //得到指定列的数据类型
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return PointsmodelData[0][columnIndex].getClass();
            }
        };
        points_Table.setModel(PointsTableModel);
        TableColumn points_column;
        int  points_columns = points_Table.getColumnCount();
        for(int i = 0;i <  points_columns;++i) {
             points_column = points_Table.getColumnModel().getColumn(i);
            if(i == 0) {
                 points_column.setPreferredWidth(40);
            }
            else {
                 points_column.setPreferredWidth(80);
            }
        }
        DefaultTableCellRenderer points_tcr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,  
                    Object value, boolean isSelected, boolean hasFocus,  
                    int row, int column) {  
                if (row % 2 == 0) {  
                    setBackground(new Color(203, 203, 203));  
  
                }
                else {  
                    setBackground(Color.WHITE);  
                }
                return super.getTableCellRendererComponent(table, value,  
                        isSelected, hasFocus, row, column);  
            }  
        }; 
        for (int i = 0; i < points_columns; i++) {  
            points_Table.getColumn(points_Table.getColumnName(i)).setCellRenderer(points_tcr);  
        }
        points_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader points_header = points_Table.getTableHeader();
        points_header.setFont(new Font("楷体",Font.PLAIN,14));
        points_header.setEnabled(false);
        points_Table.setRowHeight(35);//设置行宽
        points_Table.setShowGrid(true);//显示网格
        points_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单行选中
        
        points_ScrollPane = new JScrollPane(points_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        points_ScrollPane.setPreferredSize(new Dimension(530,310));
        points_ScrollPane.setViewportView(points_Table);
        minTimeLocal_Panel.add(points_ScrollPane);
        
        button_Panel = new JPanel();
        Loops_Label = new JLabel("循环次数");
        Loops_Value = new JLabel("1");
        Loops_Value.setBorder(BorderFactory.createEtchedBorder());
        Loops_Value.setPreferredSize(new Dimension(25,25));
        button_Panel.add(Loops_Label);
        button_Panel.add( Loops_Value);
        minTimeLocal_Panel.add(button_Panel,BorderLayout.SOUTH);
        
        //singleRead_Button = new JButton("手动读取");
        autoRead_Button = new JButton("启动检测");
        breakAuto_Button = new JButton("终止检测");
        //singleRead_Button.setEnabled(false);
        autoRead_Button.setEnabled(false);
        breakAuto_Button.setEnabled(false);
        //button_Panel.add(singleRead_Button);
        button_Panel.add(autoRead_Button);
        button_Panel.add(breakAuto_Button);
        
        //启动检测
        autoRead_Button.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent ae) {
               autoRead_ButtonActionPerformed(ae);
           }
        });
        //终止检测
        breakAuto_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                breakAuto_ButtonActionPerformed(ae);
            }
        });
        
        
        
        //测试结果表格设置
        String[] ResultColumnNames = {"次数","","P0","P1+1","P1+2","P1+3","P1+4","P1+5","P1+6","P1+7"};
        Object[][] ResultmodelData = new Object[8][10];
        for(int i = 0;i < ResultmodelData.length;++i) {
            for(int j = 0;j < ResultmodelData[0].length;++j) {
                switch (j) {
                    case 0:
                        switch (i) {
                            case 0:
                            case 1:                           
                                ResultmodelData[i][j] = "1";
                                break;
                            case 2:
                            case 3:
                                ResultmodelData[i][j] = "2";
                                break;
                            case 4:
                            case 5:
                                ResultmodelData[i][j] = "3";
                                break;
                            case 6:
                            case 7:
                                ResultmodelData[i][j] = "平均";
                                break;
                            default:
                                break;
                        }   break;
                    case 1:
                        if(i % 2 == 0){
                            ResultmodelData[i][j] = "距离";
                        }
                        else
                            ResultmodelData[i][j] = "时间";
                        break;
                    default:
                        ResultmodelData[i][j] = 0.0;
                        break;
                }   
            }
        }
        result_Table = new JTable(ResultmodelData,ResultColumnNames);
        minTimeResult_Panel.add(result_Table);
        DefaultTableModel ResultTableModel = new DefaultTableModel(ResultmodelData.length,ResultmodelData[0].length) {
            // 以下为继承自AbstractTableModle的方法，可以自定义 
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;//editable[column];
            }
            //得到列名
            @Override
            public String getColumnName(int column) {
                return ResultColumnNames[column];
            }
            //得到列数
            @Override
            public int getColumnCount() {
                return ResultColumnNames.length;
            }
            //得到行数
            @Override
            public int getRowCount() {
                return ResultmodelData.length;
            }
            //得到数据对应的对象
            @Override
            public Object getValueAt(int rowIndex,int columnIndex) {
                return ResultmodelData[rowIndex][columnIndex];
            }
            
            //设置对应的对象
            @Override
            public void setValueAt(Object object,int rowIndex,int columnIndex) {
                ResultmodelData[rowIndex][columnIndex] = object;
                //通知监听器数据单元数据已经改变
                fireTableCellUpdated(rowIndex,columnIndex);
            }
            
            //得到指定列的数据类型
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return ResultmodelData[0][columnIndex].getClass();
            }
        };
        result_Table.setModel(ResultTableModel);
        TableColumn result_column;
        int result_columns = result_Table.getColumnCount();
        for(int i = 0;i < result_columns;++i) {
            result_column = result_Table.getColumnModel().getColumn(i);
            if(i == 0) {
                result_column.setPreferredWidth(40);
            }
            else {
                result_column.setPreferredWidth(80);
            }
        }
        DefaultTableCellRenderer result_tcr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,  
                    Object value, boolean isSelected, boolean hasFocus,  
                    int row, int column) {  
                if (row % 2 == 0) {  
                    setBackground(new Color(203, 203, 203));  
  
                }
                else {  
                    setBackground(Color.WHITE);  
                }
                return super.getTableCellRendererComponent(table, value,  
                        isSelected, hasFocus, row, column);  
            }  
        }; 
        for (int i = 0; i < result_columns; i++) {  
            result_Table.getColumn(result_Table.getColumnName(i)).setCellRenderer(result_tcr);  
        }
        result_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader result_header = result_Table.getTableHeader();
        result_header.setFont(new Font("楷体",Font.PLAIN,14));
        result_header.setEnabled(false);
        result_Table.setRowHeight(35);//设置行宽
        result_Table.setShowGrid(true);//显示网格
        result_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单行选中
        
        result_ScrollPane = new JScrollPane(result_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        result_ScrollPane.setPreferredSize(new Dimension(765,310));
        result_ScrollPane.setViewportView(result_Table);
        minTimeResult_Panel.add(result_ScrollPane);
          
        
    }
    
    public void SetValueAll(final Object[][] values) {
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 0;i < 8;++i) {
            for(int j = 1;j < 7;++j) {
                
                double v = Double.parseDouble(values[i][j-1].toString());
                points_Table.setValueAt(df.format(v), i, j);
            }
        }
    }
    
    public void ProcessEnable() {
        autoRead_Button.setEnabled(true);
        breakAuto_Button.setEnabled(true);
    }
    
    private void autoRead_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS 
                && trackerThread.GetCurrProcess() != CURRENTPROCESS.MINTIME_PAGE_BREAK_AUTO_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
            return;
        }  /*
        if(trackerThread.GetCoordTransformFlag() == false) {
            JOptionPane.showMessageDialog(this, "请先进行自动标定或点击手动计算完成标定！");
            return;
        }*/
        trackerThread.ProcessChanged(CURRENTPROCESS.MINTIME_PAGE_CONTINUE_PROCESS);
        trackerThread.StartCalibration();
    }
    private void breakAuto_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        trackerThread.ProcessChanged(CURRENTPROCESS.MINTIME_PAGE_BREAK_AUTO_PROCESS);
        trackerThread.frame.SetCmdSeqString("终止检测中···");

    }
    public void SetTrackerValue(final Object[] values) {
        int row = points_Table.getSelectedRow();
        if(row == -1){
            row += 1;
        }
        System.out.println("SetTrackerValue_row(386):" + row);
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        if(points_Table.isRowSelected(row)) { 
            for(int i = 4;i < 7;++i) {
                double v = Double.parseDouble(values[i-4].toString());
                points_Table.setValueAt(df.format(v),row,i);
            }
        }
    }  
    
    public void SetResultValue(final double[] values) {
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        int n = 0;
        for(int i = 0;i < 8;++i){
            for(int j = 2;j < 10;++j) {
                switch (i) {
                            case 0:
                            case 2: 
                            case 4:
                            case 6:   
                                double v = values[n];
                                ++n;
                                result_Table.setValueAt(df.format(v),i,j);               
                                break;
                            default:
                                break;
                }
            }      
        }
    }
    public int GetSelectionRow() {
        return points_Table.getSelectedRow();
    }
    //滚动条跟随表中选择的行自动滚动
    public void SetSelectionRow(int row) {
        int width = points_ScrollPane.getViewport().getHeight();
        Point p = points_ScrollPane.getViewport().getViewPosition();
        JTableHeader header = points_Table.getTableHeader();
        int rowWidth = header.getHeight() + points_Table.getRowHeight() * row;
        if(rowWidth > width) {
            p.setLocation(p.getX(), points_Table.getRowHeight() * (row + 2) - width);
        }
        else {
            p.setLocation(p.getX(),0);
        }
        points_ScrollPane.getViewport().setViewPosition(p);
        points_Table.setRowSelectionInterval(row, row);
    }
    public void Repeat_Data_MT(double diff) {
        DecimalFormat df = new DecimalFormat("0");
        Loops_Value.setText(df.format(diff));
    }
    public double[][] GetTableValue(){
        int rowCnt = points_Table.getRowCount();
        int columnCnt = points_Table.getColumnCount();
        System.out.println("rowCnt:" + rowCnt);
        System.out.println("columnCnt:" + columnCnt);
        Object[][] value = new Object[rowCnt][columnCnt -1];
        double[][] number = new double[rowCnt][columnCnt -1];
        for(int i = 0; i < rowCnt;++i) {
            for(int j = 1;j < columnCnt;++j) {
                value[i][j-1] = points_Table.getModel().getValueAt(i, j);
                number[i][j-1] = Double.parseDouble(value[i][j-1].toString());
            }
        }
        return number;
    }
   
    private JPanel minTimeLocal_Panel;
    private JPanel minTimeResult_Panel;
    private JPanel minTime19Table_Panel;
    private JPanel minTime20Table_Panel;
    private JPanel minTimeLoops_Panel;
    private JPanel button_Panel;
    
    private JLabel Table19_Label;
    private JLabel Table20_Label;
    private JLabel PointsLoops_Label;
    
    private JTable points_Table;
    private JScrollPane points_ScrollPane;
    private JTable result_Table;
    private JScrollPane result_ScrollPane;
    
    private JLabel Loops_Label;
    private JLabel Loops_Value;
    
   //private JButton singleRead_Button;
    private JButton autoRead_Button;
    private JButton breakAuto_Button;
    
    private TrackerThread trackerThread;

}
