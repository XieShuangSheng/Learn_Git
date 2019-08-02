/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author szhc
 */
public class TrajectorySpeedPanel extends MyPanel{
    public TrajectorySpeedPanel() {
        initComponents();
    }
    private void initComponents() {
        trackSpeed_Panel = new JPanel();
        trackSpeedDraw_Panel = new JPanel();
        trackSpeedTable_Panel = new JPanel();
        trackSpeedResult_Panel = new JPanel();
        
        ImageIcon trackSpeedDraw_img;
        ImageIcon trackSpeedTable_img;  
        trackSpeedDraw_img = new ImageIcon("Res/trackSpeedDraw.png");
        trackSpeedTable_img = new ImageIcon("Res/trackSpeedTable.png");
        trackSpeedDraw_Label = new JLabel(trackSpeedDraw_img);
        trackSpeedTable_Label = new JLabel(trackSpeedTable_img);
        
        trackSpeed_Panel.setBorder(BorderFactory.createTitledBorder("轨迹速度特性——测量点位设置"));
        trackSpeedDraw_Panel.setBorder(BorderFactory.createTitledBorder("轨迹速度特性图"));
        trackSpeedTable_Panel.setBorder(BorderFactory.createTitledBorder("轨迹速度特性试验条件"));
        trackSpeedResult_Panel.setBorder(BorderFactory.createTitledBorder("测试结果"));
        
        trackSpeed_Panel.setBounds(0, 0, 700, 200);
        trackSpeedDraw_Panel.setBounds(700, 300, 650, 360);
        trackSpeedTable_Panel.setBounds(700, 0, 900, 300);
        trackSpeedResult_Panel.setBounds(0, 200, 700, 460);
        
        this.setLayout(null);
        this.add(trackSpeed_Panel);
        this.add(trackSpeedDraw_Panel);
        this.add(trackSpeedTable_Panel);
        this.add(trackSpeedResult_Panel);
        
        trackSpeedDraw_Panel.add(trackSpeedDraw_Label);
        trackSpeedTable_Panel.add(trackSpeedTable_Label);
        
        Loops_Label = new JLabel("循环次数");
        Loops_Value = new JLabel("1");
        Loops_Value.setBorder(BorderFactory.createEtchedBorder());
        Loops_Value.setPreferredSize(new Dimension(25,25));
        change_Button = new JButton("修改数据");
        startMeasure_Button = new JButton("启动测量");
        stopMeasure_Button = new JButton("停止测量");
        

        
         //点位表格设置
        String[] trackSpeedColumn = {"点位","坐标X","坐标Y","坐标Z"};
        Object[][] trackSpeedModelData = new Object[2][4];
        for(int i = 0;i < trackSpeedModelData.length;++i) {
            for(int j = 0;j < trackSpeedModelData[0].length;++j) {
                if(i == 0 && j == 0) trackSpeedModelData[i][j] = "起点";
                else if(i == 1 && j == 0) trackSpeedModelData[i][j] = "终点";
                else trackSpeedModelData[i][j] = 0.0;
            }
        }
        trackSpeed_Table = new JTable(trackSpeedModelData,trackSpeedColumn);
        DefaultTableModel settingTableModel = new DefaultTableModel(trackSpeedModelData.length,trackSpeedModelData[0].length) {
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
            @Override
            public String getColumnName(int column) {
                return trackSpeedColumn[column];
            }
            @Override
            public int getColumnCount() {
                return trackSpeedColumn.length;
            }
            @Override
            public int getRowCount() {
                return trackSpeedModelData.length;
            }
            @Override
            public Object getValueAt(int row,int column) {
                return trackSpeedModelData[row][column];
            }
            @Override
            public void setValueAt(Object obj,int row,int column) {
                trackSpeedModelData[row][column] = obj;
                fireTableCellUpdated(row,column);
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return trackSpeedModelData[0][column].getClass();
            }
        };
        trackSpeed_Table.setModel(settingTableModel);
        TableColumn column = null;
        int columns = trackSpeed_Table.getColumnCount();
        for(int i = 0;i < columns;++i) {
            column = trackSpeed_Table.getColumnModel().getColumn(i);
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
            trackSpeed_Table.getColumn(trackSpeed_Table.getColumnName(i)).setCellRenderer(settingPoints_TCR);
        }
        trackSpeed_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = trackSpeed_Table.getTableHeader();
        header.setFont(new Font("楷体",Font.PLAIN,14));
        header.setEnabled(false);
        trackSpeed_Table.setRowHeight(40);
        trackSpeed_Table.setShowGrid(true);
        trackSpeed_Table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trackSpeed_ScrollPane = new JScrollPane(trackSpeed_Table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       trackSpeed_ScrollPane.setPreferredSize(new Dimension(610,110));
        trackSpeed_ScrollPane.setViewportView(trackSpeed_Table);
        trackSpeed_Panel.add(trackSpeed_ScrollPane);
        
        JPanel button_Panel = new JPanel();
        button_Panel.add(Loops_Label);
        button_Panel.add( Loops_Value);
        button_Panel.add(change_Button);
        button_Panel.add(startMeasure_Button);
        button_Panel.add(stopMeasure_Button);
        trackSpeed_Panel.add(button_Panel,BorderLayout.SOUTH);
        change_Button.setEnabled(false);
        startMeasure_Button.setEnabled(false);
        stopMeasure_Button.setEnabled(false);
        
        
    }
    
    private JPanel trackSpeed_Panel;
    private JPanel trackSpeedDraw_Panel;
    private JPanel trackSpeedTable_Panel;
    private JPanel trackSpeedResult_Panel;
    
    private JLabel trackSpeedDraw_Label;
    private JLabel trackSpeedTable_Label;
    
    private JTable points_Table;
    private JScrollPane points_ScrollPane;
    private JTable result_Table;
    private JScrollPane result_ScrollPane;
    
    private JButton change_Button;
    private JButton startMeasure_Button;
    private JButton stopMeasure_Button;
    
     private JTable trackSpeed_Table;
     
     private JScrollPane trackSpeed_ScrollPane;
     
    private JLabel Loops_Label;
    private JLabel Loops_Value;
}
