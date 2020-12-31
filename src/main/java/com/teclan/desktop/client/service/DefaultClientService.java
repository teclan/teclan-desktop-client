package com.teclan.desktop.client.service;

import com.teclan.desktop.client.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultClientService implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientService.class);

    public void login(String account, String password) throws Exception{
        LOGGER.info("正在登录，账号:{},密码:{}",account,password);
    }
}
