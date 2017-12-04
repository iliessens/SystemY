package be.dist.node.replication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileIO {

    private Map<String,FileInformation> informationMap;

    public List<String> getLocalFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get("files/original/"))) {
           return paths
                    .filter(Files::isRegularFile)
                   .map(Path::getFileName)
                   .filter(Objects::nonNull)
                   .map(Path::toString)
                   .collect(Collectors.toList());
        }
        catch (IOException e) {
            System.out.println("Error making fileslisting!");
            e.printStackTrace();
            return null;
        }
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

    public Map<String,FileInformation> getLogFiles() {
        Map logFiles = new HashMap<String,LogFiles>();
        return logFiles;
    }

}
