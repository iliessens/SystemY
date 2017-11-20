package be.dist.node.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender {
    MulticastSocket socket;
    InetAddress group;
    InetAddress ownIp;

    public MulticastSender(String ownIP) {
        try {
            group = InetAddress.getByName("228.5.6.7");
            socket = new MulticastSocket(6789);
            this.ownIp = InetAddress.getByName(ownIP);
            socket.setInterface(this.ownIp);
            socket.joinGroup(group);
        }
        catch (IOException e) {
            System.out.println("Error starting multicast sender");
        }
    }

    public void sendHello(String name) {
        try {
            // join a Multicast group and send the group salutations
            // zeer belangrijk
            DatagramPacket hi = new DatagramPacket(name.getBytes(), name.length(),
                    group, 6789);
            socket.send(hi);

        }
        catch(IOException e) {
            System.out.println("Er ging iets mis.");
        }
    }

    public void close() {
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
