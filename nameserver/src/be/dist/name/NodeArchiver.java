package be.dist.name;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NodeArchiver {

    public NodeArchiver() {
    }

    public void save(Map<Integer,String> repository) {
        try {
        FileWriter fw = new FileWriter("nodes.csv");
        BufferedWriter writer = new BufferedWriter(fw);

        for(Map.Entry<Integer,String> entry: repository.entrySet()) {
                writer.write(entry.getKey()+",");
                writer.write(entry.getValue()+"\n");
        }

        writer.close();
        } catch (IOException e) {
            System.out.println("Error saving nodeList!");
        }
    }

}
