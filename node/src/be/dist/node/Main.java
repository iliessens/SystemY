package be.dist.node;

import be.dist.node.agents.LocalIP;
import be.dist.node.discovery.MulticastListener;
import be.dist.node.discovery.MulticastSender;
import be.dist.node.discovery.NodeRMIServer;
import be.dist.node.replication.TCPListener;

public class Main {

    public static void main(String[] args) throws Exception{
        String ip;
        String name;
        if(args.length < 2) {
            //ip= "10.2.1.10";
            ip= "127.0.0.1";
            name = "Imre";

            System.out.println("No command line parameters: using defaults");
        }
        else {
            ip = args[1];
            name = args[0];
            System.out.println(ip);
        }

        LocalIP.setLocalIP(ip);

        TCPListener tcpListener = new TCPListener(7899);
        tcpListener.start();
        System.out.println("TCP listener started");


        NodeSetup setup = new NodeSetup(name,ip);
        new NodeRMIServer(ip,setup);

        MulticastSender sender = new MulticastSender(ip);
        sender.sendHello(name);


        // After setup
        MulticastListener listener = new MulticastListener(ip,setup);
        listener.start();

        // download listener
        new TCPListener(7900,"files/downloads/").run();

        System.out.println("Multicast listener enabled...");
        System.out.println("Node staat volledig aan.");

        UIThread ui = new UIThread(setup);
        ui.start();
    }
}
