/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.Dimension;

/**
 *
 * @author szhc
 */
public class DigitalMeter_Label extends JPanel{
    
    public DigitalMeter_Label() {
        maxValue = 0.0;
        minValue = 0.0;
        dotPose = 0;
        disp_Str = "";
        initComponents();
    }
    public DigitalMeter_Label(String str) {
        maxValue = 0.0;
        minValue = 0.0;
        dotPose = 0;
        disp_Str = str;
        initComponents();
    }
    public DigitalMeter_Label(double max,double min,String str) {
        maxValue = max;
        minValue = min;
        disp_Str = str;
        initComponents();
    }
    public DigitalMeter_Label(double max,double min,int dot,String str) {
        maxValue = max;
        minValue = min;
        dotPose = dot;
        disp_Str = str;
        initComponents();
    }
    
    private void initComponents() {
        Dimension d = new Dimension(60,30);
        content_TextField = new JTextField();
        disp_Label = new JLabel(disp_Str);
        content_TextField.setPreferredSize(d);
        disp_Label.setPreferredSize(d);
        this.add(disp_Label);
        this.add(content_TextField);
        
        content_TextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyCh = e.getKeyChar();
                String str = content_TextField.getText();
                int start = content_TextField.getSelectionStart();
                int end = content_TextField.getSelectionEnd();
                if((keyCh >= KeyEvent.VK_0) && (keyCh <= KeyEvent.VK_9) || keyCh == '.' || keyCh == '-') {
                    switch (keyCh) {
                        case '.':
                            if(dotPose == 0 || str.length() == 0 || str.contains(".")) {
                                e.consume();
                            }
                            break;
                        case '-':
                            if(minValue >=0 && maxValue > minValue) {
                                e.consume();
                            }   
                            else if(str.contains("-")) {
                                StringBuilder sb = new StringBuilder(str);
                                sb.deleteCharAt(0);
                                str = sb.toString();
                                content_TextField.setText(str);
                                e.consume();
                            }
                            else {
                                StringBuilder sb = new StringBuilder(str);
                                if(end - start == str.length()) {
                                    sb.delete(start, end);
                                }
                                sb.insert(0, keyCh);
                                str = sb.toString();
                                content_TextField.setText(str);
                                e.consume();
                            }
                            break;
                        default:
                            if(maxValue > minValue) {
                                StringBuilder sb = new StringBuilder(str);
                                for(int i = start; i< end;++i) {
                                    sb.deleteCharAt(start);
                                }
                                sb.insert(start, keyCh);
                                str = sb.toString();
//                                System.out.println(str);
                                double v = Double.parseDouble(str);
                                if(v> maxValue || v < minValue) {
                                    e.consume();
                                }
                                else if(str.lastIndexOf(".") != -1)
                                    if(str.length() - str.lastIndexOf(".") > dotPose + 1) {
                                        e.consume();
                                }
                            }
                            break;
                    }
                    
                }
                else {
                    e.consume();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                
            }
        });
        
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                content_TextField.selectAll();
            }
        });
        
        content_TextField.setText("0");
    }

    private double maxValue = 0.0;
    private double minValue = 0.0;
    private int dotPose = 0;
    private final String disp_Str;
    private JTextField content_TextField;
    private JLabel disp_Label;
}
