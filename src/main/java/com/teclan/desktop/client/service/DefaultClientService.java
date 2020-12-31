package com.teclan.desktop.client.service;

import com.alibaba.fastjson.JSONObject;
import com.teclan.desktop.client.utils.FileUtils;
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
        int index =1;
        for(String filePath:filePaths){
            jProgressBar.setValue(0);
            jLabel.setText(String.format("当前:%s (上传进度：%d/%d),",filePath,index++,filePaths.size()));
            // 上传
            jProgressBar.setValue(100);
        }
    }

    @Override
    public void download(JProgressBar jProgressBar, JLabel jLabel, List<String> filePaths) throws Exception {
        int index =1;
        for(String filePath:filePaths){
            jProgressBar.setValue(0);
            jLabel.setText(String.format("当前:%s (下载进度：%d/%d),",filePath,index++,filePaths.size()));
            // 上传
            jProgressBar.setValue(100);
        }
    }

    @Override
    public void reloadRemoteFileList(JTable jTable, String remoteFilePath) {
        LOGGER.info("即将获取服务器文件列表：{}",remoteFilePath);
        // TODO
        // 获取远程文件列表
        JSONObject jsonObject = new JSONObject();
        try {
            FileUtils.flusFileList(jTable,jsonObject);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

    }
}
