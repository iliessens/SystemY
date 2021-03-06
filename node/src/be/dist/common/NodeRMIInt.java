package be.dist.common;

import be.dist.node.replication.Bestandsfiche;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeRMIInt extends Remote {
    void setupNode(String nameserverIP, int numberOfNodes) throws RemoteException;
    void setNeighbours(Node previous, Node next) throws RemoteException;
    boolean isAlive() throws RemoteException;
    void runAgent(Agent agentToRun) throws RemoteException;
    boolean hasFile(String filename) throws RemoteException;
    void receiveBestandsFiche(Bestandsfiche bestandsfiche, String filename) throws RemoteException;
    void deleteReplica(String path) throws RemoteException;
    Node getPrevious() throws RemoteException;
    void shutdownHandlerOwner(String fileName) throws RemoteException;
    void shutdownHandlerAlsDezeNodeOwnerIPVDegeneDieDitOproeptViaRMI(String fileName) throws RemoteException;
    }
