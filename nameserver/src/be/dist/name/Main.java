package be.dist.name;

import be.dist.common.exceptions.NamingServerException;

public class Main {

    public static void main(String[] args) {
        NamingRMI rmi = new  NamingRMI("127.0.0.1");
    }
}
