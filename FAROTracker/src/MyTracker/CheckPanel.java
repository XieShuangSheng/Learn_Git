/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.event.EventListenerList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.CellEditorListener;
import java.util.EventObject;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.regex.Pattern;
/**
 *
 * @author szhc
 */

class TableCellRenderer_ComboBox extends JComboBox implements TableCellRenderer {
    public TableCellRenderer_ComboBox() {
        super();
        addItem("朝上");
        addItem("朝下");
        addItem("朝右");
        addItem("朝左");
        addItem("朝外/后");
        addItem("朝内/前");
    }
    @Override
    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,
            boolean isFocus,int row,int column) {
        int index = Integer.parseInt(value.toString());
        setSelectedIndex(index);
        return this;
    }
}

class TableCellEditor_ComboBox extends JComboBox implements TableCellEditor {
    private EventListenerList listenerList = new EventListenerList();
    private ChangeEvent changeEvent = new ChangeEvent(this);
    
    public TableCellEditor_ComboBox() {
        super();
        addItem("朝上");
        addItem("朝下");
        addItem("朝右");
        addItem("朝左");
        addItem("朝外/后");
        addItem("朝内/前");
        
        //方法1
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               fireEditingStopped();
           } 
        });
    }
    
    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class,l);
    }
    
    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }
    
    private void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for(int i = 0;i < listeners.length;++i) {
            if(listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener)listeners[i + 1];
                listener.editingStopped(changeEvent);
            }
        }
    }
    
    @Override
    public void cancelCellEditing() {
        
    }
    //方法2
    @Override
    public boolean stopCellEditing() {
//        fireEditingStopped();
        return true;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,
            int row,int column) {
        int index = Integer.parseInt(value.toString());
        setSelectedIndex(index);
        return this;
    }
    
    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
    
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
    
    @Override
    public Object getCellEditorValue() {
        int index = getSelectedIndex();
        return index;
    }
}

public class CheckPanel extends MyPanel{
    
    public CheckPanel() {
        initComponents();
    }
    
    private void initComponents() {
//        this.setLayout(new GridBagLayout());
        this.setLayout(null);
        main_Panel = new JPanel();
        result_Panel = new JPanel();
        param_Panel = new JPanel();
        struct_Panel = new JPanel();
        errDispPanel = new ErrorDispPanel("偏差显示","点位偏差");
        
        this.add(main_Panel);
        this.add(param_Panel);
        this.add(result_Panel);
        this.add(struct_Panel);
        main_Panel.setBounds(0, 0, 850, 350);
        param_Panel.setBounds(850,0,500,330);
        result_Panel.setBounds(0, 350, 850, 210);
        struct_Panel.setBounds(850,330,500,235);
        
//        GridBagConstraints c = new GridBagConstraints();
//        c.fill = GridBagConstraints.BOTH;
//        c.gridx = 0;
//        c.gridy = 0;
//        c.gridheight = 1;
//        c.ipadx = 800;
//        c.ipady = 400;
//        c.weightx = 100;
//        c.weighty = 100;
//        this.add(main_Panel,c);
//        c.gridx = 0;
//        c.gridy = 1;
//        c.gridheight = 1;
//        c.ipadx = 800;
//        c.ipady = 200;
//        this.add(result_Panel,c);
//        c.gridx = 1;
//        c.gridy = 0;
//        c.gridheight = 1;
//        c.ipadx = 400;
//        c.ipady = 400;
//        this.add(param_Panel,c);
//        c.gridx = 1;
//        c.gridy = 1;
//        c.gridheight = 1;
//        c.ipadx = 400;
//        c.ipady = 200;
//        this.add(struct_Panel,c);
        
        String[] columnNames = {"点位","关节角1","关节角2","关节角3","关节角4","关节角5","关节角6",
            "坐标X","坐标Y","坐标Z",};
        Object[][] modelData = new Object[50][10];
        for(int i = 0;i < 50;++i) {
            for(int j = 0;j < 10;++j) {
                if(j == 0) {
                    modelData[i][j] = i+1;
                }
                else {
                    modelData[i][j] = 0.0;
                }
            }
        }

        main_Table = new JTable(modelData,columnNames);
        main_Panel.add(main_Table);
        main_Panel.setLayout(new BorderLayout());
        main_Panel.setBorder(BorderFactory.createTitledBorder("数据"));
        
        DefaultTableModel tableModel = new DefaultTableModel(50,10) {
            // 以下为继承自AbstractTableModle的方法，可以自定义 
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
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
        main_Table.setModel(tableModel);
        
        TableColumn column = null;
        int columns = main_Table.getColumnCount();
        for(int i = 0;i < columns;++i) {
            column = main_Table.getColumnModel().getColumn(i);
            column.setPreferredWidth(80);
        }

        main_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = main_Table.getTableHeader();
        header.setFont(new Font("楷体",Font.PLAIN,14));
        header.setEnabled(false);
        main_Table.setRowHeight(15);//设置行宽
        main_Table.setShowGrid(true);//显示网格
        main_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单行选中
        
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
//                if(column >= 7) {
//                    setBackground(Color.YELLOW);
//                }
                return super.getTableCellRendererComponent(table, value,  
                        isSelected, hasFocus, row, column);  
            }  
  
        };  
        for (int i = 0; i < columns; i++) {  
            main_Table.getColumn(main_Table.getColumnName(i)).setCellRenderer(tcr);  
        }
        
