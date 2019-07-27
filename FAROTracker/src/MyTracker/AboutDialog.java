/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author szhc
 */
public class AboutDialog extends JDialog{
    public AboutDialog() {
        super();
        initComponents();
    }
    private void initComponents() {
        this.setPreferredSize(new Dimension(400,300));
        
        softName_Label = new JLabel("机器人结构激光标定软件V1.0.3");
        author_Label = new JLabel("著作权人：深圳市华成工业控制有限公司");
        homePage_Label = new JLabel("公司主页：");
        homePage_URL = new JLabel("<html><a href='http://www.hc-system.com'>http://www.hc-system.com</a></html>");
        homePage_URL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel info = new JPanel();
        info.setLayout(new GridLayout(5,1));
        info.setBorder(BorderFactory.createRaisedBevelBorder());
        info.add(softName_Label);
        info.add(author_Label);
        info.add(homePage_Label);
        info.add(homePage_URL);
        this.add(info);
        
        homePage_URL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                try {
                    Runtime.getRuntime().exec("cmd.exe /c start http://www.hc-system.com");
                }
                catch(IOException e) {
                    System.out.println(e);
                }
            }
        });
        
        this.setTitle("关于");
        this.setResizable(false);
        this.setModal(true);
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        Image img = toolKit.getImage("Res/Tracker.png");
        this.setIconImage(img);
        this.setLocation(500, 300);
        pack();
    }
    
    private JLabel softName_Label;
    private JLabel author_Label;
    private JLabel homePage_Label;
    private JLabel homePage_URL;
}
