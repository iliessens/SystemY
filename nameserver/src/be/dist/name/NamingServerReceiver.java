package be.dist.name;
import be.dist.common.NodeRMIInt;
import be.dist.common.exceptions.NamingServerException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NamingServerReceiver {

    int mcPort = 6789;
    String mcIPStr = "228.5.6.7";
    MulticastSocket mcSocket = null;
    InetAddress mcIPAddress = null;
    DatagramPacket packet;
    boolean start;
    NodeRepository serverRepository;
    String myIP;
    public NamingServerReceiver(NodeRepository namingServer, String ip) {
        try {
            myIP = ip;
            serverRepository = namingServer;
            mcIPAddress = InetAddress.getByName(mcIPStr);
            System.out.println(mcIPAddress);
            mcSocket = new MulticastSocket(mcPort);
            //heilig lijntje code incoming
            mcSocket.setInterface(InetAddress.getByName(myIP));
            System.out.println("Multicast Receiver running at:"
                    + mcSocket.getLocalSocketAddress());
            mcSocket.joinGroup(mcIPAddress);

            packet = new DatagramPacket(new byte[1024], 1024);
            start = true;
            this.startReceiver();
        }
        catch(IOException ex){System.out.println("error error");}
    }
    public void startReceiver() throws IOException{
        while(start)
        {
            System.out.println("Waiting for a  multicast message...");
            mcSocket.receive(packet);
            String IPSender = packet.getAddress().getHostAddress();
            String msg = new String(packet.getData(), packet.getOffset(),
                    packet.getLength());
            System.out.println("[Multicast  Receiver] Received:" + msg);
            try {
                serverRepository.addNode(msg, IPSender);
                this.RMINodeSetup(IPSender);
            }
            catch(NamingServerException e){
                e.printStackTrace();
            }

        }
        mcSocket.leaveGroup(mcIPAddress);
        mcSocket.close();
    }
    public void RMINodeSetup(String ip) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            NodeRMIInt remoteSetup = (NodeRMIInt) registry.lookup("nodeSetup");
            int length = serverRepository.getLength();
            remoteSetup.setupNode(myIP, length);
        }
        catch(RemoteException e){e.printStackTrace();}
        catch(NotBoundException e) {
            e.printStackTrace();
        }
    }
}
