package com.teclan.desktop.client;

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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DesktopClientInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopClientInit.class);
    private static ActionListener defaultLoginActionListener;
    public static  JTable REMOTE_FILE_TABLE = new JTable(){
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };

    static {

        REMOTE_FILE_TABLE.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()==MouseEvent.BUTTON1 && mouseEvent.getClickCount()==2){
                    int focusedRowIndex = REMOTE_FILE_TABLE.rowAtPoint(mouseEvent.getPoint());
                    if (focusedRowIndex == -1) {
                        return;
                    }
                    REMOTE_FILE_TABLE.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                    int[] selectRowIdxs = REMOTE_FILE_TABLE.getSelectedRows();
                    for (int index : selectRowIdxs) {
                        String fileName = (String) REMOTE_FILE_TABLE.getValueAt(index, 0);
                        String fileType = (String) REMOTE_FILE_TABLE.getValueAt(index, 1);
                        LOGGER.info("双击文件:{}", fileName);
                        String remote = DesktopClientInit.JT_REMOTE_PATH.getText();
                        if("..".equals(fileName) && !"/".equals(DesktopClientInit.JT_REMOTE_PATH.getText())){
                            remote = remote.substring(0,remote.lastIndexOf("/"));
                            remote = "".equals(remote)?"/":remote;
                            remote = FileUtils.afterFormatFilePath(remote);
                            DesktopClientInit.JT_REMOTE_PATH.setText(remote);
                            clientService.reloadRemoteFileList(REMOTE_FILE_TABLE,JT_REMOTE_PATH.getText());
                        }else if("文件夹".equals(fileType)) {
                            remote += "/"+fileName;
                            remote = FileUtils.afterFormatFilePath(remote);
                            if( "/..".equals(remote)){
                                return;
                            }
                            remote = FileUtils.afterFormatFilePath(remote);
                            DesktopClientInit.JT_REMOTE_PATH.setText(remote);
                            clientService.reloadRemoteFileList(REMOTE_FILE_TABLE,JT_REMOTE_PATH.getText());
                        }
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
    }

    public static JTextField JT_REMOTE_PATH = new JTextField("/");
    public static final JTextField JT_LOCAL_FILE_PATH = new JTextField();
    public static ClientService clientService;

    public static void initLoginFrem(ClientService clientService) throws IOException {

        DesktopClientInit.clientService=clientService;

        /**
         * 登录主窗体
         */
        JFrame loginFrem = new JFrame();
        loginFrem.setIconImage(ImageIO.read(new File(System.getProperty("user.dir")+"/img/file.jpg")));
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
        workSpace.setIconImage(ImageIO.read(new File(System.getProperty("user.dir")+"/img/file.jpg")));
        workSpace.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        workSpace.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        workSpace.setUndecorated(true);
        workSpace.setPreferredSize(new Dimension(1800, 1000));
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
        JT_LOCAL_FILE_PATH.setBorder(Constant.BORDER);
        JT_LOCAL_FILE_PATH.setPreferredSize(new Dimension(330, 30));
        JT_LOCAL_FILE_PATH.setEditable(false);
        JT_LOCAL_FILE_PATH.setFont(new Font("宋体",Font.BOLD,14));
        JT_LOCAL_FILE_PATH.setScrollOffset(5);
        JButton chooser = new JButton("选择");

        chooser.setFont(Constant.FONT_SIZE_20);
        LocalFilePath.add(jlLocalPath);
        LocalFilePath.add(JT_LOCAL_FILE_PATH);
        LocalFilePath.add(chooser);
        info.add(BorderLayout.WEST, LocalFilePath);

        JPanel remoteFilePath = new JPanel();
        JLabel jlRemotePath = new JLabel("服务器文件路径:");
        jlRemotePath.setFont(Constant.FONT_SIZE_20);
        JT_REMOTE_PATH.setBorder(Constant.BORDER);
        JT_REMOTE_PATH.setPreferredSize(new Dimension(330, 30));
        JButton flush = new JButton("刷新");
        flush.setFont(new Font("宋体",Font.BOLD,14));
//        jtRemotePath.setEditable(false);
        JT_REMOTE_PATH.setFont(Constant.FONT_SIZE_20);
        remoteFilePath.add(jlRemotePath);
        remoteFilePath.add(JT_REMOTE_PATH);
        remoteFilePath.add(flush);
        info.add(BorderLayout.EAST, remoteFilePath);


        JTable localTable = FileUtils.fileInfoTableInit();
        JScrollPane localFileTable = new JScrollPane(localTable);

        JPanel option = new JPanel();
        GridLayout gridLayout = new GridLayout(13, 1);
        option.setLayout(gridLayout);


        JButton upload = new JButton(" 上       传 ");
        upload.setFont(Constant.FONT);
        JButton download = new JButton(" 下     载 ");
        download.setFont(Constant.FONT);
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(upload);
        option.add(getBlankButton());
        option.add(download);
        option.add(getBlankButton());
        JButton delete = new JButton("删除远程文件");
        delete.setFont(Constant.FONT);
        option.add(delete);
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());
        option.add(getBlankButton());


        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("headers","文件名,文件大小,最后修改日期,权限");
//        jsonObject.put("datas",new JSONArray());
//        FileUtils.flusFileList(REMOTE_FILE_TABLE,jsonObject);
        REMOTE_FILE_TABLE.setFont(new Font("",Font.PLAIN,14));
        JScrollPane remoteFileTable = new JScrollPane(REMOTE_FILE_TABLE);
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
        JLabel copyRight = new JLabel("                                                                                  "+ Constant.COPYRIGHT);
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

        flush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String remoteFilePath = JT_REMOTE_PATH.getText();
                if(Assert.assertNullString(remoteFilePath)){
                    DialogUtils.showWarn("服务器文件路径不能为空!",new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            LOGGER.info("弹出窗口即将关闭...");
                        }
                    });
                }else {
                    // 加载远程文件列表
                    clientService.reloadRemoteFileList(REMOTE_FILE_TABLE,remoteFilePath);
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
                    clientService.upload(progressBar, JT_LOCAL_FILE_PATH.getText(), JT_REMOTE_PATH.getText(),uploadFile,filePaths);
                } catch (Exception e) {
                  LOGGER.error(e.getMessage(),e);
                  DialogUtils.showError(e.getMessage());
                }
            }
        });
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] selectRowIdxs = REMOTE_FILE_TABLE.getSelectedRows();
                List<String> paths = new ArrayList<>();
                for (int index : selectRowIdxs) {
                    String absolutePath = (String) REMOTE_FILE_TABLE.getValueAt(index, 0);
                    LOGGER.info("即将下载文件:{}", absolutePath);
                    paths.add((String) REMOTE_FILE_TABLE.getValueAt(index, 0));
                }
                try {
                    clientService.download(progressBar, JT_LOCAL_FILE_PATH.getText(), JT_REMOTE_PATH.getText(),uploadFile,paths);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(),e);
                    DialogUtils.showError(e.getMessage());
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] selectRowIdxs = REMOTE_FILE_TABLE.getSelectedRows();
                List<String> paths = new ArrayList<>();
                for (int index : selectRowIdxs) {
                    String absolutePath = (String) REMOTE_FILE_TABLE.getValueAt(index, 0);
                    LOGGER.info("即将删除远程文件:{}", absolutePath);
                    paths.add( (String) REMOTE_FILE_TABLE.getValueAt(index, 0));
                }
                try {
                    clientService.delete(JT_REMOTE_PATH.getText(),REMOTE_FILE_TABLE,paths);
                } catch (Exception e) {
                   LOGGER.error(e.getMessage(),e);
                    DialogUtils.showError(e.getMessage());
                }
            }
        });

        chooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  //按钮点击事件
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setMultiSelectionEnabled(true);
                chooser.setOpaque(false);
                chooser.setDialogTitle("选择本地文件...");
                chooser.setFont(new Font("",Font.PLAIN,20));
                int returnVal = chooser.showOpenDialog(chooser);        //是否打开文件选择框

                if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型

                    String absolutePath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
                    String fileName = chooser.getSelectedFile().getName();
                    LOGGER.info("选择文件:{}，绝对路径：{}", absolutePath, fileName);
                    JT_LOCAL_FILE_PATH.setText(absolutePath);


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
