package be.dist.node;

public class Main {

    public static void main(String[] args) throws Exception{
        //String ip= "10.2.1.10";
        String ip= "127.0.0.1";
        NodeSetup setup = new NodeSetup("Jefke",ip);

        MulticastSender sender = new MulticastSender(ip);
        sender.sendHello();
        new NodeRMIServer(ip,setup);

        // After setup
        MulticastListener listener = new MulticastListener(ip,setup);
        listener.start();
        System.out.println("Node staat volledig aan.");
        listener.stopListen();
    }
}
