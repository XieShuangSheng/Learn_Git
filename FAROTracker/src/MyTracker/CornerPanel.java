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
public class CornerPanel extends MyPanel{
    public CornerPanel() {
        initComponents();
    }
    private void initComponents() {
        corner_Panel = new JPanel();
        
        corner_Panel.setBorder(BorderFactory.createTitledBorder("圆角误差和拐角超调"));
        
        this.setLayout(new GridLayout(1,1));
        this.add(corner_Panel);
    }
    
    private JPanel corner_Panel;
}
