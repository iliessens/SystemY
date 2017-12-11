package be.dist.name;

import be.dist.common.exceptions.NamingServerException;

public class Main {

    public static void main(String[] args) {
        String myIP;
        if(args.length < 1) {
            myIP= "10.2.1.15";
            System.out.println("No command line parameters: using default");
        }
        else {
            myIP = args[0];
            System.out.println("Using IP address from Command Line: "+myIP);
        }

        NodeRepository namingServer = new NodeRepository(myIP);
        NamingRMI rmi = new  NamingRMI(myIP, namingServer);
        NamingServerReceiver serverReciever = new NamingServerReceiver(namingServer, myIP);
    }
}
