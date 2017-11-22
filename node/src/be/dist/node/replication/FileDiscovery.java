package be.dist.node.replication;

import be.dist.common.NamingServerInt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FileDiscovery {
    private static FileDiscovery discovery;

    NamingServerInt remoteSetup;
    String myIP;
    String myName;
    FileIO io;

    public FileDiscovery(String ServerIP, String ip, String name){
        myIP = ip;
        myName = name;
        try {
            Registry registry = LocateRegistry.getRegistry(ServerIP);
            remoteSetup = (NamingServerInt) registry.lookup("ServerRepository");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        discovery = this;
    }

    public void discoverFiles() {
        io =  new FileIO();
        for(String name : io.getLocalFiles()) {
            fileCheck(name);
        }
    }


    public void fileCheck(String fileName)  {
        try {
            FileInformation fileInfo;
            String fileOwner = remoteSetup.getOwner(fileName);
            if (fileOwner.equals(myIP)) {
                String fileDuplicate = remoteSetup.getOwner(myName);
                //sendFile to IP fileDuplicate
                fileInfo = io.getMap().get(fileName);
                fileInfo.setLocal(true);
                fileInfo.setOwner(true);
            } else {
                //sendFile to IP fileOwner
                fileInfo = io.getMap().get(fileName);
                fileInfo.setLocal(true);
                fileInfo.setOwner(false);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
            // doe failure handler
        }

    }

    public static void checkDownloads(String filename) {
        discovery.fileCheckDownloads(filename);
    }

    public void fileCheckDownloads(String fileName)  {
        try {
            FileInformation fileInfo;
            String fileOwner = remoteSetup.getOwner(fileName);
            if (fileOwner.equals(myIP)) {
                String fileDuplicate = remoteSetup.getOwner(myName);
                fileInfo = new FileInformation(false, true, fileName);
                io.getMap().put(fileName, fileInfo);

            } else {
                fileInfo = new FileInformation(false, false, fileName);
                io.getMap().put(fileName, fileInfo);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
            // doe failure handler
        }

    }
}
