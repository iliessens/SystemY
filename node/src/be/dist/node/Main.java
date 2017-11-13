package be.dist.node;

public class Main {

    public static void main(String[] args) throws Exception{
        String ip= "10.2.1.15";
       // String ip= "127.0.0.1";
        String name = "meeeee";
        NodeSetup setup = new NodeSetup(name,ip);

        MulticastSender sender = new MulticastSender(ip);
        sender.sendHello(name);
        new NodeRMIServer(ip,setup);

        //fileDiscovery
        FileDiscovery filediscovery = new FileDiscovery(setup.getNameServerIP(), ip, name);

        // After setup
        MulticastListener listener = new MulticastListener(ip,setup);
        listener.start();
        System.out.println("Node staat volledig aan.");

        UIThread ui = new UIThread(setup);
        ui.start();
    }
}
