import be.dist.node.replication.TCPSender;
import org.junit.Test;

public class TCPSenderTest {

    @Test
    public void sendTestFile() {
        TCPSender sender = new TCPSender(7896);
        sender.send("127.0.0.1","files/original/testfile.txt");
    }
}
