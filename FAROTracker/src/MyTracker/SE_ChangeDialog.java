/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MyTracker;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author xss
 */
public class SE_ChangeDialog extends JDialog{
    MyPanel frame;
    public SE_ChangeDialog(MyPanel parent) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,1));
        panel.add(new JLabel("坐标X:"));
        axis1Angle_TextField = new DigitalMeter(360,-360,4);
        panel.add(axis1Angle_TextField);
        panel.add(new JLabel("坐标Y:"));
        axis2Angle_TextField = new DigitalMeter(360,-360,4);
        panel.add(axis2Angle_TextField);
        panel.add(new JLabel("坐标Z:"));
        axis3Angle_TextField = new DigitalMeter(360,-360,4);
        panel.add(axis3Angle_TextField);
        
        JPanel button_Panel = new JPanel();
        ok_Button = new JButton("确定");
        cancel_Button = new JButton("取消");
        button_Panel.add(ok_Button);
        button_Panel.add(cancel_Button);
        
        ok_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ok_ButtonActionPerformed(ae);
            }
        });
        
        cancel_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                cancel_ButtonActionPerformed(ae);
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {//窗口获得焦点
                Object[] ret;
                ret = frame.GetTableRowValue();
                setAxisParams(ret);
                axis1Angle_TextField.requestFocus();
            }
        });
        
        this.frame = parent;
        
        this.setPreferredSize(new Dimension(600,130));
        this.setTitle("修改坐标");
        this.setResizable(false);
        this.setModal(true);
        this.setLayout(new GridLayout(2,1));
        this.setLocationRelativeTo(parent);
        this.add(panel);
        this.add(button_Panel);
        this.pack();
    }
    
    private void ok_ButtonActionPerformed(ActionEvent ae) {
        Object[] values;
        values = getAxisParams();
        frame.SetTableValue(values);
        this.setVisible(false);
    }
    private void cancel_ButtonActionPerformed(ActionEvent ae) {
        this.setVisible(false);
    }
    
    private void setAxisParams(Object[] values) {
        axis1Angle_TextField.setText(String.valueOf(values[0]));
        axis2Angle_TextField.setText(String.valueOf(values[1]));
        axis3Angle_TextField.setText(String.valueOf(values[2]));
    }
    private Object[] getAxisParams() {
        Object[] ret = new Object[6];
        ret[0] = axis1Angle_TextField.getText();
        ret[1] = axis2Angle_TextField.getText();
        ret[2] = axis3Angle_TextField.getText();
        return ret;
    }
    
    
    private DigitalMeter axis1Angle_TextField;
    private DigitalMeter axis2Angle_TextField;
    private DigitalMeter axis3Angle_TextField;
    private JButton ok_Button;
    private JButton cancel_Button;
}

