package be.dist.node.agents;

import java.util.HashMap;
import java.util.Map;

public class LocalFileList {

    private static Map<String,AgentFile> fileMap;

    public LocalFileList() {
        fileMap = new HashMap<>();
    }

    public static Map<String, AgentFile> getFileMap() {
        return fileMap;
    }

    public static void setFileMap(Map<String, AgentFile> fileMap) {
        LocalFileList.fileMap = fileMap;
    }

    public static void lockFile(String filename) {
        if(fileMap.containsKey(filename)) {
            fileMap.get(filename).requestLock();
        }
        else {
            System.err.println("File not (yet) known in the network can't lock.");
        }
    }
}
