package be.dist.node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender {

    public void send() {
        try {
            System.out.println("Sending...");
            // join a Multicast group and send the group salutations
            String msg = "Hello";
            InetAddress group = InetAddress.getByName("228.5.6.7");
            MulticastSocket s = new MulticastSocket(6789);
            // zeer belangrijk
            s.setInterface(InetAddress.getByName("10.2.1.10"));
            s.joinGroup(group);
            DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
                    group, 6789);
            s.send(hi);
            // get their responses!
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            s.receive(recv);

            // OK, I'm done talking - leave the group...
            s.leaveGroup(group);

        }
        catch(IOException e) {
            System.out.println("Er ging iets mis.");
        }
    }
}
