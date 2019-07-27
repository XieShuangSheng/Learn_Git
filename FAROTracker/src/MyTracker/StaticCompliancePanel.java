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
public class StaticCompliancePanel extends MyPanel{
    public StaticCompliancePanel() {
        initComponents();
    }
    private void initComponents() {
        staticComp_Panel = new JPanel();
        
        staticComp_Panel.setBorder(BorderFactory.createTitledBorder("静态柔顺性"));
        
        this.setLayout(new GridLayout(1,1));
        this.add(staticComp_Panel);
    }
    
    private JPanel staticComp_Panel;
}
