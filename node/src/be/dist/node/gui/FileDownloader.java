package be.dist.node.gui;

import be.dist.common.NodeRMIInt;
import be.dist.node.agents.AgentFile;
import be.dist.node.agents.LocalIP;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FileDownloader {

    public FileDownloader() {

    }

    public void downloadFile(AgentFile selectedFile) {
        NodeRMIInt nodeRMIInt = getRemoteSetup(selectedFile.getOwnerIP());
        try {
            nodeRMIInt.sendFileTo(LocalIP.getLocalIP(),selectedFile.getFileName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    // TODO change to static method in NodeSetup
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
}
