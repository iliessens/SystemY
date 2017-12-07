package be.dist.node.agents;

import be.dist.node.FileInformation;

public class AgentFile extends FileInformation {
    private String lockedBy;

    public AgentFile(String filename, String lockedBy) {
        super(filename);
        this.lockedBy = lockedBy;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public boolean isLocked() {
        return lockedBy != null;
    }
 }
