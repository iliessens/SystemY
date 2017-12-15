import be.dist.node.replication.TCPListener;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class TCPReceiverTest {

    @Ignore
    @Test
    public void receiveFile() {
        TCPListener listener = new TCPListener(7899);
        listener.run();
    }

    @Test
    public void testFolderCreation() {
        new TCPListener(7899,"files/testfolder");
        File folder = new File("files/testfolder");
        assert folder.exists();
        assert folder.delete(); // make sure its deleted
    }
}
