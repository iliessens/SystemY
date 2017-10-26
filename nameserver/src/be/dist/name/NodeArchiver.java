package be.dist.name;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NodeArchiver {
    private String filename = "nodes.csv";

    public NodeArchiver() {
    }

    public void save(Map<Integer,String> repository) {
        try {
        FileWriter fw = new FileWriter(filename);
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

    public Map<Integer,String> read() {
        // Not ready
        try {
            FileReader fr = new FileReader(filename);
            return null;

        } catch (FileNotFoundException e) {
            System.out.println("Error reading nodeList.");
        }
    }

}
