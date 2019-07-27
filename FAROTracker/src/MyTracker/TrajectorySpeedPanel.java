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
public class TrajectorySpeedPanel extends MyPanel{
    public TrajectorySpeedPanel() {
        initComponents();
    }
    private void initComponents() {
        trajSpeed_Panel = new JPanel();
        
        trajSpeed_Panel.setBorder(BorderFactory.createTitledBorder("轨迹速度特性"));
        
        this.setLayout(new GridLayout(1,1));
        this.add(trajSpeed_Panel);
    }
    
    private JPanel trajSpeed_Panel;
}
