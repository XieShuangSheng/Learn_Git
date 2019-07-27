/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author szhc
 */
public class DigitalMeter extends JTextField{
    
    public DigitalMeter() {
        maxValue = 0.0;
        minValue = 0.0;
        dotPose = 0;
        initComponents();
    }
    public DigitalMeter(double max,double min) {
        maxValue = max;
        minValue = min;
        initComponents();
    }
    public DigitalMeter(double max,double min,int dot) {
        maxValue = max;
        minValue = min;
        dotPose = dot;
        initComponents();
    }
    
    private void initComponents() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyCh = e.getKeyChar();
                String str = getText();
                int start = getSelectionStart();
                int end = getSelectionEnd();
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
                                setText(str);
                                e.consume();
                            }
                            else {
                                StringBuilder sb = new StringBuilder(str);
                                if(end - start == str.length()) {
                                    sb.delete(start, end);
                                }
                                sb.insert(0, keyCh);
                                str = sb.toString();
                                setText(str);
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
                selectAll();
            }
        });
        
        setText("0");
    }
    
    public void SetValue(Object value) {
        this.setText(String.valueOf(value));
    }
    
    public Object GetValue() {
        return this.getText();
    }
    public int GetValueInteger() {
        return Integer.parseInt(this.getText());
    }
    public double GetValueDouble() {
        return Double.parseDouble(this.getText());
    }
    
    private double maxValue = 0.0;
    private double minValue = 0.0;
    private int dotPose = 0;
}
