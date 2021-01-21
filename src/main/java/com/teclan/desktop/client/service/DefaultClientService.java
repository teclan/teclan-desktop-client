package com.teclan.desktop.client.service;

import com.alibaba.fastjson.JSONObject;
import com.teclan.desktop.client.DesktopClientInit;
import com.teclan.desktop.client.config.CommonConfig;
import com.teclan.desktop.client.contant.Constant;
import com.teclan.desktop.client.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teclan.netty.client.FileClient;
import teclan.netty.handler.FileClientHandler;
import teclan.netty.handler.Monitor;
import teclan.netty.handler.ParamFetcher;

import javax.swing.*;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultClientService implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientService.class);

    private static FileClient fileClient;

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

        body = HttpUtils.get("file/root.do");
        code = body.getString("code");
        if (Assert.assertNotEquals("200", code)) {
            throw new Exception(body.getString("message"));
        }else {
            Constant.REMOTE_ROOT = body.getString("data");
        }


        reloadRemoteFileList(DesktopClientInit.REMOTE_FILE_TABLE, "/");
        startFileClient();
    }

    @Override
    public void upload(JProgressBar jProgressBar, String local, String remote, JLabel jLabel, List<String> filePaths) throws Exception {

        if(filePaths.isEmpty()){
            throw new Exception("至少选择一个本地文件 ...");
        }

        Set<String> paths = new HashSet<>();
        for (String filePath : filePaths) {
            paths.addAll(FileUtils.getFileLis(local,new File(local+"/"+filePath)));
        }

        int index = 1;
        remote =FileUtils.afterFormatFilePath(Constant.REMOTE_ROOT+"/"+remote);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("remote",remote);
        jsonObject.put("local", local);
        jsonObject.put("paths", Objects.list2JSONArray(paths));
        JSONObject body = HttpUtils.post("file/upload.do", jsonObject);
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
            upload(local,remote,filePath);
            jProgressBar.setValue(100);
        }
    }

    private void upload(String local,String remote,String filePath) throws Exception {
        File file = new File(local+File.separator+filePath);
        fileClient.upload(local,remote,filePath);

        if(file.isDirectory()){
            String[] files = file.list();
            for(String f:files){
                upload(local+File.separator+filePath+File.separator,remote+File.separator+filePath,f);
            }
        }

    }

    @Override
    public void download(JProgressBar jProgressBar, String local, String remote, JLabel jLabel, List<String> filePaths) throws Exception {

        if(filePaths.isEmpty()){
            throw new Exception("至少选择一个服务器上的文件 ...");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("remote", Constant.REMOTE_ROOT+File.separator+remote);
        jsonObject.put("local", local);
        jsonObject.put("paths", Objects.list2JSONArray(filePaths));
        JSONObject body = HttpUtils.post("file/download.do", jsonObject);
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
    public void delete(String remote, JTable jTable, List<String> filePaths) throws Exception {

        if(filePaths.isEmpty()){
            throw new Exception("至少选择一个服务器上的文件 ...");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("remote", remote);
        jsonObject.put("paths", Objects.list2JSONArray(filePaths));
        JSONObject body = HttpUtils.post("file/delete.do", jsonObject);
        String code = body.getString("code");
        if (Assert.assertNotEquals("200", code)) {
            throw new Exception(body.getString("message"));
        }
        reloadRemoteFileList(jTable, remote);
    }

    @Override
    public void reloadRemoteFileList(JTable jTable, String remoteFilePath) {
        LOGGER.info("即将获取服务器文件列表：{}", remoteFilePath);

        try {
            JSONObject body = HttpUtils.get("file/list.do?path=" + remoteFilePath);
            String code = body.getString("code");
            if (Assert.assertNotEquals("200", code)) {
                throw new Exception(body.getString("message"));
            } else {
                JSONObject data = body.getJSONObject("data");
                LOGGER.info("查询文件列表返回:{}", data);
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


        fileClientHandler.setParamFetcher(new ParamFetcher() {
            public JSONObject get() {
                // 自定义逻辑
                return null;
            }
        });

        fileClient = new FileClient(CommonConfig.getConfig().getString("server.file.host"), CommonConfig.getConfig().getInt("server.file.port"));
        fileClient.start();
    }

    public static FileClient getFileClient() {
        return fileClient;
    }
}
