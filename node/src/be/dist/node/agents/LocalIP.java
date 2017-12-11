package be.dist.node.agents;

public class LocalIP {

    private static String localIP;
    private static String nameServerIP;

    public static void setLocalIP(String ip) {
        localIP = ip;
    }

    public static String getLocalIP() {
        return localIP;
    }

    public static String getNameServerIP() {
        return nameServerIP;
    }

    public static void setNameServerIP(String nameServerIP) {
        LocalIP.nameServerIP = nameServerIP;
    }
}
