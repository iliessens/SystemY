package be.dist.node;

import be.dist.common.NodeRMIInt;
import java.rmi.RemoteException;

public class NodeSetup  implements NodeRMIInt{
    private String nameIP;
    private int numberOfNodes;
    private Node previous;
    private Node next;

    @Override
    public void setupNode(String nameserverIP, int numberOfNodes) throws RemoteException {
        this.numberOfNodes = numberOfNodes;
        this.nameIP = nameserverIP;
    }
}
