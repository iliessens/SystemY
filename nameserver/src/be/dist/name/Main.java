package be.dist.name;

import be.dist.common.exceptions.NamingServerException;

public class Main {

    public static void main(String[] args) {
        String myIP = "10.2.1.15";
        NodeRepository namingServer = new NodeRepository(myIP);
        NamingRMI rmi = new  NamingRMI(myIP, namingServer);
        NamingServerReceiver serverReciever = new NamingServerReceiver(namingServer, myIP);
    }
}
