package be.dist.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NamingServerInt extends Remote{
    void addNode(String name,String IPadddres) throws RemoteException;
    void removeNode(String name) throws RemoteException;

    /**
     * @param name hash of the node
     * @return IP of the node
     */
    String getNodeIp(String name) throws RemoteException;

    /**
     * @param filename Name of file including extension
     * @return IP of file owner
     */
    String getOwner(String filename) throws RemoteException;

    Node getPrevious(String ip) throws RemoteException;
    Node getNext(String ip) throws RemoteException;
}
