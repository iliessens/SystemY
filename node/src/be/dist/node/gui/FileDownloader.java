package be.dist.node.gui;

import be.dist.common.NodeRMIInt;
import be.dist.node.agents.AgentFile;
import be.dist.node.agents.LocalFileList;
import be.dist.node.agents.LocalIP;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileDownloader {

    private Map<AgentFile,FileLockState> downloadRequests;

    public FileDownloader() {
        downloadRequests = new HashMap<>();
    }

    public void downloadFile(AgentFile selectedFile) {

        if(selectedFile.isLocked()) {
            downloadRequests.put(selectedFile,FileLockState.WAITFORRELEASE);
        }
        else {
            downloadRequests.put(selectedFile,FileLockState.WAITFORLOCK);
            selectedFile.requestLock();
        }
    }

    public void update() {
        Iterator<Map.Entry<AgentFile,FileLockState> > it = downloadRequests.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<AgentFile,FileLockState> currentFile = it.next();
            AgentFile fileObject = currentFile.getKey();
            switch (currentFile.getValue()) {
                case WAITFORRELEASE: if(!fileObject.isLocked()) {
                    fileObject.requestLock(); // request lock
                    downloadRequests.put(fileObject, FileLockState.WAITFORLOCK); // update state
                }
                    break;

                case WAITFORLOCK: if(fileObject.getLockedBy().equals(LocalIP.getLocalIP())) { // file is locked by us
                    // lock succeeded
                    downloadRequests.put(fileObject,FileLockState.LOCKED); // update state
                }
                    break;

                case LOCKED: // wait until lock is set by all nodes, then download
                    executeDownload(fileObject);
                    fileObject.removeLockRequest(); // download is started, lock can be removed
                    downloadRequests.put(fileObject,FileLockState.UNLOCKED); // update state
                    break;

                // we are done, remove from map
                // Lock should be removed by now
                case UNLOCKED: it.remove();

            }
        }
    }

    public void executeDownload(AgentFile selectedFile) {
        NodeRMIInt nodeRMIInt = getRemoteSetup(selectedFile.getOwnerIP());
        try {
            nodeRMIInt.sendFileTo(LocalIP.getLocalIP(),selectedFile.getFileName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // TODO change to static method in NodeSetup
    private NodeRMIInt getRemoteSetup(String ip) {
        NodeRMIInt remoteSetup = null;
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            remoteSetup = (NodeRMIInt) registry.lookup("nodeSetup");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return remoteSetup;
    }
}
