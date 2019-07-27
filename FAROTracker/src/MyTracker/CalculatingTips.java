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
public class CalculatingTips extends JDialog{
    public CalculatingTips() {
        super();
        initComponents();
    }
    private void initComponents() {
        this.setPreferredSize(new Dimension(200,100));
        this.setLayout(new BorderLayout());
        tips_Label = new JLabel("计算中，请稍候···",SwingConstants.CENTER);
        this.add(tips_Label,BorderLayout.CENTER);
        this.setTitle("提示");
        this.setLocation(500, 300);
        
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        Image img = toolKit.getImage("Res/Tracker.png");
        this.setIconImage(img);
        this.setAlwaysOnTop(true);
        pack();
    }
    
    private JLabel tips_Label;
}
