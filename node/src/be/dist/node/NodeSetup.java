package be.dist.node;

import be.dist.common.NameHasher;
import be.dist.common.NamingServerInt;
import be.dist.common.Node;
import be.dist.common.NodeRMIInt;
import be.dist.node.discovery.FailureHandler;
import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.NewFilesChecker;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NodeSetup  implements NodeRMIInt{
    private String nameIP;
    private int numberOfNodes;
    private volatile Node previous;
    private volatile Node next;
    private String name;
    private String ownIp;

    private Node selfNode;
    private FileDiscovery discovery;

    public NodeSetup(String name, String ownIp) {
        this.name = name;
        this.ownIp = ownIp;
        int ownHash = NameHasher.getHash(name);
        selfNode = new Node( ownHash, ownIp);
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

        FailureHandler.connect(nameIP);

        System.out.println("Setup from namingserver received, Nodes in network: "+numberOfNodes);
        System.out.println("Nameserver is at: "+nameserverIP);

        if(numberOfNodes <= 1) {
            this.previous = selfNode;
            this.next = selfNode;
        }
        else {
            // Neighbours will be received from other node
            next = null;
            previous = null;
        }
        doReplicationWhenSetup();
    }

    @Override
    public synchronized void setNeighbours(Node newPrevious, Node newNext) throws RemoteException {
        System.out.println("Received new next: "+(newNext == null ? "Not set" : newNext.getIp()));
        System.out.println("Received new previous: "+(newPrevious == null ? "Not set" : newPrevious.getIp()));
        boolean firstSetup = false;
        if ((this.previous == null) || (this.next ==null)) firstSetup = true;
        else if (this.previous == selfNode && this.next == selfNode) firstSetup = true;

        if(newPrevious != null) previous = newPrevious;
        if (newNext!= null) next = newNext;
        System.out.println("New neighbours set");
        System.out.println(next.getIp()+"test robbe");
        if(firstSetup) doReplicationWhenSetup();
        System.out.println(next.getIp()+"test2 robbe");
        printNeighbours();
    }

    public void printNeighbours() {
        System.out.println("Previous: " + (previous != null ? previous.getIp() : "Not set"));
        System.out.println("Next: " + (next != null ? next.getIp() : "Not set"));
    }

    /**
     * Allows are remote host to check if a node is still online
     * Should (for now) always return true
     * @return True when node is on and in good condition
     */
    @Override
    public boolean isAlive() throws RemoteException {
        System.out.println("Received isAlive request...");
        return true;
    }

    public void processAnnouncement(String ip, String naam) {
        System.out.println("Announcement from new node received");
        int ownHash = NameHasher.getHash(naam);
        int newNodeHash = NameHasher.getHash(naam);
        Node newNode = new Node(newNodeHash,ip);

        if (previous.getIp().equals(ownIp) || next.getIp().equals(ownIp)) {
            // Node was alone
            sendNeighbours(ip);
            next = newNode;
            previous = newNode;
        } else {
            if ((ownHash < newNodeHash) && (newNodeHash < next.getHash())) {
                // Deze node is de vorige
                next = newNode;
                // Doorgeven naar nieuwe node
                sendNeighbours(ip);

                // TODO send files to new node
                //discovery.filecheckNewNode(ip);
            }
            if ((previous.getHash() < newNodeHash) && (ownHash > newNodeHash)) {
                // Deze node is de volgende van de nieuwe node --> De nieuwe is de vorige
                previous = newNode;
            }
        }
    }

    // stuur buren naar opgegeven IP
    private void sendNeighbours(String ip) {
        System.out.println("I am previous node. Sending neighbours...");
        try {
            NodeRMIInt remoteSetup = getRemoteSetup(ip);
           // if(remoteSetup.isAlive()) System.out.println("Connection with remote node OK");
            System.out.println(this.next.getHash());

            remoteSetup.setNeighbours(selfNode,this.next);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNeighboursShutdown() {
        try {
            if(previous != null) {
                // send next to previous
                NodeRMIInt prevSetup = getRemoteSetup(previous.getIp());
                prevSetup.setNeighbours(null, next);
            }

            if(next != null) {
                // send previous to next
                NodeRMIInt nextSetup = getRemoteSetup(next.getIp());
                nextSetup.setNeighbours(previous, null);
            }

            //Notify nameserver of shutdown
            if(nameIP != null) {
                Registry registry = LocateRegistry.getRegistry(nameIP);
                NamingServerInt nameServer = (NamingServerInt) registry.lookup("NamingServer");
                nameServer.removeNode(name);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private NodeRMIInt getRemoteSetup(String ip) {
        NodeRMIInt remoteSetup = null;
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            remoteSetup = (NodeRMIInt) registry.lookup("nodeSetup");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return remoteSetup;
    }

    private void doReplicationWhenSetup() {
        if (nameIP == null) return;
        if (previous == null) return;
        if (next == null) return;
        if (numberOfNodes <= 1) return;
        // All setup is received
        // Start replicating files

        Thread discoveryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                discovery =  new FileDiscovery(nameIP, ownIp, name);
            }
        });
        discoveryThread.start();
        new NewFilesChecker().start();
    }

}
