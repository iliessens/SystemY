import be.dist.node.replication.TCPSender;
import org.junit.Ignore;
import org.junit.Test;

public class TCPSenderTest {

    @Ignore
    @Test
    public void sendTestFile() {
        TCPSender sender = new TCPSender(7899);
        sender.send("10.2.1.15","files/original/testfile.txt");
    }
}
