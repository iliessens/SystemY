package be.dist.node.agents;

import java.util.*;
import java.util.function.Consumer;

public class LocalFileList {

    private static Map<String,AgentFile> fileMap;
    private static Set<Consumer<Map<String,AgentFile>>> observers = new HashSet<>();

    public LocalFileList() {
        fileMap = new HashMap<>();
    }

    public static Map<String, AgentFile> getFileMap() {
        return Collections.unmodifiableMap(fileMap); // prevent modifications to ensure observer pattern
    }

    public static void setFileMap(Map<String, AgentFile> fileMap) {
        LocalFileList.fileMap = fileMap;
        // map changed, notify observers
        observers.forEach(x -> x.accept(fileMap));
    }

    public static void addObserver(Consumer<Map<String,AgentFile>> consumer) {
        observers.add(consumer);
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
