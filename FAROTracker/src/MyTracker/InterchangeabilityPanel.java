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
public class InterchangeabilityPanel extends MyPanel{
    public InterchangeabilityPanel() {
        initComponents();
    }
    private void initComponents() {
        interChange_Panel = new JPanel();
        sketch_Panel = new JPanel();
        ImageIcon img;
        img = new ImageIcon("Res/interchange.png");
        sketch_Label = new JLabel(img);
        
        interChange_Panel.setBorder(BorderFactory.createTitledBorder("互换性"));
        sketch_Panel.setBorder(BorderFactory.createTitledBorder("示意图"));
        
        sketch_Panel.add(sketch_Label);
        
        this.setLayout(new GridLayout(1,2));
        this.add(interChange_Panel);
        this.add(sketch_Panel);
    }
    
    private JPanel interChange_Panel;
    private JPanel sketch_Panel;
    
    private JLabel sketch_Label;
}
