package com.teclan.desktop.client.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teclan.desktop.client.contant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

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


    public static JTable fileInfoTableInit() {

        Vector vData = new Vector();
        Vector vName = new Vector();
        vName.add("文件名");
        vName.add("文件大小");
        vName.add("修改时间");


        Vector vRow = new Vector();
        vRow.add("..");
        vRow.add("");
        vRow.add("");
        vData.add(vRow);

        DefaultTableModel model = new DefaultTableModel(vData, vName);
        model.isCellEditable(-1,-1);
        JTable table = new JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table.setFont(new Font("",Font.PLAIN,14));
        table.setModel(model);

        return table;
    }

    public static void flusFileListByPath(JTable jTable,String filePath) {

        Vector vData = new Vector();
        Vector vName = new Vector();
        vName.add("文件名");
        vName.add("文件大小");
        vName.add("修改时间");

        File[] files = getFileList(filePath);

        Object[][] rows = new Object[files.length + 1][3];
        rows[0] = new Object[]{"..", "", ""};

        Vector vRow = new Vector();
        vRow.add("..");
        vRow.add("");
        vRow.add("");
        vData.add(vRow);

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            vRow = new Vector();
            vRow.add(file.getName());
            vRow.add(getFileSize(file));
            vRow.add(DateUtils.getDataString(file.lastModified()));
            vData.add(vRow);
        }
        DefaultTableModel model = new DefaultTableModel(vData, vName);
        jTable.setModel(model);
    }


    public static void flusFileList(JTable jTable, JSONObject jsonObject) throws Exception {

        Vector vName = new Vector();

        JSONObject headers = jsonObject.getJSONObject("headers");

        for(String key:headers.keySet()){
            vName.add(headers.getString(key));
        }

        JSONArray datas = jsonObject.getJSONArray("datas");


        Vector vData = new Vector();
        for(int i=0;i<datas.size()  ;i++){
            JSONObject data = datas.getJSONObject(i);
            if(data.keySet().size()!=headers.keySet().size()){
                throw new Exception("列表数据列数与表头列数不一致");
            }
            Vector vRow = new Vector();
            for(String key:headers.keySet()){
                vRow.add(data.getString(key));
            }
            vData.add(vRow);
        }
        DefaultTableModel model = new DefaultTableModel(vData, vName);
        jTable.setModel(model);
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
