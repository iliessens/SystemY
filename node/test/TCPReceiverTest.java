import be.dist.node.TCPListener;

public class TCPReceiverTest {

    public void receiveFile() {
        TCPListener listener = new TCPListener(7896);
        listener.run();
    }
}
