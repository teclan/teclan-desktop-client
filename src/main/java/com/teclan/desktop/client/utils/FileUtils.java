package com.teclan.desktop.client.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teclan.desktop.client.contant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()==MouseEvent.BUTTON1 && mouseEvent.getClickCount()==2){
                    int focusedRowIndex = table.rowAtPoint(mouseEvent.getPoint());
                    if (focusedRowIndex == -1) {
                        return;
                    }
                    table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                    int[] selectRowIdxs = table.getSelectedRows();
                    for (int index : selectRowIdxs) {
                        String fileName = (String) table.getValueAt(index, 0);
                        LOGGER.info("双击文件:{}", fileName);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

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
        jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    }

    private static void createPopupMenu4Remote(JTable jTable){
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem setPrivate = new JMenuItem();
        setPrivate.setOpaque(false);
        setPrivate.setText("  设为隐私文件  ");
        setPrivate.setFont(Constant.FONT);
        setPrivate.setBackground(Color.gray);
        setPrivate.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               LOGGER.info("设为隐私文件...");
            }
        });
        jPopupMenu.add(setPrivate);

        JMenuItem setPublic = new JMenuItem();
        setPrivate.setOpaque(false);
        setPublic.setText("  设为公开文件  ");
        setPublic.setFont(Constant.FONT);
        setPrivate.setBackground(Color.gray);
        setPublic.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LOGGER.info("设为公开文件...");
            }
        });
        jPopupMenu.add(setPublic);

        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    int focusedRowIndex = jTable.rowAtPoint(evt.getPoint());
                    if (focusedRowIndex == -1) {
                        return;
                    }
                    jTable.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                    jPopupMenu.show(jTable, evt.getX(), evt.getY());
                }
            }
        });
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

        createPopupMenu4Remote(jTable);
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
