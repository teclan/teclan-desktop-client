package com.teclan.desktop.client.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teclan.desktop.client.DesktopClientInit;
import com.teclan.desktop.client.config.CommonConfig;
import com.teclan.desktop.client.contant.Constant;
import com.teclan.desktop.client.utils.Assert;
import com.teclan.desktop.client.utils.DialogUtils;
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

    private  FileClient fileClient;

    public void login(String account, String password) throws Exception {
        LOGGER.info("正在登录，账号:{},密码:{}", account, password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", account);
        jsonObject.put("password", password);

        JSONObject body = HttpUtils.post("login.do", jsonObject);
        LOGGER.info("登录返回：{}", body);
        String code = body.getString("code");
        if (Assert.assertNotEquals("200", code)) {
            throw new Exception(body.getString("message"));
        } else {
            JSONObject data = body.getJSONObject("data");
            Constant.USER = data.getString("user");
            Constant.TOKEN = data.getString("token");
        }

        reloadRemoteFileList(DesktopClientInit.REMOTE_FILE_TABLE,"/");
        startFileClient();
    }

    @Override
    public void upload(JProgressBar jProgressBar,String local,String remote, JLabel jLabel, List<String> filePaths) throws Exception {
        int index = 1;


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("remote",remote);
        jsonObject.put("local",local);
        jsonObject.put("paths",filePaths);
        JSONObject body = HttpUtils.post("file/upload.do",jsonObject);
        String code = body.getString("code");
        if (Assert.assertNotEquals("200", code)) {
            LOGGER.error(body.getString("message"));
            DialogUtils.showError(body.getString("message"));
            return;
        }

        for (String filePath : filePaths) {
            jProgressBar.setValue(0);
            jLabel.setText(String.format("当前:%s (上传进度：%d/%d),", filePath, index++, filePaths.size()));
            // 上传
            jProgressBar.setValue(100);
        }
    }

    @Override
    public void download(JProgressBar jProgressBar,String remote, JLabel jLabel,List<String> filePaths) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("remote",remote);
        jsonObject.put("paths",filePaths);
        JSONObject body = HttpUtils.post("file/download.do",jsonObject);
        String code = body.getString("code");
        if (Assert.assertNotEquals("200", code)) {
            LOGGER.error(body.getString("message"));
            DialogUtils.showError(body.getString("message"));
            return;
        }

        int index = 1;
        for (String filePath : filePaths) {
            jProgressBar.setValue(0);
            jLabel.setText(String.format("当前:%s (下载进度：%d/%d),", filePath, index++, filePaths.size()));
            // 上传
            jProgressBar.setValue(100);
        }
    }

    @Override
    public void delete(String remote,JTable jTable,List<String> filePaths) throws Exception{
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("remote",remote);
          jsonObject.put("paths",filePaths);
         JSONObject body = HttpUtils.post("file/delete.do",jsonObject);
         String code = body.getString("code");
         if (Assert.assertNotEquals("200", code)) {
            throw new Exception(body.getString("message"));
        }
        reloadRemoteFileList(jTable,remote);
    }

    @Override
    public void reloadRemoteFileList(JTable jTable, String remoteFilePath) {
        LOGGER.info("即将获取服务器文件列表：{}", remoteFilePath);

        try {
            JSONObject body = HttpUtils.get("file/list.do?path="+remoteFilePath);
            String code = body.getString("code");
            if (Assert.assertNotEquals("200", code)) {
                throw new Exception(body.getString("message"));
            } else {
                JSONObject data = body.getJSONObject("data");
                LOGGER.info("查询文件列表返回:{}",data );
                FileUtils.flusFileList(DesktopClientInit.REMOTE_FILE_TABLE, data);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            DialogUtils.showError(e.getMessage());
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

        fileClient = new FileClient(CommonConfig.getConfig().getString("server.file.host"), CommonConfig.getConfig().getInt("server.file.port"));
        fileClient.start();
    }

}
