/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author szhc
 */
public class CoordTransform extends JDialog{
    public CoordTransform() {
        initComponents();
    }
    private void initComponents() {
        d0_0 = new DigitalMeter();d0_1 = new DigitalMeter();d0_2 = new DigitalMeter();d0_3 = new DigitalMeter();
        d1_0 = new DigitalMeter();d1_1 = new DigitalMeter();d1_2 = new DigitalMeter();d1_3 = new DigitalMeter();
        d2_0 = new DigitalMeter();d2_1 = new DigitalMeter();d2_2 = new DigitalMeter();d2_3 = new DigitalMeter();
        d3_0 = new DigitalMeter();d3_1 = new DigitalMeter();d3_2 = new DigitalMeter();d3_3 = new DigitalMeter();
        
        t0_0 = new DigitalMeter();t0_1 = new DigitalMeter();t0_2 = new DigitalMeter();t0_3 = new DigitalMeter();
        t1_0 = new DigitalMeter();t1_1 = new DigitalMeter();t1_2 = new DigitalMeter();t1_3 = new DigitalMeter();
        t2_0 = new DigitalMeter();t2_1 = new DigitalMeter();t2_2 = new DigitalMeter();t2_3 = new DigitalMeter();
        t3_0 = new DigitalMeter();t3_1 = new DigitalMeter();t3_2 = new DigitalMeter();t3_3 = new DigitalMeter(); 
        
        d_List = new ArrayList<>();
        d_List.add(d0_0);d_List.add(d1_0);d_List.add(d2_0);d_List.add(d3_0);
        d_List.add(d0_1);d_List.add(d1_1);d_List.add(d2_1);d_List.add(d3_1);
        d_List.add(d0_2);d_List.add(d1_2);d_List.add(d2_2);d_List.add(d3_2);
        d_List.add(d0_3);d_List.add(d1_3);d_List.add(d2_3);d_List.add(d3_3);
        
        t_List = new ArrayList<>();
        t_List.add(t0_0);t_List.add(t1_0);t_List.add(t2_0);t_List.add(t3_0);
        t_List.add(t0_1);t_List.add(t1_1);t_List.add(t2_1);t_List.add(t3_1);
        t_List.add(t0_2);t_List.add(t1_2);t_List.add(t2_2);t_List.add(t3_2);
        t_List.add(t0_3);t_List.add(t1_3);t_List.add(t2_3);t_List.add(t3_3);
        
        
        d0_0.setEnabled(false);d0_1.setEnabled(false);d0_2.setEnabled(false);d0_3.setEnabled(false);
        d1_0.setEnabled(false);d1_1.setEnabled(false);d1_2.setEnabled(false);d1_3.setEnabled(false);
        d2_0.setEnabled(false);d2_1.setEnabled(false);d2_2.setEnabled(false);d2_3.setEnabled(false);
        d3_0.setEnabled(false);d3_1.setEnabled(false);d3_2.setEnabled(false);d3_3.setEnabled(false);
        
        t0_0.setEnabled(false);t0_1.setEnabled(false);t0_2.setEnabled(false);t0_3.setEnabled(false);
        t1_0.setEnabled(false);t1_1.setEnabled(false);t1_2.setEnabled(false);t1_3.setEnabled(false);
        t2_0.setEnabled(false);t2_1.setEnabled(false);t2_2.setEnabled(false);t2_3.setEnabled(false);
        t3_0.setEnabled(false);t3_1.setEnabled(false);t3_2.setEnabled(false);t3_3.setEnabled(false);
        
        JPanel d_Panel = new JPanel();
        d_Panel.setLayout(new GridLayout(4,4));
//        d_Panel.setPreferredSize(new Dimension(300,150));
        d_Panel.setBorder(BorderFactory.createTitledBorder("坐标准直(激光仪到基坐标系)"));
        d_Panel.add(d0_0);d_Panel.add(d0_1);d_Panel.add(d0_2);d_Panel.add(d0_3);
        d_Panel.add(d1_0);d_Panel.add(d1_1);d_Panel.add(d1_2);d_Panel.add(d1_3);
        d_Panel.add(d2_0);d_Panel.add(d2_1);d_Panel.add(d2_2);d_Panel.add(d2_3);
        d_Panel.add(d3_0);d_Panel.add(d3_1);d_Panel.add(d3_2);d_Panel.add(d3_3);
        
        JPanel t_Panel = new JPanel();
        t_Panel.setLayout(new GridLayout(4,4));
//        t_Panel.setPreferredSize(new Dimension(300,150));
        t_Panel.setBorder(BorderFactory.createTitledBorder("工具坐标"));
        t_Panel.add(t0_0);t_Panel.add(t0_1);t_Panel.add(t0_2);t_Panel.add(t0_3);
        t_Panel.add(t1_0);t_Panel.add(t1_1);t_Panel.add(t1_2);t_Panel.add(t1_3);
        t_Panel.add(t2_0);t_Panel.add(t2_1);t_Panel.add(t2_2);t_Panel.add(t2_3);
        t_Panel.add(t3_0);t_Panel.add(t3_1);t_Panel.add(t3_2);t_Panel.add(t3_3);
        
        String[] columnName = {"靶球","坐标X","坐标Y","坐标Z"};
        Object[][] modelData = new Object[3][4];
        for(int i = 0;i < modelData.length;++i) {
            for(int j = 0;j < modelData[0].length;++j) {
                if(j == 0) {
                    if(i == 0) {
                        modelData[i][j] = String.valueOf(i+1)+"(Main)";
                    }
                    else {
                        modelData[i][j] = i+1;
                    }
                }
                else {
                    modelData[i][j] = 0.0;
                }
            }
        }
        JPanel target_Panel = new JPanel();
        targetBall_TablePanel = new TablePanel(modelData,columnName);
        targetBall_TablePanel.setPreferredSize(new Dimension(305,88));
        target_Panel.setBorder(BorderFactory.createTitledBorder("靶球位置"));
        target_Panel.setLayout(new BorderLayout());
        readPose_Button = new JButton("设入位置");
        testPose_Button = new JButton("测试位置");
        target_Panel.add(targetBall_TablePanel,BorderLayout.NORTH);
        JPanel button_Panel = new JPanel();
        button_Panel.setLayout(new FlowLayout());
        button_Panel.add(readPose_Button);
        button_Panel.add(testPose_Button);
        target_Panel.add(button_Panel,BorderLayout.SOUTH);

        this.setLayout(new GridLayout(3,1));
        this.setPreferredSize(new Dimension(600,600));
        this.add(d_Panel);
        this.add(t_Panel);
        this.add(target_Panel);

        readPose_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                readPose_ButtonActionPerformed(ae);
            }
        });
        testPose_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                testPose_ButtonActionPerformed(ae);
            }
        });
        
        
        this.setTitle("坐标变换");
        this.setModal(true);
        this.setResizable(false);
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        int width = toolKit.getScreenSize().width;
        int height = toolKit.getScreenSize().height;
        int local_X = (width-this.getWidth())/2;
        int local_Y = (height-this.getHeight())/2;
        System.out.println(local_X + " " + local_Y);
        this.setLocation(local_X,local_Y);
        pack();
    }
    
    public void SetValuesCoord(double[] values) {
        DigitalMeter d;
        DecimalFormat df = new DecimalFormat("0.0000");
        for(int i = 0;i < values.length;++i) {
            d = (DigitalMeter)d_List.get(i);
            d.SetValue(df.format(values[i]));
        }
    }
    public void SetValuesTool(double[] values) {
        DigitalMeter d;
        DecimalFormat df = new DecimalFormat("0.0000");
        for(int i = 0;i < t_List.size();++i) {
            d = (DigitalMeter)t_List.get(i);
            d.SetValue(df.format(0));
        }
        t0_3.SetValue(df.format(values[0]));
        t1_3.SetValue(df.format(values[1]));
        t2_3.SetValue(df.format(values[2]));
        t3_3.SetValue(df.format(1));
    }
    
    public void readPose_ButtonActionPerformed(ActionEvent ae) {
        if(targetBall_TablePanel.getSelectedRow() == -1) {
            return;
        }
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        trackerThread.ProcessChanged(CURRENTPROCESS.COORDTRANS_SINGLEREAD_PROCESS);
        trackerThread.SingleReadMeasure();
    }
    private void testPose_ButtonActionPerformed(ActionEvent ae) {
        if(trackerThread == null) {
            trackerThread = MyTracker.getTrackerThread();
        }
        int row = targetBall_TablePanel.getSelectedRow();
        if(row != -1) {
            double[] coord = new double[3];
            for(int i = 1;i < 4;++i) {
                coord[i-1] = Double.parseDouble(targetBall_TablePanel.getModel().getValueAt(row, i).toString());
            }
            trackerThread.MoveTo(coord,true);
        }
    }
    public void SetTrackerValue(final Object[] values) {
        int row = targetBall_TablePanel.getSelectedRow();
        DecimalFormat df = new DecimalFormat("0.000"); //设置数据显示格式为X.XXXX
        if(targetBall_TablePanel.isRowSelected(row)) {
            for(int i = 1;i < 4;++i) {
                double v = Double.parseDouble(values[i-1].toString());
                targetBall_TablePanel.setValueAt(df.format(v),row,i);
            }
        }
    }
    
    //coord transform
    private DigitalMeter d0_0,d0_1,d0_2,d0_3;
    private DigitalMeter d1_0,d1_1,d1_2,d1_3;
    private DigitalMeter d2_0,d2_1,d2_2,d2_3;
    private DigitalMeter d3_0,d3_1,d3_2,d3_3;
    private List d_List;
    //tool coord
    private DigitalMeter t0_0,t0_1,t0_2,t0_3;
    private DigitalMeter t1_0,t1_1,t1_2,t1_3;
    private DigitalMeter t2_0,t2_1,t2_2,t2_3;
    private DigitalMeter t3_0,t3_1,t3_2,t3_3;
    private List t_List;
    //target ball position
    private TablePanel targetBall_TablePanel;
    private JButton readPose_Button;
    private JButton testPose_Button;
  
    private TrackerThread trackerThread;
}
