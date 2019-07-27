/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author szhc
 */
public class DistancePanel extends MyPanel{
    public DistancePanel() {
        initComponents();
    }
    private void initComponents() {
        distance_Panel = new JPanel();
        sketch_Panel = new JPanel();
        ImageIcon img= new ImageIcon("Res/distance.png");
        ImageIcon imgRun = new ImageIcon("Res/distanceRun.png");
        sketch_Label = new JLabel(img);
        sketchRun_Label = new JLabel(imgRun);
        start_Button = new JButton("开始测量");
        stop_Button = new JButton("停止测量");
        
        distance_Panel.setBorder(BorderFactory.createTitledBorder("距离准确度和重复性"));
        sketch_Panel.setBorder(BorderFactory.createTitledBorder("示意图"));
        sketch_Panel.add(sketch_Label);
        sketch_Panel.add(sketchRun_Label);
        
        this.setLayout(null);
        distance_Panel.setBounds(0, 0, 900, 550);
        sketch_Panel.setBounds(900, 0, 450, 550);
        this.add(distance_Panel);
        this.add(sketch_Panel);
    }
    
    private JPanel distance_Panel;
    private JPanel sketch_Panel;
    
    private JLabel sketch_Label;
    private JLabel sketchRun_Label;
    
    private JButton start_Button;
    private JButton stop_Button;
}
