package com.teclan.desktop.client.utils;

import com.teclan.desktop.client.contant.Contant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class DialogUtils {

    public static void showError(String message, WindowAdapter windowAdapter){
        show(message,JRootPane.ERROR_DIALOG,windowAdapter);
    }

    public static void showInfo(String message, WindowAdapter windowAdapter){
        show(message,JRootPane.INFORMATION_DIALOG ,windowAdapter);
    }

    public static void showWarn(String message, WindowAdapter windowAdapter){
        show(message,JRootPane.WARNING_DIALOG,windowAdapter);
    }

    public static void show(String message,int style, WindowAdapter windowAdapter){
        JFrame jFrame = new JFrame();
        jFrame.setUndecorated(true); // 去掉窗口的装饰
        jFrame.getRootPane().setWindowDecorationStyle(style);//采用指定的窗口装饰风格
        jFrame.setSize(200, 100);
        jFrame.setLocationRelativeTo(null);//在屏幕中居中显示
        jFrame.setResizable(false);
        Container container=jFrame.getContentPane();//将窗体转化为容器
        JButton jb=new JButton(message);
        jb.setFont(Contant.FONT_SIZE_20);
        jFrame.addWindowListener(windowAdapter);
        container.add(jb);
        jFrame.setVisible(true);//使窗体可见
    }

}
