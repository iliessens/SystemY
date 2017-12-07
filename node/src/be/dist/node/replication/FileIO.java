package be.dist.node.replication;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class FileIO {

    private Map<String,FileInformation> informationMap;
    private HashMap<String,Bestandsfiche> bestandsfiches;

    public FileIO()
    {
        bestandsfiches = new HashMap<String,Bestandsfiche>();
    }

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
                    if ((!fileEntry.isDirectory())&&(!fileEntry.isHidden())) { // check if directory or hidden
                        files.add(fileEntry.getName());
                        //System.out.println("File discovered: "+fileEntry.getName());
                    }
                }
            }
            return files;
    }

    public Map<String,FileInformation> getMap() {
        if(informationMap == null) {
            informationMap = getLocalFiles()
                    .stream()
                    .map(x -> new FileInformation(true, null, x))
                    .collect(Collectors.toMap(FileInformation::getFileName, x -> x));
        }
        return informationMap;
    }

    public Map<String,Bestandsfiche> getBestandsfiches() {
        return bestandsfiches;
    }

}