        scrollPane = new JScrollPane(main_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setViewportView(main_Table); //构造函数中已经指定了视图，此处不需要再次指定
        main_Panel.add(scrollPane);
        
        readData_Button = new JButton("手动读取");
        changeData_Button = new JButton("修改坐标");
        startCal_Button = new JButton("启动标定");
        manualCal_Button = new JButton("手动计算");
        errorDisp_Button = new JButton("查看偏差");
        moveTo_Button = new JButton("移动到点");
        autoSearch_Button = new JButton("自动寻光");
        JPanel button_Panel = new JPanel();
        button_Panel.add(readData_Button);
        button_Panel.add(manualCal_Button);
        button_Panel.add(changeData_Button);
        button_Panel.add(startCal_Button);
        button_Panel.add(errorDisp_Button);
        button_Panel.add(moveTo_Button);
        button_Panel.add(autoSearch_Button);
        main_Panel.add(button_Panel,BorderLayout.SOUTH);
        readData_Button.setEnabled(false);
        changeData_Button.setEnabled(false);
        startCal_Button.setEnabled(false);
        manualCal_Button.setEnabled(false);
        errorDisp_Button.setEnabled(false);
        moveTo_Button.setEnabled(false);
        autoSearch_Button.setEnabled(false);
        
        //start result table
        String[] resultColumn = {"轴号","零位修正值(°)","减速比修正因子","减速比"};
        Object[][] resultModelData = new Object[6][resultColumn.length];
        for(int i = 0;i < 6;++i) {
            for(int j = 0;j < resultColumn.length;++j) {
                if(j == 0) {
                    resultModelData[i][j] = "轴"+String.valueOf(i+1);
                }
                else {
                    resultModelData[i][j] = 0;
                }
            }
        }
        result_Table = new JTable(resultModelData,resultColumn);
        
        DefaultTableModel resultTableModel = new DefaultTableModel(resultModelData.length,resultModelData[0].length) {
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
            //得到列名
            @Override
            public String getColumnName(int column) {
                return resultColumn[column];
            }
            @Override
            public int getColumnCount() {
                return resultColumn.length;
            }
            //得到行数
            @Override
            public int getRowCount() {
                return resultModelData.length;
            }
            //得到数据对应的对象
            @Override
            public Object getValueAt(int row,int column) {
                return resultModelData[row][column];
            }
            @Override
            public void setValueAt(Object object,int row,int column) {
                resultModelData[row][column] = object;
                fireTableCellUpdated(row,column);
            }
            //得到指定列的数据类型
            @Override
            public Class<?> getColumnClass(int column) {
                return resultModelData[0][column].getClass();
            }
        };
        result_Table.setModel(resultTableModel);
        
        result_Panel.setLayout(new BorderLayout());
        result_Panel.add(result_Table);
        result_Panel.setBorder(BorderFactory.createTitledBorder("校验结果"));
        columns = result_Table.getColumnCount();
        for(int i = 0;i < columns;++i) {
            column = result_Table.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(30);
                    break;
                case 3:
                    column.setPreferredWidth(50);
                    break;
                default:
                    column.setPreferredWidth(80);
                    break;
            }
        }
        
