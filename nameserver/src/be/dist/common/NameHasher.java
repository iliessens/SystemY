package be.dist.common;

public class NameHasher {

    public static int getHash(String name) {
        int hash = Math.abs(name.hashCode() % 32768);
        return hash;
    }

}
