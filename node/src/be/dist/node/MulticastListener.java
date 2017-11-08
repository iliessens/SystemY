package be.dist.node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class MulticastListener extends Thread{
    private boolean listening;
    private InetAddress ownIp;
    private NodeSetup setup;

    public MulticastListener(String ip,NodeSetup setup) throws IOException{
        ownIp = InetAddress.getByName(ip);
        listening = true;
        this.setup = setup;
    }

    public void run() {
        try {
            int mcPort = 6789;
            String mcIPStr = "228.5.6.7";
            MulticastSocket mcSocket = null;
            InetAddress mcIPAddress = null;
            mcIPAddress = InetAddress.getByName(mcIPStr);
            mcSocket = new MulticastSocket(mcPort);
            mcSocket.setInterface(this.ownIp);
            System.out.println("Multicast Receiver running at:"
                    + mcSocket.getLocalSocketAddress());
            mcSocket.joinGroup(mcIPAddress);

            while (listening) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

                System.out.println("Waiting for a  multicast message...");
                mcSocket.receive(packet);
                processPacket(packet);

            }
            mcSocket.leaveGroup(mcIPAddress);
            mcSocket.close();
        }
        catch (IOException e) {
            System.out.println("Error while listening on multicast.");
        }
    }

    public void stopListen() {
        listening = false;
    }

    private void processPacket(DatagramPacket packet) {
        String msg = new String(packet.getData(), packet.getOffset(),
                packet.getLength());
        String sourceIp = packet.getAddress().getHostAddress();
        setup.processAnnouncement(sourceIp,msg);
    }

}