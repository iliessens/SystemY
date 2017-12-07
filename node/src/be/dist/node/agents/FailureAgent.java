package be.dist.node.agents;

import be.dist.common.NamingServerInt;
import be.dist.node.NodeSetup;
import be.dist.node.discovery.FailureHandler;
import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.NodeFileInformation;

import java.io.Serializable;
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
                .filter(x -> getOwnerIP(x.getKey()).equals(failedIP))
                .forEach(x -> failedFileHandler(x.getKey()));

        if(startIP.equals(LocalIP.getLocalIP())) {
            // done, stop circulating and remove from nameserver
            stopCirculating = true;
            removeFromNameserver();
        }
    }

    private void failedFileHandler(String filename) {
        // TODO do something
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
