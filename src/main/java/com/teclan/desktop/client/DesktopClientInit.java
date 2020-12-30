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

    public static void initLoginFrem(ClientService clientService){

        /**
         * 登录主窗体
         */
        JFrame loginFrem = new JFrame( );
        loginFrem.setUndecorated(true);
        loginFrem.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        loginFrem.setSize(600, 400);
        loginFrem.setResizable(false);
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

        account.setText("1");
        password.setText("1");

    }

    /**
     * 设置主工作空间
     */
    public static void showWorkSpace(String user){

        JFrame workSpace = new JFrame();
        workSpace.setSize(1300, 700);
        workSpace.setLocationRelativeTo(null);//在屏幕中居中显示
        workSpace.setUndecorated(true);
        workSpace.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        workSpace.setResizable(false);
        workSpace.setLayout(new BorderLayout());

        JPanel info = new JPanel();
        info.setLayout(new BorderLayout());
        info.setSize(200,1300);
        JLabel userAndService = new JLabel("当前登录用户:"+user+"                           服务器:"+Contant.SERVER_ADDRESS);
        userAndService.setSize(100,1300);
        userAndService.setFont(Contant.FONT);
        info.add(BorderLayout.NORTH,userAndService);

        JPanel jpFile = new JPanel();
        jpFile.setSize(100,1300);
        jpFile.setBounds(100,1300,0,0);
        JLabel localPath = new JLabel("本地文件路径:");
        localPath.setFont(Contant.FONT);
        JTextField path = new JTextField("--请选择--");
        path.setEditable(false);
        path.setFont(Contant.FONT);
        JButton chooser = new JButton("选择文件...");
        chooser.setFont(Contant.FONT);
        jpFile.add(localPath);
        jpFile.add(path);
        jpFile.add(chooser);

        info.add(BorderLayout.CENTER,jpFile);


        JPanel localAndRemote = new JPanel();
        localAndRemote.setBackground(Color.GRAY);
        localAndRemote.setSize(200,1300);

        workSpace.add(BorderLayout.NORTH,info);
        workSpace.add(BorderLayout.CENTER,localAndRemote);
        workSpace.setVisible(true);
    }

}
