package com.teclan.desktop.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.text.DecimalFormat;

public class FileUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    private static final DecimalFormat DF = new DecimalFormat("######0.00");

    public static File[] getFileList(String filePath) {
        return getFileList(new File(filePath));
    }

    public static File[] getFileList(File file) {
        if (!file.exists()) {
            LOGGER.warn("文件不存在，路径:{}", file.getAbsolutePath());
        }
        File[] files = null;

        if (file.isDirectory()) {
            files = file.listFiles();
        } else {
            files = new File[1];
            files[0] = file;
        }
        return files;
    }


    public static JScrollPane flusFileListByPath(String filePath) {
        String[] headers = new String[]{"文件名", "文件大小", "修改时间"};

        File[] files = getFileList(filePath);

        Object[][] rows = new Object[files.length + 1][3];
        rows[0] = new Object[]{"..", "", ""};

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            rows[i + 1] = new Object[]{file.getName(), getFileSize(file), DateUtils.getDataString(file.lastModified())};
        }
        JTable table = new JTable(rows, headers);
        JScrollPane jScroll = new JScrollPane(table);

        return jScroll;
    }

    private static String getFileSize(File file) {
        String size = "";
        double length = file.length();

        length = length * 1.0 / 1024; // KB
        size = DF.format(length) + "KB";

        if (length > 1024) {
            length = length / 1024; // M
            size = DF.format(length) + "MB";
        }

        if (length > 1024) {
            length = length / 1024; // G
            size = DF.format(length) + "GB";
        }
        return size;
    }
}
