package be.dist.node.replication;

import be.dist.node.FileInformation;

public class NodeFileInformation extends FileInformation{

    private Boolean isLocal;
    private Boolean isOwner;

    public NodeFileInformation(Boolean isLocal, Boolean isOwner, String fileName) {
        super(fileName);
        this.isLocal = isLocal; // heeft originele
        this.isOwner = isOwner;
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
