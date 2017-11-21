package be.dist.node.replication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPListener extends Thread {

    private int port = 7896;

    public TCPListener(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);

            while (true) {
                System.out.println("AWAITING CLIENTS ...");
                Socket openedConnection = socket.accept();
                Connection temp = new Connection(openedConnection);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
