package be.dist.node;

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
            int bytesRead;
            System.out.println("[DOWNLOADING FILE]");

            if ((bytesRead = in.read(buffer)) != -1)
                fileName = new String(Arrays.copyOfRange(buffer, 0, 255), "UTF-8");

            OutputStream localWriter = new FileOutputStream(fileName);

            while ((bytesRead = in.read(buffer)) != -1) {
                localWriter.write(buffer, 0, bytesRead);
            }

            System.out.println("[DOWNLOAD COMPLETE]");
            System.out.println("DISCONNECTING ... ");
            in.close();
            localWriter.close();
            connection.close();
            System.out.println("[DISCONNECTED]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}