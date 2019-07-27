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
public class MinTimeLocalPanel extends MyPanel{
    public MinTimeLocalPanel() {
        initComponents();
    }
    private void initComponents() {
        minTimeLocal_Panel = new JPanel();
        
        minTimeLocal_Panel.setBorder(BorderFactory.createTitledBorder("最小稳定时间"));
        
        this.setLayout(new GridLayout(1,1));
        this.add(minTimeLocal_Panel);
    }
    
    private JPanel minTimeLocal_Panel;
}
