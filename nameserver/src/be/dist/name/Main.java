package be.dist.name;

public class Main {

    public static void main(String[] args) {
        String naam;
        NodeRepository test = new NodeRepository();
        test.addNode("freddie","192.168.0.7");
        naam = test.getNodeIp("freddie");
        System.out.println(naam);
        test.addNode("imre","192.168.0.8");
        naam = test.getNodeIp("imre");
        System.out.println(naam);
        test.printNodes();
        test.removeNode("freddie");
        naam = test.getNodeIp("freddie");
        System.out.println(naam);
        naam = test.getNodeIp("imre");
        System.out.println(naam);
    }
}
