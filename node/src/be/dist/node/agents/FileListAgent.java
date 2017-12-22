package be.dist.node.agents;

import be.dist.common.Agent;
import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.NodeFileInformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileListAgent implements Agent {
    // the fileList contained in the agent
    private Map<String,AgentFile> fileList;

    public FileListAgent() {
        // Treemap omdat elke filename maar een keer mag vookomen
        fileList = new HashMap<>();
    }

    @Override
    public void run() {
       FileDiscovery discovery =  FileDiscovery.getInstance(); // Get the local filediscovery
        Map<String,NodeFileInformation> list = discovery.getFileList();

        removeLocalLocks();

        List<String> localFiles = list.entrySet().stream()
                .filter(x -> x.getValue().getOwner() != null)
                .filter(x -> x.getValue().getOwner()) // only keep owned files
                .map(Map.Entry::getKey) // only keep filename
                .collect(Collectors.toList());

        for (String name: localFiles) {
            if(!fileList.containsKey(name)) { // bestand zit er nog niet in
                fileList.put(name, new AgentFile(name, LocalIP.getLocalIP()));
            }
        }

        processLocks();

        // save current list to local node
        LocalFileList.setFileMap(fileList);
        
    }

    // remove locks set earlier by this node
    private void removeLocalLocks() {
        fileList.entrySet().stream()
                .filter(x -> LocalIP.getLocalIP().equals(x.getValue().getLockedBy()))
                .forEach(x -> x.getValue().setLockedBy(null));
    }

    // add locks requested by this node
    private void processLocks() {
        Map<String,AgentFile> localList = LocalFileList.getFileMap();
        localList.entrySet().stream()
                .filter(x -> x.getValue().hashLockRequest()) // find files that have a request on them
                .filter(x -> fileList.get(x.getKey()).getLockedBy() == null) // only lock files that are not already locked
                .forEach(x -> fileList.get(x.getKey())
                        .setLockedBy(LocalIP.getLocalIP())); // lock the files
    }

    @Override
    public boolean getStopFlag() {
        return false;
    }
}
