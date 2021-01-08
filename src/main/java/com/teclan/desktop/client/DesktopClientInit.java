package com.teclan.desktop.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.teclan.desktop.client.contant.Constant;
import com.teclan.desktop.client.listener.DefaultLoginActionListener;
import com.teclan.desktop.client.service.ClientService;
import com.teclan.desktop.client.utils.Assert;
import com.teclan.desktop.client.utils.DialogUtils;
import com.teclan.desktop.client.utils.FileUtils;
import com.teclan.desktop.client.utils.MoveLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DesktopClientInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopClientInit.class);
    private static ActionListener defaultLoginActionListener;

    public static void initLoginFrem(ClientService clientService) {

        /**
         * 登录主窗体
         */
        JFrame loginFrem = new JFrame();
        loginFrem.setUndecorated(true);
        loginFrem.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        loginFrem.setSize(600, 400);
        loginFrem.setResizable(false);
        loginFrem.setLocationRelativeTo(null);//在屏幕中居中显示
        loginFrem.setTitle("");
        loginFrem.setLayout(new GridLayout(6, 1, 0, 0));

        // 无设置信息，仅作为占位排版使用
        JPanel jpNothing = new JPanel();
        JLabel jlNothing = new JLabel(Constant.SYSTEM);
        jlNothing.setFont(Constant.FONT_SIZE_20);
        jpNothing.add(jlNothing);
        // 账号信息面板
        JPanel jpAccount = new JPanel();
        JLabel jlAccount = new JLabel("账号：");
        jlAccount.setFont(Constant.FONT_SIZE_20);
        final JTextField account = new JTextField(10);
        account.setFont(Constant.FONT_SIZE_20);
        jpAccount.add(jlAccount);
        jpAccount.add(account);
        // 密码信息面板
        JPanel jpPassword = new JPanel();
        JLabel jlPassword = new JLabel("密码：");
        jlPassword.setFont(Constant.FONT_SIZE_20);
        final JPasswordField password = new JPasswordField(10);
        password.setFont(Constant.FONT_SIZE_20);
        jpPassword.add(jlPassword);
        jpPassword.add(password);

        /**
         * 重置、登录按钮信息面板
         */
        JPanel jpReset = new JPanel();
        JButton reset = new JButton("重置");
        reset.setFont(Constant.FONT_SIZE_20);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                account.setText("");
                password.setText("");
            }
        });
        final JButton login = new JButton("登录");
        login.setFont(Constant.FONT_SIZE_20);

        defaultLoginActionListener = new DefaultLoginActionListener(clientService, loginFrem, account, password);
        login.addActionListener(defaultLoginActionListener);
        jpReset.add(reset);
        jpReset.add(login);
        /**
         * 版权信息面板
         */
        JPanel jpCopyRight = new JPanel();
        JLabel jlCopyRight = new JLabel(Constant.COPYRIGHT);
        jpCopyRight.add(jlCopyRight);


        loginFrem.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                LOGGER.info("主窗口即将关闭...");
                System.exit(0);
            }
        });

        password.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {

            }

            public void keyReleased(KeyEvent keyEvent) {

            }

            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
                    login.doClick();
                }
            }
        });

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            public boolean postProcessKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
                    login.doClick();
                }
                return true;
            }
        });

        loginFrem.add(new JPanel());
        loginFrem.add(jpNothing);
        loginFrem.add(jpAccount);
        loginFrem.add(jpPassword);
        loginFrem.add(jpReset);
        loginFrem.add(jpCopyRight);
        loginFrem.setVisible(true);

        account.setText("admin");
        password.setText("admin");

    }

    /**
     * 设置主工作空间
     */
    public static void showWorkSpace(ClientService clientService,String user) throws Exception {

        JFrame workSpace = new JFrame();
        workSpace.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        workSpace.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        workSpace.setUndecorated(true);
        workSpace.setPreferredSize(new Dimension(1300, 800));
        workSpace.setLocationRelativeTo(null);//在屏幕中居中显示

//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        int x = (int)(toolkit.getScreenSize().getWidth()-workSpace.getWidth())/2;
//        int y = (int)(toolkit.getScreenSize().getHeight()-workSpace.getHeight())/2;
//        workSpace.setLocation(x, y);
//        workSpace.setPreferredSize(new Dimension(1300, 800));
//        workSpace.setVisible(true);
//        workSpace.setResizable(false);
//        workSpace.setLayout(new BorderLayout(20, 10));
        workSpace.pack();

        JPanel info = new JPanel();
        info.setLayout(new BorderLayout(20, 10));
        MoveLabel userAndService = new MoveLabel("当前登录用户:" + user + "  服务器:" + Constant.SERVER_ADDRESS);
        userAndService.setFont(Constant.FONT_SIZE_20);
        userAndService.setForeground(Color.BLUE);
        info.add(BorderLayout.NORTH, userAndService);

        JPanel LocalFilePath = new JPanel();
        JLabel jlLocalPath = new JLabel("本地文件路径:");
        jlLocalPath.setFont(Constant.FONT_SIZE_20);
        final JTextField jtLocalPath = new JTextField();
        jtLocalPath.setBorder(Constant.BORDER);
        jtLocalPath.setPreferredSize(new Dimension(330, 30));
        jtLocalPath.setEditable(false);
        jtLocalPath.setFont(new Font("宋体",Font.BOLD,14));
        jtLocalPath.setScrollOffset(5);
        JButton chooser = new JButton("选择");

        chooser.setFont(Constant.FONT_SIZE_20);
        LocalFilePath.add(jlLocalPath);
        LocalFilePath.add(jtLocalPath);
        LocalFilePath.add(chooser);
        info.add(BorderLayout.WEST, LocalFilePath);

        JPanel remoteFilePath = new JPanel();
        JLabel jlRemotePath = new JLabel("服务器文件路径:");
        jlRemotePath.setFont(Constant.FONT_SIZE_20);
        JTextField jtRemotePath = new JTextField();
        jtRemotePath.setBorder(Constant.BORDER);
        jtRemotePath.setPreferredSize(new Dimension(330, 30));
        JButton open = new JButton("刷新");
        open.setFont(new Font("宋体",Font.BOLD,14));
//        jtRemotePath.setEditable(false);
        jtRemotePath.setFont(Constant.FONT_SIZE_20);
        remoteFilePath.add(jlRemotePath);
        remoteFilePath.add(jtRemotePath);
        remoteFilePath.add(open);
        info.add(BorderLayout.EAST, remoteFilePath);


        JTable localTable = FileUtils.fileInfoTableInit();
        JScrollPane localFileTable = new JScrollPane(localTable);

        JPanel option = new JPanel();
        GridLayout gridLayout = new GridLayout(13, 1);
        option.setLayout(gridLayout);


        JButton upload = new JButton("上传");
        JButton download = new JButton("下载");
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(upload);
        option.add(getBlankButton());
        option.add(download);
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());


        JTable remotrTable = new JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("headers","文件名,文件大小,最后修改日期,权限");
        jsonObject.put("data",new JSONArray());
        FileUtils.flusFileList(remotrTable,jsonObject);
        JScrollPane remoteFileTable = new JScrollPane(remotrTable);
        Box hBox01 = Box.createHorizontalBox();
        hBox01.add(localFileTable);
        hBox01.add(option);
        hBox01.add(remoteFileTable);

        Box vBox = Box.createVerticalBox();
        vBox.setSize(1300,700);
        vBox.add(hBox01);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout(0, 10));
        final JProgressBar progressBar=new JProgressBar();
        progressBar.setOrientation(JProgressBar.HORIZONTAL);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setBorderPainted(true);
