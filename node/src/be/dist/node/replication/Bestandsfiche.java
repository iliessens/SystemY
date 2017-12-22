package be.dist.node.replication;

import java.io.Serializable;
import java.util.TreeSet;

public class Bestandsfiche implements Serializable {

    private String lokaalIP;
    private TreeSet downloadLocaties;
    private String replicatieLocatie;

    public Bestandsfiche(String lokaalIP, String replicatieLocatie, TreeSet<String> downloadLocaties) {
        this.lokaalIP = lokaalIP;
        this.downloadLocaties = downloadLocaties;
        this.replicatieLocatie = replicatieLocatie;
    }

    public Bestandsfiche(String lokaalIP, String replicatieLocatie) {
        this.lokaalIP = lokaalIP;
        this.downloadLocaties = new TreeSet<String>();
        this.replicatieLocatie = replicatieLocatie;
    }


    public String getLocalIP() { return lokaalIP; }

    public void addDownloadLocatie(String IP) { downloadLocaties.add(IP); }

    public TreeSet<String> getDownloadLocaties(){ return downloadLocaties; }

    public void setLocal(String local) {
        this.lokaalIP = local;
    }

    public String getReplicatieLocatie() {
        return replicatieLocatie;
    }

    public void setReplicatieLocatie(String locatie) {
        this.replicatieLocatie = locatie;
    }

    public int getAmountOfDownloads() {
        return downloadLocaties.size();
    }
}
