package be.dist.node;

public class Main {

    public static void main(String[] args) throws Exception{
        //String ip= "10.2.1.10";
        String ip= "143.169.201.225";


        MulticastSender sender = new MulticastSender(ip);
        sender.sendHello();
        NodeSetup setup = new NodeSetup();
        new NodeRMIServer(ip,setup);

        // After setup
        MulticastListener listener = new MulticastListener(ip);
        listener.start();
        System.out.println("Node is volledig op");
        listener.stopListen();
    }
}
