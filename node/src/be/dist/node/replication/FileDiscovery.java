package be.dist.node.replication;

import be.dist.common.NamingServerInt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FileDiscovery {
    NamingServerInt remoteSetup;
    String myIP;
    String myName;
    FileIO io;
    TCPSender sender;

    public FileDiscovery(String ServerIP, String ip, String name){
        myIP = ip;
        myName = name;
        try {
            Registry registry = LocateRegistry.getRegistry(ServerIP);
            remoteSetup = (NamingServerInt) registry.lookup("ServerRepository");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        sender = new TCPSender(7899);
        this.discoverFiles();

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
            String fileOwnerIP = remoteSetup.getOwner(fileName);
            String filePath = "files/original/"+fileName;
            if (fileOwnerIP.equals(myIP)) {
                String fileDuplicateIP = remoteSetup.getOwner(myName);
                // dit werkt omdat deze de eigenaar van de file is. en de eigenaar van een file dat de naam van deze node heeft. is de vorigge node
                //Dus hiermee krijgen we het Ip van de vorige node.
                sender.send(fileDuplicateIP, filePath);
                fileInfo = io.getMap().get(fileName);
                fileInfo.setLocal(true);
                fileInfo.setOwner(true);
            } else {
                sender.send(fileOwnerIP, filePath);
                fileInfo = io.getMap().get(fileName);
                fileInfo.setLocal(true);
                fileInfo.setOwner(false);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
            // doe failure handler TODO
        }

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
            // doe failure handler TODO
        }

    }
}
