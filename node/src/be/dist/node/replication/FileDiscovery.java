package be.dist.node.replication;

import be.dist.common.NamingServerInt;
import be.dist.node.NodeSetup;

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
        System.out.println("Filediscovery entered...");
        myIP = ip;
        myName = name;
        io =  new FileIO();
        try {
            Registry registry = LocateRegistry.getRegistry(ServerIP);
            remoteSetup = (NamingServerInt) registry.lookup("NamingServer");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        sender = new TCPSender(7899);
        discovery = this;
    }

    public static FileDiscovery getInstance() {
        return discovery;
    }


    public void discoverFiles() {
        System.out.println("Filediscovery starting... (die van imre) robbe print");
        for(String name : io.getLocalFiles()) {
            fileCheck(name);
        }
    }

    public void fileCheck(String fileName)  {
        System.out.println("Checking file: "+fileName);
        try {
            FileInformation fileInfo;
            String fileOwnerIP = remoteSetup.getOwner(fileName);
            String filePath = "files/original/"+fileName;

            fileInfo = io.getMap().get(fileName);
            if (fileInfo == null) { // new file not yet in map
                fileInfo = new FileInformation(true,false,fileName);
                io.getMap().put(fileName,fileInfo);
            }

            if (fileOwnerIP.equals(myIP)) {
                String fileDuplicateIP = remoteSetup.getOwner(myName);
                // dit werkt omdat deze de eigenaar van de file is. en de eigenaar van een file dat de naam van deze node heeft. is de vorigge node
                //Dus hiermee krijgen we het Ip van de vorige node.
                sender.send(fileDuplicateIP, filePath);
                fileInfo.setLocal(true);
                fileInfo.setOwner(true);
                io.newBestandsfiche(fileName, myIP, fileDuplicateIP);
            } else {
                sender.send(fileOwnerIP, filePath);
                fileInfo.setLocal(true);
                fileInfo.setOwner(false);
                io.newBestandsfiche(fileName, myIP, fileOwnerIP);
                //TODO
                //roep de functie in NodeSetup aan
                io.getBestandsfiches().remove(fileName);
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
                // hij checkt eerst of hij hiervan de eigenaar was. zo niet dan kan de nieuwe node hier onmogelijk de eigenaar van worden.
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

    public void fileCheckShutdownNode(){
        for(Map.Entry<String,FileInformation> entry : io.getMap().entrySet()) {
            if (entry.getValue().getLocal()) {
                if (entry.getValue().getOwner()){
                    //send remove file to the duplicate
                    //String ipDuplicate = entry.getValue().getDownloadLocaties();



                }
                else{
                    //send shutdown to owner. if no downloads then remove file on owner side. else update logFile
                }

            }
            else{
                if (entry.getValue().getOwner()){
                    //check Log file for duplicates
                    //if previous is local of the file,
                    // send file to prevous previous and send updated log file to previous (new owner)
                    //else
                    // send file to previous and updated logfile to previous (new owner)
                }
                else{
                    //get previousNode from nodeSetup
                    //get Owner file
                    //if (getOwnerIP = previos IP)
                    //Send file to previousPrevious then give new IP to Owner and remove my Ip to Owner (both from log file)
                    //else
                    // send file to previous and new IP to owner and remove my Ip to owner (both from log file)
                }
            }
        }
    }

    public FileIO getIO(){
        return io;
    }
}
