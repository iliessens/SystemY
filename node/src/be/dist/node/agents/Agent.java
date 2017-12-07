package be.dist.node.agents;

import java.io.Serializable;

public interface Agent extends Runnable, Serializable {

    boolean getStopFlag();
}
