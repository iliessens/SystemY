package be.dist.node;

import be.dist.common.NamingServerInt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FileDiscovery {
    NamingServerInt remoteSetup;
    String myIP;
    String myName;
    public FileDiscovery(String ServerIP, String ip, String name){
        myIP = ip;
        myName = name;
        try {
            Registry registry = LocateRegistry.getRegistry(ServerIP);
            remoteSetup = (NamingServerInt) registry.lookup("ServerRepository");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }


    public void fileCheck(String fileName) throws RemoteException {

            String fileOwner = remoteSetup.getOwner(fileName);
            if(fileOwner.equals(myIP)){
                String fileDuplicate = remoteSetup.getOwner(myName);
                //sendFile to IP fileDuplicate
            }
            else{
                //sendFile to IP fileOwner
            }

    }
}
