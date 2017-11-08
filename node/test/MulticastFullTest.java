import be.dist.node.MulticastListener;
import be.dist.node.MulticastSender;
import be.dist.node.NodeSetup;
import org.junit.Test;

public class MulticastFullTest {

    @Test
    public void SendAndReceive() throws Exception{
        MulticastSender sender = new MulticastSender("127.0.0.1");
        NodeSetup setup = new NodeSetup("Jef","127.0.0.1");
        MulticastListener listener = new MulticastListener("127.0.0.1",setup);

        sender.sendHello("Jan");

        assert setup.getPrevious() != null;
        assert setup.getNext() != null;
    }
}
