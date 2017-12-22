package be.dist.node.agents;

import be.dist.node.FileInformation;

import java.io.Serializable;

public class AgentFile extends FileInformation implements Serializable {
    private String lockedBy;
    private String ownerIP;
    private boolean lockRequest; // only useful in local list

    public AgentFile(String filename, String ownerIP) {
        super(filename);
        this.lockedBy = null;
        this.ownerIP = ownerIP;
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

    public String getOwnerIP() {
        return ownerIP;
    }

    public void requestLock() {
        lockRequest = true;
    }

    public void removeLockRequest() {
        lockRequest = false;
    }
    public boolean hashLockRequest() {
        return  lockRequest;
    }
 }
