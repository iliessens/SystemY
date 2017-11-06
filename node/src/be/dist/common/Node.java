package be.dist.common;

public class Node {
    private int hash;
    private String ip;

    public Node(int hash, String ip) {
        this.hash = hash;
        this.ip = ip;
    }

    public int getHash() {
        return hash;
    }

    public String getIp() {
        return ip;
    }
}
