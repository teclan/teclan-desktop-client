package com.teclan.desktop.client;

import com.teclan.desktop.client.contant.Contant;
import com.teclan.desktop.client.listener.DefaultLoginActionListener;
import com.teclan.desktop.client.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DesktopClientInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopClientInit.class);

    public  static  void init(ClientService clientService){

        /**
         * 登录主窗体
         */
        Frame loginFrem = new Frame( );
        loginFrem.setSize(600, 400);
        loginFrem.setLocationRelativeTo(null);//在屏幕中居中显示
        loginFrem.setTitle("");
        loginFrem.setLayout(new GridLayout(6,1,0,0));

        // 无设置信息，仅作为占位排版使用
        JPanel jpNothing = new JPanel();
        JLabel jlNothing = new JLabel(Contant.SYSTEM);
        jlNothing.setFont(Contant.FONT);
        jpNothing.add(jlNothing);
        // 账号信息面板
        JPanel jpAccount = new JPanel();
        JLabel jlAccount = new JLabel("账号：");
        jlAccount.setFont(Contant.FONT);
        final JTextField account = new JTextField(10);
        account.setFont(Contant.FONT);
        jpAccount.add(jlAccount);
        jpAccount.add(account);
        // 密码信息面板
        JPanel jpPassword = new JPanel();
        JLabel jlPassword = new JLabel("密码：");
        jlPassword.setFont(Contant.FONT);
        final JPasswordField password = new JPasswordField(10);
        password.setFont(Contant.FONT);
        jpPassword.add(jlPassword);
        jpPassword.add(password);

        /**
         * 重置、登录按钮信息面板
         */
        JPanel jpReset = new JPanel();
        JButton reset = new JButton("重置");
        reset.setFont(Contant.FONT);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                account.setText("");
                password.setText("");
            }
        });
        final JButton login = new JButton("登录");
        login.setFont(Contant.FONT);
        login.addActionListener(new DefaultLoginActionListener(clientService,loginFrem,account,password ));
        jpReset.add(reset);
        jpReset.add(login);
        /**
         * 版权信息面板
         */
        JPanel jpCopyRight = new JPanel();
        JLabel jlCopyRight = new JLabel(Contant.COPYRIGHT);
        jpCopyRight.add(jlCopyRight);


        loginFrem.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                LOGGER.info("主窗口即将关闭...");
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

        loginFrem.add(new JPanel());
        loginFrem.add(jpNothing);
        loginFrem.add(jpAccount);
        loginFrem.add(jpPassword);
        loginFrem.add(jpReset);
        loginFrem.add(jpCopyRight);
        loginFrem.setVisible(true);

    }


}
