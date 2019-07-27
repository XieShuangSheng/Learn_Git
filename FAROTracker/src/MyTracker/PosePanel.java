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
 * @author szhc
 */
public class PosePanel extends MyPanel{
    
    public PosePanel() {
        initComponents();
    }
    
    private void initComponents() {
        this.setLayout(null);
        points_Panel = new JPanel();        
        result_Panel = new JPanel();
        coord_Panel = new JPanel();        
        button_Panel = new JPanel();
        sketch_Panel = new JPanel();
        singleRead_Button = new JButton("手动读取");
        change_Button = new JButton("修改坐标");
        autoRead_Button = new JButton("启动检测");
        breakAuto_Button = new JButton("终止检测");
        ImageIcon img = new ImageIcon("Res/PosePanel.png");
        sketch_Label = new JLabel(img);
        chgDialog = new ChangeDialog(this);
        chgDialogWorldCoord = new ChangeDialogWorldCoord(this);
        worldCoord_Panel = new JPanel();
        changeWorldCoord_Button = new JButton("修改坐标");
        
//        coord_Panel.setBorder(BorderFactory.createTitledBorder("点位分布"));
        coord_Panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        result_Panel.setBorder(BorderFactory.createTitledBorder("检测结果"));
        points_Panel.setBorder(BorderFactory.createTitledBorder("点位-关节坐标"));
        sketch_Panel.setBorder(BorderFactory.createTitledBorder("位姿所用平面"));
//        worldCoord_Panel.setBorder(BorderFactory.createTitledBorder("点位-全局坐标"));
        
        points_Panel.setLayout(new BorderLayout());
        
        button_Panel.add(singleRead_Button);
        button_Panel.add(change_Button);
        button_Panel.add(autoRead_Button);
        button_Panel.add(breakAuto_Button);
        points_Panel.add(button_Panel,BorderLayout.SOUTH);
        singleRead_Button.setEnabled(false);
        change_Button.setEnabled(false);
        changeWorldCoord_Button.setEnabled(false);
        autoRead_Button.setEnabled(false);
        breakAuto_Button.setEnabled(false);
        
        sketch_Panel.add(sketch_Label);
        
        points_Panel.setBounds(0, 0, 810, 210);
//        worldCoord_Panel.setBounds(810, 0, 530, 210);
        sketch_Panel.setBounds(0, 210, 400, 350);
        coord_Panel.setBounds(400, 220, 530, 335);
        result_Panel.setBounds(930, 210, 410, 350);
        this.add(points_Panel);
//        this.add(worldCoord_Panel);
        this.add(sketch_Panel);
        this.add(result_Panel);
        this.add(coord_Panel);
        
        String[] columnNames = {"点位","关节角1","关节角2","关节角3","关节角4","关节角5","关节角6",
            "坐标X","坐标Y","坐标Z"};
        Object[][] modelData = new Object[5][10];
        for(int i = 0;i < modelData.length;++i) {
            for(int j = 0;j < modelData[0].length;++j) {
                if(j == 0) {
                    modelData[i][j] = "P" + String.valueOf(i+1);
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
        
        TableColumn column = null;
        int columns = points_Table.getColumnCount();
        for(int i = 0;i < columns;++i) {
            column = points_Table.getColumnModel().getColumn(i);
            if(i == 0) {
                column.setPreferredWidth(40);
            }
            else {
                column.setPreferredWidth(80);
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
        points_Table.setRowHeight(20);//设置行宽
        points_Table.setShowGrid(true);//显示网格
        points_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单行选中
        
        points_ScrollPane = new JScrollPane(points_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        points_ScrollPane.setPreferredSize(new Dimension(600,315));
        points_ScrollPane.setViewportView(points_Table);
        points_Panel.add(points_ScrollPane);
        
        
        String[] result_columnNames = {"循环次数","最大偏差(mm)","平均偏差(mm)","有效偏差(mm)"};
        Object[][] result_modelData = new Object[30][4];
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
        
        TableColumn result_column = null;
        int result_columns = result_Table.getColumnCount();
        for(int i = 0;i < result_columns;++i) {
            result_column = result_Table.getColumnModel().getColumn(i);
            result_column.setPreferredWidth(95);
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
        result_Table.setRowHeight(18);//设置行宽
        result_Table.setShowGrid(true);//显示网格
        result_Table.setEnabled(false);
        
        result_ScrollPane = new JScrollPane(result_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        result_ScrollPane.setPreferredSize(new Dimension(400,208));
        result_ScrollPane.setViewportView(result_Table);
        result_Panel.add(result_ScrollPane);
        
        repeatDiff_Label = new JLabel("RP");
        repeatDiff_Value = new JLabel("0.000");
        RPa_Label = new JLabel("RPa");
        RPa_Value = new JLabel("0.000");
        RPb_Label = new JLabel("RPb");
        RPb_Value = new JLabel("0.000");
        RPc_Label = new JLabel("RPc");
        RPc_Value = new JLabel("0.000");
        repeatDiff_Value.setBorder(BorderFactory.createEtchedBorder());
        repeatDiff_Value.setPreferredSize(new Dimension(80,22));
        RPa_Value.setBorder(BorderFactory.createEtchedBorder());
        RPa_Label.setPreferredSize(new Dimension(40,22));
        RPa_Value.setPreferredSize(new Dimension(80,22));
        RPb_Value.setBorder(BorderFactory.createEtchedBorder());
        RPb_Label.setPreferredSize(new Dimension(40,22));
        RPb_Value.setPreferredSize(new Dimension(80,22));
        RPc_Value.setBorder(BorderFactory.createEtchedBorder());
        RPc_Label.setPreferredSize(new Dimension(40,22));
        RPc_Value.setPreferredSize(new Dimension(80,22));
        JPanel RP_Panel = new JPanel();
        RP_Panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridx = 0;c.gridy = 0;
        RP_Panel.add(repeatDiff_Label,c);
        c.gridx = 1;c.gridy = 0;
        RP_Panel.add(repeatDiff_Value,c);
        c.gridx = 0;c.gridy = 1;
        RP_Panel.add(RPa_Label,c);
        c.gridx = 1;c.gridy = 1;
        RP_Panel.add(RPa_Value,c);
        c.gridx = 2;c.gridy = 1;
        RP_Panel.add(RPb_Label,c);
        c.gridx = 3;c.gridy = 1;
        RP_Panel.add(RPb_Value,c);
        c.gridx = 4;c.gridy = 1;
        RP_Panel.add(RPc_Label,c);
        c.gridx = 5;c.gridy = 1;
        RP_Panel.add(RPc_Value,c);
        result_Panel.add(RP_Panel);
        
        String[] worldCoord_ColNames = {"点位","X轴坐标","Y轴坐标","Z轴坐标","U轴坐标","V轴坐标","W轴坐标"};
        Object[][] worldCoord_data = new Object[5][7];
        for(int i = 0;i < worldCoord_data.length;++i) {
            for(int j = 0;j < worldCoord_data[0].length;++j) {
                if(j == 0) {
                    worldCoord_data[i][j] = "P" + String.valueOf(i+1);
                }
                else {
                    worldCoord_data[i][j] = 0.0;
                }
            }
        }
        worldCoordPoints_TablePanel = new TablePanel(worldCoord_data,worldCoord_ColNames);
        worldCoordPoints_TablePanel.setBounds(810, 0, 530, 210);
        worldCoordPoints_TablePanel.setBorder(BorderFactory.createTitledBorder("点位-全局坐标"));
        worldCoordPoints_TablePanel.setPreferredSize(new Dimension(480,130));
        worldCoordPoints_TablePanel.setColumnWidth(0, 40);
        worldCoordPoints_TablePanel.add(changeWorldCoord_Button);
        this.add(worldCoordPoints_TablePanel);
        
        coord_Paint = new CoordinatePaint("点位分布图","激光仪坐标系");
        coord_Panel.add(coord_Paint);
        
        singleRead_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                singleRead_ButtonActionPerformed(ae);
            }
        });
        change_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                change_ButtonActionPerformed(ae);
            }
        });
        changeWorldCoord_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                changeWorldCoord_ButtonActionPerformed(ae);
            }
        });
        autoRead_Button.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent ae) {
               autoRead_ButtonActionPerformed(ae);
           }
        });
        breakAuto_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                breakAuto_ButtonActionPerformed(ae);
            }
        });
    }
    
    @Override
    public Object[] GetTableRowValue(){
        Object[] value = new Object[6];
        int row = points_Table.getSelectedRow();
        for(int i = 1;i < 7;++i) {
            value[i-1] = points_Table.getModel().getValueAt(row, i);
        }
        return value;
    }
    @Override
    public void SetTableValue(final Object[] values) {
        int row = points_Table.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 1;i < 7;++i) {
            double v = Double.parseDouble(values[i-1].toString());
            points_Table.setValueAt(df.format(v), row, i);
        }
        if(row != -1) {
            if(trackerThread == null)
            {
                if(trackerThread == null) {
                    trackerThread = MyTracker.getTrackerThread();
                }
            }
            double[] v=trackerThread.PointCoordinatorTrans();
            for(int i = 1;i < 7;++i) {
                worldCoordPoints_TablePanel.setValueAt(df.format(v[i-1]), row, i);
            }
        }
    }
    @Override
    public Object[] GetWorldCoordTableRowValue(){
        Object[] value = new Object[6];
        int row = worldCoordPoints_TablePanel.getSelectedRow();
        for(int i = 1;i < 7;++i) {
            value[i-1] = worldCoordPoints_TablePanel.getValueAt(row, i);
        }
        return value;
    }
    @Override
    public void SetWorldCoordTableValue(final Object[] values) {
        int row = worldCoordPoints_TablePanel.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 1;i < 7;++i) {
            double v = Double.parseDouble(values[i-1].toString());
            worldCoordPoints_TablePanel.setValueAt(df.format(v), row, i);
        }
    }
    
    private void singleRead_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
            return;
        }
        trackerThread.ProcessChanged(CURRENTPROCESS.POSE_PAGE_SINGLEREAD_PROCESS);
        trackerThread.SingleReadMeasure();
    }
    private void change_ButtonActionPerformed(ActionEvent ae) {
        int row = points_Table.getSelectedRow();
        if(points_Table.isRowSelected(row)) {
            chgDialog.setVisible(true);
        }
    }
    private void changeWorldCoord_ButtonActionPerformed(ActionEvent ae) {
        int row = worldCoordPoints_TablePanel.getSelectedRow();
        if(worldCoordPoints_TablePanel.isRowSelected(row)) {
            chgDialogWorldCoord.setVisible(true);
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
        coord_Paint.ClearPoint();
        trackerThread.ProcessChanged(CURRENTPROCESS.POSE_PAGE_CONTINUE_PROCESS);
        trackerThread.StartCalibration();
    }
    private void breakAuto_ButtonActionPerformed(ActionEvent ae) {
        trackerThread.ProcessChanged(CURRENTPROCESS.POSE_PAGE_BREAK_AUTO_PROCESS);
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
    
    public void SetSelectionRow(int row) {
        points_Table.setRowSelectionInterval(row, row);
    }
    
    //滚动条跟随表中选择的行自动滚动
    public void SetSelectionRow_Result(int row) {
        int width = result_ScrollPane.getViewport().getHeight();
        Point p = result_ScrollPane.getViewport().getViewPosition();
        JTableHeader header = result_Table.getTableHeader();
        int rowWidth = header.getHeight() + result_Table.getRowHeight() * row;
        
        if(rowWidth > width) {
            p.setLocation(p.getX(), result_Table.getRowHeight() * (row + 2) - width);
        }
        else {
            p.setLocation(p.getX(),0);
        }
        result_ScrollPane.getViewport().setViewPosition(p);
        result_Table.setRowSelectionInterval(row, row);
    }
    
    public int GetSelectionRow() {
        return points_Table.getSelectedRow();
    }
    
    public Object[][] GetTableValue(){
        int rowCnt = points_Table.getRowCount();
        int columnCnt = points_Table.getColumnCount();
        Object[][] value = new Object[rowCnt][columnCnt];
        for(int i = 0; i < rowCnt;++i) {
            for(int j = 1;j < columnCnt;++j) {
                value[i][j-1] = points_Table.getModel().getValueAt(i, j);
            }
        }
        return value;
    }
    
    public void CoordTrans(double[][] values) {
        int row = worldCoordPoints_TablePanel.getRowCount();
        int column = worldCoordPoints_TablePanel.getColumnCount();
        
        DecimalFormat df = new DecimalFormat("0.000");
        for(int i = 0;i < row;++i) {
            for(int j = 1;j < column;++j) {
                worldCoordPoints_TablePanel.setValueAt(df.format(values[i][j-1]), i, j);
            }
        }
    }
    
    public void DispResult(final Object[] values) {
        DecimalFormat df = new DecimalFormat("0.000");
        for(int j = 1;j < values.length;++j) {
            if(j < 4) {
                result_Table.setValueAt(df.format(values[j]), Integer.parseInt(values[0].toString()), j);
            }
        }
    }
    
    public Object[] GetRepeatDiff() {
        Object[] ret = new Object[1];
        ret[0] = repeatDiff_Value.getText();
        return ret;
    }
    public void SetRepeatDiff(Object[] values) {
        for(int i = 0;i < values.length;++i) {
            repeatDiff_Value.setText(String.valueOf(values[i]));
        }
    }
    
    public void RepeatDiffDisp(double diff) {
        DecimalFormat df = new DecimalFormat("0.000");
        repeatDiff_Value.setText(df.format(diff));
    }
    
    public void SetValueAll(final Object[][] values) {
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 0;i < 5;++i) {
            for(int j = 1;j < 7;++j) {
                double v = Double.parseDouble(values[i][j-1].toString());
                points_Table.setValueAt(df.format(v), i, j);
            }
        }
    }
    
    public Object getValueAt(int i,int j) {
        return points_Table.getModel().getValueAt(i, j);
    }
    
    public void ProcessEnable() {
        singleRead_Button.setEnabled(true);
        change_Button.setEnabled(true);
        changeWorldCoord_Button.setEnabled(true);
        autoRead_Button.setEnabled(true);
        breakAuto_Button.setEnabled(true);
    }
    
    public void AddPointsDisplay(Object[] values) {
        double x = Double.parseDouble(values[0].toString());
        double y = Double.parseDouble(values[1].toString());
        double z = Double.parseDouble(values[2].toString());
        coord_Paint.AddPoint(x,y,z);
    }
    
    private JPanel points_Panel;
    private JTable points_Table;
    private JScrollPane points_ScrollPane;
    
    private JPanel button_Panel;
    private JButton singleRead_Button;
    private JButton change_Button;
    private JButton autoRead_Button;
    private JButton breakAuto_Button;
    
    private JPanel result_Panel;
    private JTable result_Table;
    private JScrollPane result_ScrollPane; 
    
    private JPanel coord_Panel;
    private CoordinatePaint coord_Paint;
    
    private JPanel sketch_Panel;
    private JLabel sketch_Label;
    
    private JPanel worldCoord_Panel;
    private TablePanel worldCoordPoints_TablePanel;
    private JButton changeWorldCoord_Button;
    
    private ChangeDialog chgDialog;
    private ChangeDialogWorldCoord chgDialogWorldCoord;
    private TrackerThread trackerThread;
    
    private JLabel repeatDiff_Label;
    private JLabel repeatDiff_Value;
    private JLabel RPa_Label;
    private JLabel RPa_Value;
    private JLabel RPb_Label;
    private JLabel RPb_Value;
    private JLabel RPc_Label;
    private JLabel RPc_Value;
}
