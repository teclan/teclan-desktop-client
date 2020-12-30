package com.teclan.desktop.client;

import com.teclan.desktop.client.service.ClientService;
import com.teclan.desktop.client.service.DefaultClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        DesktopClientInit.init(new DefaultClientService());
    }
}
