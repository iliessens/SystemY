package be.dist.node;

import be.dist.common.NameHasher;
import be.dist.common.NamingServerInt;
import be.dist.common.Node;
import be.dist.common.NodeRMIInt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NodeSetup  implements NodeRMIInt{
    private String nameIP;
    private int numberOfNodes;
    private Node previous;
    private Node next;
    private String name;
    private String ownIp;

    public NodeSetup(String name, String ownIp) {
        this.name = name;
        this.ownIp = ownIp;
    }

    public Node getPrevious() {
        return previous;
    }

    public Node getNext() {
        return next;
    }

    public String getNameServerIP() {
        return nameIP;
    }

    @Override
    public void setupNode(String nameserverIP, int numberOfNodes) throws RemoteException {
        this.numberOfNodes = numberOfNodes;
        this.nameIP = nameserverIP;

        if(numberOfNodes < 1) {
            int ownHash = NameHasher.getHash(name);
            Node selfNode = new Node( ownHash, ownIp);
            this.previous = selfNode;
            this.next = selfNode;
        }
        else {
            // Neighbours will be received from other node
            next = null;
            previous = null;
        }
    }

    @Override
    public void setNeighbours(Node previous, Node next) throws RemoteException {
        if(previous != null) this.previous = previous;
        if (next!= null) this.next = next;
    }

    public void processAnouncement(String ip, String naam) {
        int ownHash = NameHasher.getHash(naam);
        int newNodeHash = NameHasher.getHash(naam);
        Node newNode = new Node(newNodeHash,ip);

        if ((ownHash < newNodeHash)&&(newNodeHash < next.getHash())) {
            // Deze node is de vorige
            next = newNode;
            // Doorgeven naar nieuwe node
            sendNeighbours(ip);

        }
        if ((previous.getHash() < newNodeHash )&&(ownHash > newNodeHash)) {
            // Deze node is de volgende van de nieuwe node --> De nieuwe is de vorige
            previous = newNode;
        }
    }

    private void sendNeighbours(String ip) {
        try {
            NodeSetup remoteSetup = getRemoteSetup(ip);

            int ownHash = NameHasher.getHash(name);
            Node selfNode = new Node( ownHash, ownIp);
            remoteSetup.setNeighbours(selfNode,this.next);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNeighboursShutdown() throws RemoteException {
        // send next to previous
        NodeSetup prevSetup = getRemoteSetup(previous.getIp());
        prevSetup.setNeighbours(null,next);

        // send previous to next
        NodeSetup nextSetup = getRemoteSetup(next.getIp());
        prevSetup.setNeighbours(previous,null);

        //Notify nameserver of shutdown
        try {
            Registry registry = LocateRegistry.getRegistry(nameIP);
            NamingServerInt nameServer = (NamingServerInt) registry.lookup("NamingServer");
            nameServer.removeNode(name);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private NodeSetup getRemoteSetup(String ip) {
        NodeSetup remoteSetup = null;
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            remoteSetup = (NodeSetup) registry.lookup("nodeSetup");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return remoteSetup;
    }

}