        header = result_Table.getTableHeader();
        header.setFont(new Font("楷体",Font.PLAIN,14));
        header.setEnabled(false);
        result_Table.setRowHeight(16);
        result_Table.setShowGrid(true);
//        result_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        result_Table.setEnabled(false);
        result_ScrollPane = new JScrollPane(result_Table,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        result_ScrollPane.setViewportView(result_Table);
        result_Panel.add(result_ScrollPane);
        
        checkPrev_Label = new JLabel("校准前:");
        maxErrorPrev_Label = new JLabel("最大偏差");
        maxErrorPrev_Value = new JLabel("0.000");
        minErrorPrev_Label = new JLabel("平均偏差");
        minErrorPrev_Value = new JLabel("0.000");
        averageErrorPrev_Label = new JLabel("有效偏差");
        averageErrorPrev_Value = new JLabel("0.000");
        
        checkPost_Label = new JLabel("校准后:");
        maxErrorPost_Label = new JLabel("最大偏差");
        maxErrorPost_Value = new JLabel("0.000");
        minErrorPost_Label = new JLabel("平均偏差");
        minErrorPost_Value = new JLabel("0.000");
        averageErrorPost_Label = new JLabel("有效偏差");
        averageErrorPost_Value = new JLabel("0.000");
        Dimension d = new Dimension(70,20);
        checkPrev_Label.setPreferredSize(new Dimension(90,15));
        maxErrorPrev_Label.setPreferredSize(d);
        maxErrorPrev_Value.setBorder(BorderFactory.createEtchedBorder());
        maxErrorPrev_Value.setPreferredSize(d);
        minErrorPrev_Label.setPreferredSize(d);
        minErrorPrev_Value.setBorder(BorderFactory.createEtchedBorder());
        minErrorPrev_Value.setPreferredSize(d);
        averageErrorPrev_Label.setPreferredSize(d);
        averageErrorPrev_Value.setBorder(BorderFactory.createEtchedBorder());
        averageErrorPrev_Value.setPreferredSize(d);
        
        checkPost_Label.setPreferredSize(new Dimension(90,15));
        maxErrorPost_Label.setPreferredSize(d);
        maxErrorPost_Value.setBorder(BorderFactory.createEtchedBorder());
        maxErrorPost_Value.setPreferredSize(d);
        minErrorPost_Label.setPreferredSize(d);
        minErrorPost_Value.setBorder(BorderFactory.createEtchedBorder());
        minErrorPost_Value.setPreferredSize(d);
        averageErrorPost_Label.setPreferredSize(d);
        averageErrorPost_Value.setBorder(BorderFactory.createEtchedBorder());
        averageErrorPost_Value.setPreferredSize(d);
        
        JPanel error_Panel = new JPanel();
        error_Panel.setLayout(new GridLayout(2,7));
        error_Panel.setBorder(BorderFactory.createRaisedBevelBorder());
        error_Panel.add(checkPrev_Label);
        error_Panel.add(maxErrorPrev_Label);
        error_Panel.add(maxErrorPrev_Value);
        error_Panel.add(minErrorPrev_Label);
        error_Panel.add(minErrorPrev_Value);
        error_Panel.add(averageErrorPrev_Label);
        error_Panel.add(averageErrorPrev_Value);
        error_Panel.add(checkPost_Label);
        error_Panel.add(maxErrorPost_Label);
        error_Panel.add(maxErrorPost_Value);
        error_Panel.add(minErrorPost_Label);
        error_Panel.add(minErrorPost_Value);
        error_Panel.add(averageErrorPost_Label);
        error_Panel.add(averageErrorPost_Value);
        result_Panel.add(error_Panel,BorderLayout.SOUTH);
        
        X1eccCaled_Value = new JLabel("0.000");
        L23Caled_Value = new JLabel("0.000");
        L24Caled_Value = new JLabel("0.000");
        L34aCaled_Value = new JLabel("0.000");
        L34bCaled_Value = new JLabel("0.000");
        L56Caled_Value = new JLabel("0.000");
        
        LxCaled_List = new ArrayList<>();
        LxCaled_List.add(X1eccCaled_Value);
        LxCaled_List.add(L23Caled_Value);
        LxCaled_List.add(L24Caled_Value);
        LxCaled_List.add(L34aCaled_Value);
        LxCaled_List.add(L34bCaled_Value);
        LxCaled_List.add(L56Caled_Value);
        
        X1eccCaled_Label = new JLabel("X1ecc(mm)");
        L23Caled_Label = new JLabel("L23(mm)");
        L24Caled_Label = new JLabel("L24(mm)");
        L34aCaled_Label = new JLabel("L34a(mm)");
        L34bCaled_Label = new JLabel("L34b(mm)");
        L56Caled_Label = new JLabel("L56(mm)");
        
        X1eccCaled_Value.setBorder(BorderFactory.createEtchedBorder());
        L23Caled_Value.setBorder(BorderFactory.createEtchedBorder());
        L24Caled_Value.setBorder(BorderFactory.createEtchedBorder());
        L34aCaled_Value.setBorder(BorderFactory.createEtchedBorder());
        L34bCaled_Value.setBorder(BorderFactory.createEtchedBorder());
        L56Caled_Value.setBorder(BorderFactory.createEtchedBorder());
        
        JPanel LxCaled_Panel = new JPanel();
        GridLayout LxCaled_Layout = new GridLayout(6,2);
        LxCaled_Panel.setLayout(LxCaled_Layout);
        LxCaled_Panel.add(X1eccCaled_Label);
        LxCaled_Panel.add(X1eccCaled_Value);
        LxCaled_Panel.add(L23Caled_Label);
        LxCaled_Panel.add(L23Caled_Value);
        LxCaled_Panel.add(L24Caled_Label);
        LxCaled_Panel.add(L24Caled_Value);
        LxCaled_Panel.add(L34aCaled_Label);
        LxCaled_Panel.add(L34aCaled_Value);
        LxCaled_Panel.add(L34bCaled_Label);
        LxCaled_Panel.add(L34bCaled_Value);
        LxCaled_Panel.add(L56Caled_Label);
        LxCaled_Panel.add(L56Caled_Value);
        
        LxCaled_Panel.setPreferredSize(new Dimension(200,70));
        
        result_Panel.add(LxCaled_Panel,BorderLayout.EAST);
        //end result table
        
        //start param table
        String[] paramColumn = {"轴号","轴向","零位","减速比"};
        Object[][] paramModelData = new Object[6][4];
        for(int i = 0;i < 6;++i) {
            for(int j = 0;j < 4;++j) {
                if(j == 0) {
                    paramModelData[i][j] = "轴" + String.valueOf(i+1);
                }
                else {
                    paramModelData[i][j] = 0;
                }
            }
        }
        param_Table = new JTable(paramModelData,paramColumn);
        param_Panel.add(param_Table);
        param_Panel.setBorder(BorderFactory.createTitledBorder("校验参数"));
        param_Panel.setLayout(new BorderLayout());
        
        DefaultTableModel paramTableModel = new DefaultTableModel(6,4) {
            boolean editable[] = {false,true,true,true};
            //以下为继承自AbstractTableModel的方法，可以自定义
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return editable[column];
            }
            //得到列名
            @Override
            public String getColumnName(int column) {
                return paramColumn[column];
            }
            //得到列数
            @Override
            public int getColumnCount() {
                return paramColumn.length;
            }
            //得到行数
            @Override
            public int getRowCount() {
                return paramModelData.length;
            }
            //得到数据对应的对象
            @Override
            public Object getValueAt(int rowIndex,int columnIndex) {
                return paramModelData[rowIndex][columnIndex];
            }
            //设置对应的对象
            @Override
            public void setValueAt(Object object,int rowIndex,int columnIndex) {
                paramModelData[rowIndex][columnIndex] = object;
                fireTableCellUpdated(rowIndex,columnIndex);
            }
            //得到指定列的数据类型
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return paramModelData[0][columnIndex].getClass();
            }
        };
        
        param_Table.setModel(paramTableModel);
        param_Table.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer_ComboBox());
        param_Table.getColumnModel().getColumn(1).setCellEditor(new TableCellEditor_ComboBox());
        
        columns = param_Table.getColumnCount();
        for(int i = 0;i < columns;++i) {
            column = param_Table.getColumnModel().getColumn(i);
            if(i == 0) {
                column.setPreferredWidth(30);
            }
            else {
                column.setPreferredWidth(80);
            }
        }
        header = param_Table.getTableHeader();
        header.setFont(new Font("楷体",Font.PLAIN,14));
        header.setEnabled(false);
        param_Table.setRowHeight(18);
        param_Table.setShowGrid(true);
        param_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        param_ScrollPane = new JScrollPane(param_Table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        param_ScrollPane.setViewportView(param_Table);
        param_Panel.add(param_ScrollPane);
        
        Dimension di = new Dimension(30,22);
        L1_TextField = new DigitalMeter(10000,-10000,3);
        L23_TextField = new DigitalMeter(10000,-10000,3);
        L34a_TextField = new DigitalMeter(10000,-10000,3);
        L34b_TextField = new DigitalMeter(10000,-10000,3);
        L56_TextField = new DigitalMeter(10000,-10000,3);
        X1ecc_TextField = new DigitalMeter(10000,-10000,3);
        L24_TextField = new DigitalMeter(10000,-10000,3);
        
        Dx_List = new ArrayList<>();
        Dx_List.add(X1ecc_TextField);
        Dx_List.add(L23_TextField);
        Dx_List.add(L24_TextField);
        Dx_List.add(L34a_TextField);
        Dx_List.add(L34b_TextField);
        Dx_List.add(L56_TextField);
        
        h45_CheckBox = new JCheckBox("h(4,5)");
        h46_CheckBox = new JCheckBox("h(4,6)");
        h56_CheckBox = new JCheckBox("h(5,6)");
        ValueSetting_Button = new JButton("设入参数");
        coordTransform_Button = new JButton("坐标变换");
        ValueSetting_Button.setEnabled(false);
//        coordTransform_Button.setEnabled(false);
        couple45_DMT = new DigitalMeter(10000.0,-10000.0,4);
        couple46_DMT = new DigitalMeter(10000.0,-10000.0,4);
        couple56_DMT = new DigitalMeter(10000.0,-10000.0,4);
        couple45_DMT.setEnabled(false);
        couple46_DMT.setEnabled(false);
        couple56_DMT.setEnabled(false);
        
        L23_TextField.setPreferredSize(di);
        L34a_TextField.setPreferredSize(di);
        L34b_TextField.setPreferredSize(di);
        L56_TextField.setPreferredSize(di);
        X1ecc_TextField.setPreferredSize(di);
        L24_TextField.setPreferredSize(di);
        
//        pause_TextField = new DigitalMeter();
        L1_Label = new JLabel("L1(mm)");
        L23_Label = new JLabel("L23(mm)");//L2
        L34a_Label = new JLabel("L34a(mm)");//L3
        L34b_Label = new JLabel("L34b(mm)");//L4
        L56_Label = new JLabel("L56(mm)");//L5
        X1ecc_Label = new JLabel("X1ecc(mm)");//L6
        L24_Label = new JLabel("L24(mm)");//L7
//        pause_Label = new JLabel("暂停(s)");
//        
        L23_Label.setPreferredSize(di);
        L34a_Label.setPreferredSize(di);
        L34b_Label.setPreferredSize(di);
        L56_Label.setPreferredSize(di);
        X1ecc_Label.setPreferredSize(di);
        L24_Label.setPreferredSize(di);
        
        JPanel Lx_Panel = new JPanel();
        Lx_Panel.setLayout(new GridLayout(5,6));
//        Lx_Panel.add(L1_Label);
//        Lx_Panel.add(L1_TextField);
        Lx_Panel.add(X1ecc_Label);
        Lx_Panel.add(X1ecc_TextField);
        Lx_Panel.add(L23_Label);
        Lx_Panel.add(L23_TextField);
        Lx_Panel.add(L24_Label);
        Lx_Panel.add(L24_TextField);
        Lx_Panel.add(L34a_Label);
        Lx_Panel.add(L34a_TextField);
        Lx_Panel.add(L34b_Label);
        Lx_Panel.add(L34b_TextField);
        Lx_Panel.add(L56_Label);
        Lx_Panel.add(L56_TextField);
//        Lx_Panel.add(pause_Label);
//        Lx_Panel.add(pause_TextField);
        Lx_Panel.add(h45_CheckBox);
        Lx_Panel.add(h46_CheckBox);
        Lx_Panel.add(h56_CheckBox);
        Lx_Panel.add(ValueSetting_Button);
        Lx_Panel.add(couple45_DMT);
        Lx_Panel.add(couple46_DMT);
        Lx_Panel.add(couple56_DMT);
        Lx_Panel.add(coordTransform_Button);
        param_Panel.add(Lx_Panel,BorderLayout.SOUTH);
        
        //end param table
        
        //start struct panel
//        struct_Panel.setBorder(BorderFactory.createTitledBorder("结构示意图"));
        struct_Panel.setBorder(BorderFactory.createRaisedBevelBorder());
        ImageIcon img = new ImageIcon("Res/struct-guide.png");
//        img.setImage(img.getImage().getScaledInstance(img.getIconWidth(), img.getIconHeight(), Image.SCALE_DEFAULT));
        structPic_Label = new JLabel(img);
        struct_Panel.add(structPic_Label);
        //end struct panel
        
        chgDialog = new ChangeDialog(this);
        tips = new CalculatingTips();
        coordTransform = new CoordTransform();
        
        readData_Button.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent ae) {
               readData_ButtonActionPerformed(ae);
           }
        });
        changeData_Button.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent ae) {
               changeData_ButtonActionPerformed(ae);
           }
        });
        startCal_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                startCal_ButtonActionPerformed(ae);
            }
        });
        manualCal_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                manualCal_ButtonActionPerformed(ae);
            }
        });
        errorDisp_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                errorDisp_ButtonActionPerformed(ae);
            }
        });
        moveTo_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                moveTo_ButtonActionPerformed(ae);
            }
        });
        autoSearch_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                autoSearch_ButtonActionPerformed(ae);
            }
        });
        ValueSetting_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ValueSetting_ButtonActionPerformed(ae);
            }
        });
        coordTransform_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                coordTransform_ButtonActionPerformed(ae);
            }
        });
        h45_CheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                h45_CheckBoxItemStateChanged(ie);
            }
        });
        h46_CheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                h46_CheckBoxItemStateChanged(ie);
            }
        });
        h56_CheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                h56_CheckBoxItemStateChanged(ie);
            }
        });
    }
    
    private void readData_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
