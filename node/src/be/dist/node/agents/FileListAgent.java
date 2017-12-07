package be.dist.node.agents;

import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.NodeFileInformation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileListAgent implements Runnable, Serializable {
    private Map<String,AgentFile> fileList; // boolean indicates lock

    public FileListAgent() {
        // Treemap omdat elke filename maar een keer mag vookomen
        fileList = new HashMap<>();
    }

    @Override
    public void run() {
       FileDiscovery discovery =  FileDiscovery.getInstance(); // Get the local filediscovery
        Map<String,NodeFileInformation> list = discovery.getFileList();

        List<String> localFiles = list.entrySet().stream()
                .filter(x -> x.getValue().getOwner()) // only keep owned files
                .map(Map.Entry::getKey) // only keep filename
                .collect(Collectors.toList());

        for (String name: localFiles) {
            fileList.put(name,new AgentFile(name,null));
        }
        
    }

    public Map<String, AgentFile> getFileList() {
        return fileList;
    }
}
