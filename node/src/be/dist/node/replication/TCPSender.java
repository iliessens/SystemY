package be.dist.node.replication;

import java.io.*;
import java.net.*;
import java.util.Arrays;


public class TCPSender {

    private int port = 7896;

    public TCPSender (int port) {
        this.port = port;
    }

    public void send(String ipOmMeeTeVerbinden, String fileName) {
        Socket senderSocket = null;
        try {
            System.out.println("CONNECTING ... ");
            senderSocket = new Socket(ipOmMeeTeVerbinden, port);
            System.out.println("[CONNECTION ESTABLISHED]");
            File file = new File(fileName);
            InputStream in = new FileInputStream(file);
            OutputStream out = senderSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            System.out.println("TRANSFERING FILE ... ");
            byte[] tempNameBytes = fileName.substring(fileName.lastIndexOf('/') + 1).getBytes("UTF-8"); // get only filename
            byte[] nameSpace = Arrays.copyOf(tempNameBytes,255);
            out.write(nameSpace);
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("[FILE SENT SUCCESFULLY]");
            out.close();
            in.close();
            senderSocket.close();
            System.out.println("[CONNECTION CLOSED]" + "\n\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}