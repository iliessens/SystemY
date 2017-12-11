package be.dist.common;

import java.io.Serializable;

public interface Agent extends Runnable, Serializable {

    boolean getStopFlag();
}
