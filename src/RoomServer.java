/**
 * 描述：服务器端，用于启动服务器。
 *
 * @author jjy
 * 
 **/

import Util.ServerPort;
import java.io.IOException;
import java.net.*;


public class RoomServer{

    public static void main(String[] args) throws IOException {
        try {
            String serverIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("服务器IP：" + serverIP);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        ServerSocket server = new ServerSocket(ServerPort.SERVER_PORT);
        new ServerFrame(server);
    }
}


