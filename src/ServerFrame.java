import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ServerFrame extends JFrame {
    private int ClientNum = 0;
    private ArrayList<Socket> clientSocketList = new ArrayList<>();
    private JPanel myMessagePanel;
    private JTextArea myChatArea;
    private JScrollPane myChatScroll;


    public ServerFrame(ServerSocket serverSocket){

        this.setSize(600,600);
        this.setTitle("聊天服务器");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        myMessagePanel=new JPanel();
        myChatArea=new JTextArea(30,40);
        myChatScroll=new JScrollPane(myChatArea);
        myMessagePanel.add(myChatScroll);
        this.add(myMessagePanel);

        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.setVisible(true);


        while (true) {
            Socket acceptSocket;
            try {
                acceptSocket = serverSocket.accept();//循环监听
                clientSocketList.add(acceptSocket);
                ClientNum++;
                myChatArea.append(new Date().toString() + "\n成功连接上第" + ClientNum + "个客户端\n" + "在线用户数" + ClientNum + '\n');
                System.out.println("成功连接上第" + ClientNum + "个客户端");
                System.out.println("在线用户数" + ClientNum);


                new ServerFrame.SocketThread(acceptSocket).start();//起一个收发消息线程
                new OffThread(acceptSocket).start();//起一个下线管理线程

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //服务器消息处理线程
    class SocketThread extends Thread{
        private Socket socket;
        private DataInputStream serverIn;
        private DataOutputStream serverOut;
        public SocketThread(Socket acceptSocket){
            this.socket = acceptSocket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    serverIn = new DataInputStream(socket.getInputStream());
                    String msg;
                    try{
                        msg = serverIn.readUTF();
                    }catch (IOException ex){
                        socket.close();
                        break;
                    }
                    myChatArea.append(msg + "\n");
                    //使用迭代器,给每个上线了的客户端都发送消息
                    Iterator<Socket> it = clientSocketList.iterator();//for(Socket it:clientSocketList)
                    while (it.hasNext()) {
                        Socket s = it.next();
                        serverOut = new DataOutputStream(s.getOutputStream());
                        serverOut.writeUTF(msg);
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //下线管理线程
    class OffThread extends Thread{
        private Socket off;
        public OffThread(Socket acceptSocket){
            this.off = acceptSocket;
        }
        @Override
        public void run(){
            boolean flag = true;
            while (flag) {
                if (off.isClosed()) {
                    clientSocketList.remove(off);
                    ClientNum--;
                    flag = false;
                    myChatArea.append(new Date().toString() + "\n有用户下线\n" + "在线用户数" + ClientNum + '\n');
                }
            }
        }
    }
}

