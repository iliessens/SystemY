package be.dist.node.gui;

public enum FileLockState {
    WAITFORRELEASE, // Locked by peer
    WAITFORLOCK, // Lock request has been set
    LOCKED, // Lock by self
    UNLOCKED // Unlocked (should not be necessary)

}
