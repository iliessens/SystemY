package be.dist.node.replication;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class FileIO {

    private Map<String,NodeFileInformation> informationMap;

    public List<String> getLocalFiles() {
//        try (Stream<Path> paths = Files.walk(Paths.get("files/original/"))) {
//           return paths
//                    .filter(Files::isRegularFile)
//                   .map(Path::getFileName)
//                   .map(Path::toString)
//                   .collect(Collectors.toList());
            List<String> files = new ArrayList<>();
            final File folder = new File("files/original");
            if (folder.listFiles() != null) {
                for (final File fileEntry : folder.listFiles()) {
                    if (!fileEntry.isDirectory()) {
                        files.add(fileEntry.getName());
                        System.out.println("File discovered: "+fileEntry.getName());
                    }
                }
            }
            return files;
    }

    public Map<String,NodeFileInformation> getMap() {
        if(informationMap == null) {
            informationMap = getLocalFiles()
                    .stream()
                    .map(x -> new NodeFileInformation(true, null, x))
                    .collect(Collectors.toMap(NodeFileInformation::getFileName, x -> x));
        }
        return informationMap;
    }

    public Map<String,NodeFileInformation> getLogFiles() {
        Map logFiles = new HashMap<String,LogFiles>();
        return logFiles;
    }

}
