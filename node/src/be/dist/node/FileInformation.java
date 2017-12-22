package be.dist.node;

import java.io.Serializable;

public class FileInformation implements Serializable {
    private String fileName;

    public FileInformation(String filename) {
        this.fileName = filename;
    }

    public String getFileName() {
        return fileName;
    }
}