//        progressBar.setBackground(Color.pink);
        progressBar.setForeground(Color.GREEN);
        JLabel uploadFile = new JLabel();
        JLabel copyRight = new JLabel("                                                           "+ Constant.COPYRIGHT);
        copyRight.setFont(new Font("宋体",Font.BOLD,16));
        copyRight.setSize(new Dimension(500, 1000));
        bottom.add(BorderLayout.NORTH,progressBar);
        bottom.add(BorderLayout.CENTER, uploadFile);
        bottom.add(BorderLayout.SOUTH,copyRight);


        workSpace.add(BorderLayout.NORTH, info);
        workSpace.add(BorderLayout.CENTER, vBox);
        workSpace.add(BorderLayout.SOUTH, bottom);

        workSpace.setVisible(true);
        workSpace.setLocationRelativeTo(null);
        workSpace.pack();

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String remoteFilePath = jtRemotePath.getText();
                if(Assert.assertNullString(remoteFilePath)){
                    DialogUtils.showWarn("服务器文件路径不能为空!",new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            LOGGER.info("弹出窗口即将关闭...");
                        }
                    });
                }else {
                    // 加载远程文件列表
                    clientService.reloadRemoteFileList(remotrTable,remoteFilePath);
                }
            }
        });

        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] selectRowIdxs = localTable.getSelectedRows();
                java.util.List<String> filePaths = new ArrayList<String>();
                for (int index : selectRowIdxs) {
                    String absolutePath = (String) localTable.getValueAt(index, 0);
                    LOGGER.info("即将上传文件:{}", absolutePath);
                    filePaths.add(absolutePath);
                }
                try {
                    clientService.upload(progressBar,uploadFile,filePaths);
                } catch (Exception e) {
                  LOGGER.error(e.getMessage(),e);
                }
            }
        });
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] selectRowIdxs = remotrTable.getSelectedRows();
                for (int index : selectRowIdxs) {
                    String absolutePath = (String) remotrTable.getValueAt(index, 0);
                    LOGGER.info("即将下载文件:{}", absolutePath);
                }
            }
        });

        chooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  //按钮点击事件
                JFileChooser chooser = new JFileChooser();             //设置选择器
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setMultiSelectionEnabled(true);             //设为多选
                int returnVal = chooser.showOpenDialog(chooser);        //是否打开文件选择框

                if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型

                    String absolutePath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
                    String fileName = chooser.getSelectedFile().getName();
                    LOGGER.info("选择文件:{}，绝对路径：{}", absolutePath, fileName);
                    jtLocalPath.setText(absolutePath);


                    FileUtils.flusFileListByPath(localTable,absolutePath);
                    JScrollPane jScrollPane = new JScrollPane(localTable);
                    hBox01.remove(0);
                    hBox01.add(jScrollPane, 0);
                    workSpace.setLocationRelativeTo(null);
                    workSpace.pack();
                }
            }
        });

    }

    private static JButton getBlankButton() {
        JButton jButton = new JButton();
        jButton.setSize(20, 10);
        jButton.setEnabled(false);
        jButton.setBorderPainted(false);
        return jButton;
    }

}
