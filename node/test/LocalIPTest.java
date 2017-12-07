import be.dist.node.agents.LocalIP;
import org.junit.Test;

public class LocalIPTest {

    @Test
    public void testLocalIP() {
        LocalIP.setLocalIP("127.0.0.1");
        assert LocalIP.getLocalIP().equals("127.0.0.1");
    }
}
