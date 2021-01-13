package com.teclan.desktop.client;

import com.teclan.desktop.client.service.DefaultClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;


public class Main {
private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
            DesktopClientInit.initLoginFrem(new DefaultClientService());
        } catch (Exception e) {
         LOGGER.error(e.getMessage(),e);
        }

    }
}
