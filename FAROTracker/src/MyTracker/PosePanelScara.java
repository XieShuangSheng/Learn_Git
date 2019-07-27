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
public class PosePanelScara extends MyPanel{
    
    public PosePanelScara() {
        initComponents();
    }
    
    private void initComponents() {
        this.setLayout(new GridBagLayout());
        points_Panel = new JPanel();        
        result_Panel = new JPanel();
        coord_Panel = new JPanel();        
        button_Panel = new JPanel();
        singleRead_Button = new JButton("单次读取");
        change_Button = new JButton("修改坐标");
        autoRead_Button = new JButton("启动检测");
        breakAuto_Button = new JButton("终止检测");
        chgDialog = new ChangeDialogScara(this);
        
//        coord_Panel.setBorder(BorderFactory.createTitledBorder("点位分布"));
        coord_Panel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        result_Panel.setBorder(BorderFactory.createTitledBorder("检测结果"));
        points_Panel.setBorder(BorderFactory.createTitledBorder("点位"));
        
        points_Panel.setLayout(new BorderLayout());
        
        button_Panel.add(singleRead_Button);
        button_Panel.add(change_Button);
        button_Panel.add(autoRead_Button);
        button_Panel.add(breakAuto_Button);
        points_Panel.add(button_Panel,BorderLayout.SOUTH);
        singleRead_Button.setEnabled(false);
        change_Button.setEnabled(false);
        autoRead_Button.setEnabled(false);
        breakAuto_Button.setEnabled(false);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 100;
        c.weighty = 100;
        c.ipadx = 500;
        c.ipady = 322;
        this.add(points_Panel,c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.ipadx = 500;
        c.ipady = 300;
        this.add(result_Panel,c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        c.ipadx = 520;
        c.ipady = 600;
        this.add(coord_Panel,c);
        
        String[] columnNames = {"点位","关节角1","关节角2","关节角3","关节角4","坐标X","坐标Y","坐标Z",};
        Object[][] modelData = new Object[10][8];
        for(int i = 0;i < modelData.length;++i) {
            for(int j = 0;j < modelData[0].length;++j) {
                if(j == 0) {
                    modelData[i][j] = i+1;
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
            column.setPreferredWidth(100);
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
        
        
        String[] result_columnNames = {"最大偏差(mm)","最小偏差(mm)","有效偏差(mm)","是否通过"};
        Object[][] result_modelData = new Object[1][4];
        for(int i = 0;i < result_modelData.length;++i) {
            for(int j = 0;j < result_modelData[0].length;++j) {
                result_modelData[i][j] = 0.0;
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
            result_column.setPreferredWidth(100);
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
        result_Table.setRowHeight(20);//设置行宽
        result_Table.setShowGrid(true);//显示网格
        result_Table.setEnabled(false);
        
        result_ScrollPane = new JScrollPane(result_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        result_ScrollPane.setPreferredSize(new Dimension(400,48));
        result_ScrollPane.setViewportView(result_Table);
        result_Panel.add(result_ScrollPane);
        
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
        Object[] value = new Object[4];
        int row = points_Table.getSelectedRow();
        for(int i = 1;i < 5;++i) {
            value[i-1] = points_Table.getModel().getValueAt(row, i);
        }
        return value;
    }
    @Override
    public void SetTableValue(final Object[] values) {
        int row = points_Table.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 1;i < 5;++i) {
            double v = Double.parseDouble(values[i-1].toString());
            points_Table.setValueAt(df.format(v), row, i);
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
    private void autoRead_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
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
            for(int i = 5;i < 8;++i) {
                double v = Double.parseDouble(values[i-5].toString());
                points_Table.setValueAt(df.format(v),row,i);
            }
        }
    }
    
    public void SetSelectionRow(int row) {
        points_Table.setRowSelectionInterval(row, row);
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
    
    public void DispResult(final Object[][] values) {
        for(int i = 0;i < 1;++i) {
            for(int j = 0;j < 4;++j) {
                if(j < 3) {
                    result_Table.setValueAt(values[i][j], i, j);
                }
                else {
                    if(Boolean.parseBoolean(values[i][j].toString()) == true) {
                        result_Table.setValueAt("通过", i, j);
                    }
                    else {
                        result_Table.setValueAt("不通过", i, j);
                    }
                }
            }
        }
    }
    
    public Object[] GetRepeatDiff() {
        Object[] ret = new Object[1];
//        ret[0] = repeatDiff_Value.getText();
        return ret;
    }
    public void SetRepeatDiff(Object[] values) {
        for(int i = 0;i < values.length;++i) {
//            repeatDiff_Value.setText(String.valueOf(values[i]));
        }
    }
    
    public void SetValueAll(final Object[][] values) {
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 0;i < points_Table.getRowCount();++i) {
            for(int j = 1;j < points_Table.getColumnCount()-3;++j) {
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
        autoRead_Button.setEnabled(true);
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
    
    private ChangeDialogScara chgDialog;
    private TrackerThread trackerThread;
}
