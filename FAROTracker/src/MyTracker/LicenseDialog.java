/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import MyTracker.Register;

/**
 *
 * @author szhc
 */
public class LicenseDialog extends JDialog{
    public LicenseDialog(Register r) {
        register = r;
        initComponents();
    }
    
    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setTitle("注册");
        this.setPreferredSize(new Dimension(500,150));
        this.setLocation(500, 300);
        this.setModal(true);
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        Image img = toolKit.getImage("Res/Tracker.png");
        this.setIconImage(img);
        
        serial_Label = new JLabel("序列号");
        serial_TextField = new JTextField();
        serial_Button = new JButton("生成序列号");
        reg_Label = new JLabel("注册码");
        reg_TextField = new JTextField(25);
        ok_Button = new JButton("确定");
        cancel_Button = new JButton("取消");
//        reg_Label.setPreferredSize(new Dimension(50,30));

        JPanel main_Panel = new JPanel();
        main_Panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.25;
        gbc.weighty = 0.4;
        main_Panel.add(serial_Label,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.4;
        main_Panel.add(serial_TextField,gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.25;
        gbc.weighty = 0.4;
        main_Panel.add(serial_Button,gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.25;
        gbc.weighty = 0.4;
        main_Panel.add(reg_Label,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 0.75;
        gbc.weighty = 0.4;
        main_Panel.add(reg_TextField,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.25;
        gbc.weighty = 0.2;
        main_Panel.add(ok_Button,gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.25;
        gbc.weighty = 0.2;
        main_Panel.add(cancel_Button,gbc);
        
        serial_TextField.setEditable(false);

        this.add(main_Panel);
        
        serial_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                serial_ButtonActionPerformed(ae);
            }
        });
        
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
        
        pack();
    }
    
    private void serial_ButtonActionPerformed(ActionEvent ae) {
        char[] cp = GetRandomSeries();
        String bkStr = "";
        for(int i = 0;i < cp.length;++i) {
            bkStr+=cp[i];
        }
        serial_TextField.setText(bkStr);
    }
    
    private void ok_ButtonActionPerformed(ActionEvent ae) {
        String series = register.GetWinHDSeries("C:");
        String regCode = reg_TextField.getText();

        String[] values = {"www.hc-system.com",series,regCode};
        
        if(register.IsRegCodeCorrect(regCode)) {
            JOptionPane.showMessageDialog(this, "注册成功！");
            this.setVisible(false);
            register.WriteValues(values);
        }
        else {
            JOptionPane.showMessageDialog(this, "注册码不可用！");
        }
    }
    private void cancel_ButtonActionPerformed(ActionEvent ae) {
        this.setVisible(false);
    }
    
    private char[] GetRandomSeries() {
        char[] ret = new char[8];
        for(int i = 0;i < ret.length;) {
            int v = (int) (Math.random() * 122);
            char c = (char)v;
            if((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                ret[i] = c;
                ++i;
            }
        }
        return ret;
    }
    
    private final Register register;
    private JLabel serial_Label; //序列号
    private JTextField serial_TextField; //序列号框
    private JButton serial_Button; //生成序列号
    private JLabel reg_Label;
    private JTextField reg_TextField;
    private JButton ok_Button;
    private JButton cancel_Button;
}