//        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
//            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
//            return;
//        }
        trackerThread.ProcessChanged(CURRENTPROCESS.CHECK_PAGE_SINGLEREAD_PROCESS);
        trackerThread.SingleReadMeasure();
    }

    private void changeData_ButtonActionPerformed(ActionEvent at) {
        int row = main_Table.getSelectedRow();
        if(main_Table.isRowSelected(row)) {
            chgDialog.setVisible(true);
        }
    }
    private void startCal_ButtonActionPerformed(ActionEvent at) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
            return;
        }
        trackerThread.ProcessChanged(CURRENTPROCESS.CHECK_PAGE_CONTINUE_PROCESS);
        trackerThread.StartCalibration();
    }
    
    private void manualCal_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        if(trackerThread.GetCurrProcess() != CURRENTPROCESS.NONE_PROCESS) {
            JOptionPane.showMessageDialog(this, "测量进行中，请稍后重试！");
            return;
        }
        trackerThread.StartCalibrationAlone();
    }
    
    private void errorDisp_ButtonActionPerformed(ActionEvent ae) {
        errDispPanel.setVisible(true);
    }
    
    private void moveTo_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        int row = main_Table.getSelectedRow();
        if(row != -1) {
            double[] coord = new double[3];
            for(int i = 7;i < 10;++i) {
                coord[i-7] = Double.parseDouble(main_Table.getModel().getValueAt(row, i).toString());
            }
            trackerThread.MoveTo(coord,true);
        }
    }
    
    private void autoSearch_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        trackerThread.SearchTarget();
    }
    
    private void ValueSetting_ButtonActionPerformed(ActionEvent ae) {
        SetCalStructParam();
    }
    private void coordTransform_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        double[] base = trackerThread.GetBase();
        double[] tool = trackerThread.GetTool();
        coordTransform.SetValuesCoord(base);
        coordTransform.SetValuesTool(tool);
        coordTransform.setVisible(true);
    }
    private void h45_CheckBoxItemStateChanged(ItemEvent ie) {
        JCheckBox checkBox = (JCheckBox)ie.getItem();
        if(checkBox.isSelected()) {
            couple45_DMT.setEnabled(true);
        }
        else {
            couple45_DMT.setEnabled(false);
        }
    }
    private void h46_CheckBoxItemStateChanged(ItemEvent ie) {
        JCheckBox checkBox = (JCheckBox)ie.getItem();
        if(checkBox.isSelected()) {
            couple46_DMT.setEnabled(true);
        }
        else {
            couple46_DMT.setEnabled(false);
        }
    }
    private void h56_CheckBoxItemStateChanged(ItemEvent ie) {
        JCheckBox checkBox = (JCheckBox)ie.getItem();
        if(checkBox.isSelected()) {
            couple56_DMT.setEnabled(true);
        }
        else {
            couple56_DMT.setEnabled(false);
        }
    }
    
    
    @Override
    public Object[] GetTableRowValue(){
        Object[] value = new Object[6];
        int row = main_Table.getSelectedRow();
        for(int i = 1;i < 7;++i) {
            value[i-1] = main_Table.getModel().getValueAt(row, i);
        }
        return value;
    }
    public Object[][] GetTableValue(){
        int rowCnt = main_Table.getRowCount();
        int columnCnt = main_Table.getColumnCount();
        Object[][] value = new Object[rowCnt][columnCnt];
        for(int i = 0; i < rowCnt;++i) {
            for(int j = 1;j < columnCnt;++j) {
                value[i][j-1] = main_Table.getModel().getValueAt(i, j);
            }
        }
        return value;
    }
    @Override
    public void SetTableValue(final Object[] values) {
        int row = main_Table.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 1;i < 7;++i) {
            double v = Double.parseDouble(values[i-1].toString());
            main_Table.setValueAt(df.format(v), row, i);
        }
    }
    public void SetTrackerValue(final Object[] values) {
        int row = main_Table.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        if(main_Table.isRowSelected(row)) {
            for(int i = 7;i < 10;++i) {
                double v = Double.parseDouble(values[i-7].toString());
                main_Table.setValueAt(df.format(v),row,i);
            }
        }
    }
    public void SetValueAll(final Object[][] values) {
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        for(int i = 0;i < 50;++i) {
            for(int j = 1;j < 10;++j) {
                double v = Double.parseDouble(values[i][j-1].toString());
                main_Table.setValueAt(df.format(v), i, j);
            }
        }
    }
    
    public Object getValueAt(int i,int j) {
        return main_Table.getModel().getValueAt(i, j);
    }
    
    
    public Object[] GetStructParam() {
        int rowCount = param_Table.getRowCount();
        int columnCount = param_Table.getColumnCount();
        Object values[] = new Object[rowCount * (columnCount-1) + 10];
        int index = 0;
        for(int j = 1;j < columnCount;++j) {
            for(int i = 0;i < rowCount;++i) {
                index = (j - 1)*rowCount + i; 
                values[index] = param_Table.getModel().getValueAt(i, j);
                System.out.println(values[index]);
            }
        }
        for(int i = 0;i < Dx_List.size();++i) {
            ++index;
            values[index] = ((DigitalMeter)Dx_List.get(i)).getText();
        }
        int checkBox_State;
        ++index;
        checkBox_State = h45_CheckBox.isSelected() ? 1 : 0;
        checkBox_State |= h46_CheckBox.isSelected() ? 2 : 0;
        checkBox_State |= h56_CheckBox.isSelected() ? 4 : 0;
        values[index] = checkBox_State;
        ++index;
        values[index] = couple45_DMT.getText();
        ++index;
        values[index] = couple46_DMT.getText();
        ++index;
        values[index] = couple56_DMT.getText();
        return values;
    }
    public double[][] GetDHParam() {
        //s[0]------------theta
        //s[1]------------d
        //s[2]------------a
        //s[3]------------alpha
        double[][] s = new double[4][6];
        s[0][0] = 0.0;
        s[0][1] = 90.0;
        s[0][2] = 0.0;
        s[0][3] = 0.0;
        s[0][4] = 0.0;
        s[0][5] = 0.0;
        
        s[1][0] = 0.0;
        s[1][1] = L24_TextField.GetValueDouble();
        s[1][2] = 0.0;
        s[1][3] = L34b_TextField.GetValueDouble();
        s[1][4] = 0.0;
        s[1][5] = L56_TextField.GetValueDouble();
        
        s[2][0] = X1ecc_TextField.GetValueDouble();
        s[2][1] = L23_TextField.GetValueDouble();
        s[2][2] = L34a_TextField.GetValueDouble();
        s[2][3] = 0.0;
        s[2][4] = 0.0;
        s[2][5] = 0.0;
        
        s[3][0] = 90.0;
        s[3][1] = 0.0;
        s[3][2] = 90.0;
        s[3][3] = -90.0;
        s[3][4] = 90.0;
        s[3][5] = 0.0;
        return s;
    }
    public void SetStructParam(final Object[][] values,final Object[] length) {
        DecimalFormat df = new DecimalFormat("0.000");
        double v = 0.0;
        int checkBox_State;
        for(int i = 0;i < 6;++i) {
            for(int j = 1;j < 4;++j) {
                if(j != 1) {
                    v = Double.parseDouble(values[i][j].toString());
                    param_Table.setValueAt(df.format(v), i, j);
                }
                else {
                    param_Table.setValueAt(values[i][j], i, j);
                }
            }
        }
        for(int i = 0;i < Dx_List.size();++i) {
            v = Double.parseDouble(length[i].toString());
            ((DigitalMeter)Dx_List.get(i)).setText(df.format(v));
        }
        checkBox_State=Integer.parseInt(length[6].toString());
        String num = Integer.toBinaryString(checkBox_State);
        if(num.length() == 2) num = "0" + num;
        if(num.length() == 1) num = "00" + num;
        
        boolean state = num.charAt(2) == '1';
        h45_CheckBox.setSelected(state);
        state = num.charAt(1) == '1';
        h46_CheckBox.setSelected(state);
        state = num.charAt(0) == '1';
        h56_CheckBox.setSelected(state);
        
        couple45_DMT.setText(length[7].toString());
        couple46_DMT.setText(length[8].toString());
        couple56_DMT.setText(length[9].toString());
    }
    
    public Object[] GetResultParams() {
        int rowCount = result_Table.getRowCount();
        int columnCount = result_Table.getColumnCount();
        Object values[] = new Object[rowCount * (columnCount-1) + 12];
        int index = 0;
        for(int j = 1;j < columnCount;++j) {
            for(int i = 0;i < rowCount;++i) {
                index = (j - 1)*rowCount + i; 
                values[index] = result_Table.getModel().getValueAt(i, j);
            }
        }
        for(int i = 0;i < LxCaled_List.size();++i) {
            ++index;
            values[index] = ((JLabel)LxCaled_List.get(i)).getText();
        }
        ++index;
        values[index] = maxErrorPrev_Value.getText();
        ++index;
        values[index] = minErrorPrev_Value.getText();
        ++index;
        values[index] = averageErrorPrev_Value.getText();
        ++index;
        values[index] = maxErrorPost_Value.getText();
        ++index;
        values[index] = minErrorPost_Value.getText();
        ++index;
        values[index] = averageErrorPost_Value.getText();
        
        return values;
    }
    
    public Object[] LeadoutStructParam() {
        int rowCount = result_Table.getRowCount();
        int columnCount = result_Table.getColumnCount();
        int index = 0;
        Object[] ret = new Object[rowCount * columnCount];
        for(int i = 1;i < columnCount;++i) {
            for(int j = 0;j < rowCount;++j) {
                ret[index] = result_Table.getValueAt(j, i);
                ++index;
            }
        }
        for(int i = 0;i < LxCaled_List.size();++i) {
            ret[index] = ((JLabel)LxCaled_List.get(i)).getText();
            ++index;
        }
        return ret;
    }
    
    public void SetCalStructParam() {
        DecimalFormat df = new DecimalFormat("0.000");
        int rowCount = result_Table.getRowCount();
        int columnCount = result_Table.getColumnCount();
        Object[] zero = new Object[6];
        Object[] ratio = new Object[6];
        Object[] length = new Object[6];
        for(int c = 1;c < columnCount;++c) {
            for(int r = 0;r < rowCount;++r) {
                if(c == 1) zero[r] = result_Table.getValueAt(r, c);
                if(c == 3) ratio[r] = result_Table.getValueAt(r, c);
            }
        }
        for(int i = 0;i < LxCaled_List.size();++i) {
            length[i] = ((JLabel)LxCaled_List.get(i)).getText();
        }
        
        
        for(int i = 0;i < 6;++i) {
            //2018.8.3 零位补偿无需设入
//            param_Table.setValueAt(zero[i], i, 2);
            param_Table.setValueAt(ratio[i], i, 3);
        }
        for(int i = 0;i < Dx_List.size();++i) {
            ((DigitalMeter)Dx_List.get(i)).setText(String.valueOf(length[i]));
        }
    }
    
    public void DispResult(final Object[] values) {
        DecimalFormat df = new DecimalFormat("0.000");
        Object[] paramCal = new Object[6];
        Object[] err = new Object[6];
        for(int i = 0;i < 5;++i) {
            for(int j = 0;j < 6;++j) {
                double v;
                if(values[i*6+j].toString().contains(".")) {
                    v = Double.parseDouble(String.valueOf(values[i*6 + j]));
                }
                else {
                    v = 0.000;
                }
                if(i < 3) result_Table.setValueAt(df.format(v), j, i+1);
                else if(i == 3) paramCal[j] = df.format(v);
                else if(i == 4) err[j] = df.format(v);
            }
        }
        DispParamCaled(paramCal);
        DispError(err);
        ValueSetting_Button.setEnabled(true);
        coordTransform_Button.setEnabled(true);
    }
    public void DispParamCaled(final Object[] values) {
        for(int i = 0;i < LxCaled_List.size();++i) {
            ((JLabel)LxCaled_List.get(i)).setText(String.valueOf(values[i]));
        }
    }
    public void DispError(final Object[] values) {
        maxErrorPrev_Value.setText(String.valueOf(values[0]));
        minErrorPrev_Value.setText(String.valueOf(values[1]));
        averageErrorPrev_Value.setText(String.valueOf(values[2]));
        maxErrorPost_Value.setText(String.valueOf(values[3]));
        minErrorPost_Value.setText(String.valueOf(values[4]));
        averageErrorPost_Value.setText(String.valueOf(values[5]));
    }
    
    //滚动条跟随表中选择的行自动滚动
    public void SetSelectionRow(int row) {
        int width = scrollPane.getViewport().getHeight();
        Point p = scrollPane.getViewport().getViewPosition();
        JTableHeader header = main_Table.getTableHeader();
        int rowWidth = header.getHeight() + main_Table.getRowHeight() * row;
        
        if(rowWidth > width) {
            p.setLocation(p.getX(), main_Table.getRowHeight() * (row + 2) - width);
        }
        else {
            p.setLocation(p.getX(),0);
        }
        scrollPane.getViewport().setViewPosition(p);
        main_Table.setRowSelectionInterval(row, row);
    }
    public int GetSelectionRow() {
        return main_Table.getSelectedRow();
    }
    
    public void SetMainTableEnable(boolean f) {
        main_Table.setEnabled(f);
    }
    
    public void ProcessEnable() {
        readData_Button.setEnabled(true);
        changeData_Button.setEnabled(true);
        startCal_Button.setEnabled(true);
        manualCal_Button.setEnabled(true);
        moveTo_Button.setEnabled(true);
        autoSearch_Button.setEnabled(true);
    }
    
    public void ErrorDispEnable() {
        errorDisp_Button.setEnabled(true);
    }
    
    public ErrorDispPanel GetErrorDisp() {
        return errDispPanel;
    }
    
    public void DisplayCalTips(boolean flag) {
        tips.setVisible(flag);
    }
    
    public CoordTransform getCoordTransPanel() {
        return coordTransform;
    }
    
    private JPanel main_Panel;
    private JPanel result_Panel;
    private JPanel param_Panel;
    private JPanel struct_Panel;
    
    private JTable main_Table;
    private JScrollPane scrollPane;
    private JButton readData_Button;
    private JButton changeData_Button;
    private JButton startCal_Button;
    private JButton manualCal_Button;
    private JButton errorDisp_Button;
    private JButton moveTo_Button;
    private JButton autoSearch_Button;
    
    private JTable result_Table;
    private JScrollPane result_ScrollPane;
    private JLabel checkPrev_Label;
    private JLabel maxErrorPrev_Label;
    private JLabel minErrorPrev_Label;
    private JLabel averageErrorPrev_Label;
    private JLabel maxErrorPrev_Value;
    private JLabel minErrorPrev_Value;
    private JLabel averageErrorPrev_Value;
        
    private JLabel checkPost_Label;
    private JLabel maxErrorPost_Label;
    private JLabel minErrorPost_Label;
    private JLabel averageErrorPost_Label;
    private JLabel maxErrorPost_Value;
    private JLabel minErrorPost_Value;
    private JLabel averageErrorPost_Value;
    
    private JTable param_Table;
    private JScrollPane param_ScrollPane;
    private DigitalMeter L1_TextField;
    private DigitalMeter L23_TextField;
    private DigitalMeter L34a_TextField;
    private DigitalMeter L34b_TextField;
    private DigitalMeter L56_TextField;
    private DigitalMeter X1ecc_TextField;
    private DigitalMeter L24_TextField;
    private List Dx_List;
    private DigitalMeter pause_TextField;
    private JCheckBox h45_CheckBox;
    private JCheckBox h46_CheckBox;
    private JCheckBox h56_CheckBox;
    private JLabel L1_Label;
    private JLabel L23_Label;
    private JLabel L34a_Label;
    private JLabel L34b_Label;
    private JLabel L56_Label;
    private JLabel X1ecc_Label;
    private JLabel L24_Label;
    private JLabel pause_Label;
    
    private JLabel X1eccCaled_Value;
    private JLabel L23Caled_Value;
    private JLabel L24Caled_Value;
    private JLabel L34aCaled_Value;
    private JLabel L34bCaled_Value;
    private JLabel L56Caled_Value;
    private List LxCaled_List;
    private JLabel X1eccCaled_Label;
    private JLabel L23Caled_Label;
    private JLabel L24Caled_Label;
    private JLabel L34aCaled_Label;
    private JLabel L34bCaled_Label;
    private JLabel L56Caled_Label;
    
    private JButton ValueSetting_Button;
    private JButton coordTransform_Button;
    
    private DigitalMeter couple45_DMT;
    private DigitalMeter couple46_DMT;
    private DigitalMeter couple56_DMT;
    
    private ChangeDialog chgDialog;
    private CalculatingTips tips;
    private CoordTransform coordTransform;

    private JLabel pauseTime_Label;
    private JTextField pauseTime_Text;
    
    private JLabel structPic_Label;
    
    private TrackerThread trackerThread;
    private ErrorDispPanel errDispPanel;
}
