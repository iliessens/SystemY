package be.dist.name;

import be.dist.common.exceptions.NamingServerException;

public class Main {

    public static void main(String[] args) {
        NodeRepository test = new NodeRepository();
        test.addNode("freddie","192.168.0.7");
        test.addNode("imre","192.168.0.8");

        String result;
        result = test.getNodeIp("freddie");
        System.out.println(result);
        result = test.getNodeIp("imre");
        System.out.println(result);

        test.removeNode("freddie");
        test.printNodes();
        try {
            test.addNode("jefke", "192.168.0.8");
        }
        catch(NamingServerException e){
            e.printStackTrace();
        }
        try {
            test.addNode("imre", "192.168.0.10");
        }
        catch(NamingServerException e){
            e.printStackTrace();
        }
        test.printNodes();
    }
}
