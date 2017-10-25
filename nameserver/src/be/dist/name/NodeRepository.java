package be.dist.name;
import be.dist.common.NamingServerInt;
import be.dist.common.exceptions.NamingServerException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class NodeRepository implements NamingServerInt {

    public TreeMap<Integer,String> nodes;
    public NodeRepository(){
        //treemap omdat het dan gesorteerd staat en simpler om in te zoeken.
        nodes = new TreeMap<>();
    }
    public void addNode(String name, String IPaddress){
        if (nodes.containsValue(IPaddress)) {
            throw new NamingServerException("Ip adress al in gebruik.");
        }
        int hash = getHash(name);
        if (nodes.containsKey(hash)) {
            throw new NamingServerException("Hash al in gebruik.");
        }
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
    public String getOwner(String fileName){
        String IPaddress = "0.0.0.0";
        int hash = getHash(fileName);
        Set set = nodes.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            if ((int)me.getKey() < hash) {
                IPaddress = (String)me.getValue();
            }
        }
        if ((int)nodes.firstKey() >= hash){
            IPaddress = (String)nodes.get(nodes.lastKey());
        }
        return IPaddress;
    }
}
