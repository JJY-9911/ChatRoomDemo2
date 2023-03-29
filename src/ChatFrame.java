import Util.ServerPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;


public class ChatFrame extends JFrame {

    private JTextArea clientChatArea;
    private JPanel clientMainPanel ;
    private JPanel clientMessagePanel;
    private JScrollPane clientChatScroll;
    private JPanel clientSendPanel;
    private JTextField clientSendText ;
    private JButton clientSendButton ;
    private DataInputStream myIn;
    private DataOutputStream myOut;



    public void creatChatFrame(String username) throws IOException {

        clientChatArea = new JTextArea(30, 40);
        clientMainPanel = new JPanel();
        clientMessagePanel = new JPanel();
        clientChatScroll = new JScrollPane(clientChatArea);
        clientSendPanel = new JPanel();
        clientSendText = new JTextField(15);
        clientSendButton = new JButton("发送");

        this.setSize(600, 600);
        this.setTitle("您好" + username + "欢迎回来");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭聊天窗口即下线
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(new BorderLayout());

        Socket mySocket = new Socket(ServerPort.SERVER_IP,ServerPort.SERVER_PORT);
        new SocketThread(mySocket).start();//起一个接收消息的线程

        //发消息
        clientSendButton.addActionListener(e -> {
            if (clientSendText.getText().length() != 0) {
                String message = username + " " + new Date().toString() + "\n" + clientSendText.getText();
                try {
                    myOut = new DataOutputStream(mySocket.getOutputStream());
                    myOut.writeUTF(message);
                    clientSendText.setText("");//发出后清空输入框

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "服务器连接失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else{
                JOptionPane.showMessageDialog(null, "聊天内容不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        //回车发送
        clientSendText.addKeyListener(new KeyAdapter() {//回车键发送消息
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    if (clientSendText.getText().length() != 0 ){
                        String msg = username + "" + new Date().toString() + "\n" + clientSendText.getText();
                        try{
                            myOut = new DataOutputStream(mySocket.getOutputStream());
                            myOut.writeUTF(msg);
                            clientSendText.setText("");
                        }catch (IOException ioException){
                            ioException.printStackTrace();
                            JOptionPane.showMessageDialog(null, "服务器连接失败", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "聊天内容不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



        clientSendPanel.add(clientSendText);
        clientSendPanel.add(clientSendButton);
        clientMainPanel.add(clientMessagePanel, BorderLayout.CENTER);
        clientMainPanel.add(clientSendPanel, BorderLayout.SOUTH);
        clientMessagePanel.add(clientChatScroll);
        this.add(clientMainPanel);

        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String msg;
                try {
                    mySocket.close();
                    dispose();//释放资源
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.setVisible(true);
        System.out.println("已打开聊天框");

    }

    class SocketThread extends Thread{

        private Socket socket;
        public SocketThread(Socket s){
            this.socket = s;
        }
        @Override
        public void run(){
            boolean flag = true;
            while (flag){
                try{
                    myIn = new DataInputStream(socket.getInputStream());
                    String str = myIn.readUTF();
                    clientChatArea.append(str + "\n" + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = false;
                    JOptionPane.showMessageDialog(null,"接收消息失败，请检查creatChatFrame.ReceiveTread");
                }
            }
        }

    }
}