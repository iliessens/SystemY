package be.dist.name;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class NodeRepository {
    public TreeMap<Integer,String> nodes;
    public NodeRepository(){
        nodes = new TreeMap<>();
    }
    public void addNode(String name, String IPaddress){
        int hash = getHash(name);
        nodes.put(hash,IPaddress);
    }
    public void removeNode(String name){
        int hash = getHash(name);
        nodes.remove(hash);
    }
    public String getNodeIp(String name){
        int hash = getHash(name);
        String IP = nodes.get(hash);
        return IP;
    }
    public void printNodes() {
        Set set = nodes.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }
    }
    public int getHash(String name){
        int hash = Math.abs(name.hashCode()%32768);
        return hash;
    }
}
