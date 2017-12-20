package be.dist.node;

import be.dist.common.NameHasher;
import be.dist.common.NamingServerInt;
import be.dist.common.Node;
import be.dist.common.NodeRMIInt;
import be.dist.node.discovery.FailureHandler;
import be.dist.node.replication.Bestandsfiche;
import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.NewFilesChecker;
import be.dist.node.replication.TCPSender;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class NodeSetup  implements NodeRMIInt{
    private volatile String nameIP;
    private int numberOfNodes;
    private volatile Node previous;
    private volatile Node next;
    private String name;
    private String ownIp;
    private FileDiscovery fileDiscovery;

    private Node selfNode;
    private volatile boolean setupDone = false;

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

        fileDiscovery = new FileDiscovery(nameIP, ownIp, name, this);


        FailureHandler.connect(nameIP);

        System.out.println("Setup from namingserver received, Nodes in network: "+numberOfNodes);
        System.out.println("Nameserver is at: "+nameserverIP);

        if(numberOfNodes <= 1) {
            this.previous = selfNode;
            this.next = selfNode;
        }
        doReplicationWhenSetup();

    }

    @Override
    public synchronized void setNeighbours(Node newPrevious, Node newNext) throws RemoteException {
        System.out.println("Received new next: "+(newNext == null ? "Not set" : newNext.getIp()));
        System.out.println("Received new previous: "+(newPrevious == null ? "Not set" : newPrevious.getIp()));
        boolean firstSetup = false;
        if (this.previous == selfNode || this.next == selfNode) firstSetup = true;

        if(newPrevious != null) previous = newPrevious;
        if (newNext!= null) next = newNext;
        System.out.println("New neighbours set");
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
            System.out.println("next3456667: " + newNode.getIp());
            sendNeighbours(ip);
            System.out.println("next2: " + newNode.getIp());
            next = newNode;
            previous = newNode;
            doReplicationWhenSetup();
        } else {
            if ((ownHash < newNodeHash) && (newNodeHash < next.getHash())) {
                // Deze node is de vorige
                next = newNode;
                System.out.println("next: " + newNode.getIp());
                // Doorgeven naar nieuwe node
                sendNeighbours(ip);

                // send files to new node
                FileDiscovery.getInstance().fileCheckNewNode(ip);
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
            System.out.println("Geraakt hij hier of geraakt hij hier niet?");
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

    public static NodeRMIInt getRemoteSetup(String ip) {
        NodeRMIInt remoteSetup = null;
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            remoteSetup = (NodeRMIInt) registry.lookup("nodeSetup");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return remoteSetup;
    }

    private synchronized void doReplicationWhenSetup() {
        if(!setupDone) {
            if ("".equals(nameIP)) return;
            System.out.println("Nameserver t: " + nameIP);
            if (previous == selfNode) return;
            System.out.println("Previous t: " + previous.getIp());
            if (next == selfNode) return;
            System.out.println("Next t: " + next.getIp());

            // query nameserver for number of nodes
            try {
                if (nameIP != null) {
                    Registry registry = LocateRegistry.getRegistry(nameIP);
                    NamingServerInt nameServer = (NamingServerInt) registry.lookup("NamingServer");
                    if (nameServer.getNumberOfNodes() <= 1) return;
                }
            }
            catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
            // All setup is received
            // Start replicating files

            System.out.println("Node fully setup... Starting replication threads.");

            Thread discoveryThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Starting filediscovery");
                    FileDiscovery.getInstance().discoverFiles();
                }
            });
            discoveryThread.start();
            new NewFilesChecker().start();
            setupDone = true;
        }
    }
    

    public void receiveBestandsFiche(Bestandsfiche bestandsfiche, String filename) {
        fileDiscovery.getIO().recieveBestandsfiche(bestandsfiche, filename);
    }

    public void deleteReplica(String path) {
        String repPath = "files/replication/";
        repPath += path;

        new File(repPath).delete();
    }

    public void shutdownHandlerOwner(String fileName) {
        int amountOfDownloads = fileDiscovery.getIO().getBestandsfiches().keySet().size();

        if (amountOfDownloads > 0) {
            // TODO
        }

        else {
            System.out.println("ROBBE CHECK 39!!!");
            String path = "files/replication/";
            path += fileName;
            new File(path).delete();
        }
    }

    public void shutdownHandlerAlsDezeNodeOwnerIPVDegeneDieDitOproeptViaRMI(String fileName) {
        String filePath = "files/original/" + fileName;
        fileDiscovery.send(previous.getIp(), filePath);
        Bestandsfiche temp = fileDiscovery.getIO().getBestandsfiches().get(fileName);
        temp.setReplicatieLocatie(previous.getIp());
    }



}
