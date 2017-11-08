package be.dist.name;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class NamingServerReceiver {

    int mcPort = 6789;
    String mcIPStr = "228.5.6.7";
    MulticastSocket mcSocket = null;
    InetAddress mcIPAddress = null;
    DatagramPacket packet;
    boolean start;
    NodeRepository serverRepository;
    public NamingServerReceiver(NodeRepository namingServer) {
        try {
            serverRepository = namingServer;
            mcIPAddress = InetAddress.getByName(mcIPStr);
            System.out.println(mcIPAddress);
            mcSocket = new MulticastSocket(mcPort);
            //heilig lijntje code incoming
            mcSocket.setInterface(InetAddress.getByName("10.2.1.15"));
            System.out.println("Multicast Receiver running at:"
                    + mcSocket.getLocalSocketAddress());
            mcSocket.joinGroup(mcIPAddress);

            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            start = true;
            this.startReceiver();
        }
        catch(IOException ex){System.out.println("error error");}
    }
    public void startReceiver() throws IOException{
        while(start);
        {
            System.out.println("Waiting for a  multicast message...");
            mcSocket.receive(packet);
            String IPSender = packet.getAddress().getHostAddress();
            String msg = new String(packet.getData(), packet.getOffset(),
                    packet.getLength());
            System.out.println("[Multicast  Receiver] Received:" + msg);
            serverRepository.addNode(msg, IPSender);

        }
        System.out.println("hablahablahab");
        mcSocket.leaveGroup(mcIPAddress);
        mcSocket.close();
    }
}
