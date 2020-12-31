package com.teclan.desktop.client.service;

import com.teclan.desktop.client.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

public class DefaultClientService implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientService.class);

    public void login(String account, String password) throws Exception{
        LOGGER.info("正在登录，账号:{},密码:{}",account,password);
    }

    @Override
    public void upload(JProgressBar jProgressBar, JLabel jLabel, List<String> filePaths) throws Exception {
        for(String filePath:filePaths){
            jProgressBar.setValue(0);
            jLabel.setText(filePath);
            // 上传
            jProgressBar.setValue(100);
        }
    }
}
