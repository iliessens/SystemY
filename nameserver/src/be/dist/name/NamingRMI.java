package be.dist.name;

import be.dist.common.NamingServerInt;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NamingRMI {

    public NamingRMI(String serverIp) {
        System.setProperty("java.rmi.server.hostname", serverIp);
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "file:src/server.policy");
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String serverName = "NamingServer";
            NamingServer namingServer = new NamingServer();
            NamingServerInt stub =
                    (NamingServerInt) UnicastRemoteObject.exportObject(namingServer, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(serverName, stub);
            System.out.println("RMI bound");
        } catch (Exception e) {
            System.err.println("Exception while setting up RMI:");
            e.printStackTrace();
        }
    }
}
