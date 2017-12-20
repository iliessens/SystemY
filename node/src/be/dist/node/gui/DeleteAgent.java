package be.dist.node.gui;

import be.dist.common.Agent;
import be.dist.node.agents.LocalIP;
import be.dist.node.replication.FileDiscovery;
import be.dist.node.replication.FileIO;

import java.io.File;

public class DeleteAgent implements Agent {

    private String deleteName;
    private String startIP;

    public DeleteAgent(String deleteName) {
        startIP = LocalIP.getLocalIP();
        this.deleteName = deleteName;
    }

    @Override
    public boolean getStopFlag() {
        return startIP.equals(LocalIP.getLocalIP()); // stop if back on startnode
    }

    // this is run on every node
    @Override
    public void run() {
        String[] paths = {"replication/","original/"};
        for (String currentPath : paths) {
            File deleteMe = new File("files/"+currentPath+deleteName); // search for file
            boolean result = deleteMe.delete(); // if file does not exist delete fails with false
            if(result) System.out.println("Deleted file "+deleteName+" on request of "+startIP);
        }

        deleteLogs();
    }

    private void deleteLogs() {
        FileIO io =  FileDiscovery.getInstance().getIO();
        io.getMap().remove(deleteName); // remove from NodeFileInformation
        io.getBestandsfiches().remove(deleteName); // remove from bestandsfiches
    }
}
