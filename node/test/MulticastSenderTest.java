import be.dist.node.MulticastSender;
import org.junit.Test;

public class MulticastSenderTest {

    @Test
    public void sendMessage() {
        MulticastSender sender = new MulticastSender("127.0.0.1");
        sender.sendHello("Testmessage");
    }
}
