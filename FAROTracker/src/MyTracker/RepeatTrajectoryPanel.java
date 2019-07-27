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
public class RepeatTrajectoryPanel extends MyPanel{
    public RepeatTrajectoryPanel() {
        initComponents();
    }
    private void initComponents() {
        repeatTraj_Panel = new JPanel();
        
        repeatTraj_Panel.setBorder(BorderFactory.createTitledBorder("重复定向轨迹准确度"));
        
        this.setLayout(new GridLayout(1,1));
        this.add(repeatTraj_Panel);
    }
    
    private JPanel repeatTraj_Panel;
}
