package be.dist.name;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
        try {
            BufferedReader reader = new BufferedReader(new FileReader("nodes.csv"));

            String line;
            TreeMap<Integer,String> readMap = new TreeMap<>();
            try {
                while((line=reader.readLine())!=null) {
                    String[] str = line.split(",");
                    for(int i=0;i<str.length;i++){
                        //System.out.println(str[0] + " ->> " + str[1]);
                        readMap.put(Integer.parseInt(str[0]), str[1]);
                    }
                }
                System.out.println(readMap);
                return readMap;

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            System.out.println("Error reading nodeList.");
        }

        return null;
    }

}
