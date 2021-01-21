package com.teclan.desktop.client.service;

import com.teclan.desktop.client.config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teclan.netty.client.FileClient;
import teclan.netty.handler.AbstractFileInfoHandler;
import teclan.netty.handler.FileServerHanlder;
import teclan.netty.model.FileInfo;
import teclan.netty.model.PackageType;


public class FileClientFileInfohandler extends AbstractFileInfoHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileClientFileInfohandler.class);

    @Override
    public void push(String remote,FileInfo fileInfo) throws Exception {
        LOGGER.warn("文件推送，{}",fileInfo);
    }


    @Override
    public void writeFail(FileInfo fileInfo) throws Exception {
        fileInfo.setPackageType(PackageType.CMD_NEED_REPEAT);
//        send(DefaultClientService.getFileClient().getFileClientHandler().getCtx(),fileInfo);
//        LOGGER.info("接收文件失败,请求重发:{}",fileInfo);
    }
}
