package be.dist.node.agents;

public class LocalIP {

    private static String localIP;

    public static void setLocalIP(String ip) {
        localIP = ip;
    }

    public static String getLocalIP() {
        return localIP;
    }
}
