package be.dist.node.replication;

import java.util.HashMap;
import java.util.Map;

public class LogFiles {

    private String Local;
    private String Owner;
    private String fileName;
    private Map gedownloaden;

    public LogFiles(String Local, String Owner, String fileName, Map Gedownloaden) {
        this.gedownloaden = new HashMap<String,Boolean>();
        this.Local = Local;
        this.Owner = Owner;
        this.fileName = fileName;
        this.gedownloaden = Gedownloaden;

    }

    public String getFileName() {
        return fileName;
    }

    public String getLocal() {

        return Local;
    }

    public void addDownload(String IP) {
        gedownloaden.put(IP, true);
    }

    public void getGedownloaden(){

    }

    public void setLocal(String local) {
        Local = local;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }
}
