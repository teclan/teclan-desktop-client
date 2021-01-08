package com.teclan.desktop.client.service;

import com.alibaba.fastjson.JSONObject;
import com.teclan.desktop.client.config.CommonConfig;
import com.teclan.desktop.client.utils.Assert;
import com.teclan.desktop.client.utils.FileUtils;
import com.teclan.desktop.client.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teclan.netty.client.FileClient;
import teclan.netty.handler.FileClientHandler;
import teclan.netty.handler.Monitor;
import teclan.netty.handler.ParamFetcher;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class DefaultClientService implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientService.class);

    public void login(String account, String password) throws Exception {
        LOGGER.info("正在登录，账号:{},密码:{}", account, password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account",account);
        jsonObject.put("password",password);

       String baseUrl =  CommonConfig.getConfig().getString("server.base_url");
       JSONObject bodey = HttpUtils.post("login.do",jsonObject);
       LOGGER.info("登录返回：{}",bodey);
       String code = bodey.getString("code");
       if(Assert.assertNotEquals("200",code)){
            throw new Exception(bodey.getString("message"));
       }
       startFileClient();
    }

    @Override
    public void upload(JProgressBar jProgressBar, JLabel jLabel, List<String> filePaths) throws Exception {
        int index = 1;
        for (String filePath : filePaths) {
            jProgressBar.setValue(0);
            jLabel.setText(String.format("当前:%s (上传进度：%d/%d),", filePath, index++, filePaths.size()));
            // 上传
            jProgressBar.setValue(100);
        }
    }

    @Override
    public void download(JProgressBar jProgressBar, JLabel jLabel, List<String> filePaths) throws Exception {
        int index = 1;
        for (String filePath : filePaths) {
            jProgressBar.setValue(0);
            jLabel.setText(String.format("当前:%s (下载进度：%d/%d),", filePath, index++, filePaths.size()));
            // 上传
            jProgressBar.setValue(100);
        }
    }

    @Override
    public void reloadRemoteFileList(JTable jTable, String remoteFilePath) {
        LOGGER.info("即将获取服务器文件列表：{}", remoteFilePath);
        // TODO
        // 获取远程文件列表
        JSONObject jsonObject = new JSONObject();
        try {
            FileUtils.flusFileList(jTable, jsonObject);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * 启动文件传输通道
     */
    private void startFileClient() throws Exception {

        FileClientHandler fileClientHandler = new FileClientHandler();

        /**
         * 设置进度监视器
         */
        fileClientHandler.setMonitor(new Monitor() {
            public String getProcess(String file) {
                // 自定义逻辑
                return null;
            }

            public void serProcess(String filePath, long max, long value) {
                // 自定义逻辑
            }

            public Map<String, String> getCahche() {
                // 自定义逻辑
                return null;
            }

            public void remove(String filePath) {
                // 自定义逻辑
            }
        });

        fileClientHandler.setParamFetcher(new ParamFetcher() {
            public JSONObject get() {
                // 自定义逻辑
                return null;
            }
        });

        FileClient fileClient = new FileClient(CommonConfig.getConfig().getString("server.file.host"), CommonConfig.getConfig().getInt("server.file.port"));
        fileClient.start();
    }

}
