package be.dist.node.agents;

import java.util.HashMap;
import java.util.Map;

public class LocalFileList {

    private static Map<String,AgentFile> fileMap;

    public static Map<String, AgentFile> getFileMap() {
        if (fileMap == null) {
            return new HashMap<>();
        }
        else return fileMap;
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

    public static boolean isLocked(String filename) {
        if (fileMap.containsKey(filename)) {
            return fileMap.get(filename).getLockedBy() != null;
        }
        else {
            return false;
        }
    }
}
