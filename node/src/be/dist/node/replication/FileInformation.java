package be.dist.node.replication;

public class FileInformation {

    private Boolean isLocal;
    private Boolean isOwner;
    private String fileName;

    public FileInformation(Boolean isLocal, Boolean isOwner, String fileName) {
        this.isLocal = isLocal; // heeft originele
        this.isOwner = isOwner;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public Boolean getLocal() {

        return isLocal;
    }

    public void setLocal(Boolean local) {
        isLocal = local;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }
}
