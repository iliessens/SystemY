package be.dist.node;

import be.dist.node.discovery.MulticastListener;
import be.dist.node.discovery.MulticastSender;
import be.dist.node.discovery.NodeRMIServer;
import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.TCPListener;

public class Main {

    public static void main(String[] args) throws Exception{
        String ip= "10.2.1.22";
       // String ip= "127.0.0.1";
        String name = "James Of Hannes of Wannes ofzo";
        NodeSetup setup = new NodeSetup(name,ip);

        MulticastSender sender = new MulticastSender(ip);
        sender.sendHello(name);
        new NodeRMIServer(ip,setup);

        // After setup
        MulticastListener listener = new MulticastListener(ip,setup);
        listener.start();
        System.out.println("Multicast listener enabled...");

        TCPListener tcpListener = new TCPListener(7899);
        System.out.println("TCP listener started");
        System.out.println("Node staat volledig aan.");

        UIThread ui = new UIThread(setup);
        ui.start();
    }
}
