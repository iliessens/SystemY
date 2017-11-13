package be.dist.node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileIO {

    public List<String> getLocalFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get("files/original/"))) {
           return paths
                    .filter(Files::isRegularFile)
                   .map(Path::toString)
                   .collect(Collectors.toList());
        }
        catch (IOException e) {
            System.out.println("Error making fileslisting!");
            return null;
        }
    }

    public Map<String,FileInformation> getMap() {
        return getLocalFiles()
                .stream()
                .map(x -> new FileInformation(true,null, x))
                .collect(Collectors.toMap(FileInformation::getFileName, x -> x))
        ;
    }

}
