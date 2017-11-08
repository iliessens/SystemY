package be.dist.name;

import be.dist.common.exceptions.NamingServerException;

public class Main {

    public static void main(String[] args) {
        NodeRepository namingServer = new NodeRepository();
        NamingRMI rmi = new  NamingRMI("127.0.0.1", namingServer);
        System.out.println("jeffffffffffffff");
        NamingServerReceiver serverReciever = new NamingServerReceiver(namingServer);
    }
}
