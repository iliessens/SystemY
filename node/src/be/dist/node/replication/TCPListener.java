package be.dist.node.replication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPListener extends Thread {

    private int port = 7896;
    private String filePath;

    public TCPListener(int port) {
        this(port,"files/replication/");
    }

    public TCPListener(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);

            while (true) {
                System.out.println("AWAITING CLIENTS ...");
                Socket openedConnection = socket.accept();
                Connection temp = new Connection(openedConnection,filePath);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
