package be.dist.node.replication;

import be.dist.common.NamingServerInt;
import be.dist.common.NodeRMIInt;
import be.dist.node.NodeSetup;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class FileDiscovery {
    private static FileDiscovery discovery;

    private NamingServerInt remoteSetup;
    private String myIP;
    private String myName;
    private FileIO io;
    private TCPSender sender;
    private NodeSetup nodeSetup;

    public FileDiscovery(String ServerIP, String ip, String name, NodeSetup setup){
        System.out.println("Filediscovery entered...");
        myIP = ip;
        myName = name;
        nodeSetup = setup;
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

    public Map<String,NodeFileInformation> getFileList() {
        return io.getMap();
    }

    public void discoverFiles() {
        System.out.println("Filediscovery starting... (die van imre) robbe print");
        for(String name : io.getLocalFiles()) {
            fileCheck(name);
        }
    }

    //om de lokaale files juist te classificeren en door te sturen
    public void fileCheck(String fileName)  {
        System.out.println("Checking file: "+fileName);
        try {
            NodeFileInformation fileInfo;
            String fileOwnerIP = remoteSetup.getOwner(fileName);
            String filePath = "files/original/"+fileName;

            fileInfo = io.getMap().get(fileName);
            if (fileInfo == null) { // new file not yet in map
                fileInfo = new NodeFileInformation(true,false,fileName);
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
                sendBestandsFiche(io.getBestandsfiches().get(fileName), fileName, fileOwnerIP);
                io.getBestandsfiches().remove(fileName);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
            // doe failure handler TODO
        }

    }

    //dit checkt de files die binnen komen van andere nodes door replicatie.
    public void fileCheckDownloads(String fileName)  {
        try {
            NodeFileInformation fileInfo;
            String fileOwner = remoteSetup.getOwner(fileName);
            if (fileOwner.equals(myIP)) {
                String fileDuplicate = remoteSetup.getOwner(myName);
                fileInfo = new NodeFileInformation(false, true, fileName);
                io.getMap().put(fileName, fileInfo);

            } else {
                fileInfo = new NodeFileInformation(false, false, fileName);
                io.getMap().put(fileName, fileInfo);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
            // doe failure handler TODO
        }

    }

    // hier wordt gecheckt ofdat wanneer er een nieuw previous node bijkomt sommige files niet daar gerepliceerd moeten worden
    // in plaats van op deze node.
    public void fileCheckNewNode(String ipNewNode) {
        try {
            //hij loopt over alle files die hij heeft, zowel origineel als replicatie.
        for(Map.Entry<String,NodeFileInformation> entry : io.getMap().entrySet()) {
            String fileName = entry.getKey();
            NodeFileInformation fileInfo = entry.getValue();
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

    // deze check kijkt wat er met alle files moet gebeuren bij afsluiten zowel de lokaale als de replicaties
    // deze wordt niet gebruikt
    public void fileCheckShutdownNode() throws RemoteException {
        Map<String, Bestandsfiche> fiches = io.getBestandsfiches();
        for(Map.Entry<String,NodeFileInformation> entry : io.getMap().entrySet()) {
            if (entry.getValue().getLocal()) { /** Deze node heeft het originele bestand */
                if (entry.getValue().getOwner()){ /** Deze node is eigenaar van hetzelfde bestand --> replicatie bevindt zich bij vorige node */
                    //
                        String replica = fiches.get(myIP).getReplicatieLocatie();
                        NodeRMIInt replicatieLocatie = NodeSetup.getRemoteSetup(replica);
                        replicatieLocatie.deleteReplica(entry.getValue().getFileName());
                    //
                }
                else { /** Deze node heeft het originele bestand maar is geen eigenaar */
                    //
                        String owner = remoteSetup.getOwner(entry.getValue().getFileName());
                        NodeRMIInt replicatieLocatie = NodeSetup.getRemoteSetup(owner);
                        replicatieLocatie.deleteReplica(entry.getValue().getFileName());
                    //
                }

            }
            else { /** Deze node heeft NIET het originele bestand */
                if (entry.getValue().getOwner()){ /** Is wel de eigenaar */
                    //check Log file for duplicates
                    //if previous is local of the file,
                    // send file to prevous previous and send updated log file to previous (new owner)
                    //else
                    // send file to previous and updated logfile to previous (new owner)

                    //

                    //
                }
                else { /** Is NIET de eigenaar */
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

    // deze check kijkt wat er met alle files moet gebeuren bij afsluiten zowel de lokaale als de replicaties
    // deze wordt wel gebruikt
    public void fileCheckShutdownNodev2() throws RemoteException{
        HashMap<String, NodeFileInformation> replicaties = new HashMap<>();
        HashMap<String, NodeFileInformation> lokale = new HashMap<>();

        for(Map.Entry<String,NodeFileInformation> entry : io.getMap().entrySet()) {
            NodeFileInformation fileInfo = entry.getValue();
            Boolean isOwner = fileInfo.getOwner();
            Boolean isLokaal = fileInfo.getLocal();
            System.out.println("Robbe check: lijst files die in mappen gestoken worden."+entry.getValue().getFileName());

            if (isLokaal) {
                lokale.put(entry.getKey(), entry.getValue());
            } else {
                replicaties.put(entry.getKey(), entry.getValue());
            }
        }
        regelReplicaties(replicaties);
        regelLokale(lokale);
    }

    //deze zorg voor het doorsturen en wijziggen van replicaties op andere nodes (deze klasse wordt aangeroepen vanop een andere node en nooit lokaal)
    public void regelReplicaties(HashMap<String, NodeFileInformation> reps) throws RemoteException {
        System.out.println("regelReplicatie!!!");
        for(Map.Entry<String,NodeFileInformation> entry : reps.entrySet()) {
            String fileName = entry.getKey();
            String filePath = "files/replication/"+fileName;
            String IPPrevious = nodeSetup.getPrevious().getIp(); // IP van vorige node
            Map<String, Bestandsfiche> fiches = io.getBestandsfiches();
            System.out.println("Robbe check : " + fileName + "file info: " + entry.getValue().getOwner());



            if (fiches.containsKey(fileName)) {
                String IPOfLocal = fiches.get(fileName).getLocalIP(); // IP van lokale versie -- gevraagd via eigenaar's bestandsfiche

                /* Wanneer een bestand dat bij deze node gerepliceerd is, lokaal aanwezig is bij zijn vorige node,
                moet het naar de vorige node van zijn vorige node verplaatst worden */
                if (IPOfLocal.equals(IPPrevious)) {
                    NodeRMIInt previous = NodeSetup.getRemoteSetup(IPPrevious);
                    String previousOfPreviousIP = previous.getPrevious().getIp();
                    sender.send(previousOfPreviousIP, filePath);
                    Bestandsfiche temp = fiches.get(fileName);
                    temp.setReplicatieLocatie(previousOfPreviousIP);
                    sendBestandsFiche(temp, fileName, previousOfPreviousIP);
                }

                /* Wanneer een node wordt afgesloten, moeten de bestanden die bij deze node gerepliceerd staan,
                verplaatst worden naar zijn vorige node, deze node wordt de nieuwe eigenaar. */
                else {
                    sender.send(IPPrevious, filePath);
                    Bestandsfiche temp = fiches.get(fileName);
                    temp.setReplicatieLocatie(IPPrevious);
                    sendBestandsFiche(temp, fileName, IPPrevious);
                }

                nodeSetup.deleteReplica(fileName);
            }

            /* In het geval dat de node een replicatie bevat waarvan hij toch GEEN owner is */
            else {
                String ownerIP = remoteSetup.getOwner(fileName);
                NodeRMIInt owner = NodeSetup.getRemoteSetup(ownerIP);
                owner.shutdownHandlerAlsDezeNodeOwnerIPVDegeneDieDitOproeptViaRMI(fileName);
                nodeSetup.deleteReplica(fileName);
            }
        }
    }

    // deze zorgt voor het regelen van de downloads op andere nodes (wordt ook nooit lokaal maar enkel door andere nodes aangeroepen)
    public void regelLokale(HashMap<String, NodeFileInformation> lokale) throws RemoteException {
        System.out.println("regelLokale!!!");
        for(Map.Entry<String, NodeFileInformation> entry : lokale.entrySet()) {
            String fileName = entry.getKey();
            String filePath = "files/replication/"+fileName;

            System.out.println("Robbe check 37: " + fileName + " owner: " + entry.getValue().getOwner());
            if (!entry.getValue().getOwner()) {
                System.out.println("Robbe check 38: " + fileName);
                String owner = remoteSetup.getOwner(fileName);
                System.out.println("IP owner " + owner);
                NodeRMIInt temp = NodeSetup.getRemoteSetup(owner);
                temp.shutdownHandlerOwner(fileName);
            }
            else {
                nodeSetup.shutdownHandlerOwner(fileName);
            }
        }
    }

    public FileIO getIO(){
        return io;
    }

    // het versturen van het bestandsfiche naar andere nodes bij wijziging eigenaar
    private void sendBestandsFiche(Bestandsfiche bestandsfiche, String filename, String ip){
        NodeRMIInt sendBestandsficheToRemote = null;
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            sendBestandsficheToRemote = (NodeRMIInt) registry.lookup("nodeSetup");
            sendBestandsficheToRemote.receiveBestandsFiche(bestandsfiche, filename);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    // het verturen van bestanden naar andere nodes
    public void send(String IP, String filePath) {
        sender.send(IP, filePath);
    }
}
