import be.dist.node.replication.TCPListener;
import org.junit.Ignore;
import org.junit.Test;

public class TCPReceiverTest {

    @Ignore
    @Test
    public void receiveFile() {
        TCPListener listener = new TCPListener(7899);
        listener.run();
    }
}
