

/**
 * 描述：客户端，包括登陆和注册功能
 *
 * @author jjy
 *
 */
import Util.MyBatis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class Client{

    public static void main(String[] args){
        new LoginFrame();
    }

    static class LoginFrame extends JFrame {
        public LoginFrame(){
            this.setSize(400,300);//窗口设置
            this.setTitle("登陆");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setResizable(true);

            JPanel mainPanel = new JPanel();

            JPanel clientSocketPanel = new JPanel();//用户名
            JLabel clientSocketLabel = new JLabel();
            JTextField clientSocketTxF = new JTextField(20);

            JPanel passWordPanel = new JPanel();//密码
            JLabel passWordLabel = new JLabel();
            JTextField passWordTxF = new JTextField(20);

            JPanel loginPanel = new JPanel();//登陆
            JButton loginButton = new JButton();

            JPanel registerPanel = new JPanel();//注册
            JButton registerButton = new JButton();

            mainPanel.setLayout(new GridLayout(4,1));

            clientSocketLabel.setText("用户名");
            clientSocketPanel.add(clientSocketLabel);
            clientSocketPanel.add(clientSocketTxF);

            passWordLabel.setText("密码");
            passWordPanel.add(passWordLabel);
            passWordPanel.add(passWordTxF);

            loginButton.setText("登陆");
            loginPanel.add(loginButton);

            registerButton.setText("注册");
            registerPanel.add(registerButton);

            mainPanel.add(clientSocketPanel);
            mainPanel.add(passWordPanel);
            mainPanel.add(loginPanel);
            mainPanel.add(registerPanel);
            this.add(mainPanel);
            setVisible(true);

            loginButton.addActionListener(e -> {
                        String userName = clientSocketTxF.getText();
                        String passWord = passWordTxF.getText();
                        if (userName.equals("") || passWord.equals("")) {
                            JOptionPane.showMessageDialog(null, "不能为空");
                        } else {
                            MyBatis myBatis = new MyBatis();

                            try {
                                int result = myBatis.Login(userName,passWord);
                                if(result==1){
                                        ChatFrame myChatFrame = new ChatFrame();
                                        myChatFrame.creatChatFrame(userName);
                                        setVisible(false);
                                        dispose();
                                }else if(result==0){
                                    JOptionPane.showMessageDialog(null, "用户名或密码错误");
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

            //点击注册
            registerButton.addActionListener(e -> {
                dispose();
                Register Register = new Register();
                Register.createRegisterFrame();
            });
        }
    }
    //注册界面
    public static class Register extends JFrame{

        public void createRegisterFrame() {
            this.setSize(400, 300);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.setTitle("注册界面");
            this.setLocationRelativeTo(null);
            this.setLayout(new BorderLayout());
            this.setResizable(false);

            JPanel registerPanel = new JPanel();
            registerPanel.setLayout(new GridLayout(4, 1));
            JLabel account = new JLabel("用户名");

            JLabel password = new JLabel("密 码");
            JTextField accountfield = new JTextField(13);
            JTextField passwordfield = new JTextField(13);
            JButton save = new JButton("保存");

            JPanel accountPanel = new JPanel();
            accountPanel.add(account);
            accountPanel.add(accountfield);

            JPanel passwordPanel = new JPanel();
            passwordPanel.add(password);
            passwordPanel.add(passwordfield);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(save);

            registerPanel.add(accountPanel);
            registerPanel.add(passwordPanel);
            registerPanel.add(buttonPanel);
            this.add(registerPanel);
            this.setVisible(true);

            save.addActionListener(e -> {
                String userName = accountfield.getText();
                String passWord = passwordfield.getText();
                if (userName.equals("") || passWord.equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入完整信息", "提示", JOptionPane.QUESTION_MESSAGE);
                }
                MyBatis myBatis = new MyBatis();
                int result = myBatis.Register(userName,passWord);
                if(result==1){
                    JOptionPane.showMessageDialog(null,"用户已存在");
                }else if(result==0){
                    JOptionPane.showMessageDialog(null,"注册成功，请登录");
                }
            });

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dispose();
                    new LoginFrame();
                }
            });
        }
    }
}







