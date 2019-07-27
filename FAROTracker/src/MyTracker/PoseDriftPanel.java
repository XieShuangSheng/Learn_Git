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
public class PoseDriftPanel extends MyPanel{
    public PoseDriftPanel() {
        initComponents();
    }
    private void initComponents() {
        poseDrift_Panel = new JPanel();
        
        poseDrift_Panel.setBorder(BorderFactory.createTitledBorder("位姿特性漂移"));
        
        this.setLayout(new GridLayout(1,1));
        this.add(poseDrift_Panel);
    }
    
    private JPanel poseDrift_Panel;
}
