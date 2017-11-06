package be.dist.node;

import be.dist.common.NodeRMIInt;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NodeRMIServer {

    public NodeRMIServer(String serverIp, NodeSetup setupObj) {
        System.setProperty("java.rmi.server.hostname", serverIp);
        try {
            String serverName = "nodeSetup";
            NodeRMIInt stub =
                    (NodeRMIInt) UnicastRemoteObject.exportObject(setupObj, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(serverName, stub);
            System.out.println("RMI bound");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }
}
