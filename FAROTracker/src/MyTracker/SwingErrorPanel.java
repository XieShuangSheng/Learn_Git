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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author xss
 */
public class SwingErrorPanel extends MyPanel{
    public SwingErrorPanel() {
        initComponents();
    }
    private void initComponents() {
        points_Panel = new JPanel();
        button_Panel = new JPanel();
        result_Panel = new JPanel();
        sketch_Panel = new JPanel();
        sketch1_Panel = new JPanel();
        Swing_Error_Parameter = new JPanel();
        
        SetSpeed_Button = new JButton("修改速度");
        singleRead_Button = new JButton("手动读取");
        change_Button = new JButton("修改坐标");
        autoRead_Button = new JButton("启动检测");
        breakAuto_Button = new JButton("终止检测");
        ImageIcon SwingError_img;
        ImageIcon SwingError1_img;
        SwingError_img = new ImageIcon("Res/SwingError.png");
        SwingError1_img = new ImageIcon("Res/SwingError1.png");
        sketch_Label = new JLabel(SwingError_img);
        sketch1_Label = new JLabel(SwingError1_img);
        String   strMsg = "<html><body>"+s+"<br>"+"<br>"+s0+"<br>"+s0_1+"<br>"+"<br>"+"<br>"+s1+"<br>"+s1_1+"<br>"
                +"<br>"+"<br>"+s2+"<br>"+s2_1+"<br>"+s2_2+"<br>"+"<br>"+"<br>"+"<br>"+"<body></html>";
        parameter = new JLabel(strMsg);
                
        points_Panel.setBorder(BorderFactory.createTitledBorder("点位——指令、实际坐标"));
        result_Panel.setBorder(BorderFactory.createTitledBorder("检测结果"));
        sketch_Panel.setBorder(BorderFactory.createTitledBorder("实际和指令摆动轨迹示意图"));
        sketch1_Panel.setBorder(BorderFactory.createTitledBorder("所选平面内的摆动试验轨迹示意图"));
        Swing_Error_Parameter.setBorder(BorderFactory.createTitledBorder("参数说明"));
        sketch_Panel.add(sketch_Label);
        sketch1_Panel.add(sketch1_Label);
        Swing_Error_Parameter.add(parameter);
        
        points_Panel.setLayout(new BorderLayout());

        button_Panel.add(SetSpeed_Button);
        button_Panel.add(singleRead_Button);
        button_Panel.add(change_Button);
        button_Panel.add(autoRead_Button);
        button_Panel.add(breakAuto_Button);
        points_Panel.add(button_Panel,BorderLayout.SOUTH);
        
        SetSpeed_Button.setEnabled(true);
        singleRead_Button.setEnabled(false);
        change_Button.setEnabled(false);
        autoRead_Button.setEnabled(false);
        breakAuto_Button.setEnabled(false);
        
        //this.setLayout(new GridLayout(1,2));
        this.setLayout(null);
        points_Panel.setBounds(0,0,800,400);
        result_Panel.setBounds(0,400,1200,200);
        sketch_Panel.setBounds(800,0,750,400);
        sketch1_Panel.setBounds(1200,400,350,480);
        Swing_Error_Parameter.setBounds(0,600, 600, 280);
        this.add(points_Panel);
        this.add(result_Panel);
        this.add(sketch_Panel);
        this.add(sketch1_Panel);
        this.add(Swing_Error_Parameter);
        
        String[] columnNames = {"点位","指令坐标X","指令坐标Y","指令坐标Z","实际坐标X","实际坐标Y","实际坐标Z"};
        Object[][] modelData = new Object[24][7];
        for(int i = 0;i < modelData.length;++i) {
            for(int j = 0;j < modelData[0].length;++j) {
                if(j == 0) {
                    modelData[i][j] = " " + String.valueOf(i+1);
                }
                else {
                    modelData[i][j] = 0.0;
                }
            }
        }
        points_Table = new JTable(modelData,columnNames);
        points_Panel.add(points_Table);
                DefaultTableModel tableModel = new DefaultTableModel(modelData.length,modelData[0].length) {
            // 以下为继承自AbstractTableModle的方法，可以自定义 
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;//editable[column];
            }
            //得到列名
            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }
            //得到列数
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }
            //得到行数
            @Override
            public int getRowCount() {
                return modelData.length;
            }
            //得到数据对应的对象
            @Override
            public Object getValueAt(int rowIndex,int columnIndex) {
                return modelData[rowIndex][columnIndex];
            }
            
            //设置对应的对象
            @Override
            public void setValueAt(Object object,int rowIndex,int columnIndex) {
                modelData[rowIndex][columnIndex] = object;
                //通知监听器数据单元数据已经改变
                fireTableCellUpdated(rowIndex,columnIndex);
            }
            
            //得到指定列的数据类型
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return modelData[0][columnIndex].getClass();
            }
        };
        points_Table.setModel(tableModel);
        
        TableColumn column;
        int columns = points_Table.getColumnCount();
        for(int i = 0;i < columns;++i) {
            column = points_Table.getColumnModel().getColumn(i);
            if(i == 0) {
                column.setPreferredWidth(40);
            }
            else {
                column.setPreferredWidth(100);
            }
        }
        
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
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
        for (int i = 0; i < columns; i++) {  
            points_Table.getColumn(points_Table.getColumnName(i)).setCellRenderer(tcr);  
        }

        points_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = points_Table.getTableHeader();
        header.setFont(new Font("楷体",Font.PLAIN,14));
        header.setEnabled(false);
        points_Table.setRowHeight(30);//设置行宽
        points_Table.setShowGrid(true);//显示网格
        points_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单行选中
        
        points_ScrollPane = new JScrollPane(points_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        points_ScrollPane.setPreferredSize(new Dimension(750,60));
        points_ScrollPane.setViewportView(points_Table);
        points_Panel.add(points_ScrollPane);
        
        
        String[] result_columnNames = {"循环次数","指令摆幅Sc","实际摆幅Sa","指令摆动速度WVc",
            "实际摆动速度WVa","指令摆动距离WDc","实际摆动距离WDa","指令摆频Fc","实际摆频Fa"};
        Object[][] result_modelData = new Object[3][9];
        for(int i = 0;i < result_modelData.length;++i) {
            for(int j = 0;j < result_modelData[0].length;++j) {
                if(j == 0) {
                    result_modelData[i][j] = i+1;
                }
                else {
                    result_modelData[i][j] = 0.0;
                }
            }
        }
        
        result_Table = new JTable(result_modelData,result_columnNames);
        result_Panel.add(result_Table);
        DefaultTableModel result_tableModel = new DefaultTableModel(result_modelData.length,result_modelData[0].length) {
            // 以下为继承自AbstractTableModle的方法，可以自定义 
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
            //得到列名
            @Override
            public String getColumnName(int column) {
                return result_columnNames[column];
            }
            //得到列数
            @Override
            public int getColumnCount() {
                return result_columnNames.length;
            }
            //得到行数
            @Override
            public int getRowCount() {
                return result_modelData.length;
            }
            //得到数据对应的对象
            @Override
            public Object getValueAt(int rowIndex,int columnIndex) {
                return result_modelData[rowIndex][columnIndex];
            }
            
            //设置对应的对象
            @Override
            public void setValueAt(Object object,int rowIndex,int columnIndex) {
                result_modelData[rowIndex][columnIndex] = object;
                //通知监听器数据单元数据已经改变
                fireTableCellUpdated(rowIndex,columnIndex);
            }
            
            //得到指定列的数据类型
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return result_modelData[0][columnIndex].getClass();
            }
        };
        result_Table.setModel(result_tableModel);
        
        //修改速度
        SetSpeed_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ChangeSpeed_ButtonActionPerformed(ae);

            }
        });
        //手动读取
        singleRead_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                singleRead_ButtonActionPerformed(ae);
            }
        });
        //修改坐标
        change_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                change_ButtonActionPerformed(ae);
            }
        });
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
        
        
        TableColumn result_column;
        int result_columns = result_Table.getColumnCount();
        for(int i = 0;i < result_columns;++i) {
            result_column = result_Table.getColumnModel().getColumn(i);
            result_column.setPreferredWidth(130);
        }
        
        DefaultTableCellRenderer result_tcr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,  
                    Object value, boolean isSelected, boolean hasFocus,  
                    int row, int column) {
                
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
        header.setEnabled(false);
        result_Table.setRowHeight(30);//设置行宽
        result_Table.setShowGrid(true);//显示网格
        result_Table.setEnabled(false);
        
        result_ScrollPane = new JScrollPane(result_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        result_ScrollPane.setPreferredSize(new Dimension(1180,120));
        result_ScrollPane.setViewportView(result_Table);
        result_Panel.add(result_ScrollPane);
        
        WS_Label = new JLabel("摆幅误差(WS)%");
        WS_Value = new JLabel("0.000");
        WF_Label = new JLabel("摆频误差(WF)%");
        WF_Value = new JLabel("0.000");
        WS_Value.setBorder(BorderFactory.createEtchedBorder());
        WS_Value.setPreferredSize(new Dimension(100,30));
        WF_Value.setBorder(BorderFactory.createEtchedBorder());
        WF_Value.setPreferredSize(new Dimension(100,30));

        JPanel SwingError_Panel = new JPanel();
        SwingError_Panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridx = 0;c.gridy = 0;
        SwingError_Panel.add(WS_Label,c);
        c.gridx = 1;c.gridy = 0;
        SwingError_Panel.add(WS_Value,c);
        c.gridx = 2;c.gridy = 0;
        SwingError_Panel.add(WF_Label,c);
        c.gridx = 3;c.gridy = 0;
        SwingError_Panel.add(WF_Value,c);

        result_Panel.add(SwingError_Panel);
    }
    
    public void SetSelectionRow(int row) {
        int width = scrollPane.getViewport().getHeight();
        Point p = scrollPane.getViewport().getViewPosition();
        JTableHeader header = points_Table.getTableHeader();
        int rowWidth = header.getHeight() + points_Table.getRowHeight() * row;
        
        if(rowWidth > width) {
            p.setLocation(p.getX(), points_Table.getRowHeight() * (row + 2) - width);
        }
        else {
            p.setLocation(p.getX(),0);
        }
        scrollPane.getViewport().setViewPosition(p);
        points_Table.setRowSelectionInterval(row, row);
    }
    
      
    public void SetTrackerValue(final Object[] values) {
        int row = points_Table.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        if(points_Table.isRowSelected(row)) {
            for(int i = 7;i < 10;++i) {
                double v = Double.parseDouble(values[i-7].toString());
                points_Table.setValueAt(df.format(v),row,i);
            }
        }
    }  
    
    public void SetResultTableValue(final double[][] values) {
        int row = result_Table.getSelectedRow();
        int column = result_Table.getSelectedColumn();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        if(result_Table.isRowSelected(row)) {
            for(int i = 0;i < row;++i) {
                for(int j = 0; j < column; j++){
                    double v = values[i][j];
                    result_Table.setValueAt(df.format(v),i+1,j);
                }
            }
        }
    }
    
    public void SetValueAll(final Object[][] values) {
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 0;i < 24;++i) {
            for(int j = 1;j < 7;++j) {
                double v = Double.parseDouble(values[i][j-1].toString());
                points_Table.setValueAt(df.format(v), i, j);
            }
        }
    }
    
    public int GetSelectionRow() {
        return points_Table.getSelectedRow();
    }
    
    public int GetPointsTableRow() {
        return points_Table.getRowCount();
    }
    public int GetPointsTableColumn() {
        return points_Table.getColumnCount();
    }
    
    public double[][] GetTableValue(){
        int rowCnt = points_Table.getRowCount();
        int columnCnt = points_Table.getColumnCount();
        double[][] value = new double[rowCnt][columnCnt];
        for(int i = 0; i < rowCnt;++i) {
            for(int j = 1;j < columnCnt;++j) {
                value[i][j-1] = (double) points_Table.getModel().getValueAt(i, j);
            }
        }
        return value;
    }
    
    public void Repeat_Data_WS(double diff) {
        DecimalFormat df = new DecimalFormat("0.000");
        WS_Value.setText(df.format(diff));
    }
    public void Repeat_Data_WF(double diff) {
        DecimalFormat df = new DecimalFormat("0.000");
        WF_Value.setText(df.format(diff));
    }
    
    public void ProcessEnable() {
        singleRead_Button.setEnabled(true);
        change_Button.setEnabled(true);        
        autoRead_Button.setEnabled(true);
        breakAuto_Button.setEnabled(true);
    }
    
    private void ChangeSpeed_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        JDialog SetSpeed = new JDialog();
        SetSpeed.setTitle("指令摆动速度设置");
        SetSpeed.setLayout(null);
        SetSpeed.setBounds(710, 190, 500, 500);
        SetSpeed.setModal(true);
        SetSpeed.setVisible(true);
        SetSpeed.setEnabled(true);
        SetSpeed.setResizable(false);
        
        
        Speed_Label = new JLabel("指令速度：");
        Speed_Value = new JLabel("0.000");
        //Speed_Label.set
        SetSpeed.add(Speed_Label);
        SetSpeed.add(Speed_Value);
    }
    
    private void singleRead_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
            return;
        }
        trackerThread.ProcessChanged(CURRENTPROCESS.SWINGERROR_PAGE_SINGLEREAD_PROCESS);
        trackerThread.SingleReadMeasure();
    }
    
    private void change_ButtonActionPerformed(ActionEvent ae) {
        int row = points_Table.getSelectedRow();
        if(points_Table.isRowSelected(row)) {
            chgDialog.setVisible(true);
        }
    }
    
    private void autoRead_ButtonActionPerformed(ActionEvent ae) {
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
        trackerThread.ProcessChanged(CURRENTPROCESS.SWINGERROR_PAGE_CONTINUE_PROCESS);
        trackerThread.StartCalibration();
    }
    
    private void breakAuto_ButtonActionPerformed(ActionEvent ae) {
        trackerThread.ProcessChanged(CURRENTPROCESS.SWINGERROR_PAGE_BREAK_AUTO_PROCESS);
    }
    
    
    
    
    
    private JScrollPane scrollPane;
    
    private TrackerThread trackerThread;
    private ChangeDialog chgDialog;
    
    private JPanel points_Panel;
    private JPanel button_Panel;
    private JTable points_Table;
    private JScrollPane points_ScrollPane;
    
    private JPanel result_Panel;
    private JTable result_Table;

    private JScrollPane result_ScrollPane; 
    
    private JLabel WS_Label;
    private JLabel WS_Value;
    private JLabel WF_Label;
    private JLabel WF_Value;
    private JLabel Speed_Label;
    private JLabel Speed_Value;

    
    private JButton SetSpeed_Button;
    private JButton singleRead_Button;
    private JButton change_Button;
    private JButton autoRead_Button;
    private JButton breakAuto_Button;

    private JPanel sketch_Panel;
    private JPanel sketch1_Panel;
    private JLabel sketch_Label;
    private JLabel sketch1_Label;
    private JPanel Swing_Error_Parameter;
    private JLabel parameter;
    private final String s = "摆幅误差（WS）———— 摆频误差（WF）" ;
    private final String s0 = "WS = ( Sa - Sc ) / Sc * 100%";
    private final String s0_1 = "测得的实际摆幅平均值 Sa" + "———— 指令摆幅 Sc";
    private final String s1 = "WF = ( Fa - Fc ) / Fc * 100%";
    private final String s1_1 = "测得的实际摆频 Fa" + "———— 指令摆频 Fc";
    private final String s2 = "Fa = 10 * WVa / ( 10 - WDa)" + "———— Fc = 10 * WVc / ( 10 - WDc)";
    private final String s2_1 = "平均的实际摆动速度 WVa" +  "———— 平均的实际摆动距离 WDa";  
    private final String s2_2 = "指令摆动速度 WVc" + "———— 指令摆动距离 WDc";      


}
