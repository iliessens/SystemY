package be.dist.node.discovery;

import be.dist.common.NamingServerInt;
import be.dist.common.Node;
import be.dist.node.NodeSetup;
import be.dist.common.Agent;
import be.dist.node.agents.FailureAgent;
import be.dist.node.agents.LocalIP;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FailureHandler {
    private static NamingServerInt namingServer;

    public static void connect(String nameIp) {
        try {
            Registry registry = LocateRegistry.getRegistry(nameIp);
            namingServer = (NamingServerInt) registry.lookup("NamingServer");

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void nodeFailed(String ip) {
        try {
            Node nextNode =namingServer.getNext(ip);
            Node previousNode = namingServer.getPrevious(ip);

            // update next with previous
            Registry registry = LocateRegistry.getRegistry(nextNode.getIp());
            NodeSetup remoteSetup = (NodeSetup) registry.lookup("nodeSetup");
            remoteSetup.setNeighbours(previousNode,null);

            //update previous with next
            registry = LocateRegistry.getRegistry(previousNode.getIp());
            remoteSetup = (NodeSetup) registry.lookup("nodeSetup");
            remoteSetup.setNeighbours(null,nextNode);


            Agent agent = new FailureAgent(ip, LocalIP.getLocalIP());
            //run failure agent on next node
            registry = LocateRegistry.getRegistry(nextNode.getIp());
            remoteSetup = (NodeSetup) registry.lookup("nodeSetup");
            remoteSetup.runAgent(agent);

        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}
