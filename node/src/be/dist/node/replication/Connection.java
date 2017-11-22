package be.dist.node.replication;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Connection extends Thread {
    Socket connection;
    String fileName;

    public Connection(Socket socket) {
        this.connection = socket;
        this.fileName = "";
        this.start();
    }

    public void run() {
        try {
            System.out.println("[CONNECTION ESTABLISHED -- " + connection.getInetAddress() + "]");

            InputStream in = connection.getInputStream();

            byte[] buffer = new byte[1024];
            byte[] nameBuffer = new byte[255];
            int bytesRead;
            System.out.println("[DOWNLOADING FILE]");

            char c = 1;
            int i = 0;
            if ((bytesRead = in.read(nameBuffer)) != -1)
                //System.out.println(bytesRead);
                while(c != 0) {
                    c = (char)nameBuffer[i];
                    if (c !=0 )
                        fileName += c;
                    i++;
                }

            System.out.println("RECEIVED FILE --> " + fileName);

            OutputStream localWriter = new FileOutputStream("files/replication/" + fileName);

            while ((bytesRead = in.read(buffer)) != -1) {
                System.out.println(bytesRead);
                localWriter.write(buffer, 0, bytesRead);
            }

            System.out.println("[DOWNLOAD COMPLETE]");
            System.out.println("DISCONNECTING ... ");
            in.close();
            localWriter.close();
            connection.close();
            FileDiscovery.checkDownloads(fileName);
            System.out.println("[DISCONNECTED]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}