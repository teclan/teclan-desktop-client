package com.teclan.desktop.client;

import com.teclan.desktop.client.contant.Contant;
import com.teclan.desktop.client.listener.DefaultLoginActionListener;
import com.teclan.desktop.client.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class DesktopClientInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopClientInit.class);


    public  static  void init(ClientService clientService){

        Frame main = new Frame( );
        main.setSize(600, 400);
        main.setLocationRelativeTo(null);//在屏幕中居中显示
//        main.setBackground(Color.BLACK);
        main.setTitle("");
        main.setLayout(new GridLayout(6,1,0,0));

        JPanel jPanel0 = new JPanel();
        JLabel jLabel0 = new JLabel(Contant.SYSTEM);
        jLabel0.setFont(Contant.FONT);
        jPanel0.add(jLabel0);

        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel("账号：");
        jLabel1.setFont(Contant.FONT);
        JTextField account = new JTextField(10);
        account.setFont(Contant.FONT);
        jPanel1.add(jLabel1);
        jPanel1.add(account);

        JPanel jPanel2 = new JPanel();
        JLabel jLabel2 = new JLabel("密码：");

        jLabel2.setFont(Contant.FONT);
        JPasswordField password = new JPasswordField(10);
        password.setFont(Contant.FONT);
        jPanel2.add(jLabel2);
        jPanel2.add(password);

//        Dimension preferredSize = new Dimension(20,20);
        JPanel jPanel3 = new JPanel();
        JButton reset = new JButton("重置");
        reset.setFont(Contant.FONT);
//        reg.setPreferredSize(preferredSize );
        final JButton login = new JButton("登录");
        login.setFont(Contant.FONT);
        login.addActionListener(new DefaultLoginActionListener(clientService,main,account,password ));
//        login.setPreferredSize(preferredSize );
        jPanel3.add(reset);
        jPanel3.add(login);

        main.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                LOGGER.info("窗口即将关闭...");
                System.exit(0);
            }
        });

        password.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {

            }
            public void keyReleased(KeyEvent keyEvent) {

            }
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyChar() == KeyEvent.VK_ENTER )
                {
                    login.doClick();
                }
            }
        });

        JPanel jPanel4 = new JPanel();
        JLabel jLabel4 = new JLabel(Contant.COPYRIGHT);
        jPanel4.add(jLabel4);

        main.add(new JPanel());
        main.add(jPanel0);
        main.add(jPanel1);
        main.add(jPanel2);
        main.add(jPanel3);
        main.add(jPanel4);
        main.setVisible(true);

    }


}
