package be.dist.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NamingServerInt extends Remote{
    void addNode(String name,String IPadddres) throws RemoteException;
    void removeNode(String name) throws RemoteException;
    String getNodeIp(String filename) throws RemoteException;
}
