package be.dist.node.agents;

import be.dist.common.Agent;
import be.dist.common.NamingServerInt;
import be.dist.common.NodeRMIInt;
import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.NodeFileInformation;
import be.dist.node.replication.TCPSender;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class FailureAgent implements Agent {

    private String failedIP;
    private String startIP;
    private boolean firstRun;
    private boolean stopCirculating;

    public FailureAgent(String failedIP, String startIP) {
        this.failedIP = failedIP;
        this.startIP = startIP;
    }

    @Override
    public void run() {
        FileDiscovery discovery = FileDiscovery.getInstance();
        Map<String,NodeFileInformation> localFiles =  discovery.getFileList();

        localFiles.entrySet().stream()
                .filter(x -> x.getValue().getLocal()) // only keep local files
                .filter(x -> getOwnerIP(x.getKey()).equals(failedIP)) // keep files that were on the failed node
                .forEach(x -> failedFileHandler(x));

        if(startIP.equals(LocalIP.getLocalIP())) {
            // done, stop circulating and remove from nameserver
            stopCirculating = true;
            removeFromNameserver();
        }
    }

    private void failedFileHandler(Map.Entry<String,NodeFileInformation> file) {
        try {
            String newOwner = getNewOwner(file.getKey());
            if (! remoteHasFile(newOwner, file.getKey())) { // only replicate if not already there

                TCPSender sender = new TCPSender(7899);
                String path;
                if (file.getValue().getLocal()) { // file was local
                    path = "files/original/" + file.getKey();
                } else {
                    path = "files/replication/" + file.getKey();
                }
                sender.send(newOwner, path);
            }
            else {
                // TODO update remote bestandsfiche
            }
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private boolean remoteHasFile(String remoteIp, String filename) throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry();
        NodeRMIInt remoteNode = (NodeRMIInt) registry.lookup("nodeSetup");
        return remoteNode.hasFile(filename);
    }

    private String getNewOwner(String filename) throws RemoteException, NotBoundException{
            Registry registry = LocateRegistry.getRegistry();
            NamingServerInt nameServer = (NamingServerInt) registry.lookup("NamingServer");
            return nameServer.getPrevious(failedIP).getIp();
    }

    private void removeFromNameserver() {
        try {
                Registry registry = LocateRegistry.getRegistry();
            NamingServerInt nameServer = (NamingServerInt) registry.lookup("NamingServer");
            nameServer.removeNodeByIp(failedIP);
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private String getOwnerIP(String filename) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            NamingServerInt nameServer = (NamingServerInt) registry.lookup("NamingServer");
            return nameServer.getOwner(filename);
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean getStopFlag() {
        return stopCirculating;
    }
}
