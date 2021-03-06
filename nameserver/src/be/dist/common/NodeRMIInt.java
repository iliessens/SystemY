package be.dist.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeRMIInt extends Remote {
    void setupNode(String nameserverIP, int numberOfNodes) throws RemoteException;
    void setNeighbours(Node previous, Node next) throws RemoteException;
    boolean isAlive() throws RemoteException;
    void runAgent(Agent agentToRun) throws RemoteException;
    boolean hasFile(String filename) throws RemoteException;
}
