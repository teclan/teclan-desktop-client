package com.teclan.desktop.client.contant;

import com.teclan.desktop.client.config.CommonConfig;

import javax.swing.border.MatteBorder;
import java.awt.*;

public class Constant {
    public static String SYSTEM = "文件管理服务系统";
    public  static String COPYRIGHT = "©2021 桂林天祺产艺广告传媒有限公司";
    public static String SERVER_ADDRESS = CommonConfig.getConfig().getString("server.file.host");
    public static Font FONT_SIZE_20 = new Font("宋体",Font.BOLD,20);
    public static Font FONT = new Font("宋体",Font.BOLD,14);
    public static MatteBorder BORDER = new MatteBorder(2, 2, 2, 2, new Color(192, 192, 192));
    public static String USER ="";
    public static String TOKEN ="";
    public static String REMOTE_ROOT="";
}
