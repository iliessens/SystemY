package be.dist.node.replication;

import be.dist.common.NamingServerInt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class FileDiscovery {
    private static FileDiscovery discovery;

    private NamingServerInt remoteSetup;
    private String myIP;
    private String myName;
    private FileIO io;
    private TCPSender sender;

    public FileDiscovery(String ServerIP, String ip, String name){
        myIP = ip;
        myName = name;
        try {
            Registry registry = LocateRegistry.getRegistry(ServerIP);
            remoteSetup = (NamingServerInt) registry.lookup("NamingServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        sender = new TCPSender(7899);
        this.discoverFiles();

        discovery = this;
    }

      public static void checkDownloads(String filename) {
              discovery.fileCheckDownloads(filename);
    }
      public static void checkLocalFiles() {
        discovery.discoverFiles();
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

    public void fileCheckNewNode(String ipNewNode) {
        try {
            //hij loopt over alle files die hij heeft, zowel origineel als replicatie.
        for(Map.Entry<String,FileInformation> entry : io.getMap().entrySet()) {
            String fileName = entry.getKey();
            FileInformation fileInfo = entry.getValue();
            String fileOwner = null;
            String filePath = null;
            fileOwner = remoteSetup.getOwner(fileName);
            if(fileInfo.getOwner()){
                // hij checkt eerst of hij hiervan de eigenaar was. zo niet dan kan de ieuwe node hier onmogelijk de eigenaar van worden.
                if(fileOwner.equals(myIP)) {
                    //dan checkt hij of hij nog altijd de owner is. zoja dan moet het file niet verplaatst worden

                }
                else{


                    if(fileInfo.getLocal()){
                        //hier checkt hij of hij het file lokaal heeft of niet, om de juiste filepath te creeeren.
                        filePath = "files/original/"+fileName;
                    }
                    else{
                        filePath = "files/replication/"+fileName;
                    }
                    //dan zet hij zichzelf niet als de owner en stuurt hij het file door naar de nieuwe eigenaar.
                    sender.send(ipNewNode, filePath);
                    fileInfo.setOwner(false);
                }
            }


        }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
