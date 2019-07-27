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
public class StabilityPanel extends MyPanel{
    public StabilityPanel() {
        initComponents();
    }
    private void initComponents() {
        posStableTime_Panel = new JPanel();
        sketch_Panel = new JPanel();
        ImageIcon img = new ImageIcon("Res/stability.png");
        sketch_Label = new JLabel(img);
        
        posStableTime_Panel.setBorder(BorderFactory.createTitledBorder("位置稳定时间和超调量"));
        sketch_Panel.setBorder(BorderFactory.createTitledBorder("示意图"));
        
        sketch_Panel.add(sketch_Label);
        
        this.setLayout(new GridLayout(1,2));
        this.add(posStableTime_Panel);
        this.add(sketch_Panel);
    }
    
    private JPanel posStableTime_Panel;
    private JPanel sketch_Panel;
    private JLabel sketch_Label;
}
