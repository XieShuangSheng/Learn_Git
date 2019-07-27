/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author szhc
 */
public class MultiDirectionPanel extends MyPanel{
    public MultiDirectionPanel() {
        initComponents();
    }
    private void initComponents() {
        control_Panel = new JPanel();
        sketch_Panel = new JPanel();
        start_Button = new JButton("开始测量");
        stop_Button = new JButton("停止测量");
        manualRead_Button = new JButton("手动读取");
        
        ImageIcon imgSketch = new ImageIcon("Res/MultiDirSketch.png");
        ImageIcon imgRun = new ImageIcon("Res/MultiDirRun.png");
        multiDirSketch_Label = new JLabel(imgSketch);
        multiDirRun_Label = new JLabel(imgRun);
        
        control_Panel.setBorder(BorderFactory.createTitledBorder("多方向位姿"));
        sketch_Panel.setBorder(BorderFactory.createTitledBorder("示意图"));
        
        JPanel button_Panel = new JPanel();
        button_Panel.add(start_Button);
        button_Panel.add(stop_Button);
        button_Panel.add(manualRead_Button);
        control_Panel.add(button_Panel);
        
        sketch_Panel.add(multiDirSketch_Label);
        sketch_Panel.add(multiDirRun_Label);
        
        this.setLayout(null);
        control_Panel.setBounds(0, 0, 800, 560);
        sketch_Panel.setBounds(800, 0, 550, 560);
        this.add(control_Panel);
        this.add(sketch_Panel);
    }
    
    private JPanel control_Panel;
    private JPanel sketch_Panel;
    
    private JButton start_Button;
    private JButton stop_Button;
    private JButton manualRead_Button;
    
    private JLabel multiDirSketch_Label;
    private JLabel multiDirRun_Label;
}
