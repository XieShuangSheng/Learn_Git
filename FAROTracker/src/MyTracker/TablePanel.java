/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author szhc
 */
public class TablePanel extends JPanel{
    public TablePanel(Object[][] data,String[] colNames) {
        initComponents(data,colNames);
    }
    private void initComponents(Object[][] data,String[] colNames) {
        columnsName = colNames;
        modelData = data;
        main_Table = new JTable(modelData,columnsName);
        this.add(main_Table);
        cellEditable = new boolean[modelData.length][modelData[0].length];
        for(int i = 0;i < modelData.length;++i) {
            for(int j = 0;j < modelData[0].length;++j) {
                cellEditable[i][j] = true;
            }
        }
        tableModel = new DefaultTableModel(modelData.length,modelData[0].length) {
            // 以下为继承自AbstractTableModle的方法，可以自定义 
            //设置列是否可以编辑
            @Override
            public boolean isCellEditable(int row,int column) {
                return cellEditable[row][column];
            }
            //得到列名
            @Override
            public String getColumnName(int column) {
                return columnsName[column];
            }
            //得到列数
            @Override
            public int getColumnCount() {
                return columnsName.length;
            }
            //得到行数
            @Override
            public int getRowCount() {
                return modelData.length;
            }
            //得到数据对应的对象
            @Override
            public Object getValueAt(int row,int column) {
                return modelData[row][column];
            }
            //设置对应的对象
            @Override
            public void setValueAt(Object data,int row,int column) {
                modelData[row][column] = data;
                fireTableCellUpdated(row,column); //通知监听器数据单元数据已经改变
            }
            //得到指定列的数据类型
            @Override
            public Class<?> getColumnClass(int column) {
                return modelData[0].getClass();
            }
        };
        main_Table.setModel(tableModel);
        
        tcr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,Object value,
                    boolean isSelected,boolean hasFocus,int row,int column) {
                if(row % 2 == 0) {
                    setBackground(new Color(203,203,203));
                }
                else {
                    setBackground(Color.WHITE);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, row);
            }
        };
        TableColumn column;
        int columnCnt = main_Table.getColumnCount();
        for(int i = 0;i < columnCnt;++i) {
            column = main_Table.getColumnModel().getColumn(i);
            column.setCellRenderer(tcr);
        }
        main_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = main_Table.getTableHeader();
        header.setFont(new Font("楷体",Font.PLAIN,14));
        header.setEnabled(false);
        main_Table.setRowHeight(20);
        main_Table.setShowGrid(true);
        main_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        main_ScrollPane = new JScrollPane(main_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        main_ScrollPane.setViewportView(main_Table);
        this.add(main_ScrollPane);
    }
    
    public void setValueAt(Object data,int row,int column) {
        main_Table.setValueAt(data, row, column);
    }
    public void setValueAtColumn(Object[] data,int column) {
        for(int i = 0;i < modelData.length;++i) {
            main_Table.setValueAt(data[i], i, column);
        }
    }
    public void setValueAtRow(Object[] data,int row) {
        for(int i = 0;i < modelData[0].length;++i) {
            main_Table.setValueAt(data[i], row, i);
        }
    }
    public Object getValueAt(int row,int column) {
        return main_Table.getValueAt(row, column);
    }
    public Object[] getValueAtColumn(int column) {
        Object[] ret = new Object[modelData.length];
        for(int i = 0;i < modelData.length;++i) {
            ret[i] = main_Table.getValueAt(i, column);
        }
        return ret;
    }
    public Object[] getValueAtRow(int row) {
        Object[] ret = new Object[modelData[0].length];
        for(int i = 0;i < modelData[0].length;++i) {
            ret[i] = main_Table.getValueAt(row, i);
        }
        return ret;
    }
    public TableModel getModel() {
        return main_Table.getModel();
    }
    public void setCellEditable(boolean flag,int row,int column) {
        cellEditable[row][column] = flag;
    }
    public void setColumnEditable(boolean flag,int column) {
        for(int i = 0;i < modelData.length;++i) {
            cellEditable[i][column] = flag;
        }
    }
    public void setRowEditable(boolean flag,int row) {
        for(int i = 0;i < modelData[0].length;++i) {
            cellEditable[row][i] = flag;
        }
    }
    public void setColumnWidth(int width) {
        TableColumn column;
        int columnCnt = main_Table.getColumnCount();
        for(int i = 0;i < columnCnt;++i) {
            column = main_Table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width);
        }
    }
    public void setColumnWidth(int col,int width) {
        TableColumn column;
        column = main_Table.getColumnModel().getColumn(col);
        column.setPreferredWidth(width);
    }
    public void setColumnWidth(int[] width) {
        TableColumn column;
        int columnCnt = main_Table.getColumnCount();
        for(int i = 0;i < columnCnt;++i) {
            column = main_Table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width[i]);
        }
    }
    @Override
    public void setPreferredSize(Dimension d) {
        main_ScrollPane.setPreferredSize(d);
    }
    public void setRowHeight(int width) {
        main_Table.setRowHeight(width);
    }
    public void setRowHeight(int row,int width) {
        main_Table.setRowHeight(row, width);
    }
    public int getSelectedRow() {
        return main_Table.getSelectedRow();
    }
    public boolean isRowSelected(int row) {
        return main_Table.isRowSelected(row);
    }
    public int getRowCount() {
        return main_Table.getRowCount();
    }
    public int getColumnCount() {
        return main_Table.getColumnCount();
    }
    
    private JTable main_Table;
    private JScrollPane main_ScrollPane;
    private String[] columnsName;
    private Object[][] modelData;
    private DefaultTableModel tableModel;
    private DefaultTableCellRenderer tcr;
    private boolean[][] cellEditable;
}
