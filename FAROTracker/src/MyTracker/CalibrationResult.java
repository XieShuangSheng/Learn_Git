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
public class CalibrationResult extends JDialog{
    public CalibrationResult() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,2));
//        panel.add(new JLabel("Zero:"));
        zero_Label = new JLabel("");
        panel.add(zero_Label);
//        panel.add(new JLabel("Ratio:"));
        ratio_Label = new JLabel("");
        panel.add(ratio_Label);
//        panel.add(new JLabel("Alpha:"));
        alpha_Label = new JLabel("");
        panel.add(alpha_Label);
//        panel.add(new JLabel("Param:"));
        param_Label = new JLabel("");
        panel.add(param_Label);
//        panel.add(new JLabel("Tool:"));
        tool_Label = new JLabel("");
        panel.add(tool_Label);
//        panel.add(new JLabel("Diff"));
        diff_Label = new JLabel("");
        panel.add(diff_Label);
        
        this.setPreferredSize(new Dimension(600,130));
        this.setTitle("标定结果");
        this.setResizable(false);
        this.setModal(true);
        this.add(panel);
        pack();
    }
    
    public void SetLabelString(String [] str) {
        zero_Label.setText(str[0]);
        ratio_Label.setText(str[1]);
        alpha_Label.setText(str[2]);
        param_Label.setText(str[3]);
        tool_Label.setText(str[4]);
        diff_Label.setText(str[5]);
    }
    
    private JLabel zero_Label;
    private JLabel ratio_Label;
    private JLabel alpha_Label;
    private JLabel param_Label;
    private JLabel tool_Label;
    private JLabel diff_Label;
}
