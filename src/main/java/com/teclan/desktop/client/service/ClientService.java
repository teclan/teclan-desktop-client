package com.teclan.desktop.client.service;

import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.io.File;
import java.util.List;

public interface ClientService {

    public void login(String account,String password) throws Exception;

    public void upload(JProgressBar jProgressBar,String local,String remote, JLabel jLabel,List<String> filePaths) throws Exception;

    public void download(JProgressBar jProgressBar,String remote, JLabel jLabel,List<String> filePaths) throws Exception;

    public void delete(String remote,JTable jTable,List<String> filePaths) throws Exception;

    public void reloadRemoteFileList(JTable jTable, String remoteFilePath);
}
