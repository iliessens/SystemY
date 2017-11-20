import be.dist.node.replication.TCPListener;

public class TCPReceiverTest {

    public void receiveFile() {
        TCPListener listener = new TCPListener(7896);
        listener.run();
    }
}
