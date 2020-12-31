package com.teclan.desktop.client.listener;

import com.teclan.desktop.client.DesktopClientInit;
import com.teclan.desktop.client.service.ClientService;
import com.teclan.desktop.client.utils.Assert;
import com.teclan.desktop.client.utils.DialogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 点击登录框触发的事件监听器
 */
public class DefaultLoginActionListener implements ActionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLoginActionListener.class);
    private Frame frame;
    private JTextField account;
    private JPasswordField password;
    private ClientService clientService;
    private boolean logined = false;

    public DefaultLoginActionListener(ClientService clientService, Frame frame, JTextField account, JPasswordField password) {
        this.account = account;
        this.password = password;
        this.clientService = clientService;
        this.frame = frame;
    }

    public synchronized void actionPerformed(ActionEvent actionEvent) {
        if (logined) {
            return;
        }
        String user = account.getText();
        String pwd = password.getText();
        try {
            if (Assert.assertNullString(user)) {
                throw new Exception("账号不能为空!");
            }
            if (Assert.assertNullString(pwd)) {
                throw new Exception("密码不能为空!");
            }

            clientService.login(user, pwd);
            logined = true;
            frame.setVisible(false);
            frame.dispose();
            DesktopClientInit.showWorkSpace(clientService,user);
            LOGGER.info("登录完成 ...");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            logined = false;
            frame.setEnabled(false);
            DialogUtils.showError(e.getMessage(), new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    LOGGER.info("弹出窗口即将关闭...");
                    frame.setEnabled(true);
                    frame.setVisible(true);
                }
            });
        }
    }
}
