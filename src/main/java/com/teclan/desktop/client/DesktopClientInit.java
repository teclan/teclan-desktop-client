package com.teclan.desktop.client;

import com.teclan.desktop.client.contant.Contant;
import com.teclan.desktop.client.listener.DefaultLoginActionListener;
import com.teclan.desktop.client.service.ClientService;
import javafx.scene.layout.Border;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;

public class DesktopClientInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopClientInit.class);
    private static ActionListener  defaultLoginActionListener ;

    public static void initLoginFrem(ClientService clientService){

        /**
         * 登录主窗体
         */
        JFrame loginFrem = new JFrame( );
        loginFrem.setUndecorated(true);
        loginFrem.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        loginFrem.setSize(600, 400);
        loginFrem.setResizable(false);
        loginFrem.setLocationRelativeTo(null);//在屏幕中居中显示
        loginFrem.setTitle("");
        loginFrem.setLayout(new GridLayout(6,1,0,0));

        // 无设置信息，仅作为占位排版使用
        JPanel jpNothing = new JPanel();
        JLabel jlNothing = new JLabel(Contant.SYSTEM);
        jlNothing.setFont(Contant.FONT_SIZE_20);
        jpNothing.add(jlNothing);
        // 账号信息面板
        JPanel jpAccount = new JPanel();
        JLabel jlAccount = new JLabel("账号：");
        jlAccount.setFont(Contant.FONT_SIZE_20);
        final JTextField account = new JTextField(10);
        account.setFont(Contant.FONT_SIZE_20);
        jpAccount.add(jlAccount);
        jpAccount.add(account);
        // 密码信息面板
        JPanel jpPassword = new JPanel();
        JLabel jlPassword = new JLabel("密码：");
        jlPassword.setFont(Contant.FONT_SIZE_20);
        final JPasswordField password = new JPasswordField(10);
        password.setFont(Contant.FONT_SIZE_20);
        jpPassword.add(jlPassword);
        jpPassword.add(password);

        /**
         * 重置、登录按钮信息面板
         */
        JPanel jpReset = new JPanel();
        JButton reset = new JButton("重置");
        reset.setFont(Contant.FONT_SIZE_20);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                account.setText("");
                password.setText("");
            }
        });
        final JButton login = new JButton("登录");
        login.setFont(Contant.FONT_SIZE_20);

        defaultLoginActionListener = new DefaultLoginActionListener(clientService,loginFrem,account,password );
        login.addActionListener(defaultLoginActionListener);
        jpReset.add(reset);
        jpReset.add(login);
        /**
         * 版权信息面板
         */
        JPanel jpCopyRight = new JPanel();
        JLabel jlCopyRight = new JLabel(Contant.COPYRIGHT);
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
                if(keyEvent.getKeyChar() == KeyEvent.VK_ENTER )
                {
                    login.doClick();
                }
            }
        });

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            public boolean postProcessKeyEvent(KeyEvent keyEvent) {
                if(keyEvent.getKeyChar() == KeyEvent.VK_ENTER )
                {
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

        account.setText("1");
        password.setText("1");

    }

    /**
     * 设置主工作空间
     */
    public static void showWorkSpace(String user){

        JFrame workSpace = new JFrame();
        workSpace.setSize(1300, 700);
        workSpace.setLocationRelativeTo(null);//在屏幕中居中显示
        workSpace.setUndecorated(true);
        workSpace.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        workSpace.setResizable(false);
        workSpace.setLayout(new BorderLayout(20,10)); // 水平间距 100 上下间距 10



        JPanel info = new JPanel();
        info.setLayout(new BorderLayout(20,10));
        JLabel userAndService = new JLabel("当前登录用户:"+user+"                           服务器:"+Contant.SERVER_ADDRESS);
        userAndService.setFont(Contant.FONT_SIZE_20);
        info.add(BorderLayout.NORTH,userAndService);

        JPanel LocalFilePath = new JPanel();
        JLabel jlLocalPath = new JLabel("本地文件路径:");
        jlLocalPath.setFont(Contant.FONT_SIZE_20);
        final JTextField jtLocalPath = new JTextField();
        jtLocalPath.setBorder(Contant.BORDER);
        jtLocalPath.setPreferredSize(new Dimension (330,30));
        jtLocalPath.setEditable(false);
        jtLocalPath.setFont(Contant.FONT_SIZE_20);
        JButton chooser = new JButton("选择");
        chooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  //按钮点击事件
                JFileChooser chooser = new JFileChooser();             //设置选择器
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setMultiSelectionEnabled(true);             //设为多选
                int returnVal = chooser.showOpenDialog(chooser);        //是否打开文件选择框

                if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型

                    String absolutePath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
                    String fileName =  chooser.getSelectedFile().getName();
                    LOGGER.info("选择文件:{}，绝对路径：{}",absolutePath,fileName);
                    jtLocalPath.setText(absolutePath);
                }
            }
        });


        chooser.setFont(Contant.FONT_SIZE_20);
        LocalFilePath.add(jlLocalPath);
        LocalFilePath.add(jtLocalPath);
        LocalFilePath.add(chooser);
        info.add(BorderLayout.WEST,LocalFilePath);

        JPanel remoteFilePath = new JPanel();
        JLabel jlRemotePath = new JLabel("服务器文件路径:");
        jlRemotePath.setFont(Contant.FONT_SIZE_20);
        JTextField jtRemotePath = new JTextField();
        jtRemotePath.setBorder(Contant.BORDER);
        jtRemotePath.setPreferredSize(new Dimension (400,30));
        jtRemotePath.setEditable(false);
        jtRemotePath.setFont(Contant.FONT_SIZE_20);
        remoteFilePath.add(jlRemotePath);
        remoteFilePath.add(jtRemotePath);
        info.add(BorderLayout.EAST,remoteFilePath);

        JScrollPane localFileTable = fileInfoTableInit();

        JPanel option = new JPanel();
        GridLayout gridLayout = new GridLayout(13,1);
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

        JScrollPane remoteFileTable = fileInfoTableInit();

        Box hBox01 = Box.createHorizontalBox();
        hBox01.add(localFileTable);
        hBox01.add(option);
        hBox01.add(remoteFileTable);

        Box vBox = Box.createVerticalBox();
        vBox.add(hBox01);

        workSpace.add(BorderLayout.NORTH,info);
        workSpace.add(BorderLayout.CENTER,vBox);

        workSpace.setVisible(true);
    }

    public static JScrollPane fileInfoTableInit(){
        String[] headers = new String[]{"文件名","文件大小","修改时间"};
        Object[][] rows = new Object[][]{
                new Object[]{"111.txt","100M","2020-12-31 08:06:23"},
                new Object[]{"22222222222222222222222.txt","100M","2020-12-31 08:06:23"},
                new Object[]{"3333333333.txt","100M","2020-12-31 08:06:23"},
                new Object[]{"11888888888888888888888888888888881.txt","100M","2020-12-31 08:06:23"}
        };
        JTable table = new JTable(rows,headers);
        JScrollPane jScrollPane=  new  JScrollPane(table);
        return jScrollPane;
    }

    private static JButton getBlankButton(){
        JButton jButton = new JButton();
        jButton.setSize(20,10);
        jButton.setEnabled(false);
        jButton.setBorderPainted(false);
        return jButton;
    }

}
