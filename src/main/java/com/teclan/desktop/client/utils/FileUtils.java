package com.teclan.desktop.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

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

}
