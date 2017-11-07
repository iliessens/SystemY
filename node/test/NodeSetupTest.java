import be.dist.common.Node;
import be.dist.node.NodeSetup;
import org.junit.Test;

public class NodeSetupTest {

    @Test
    public void testData() throws Exception {
        NodeSetup setup  = new NodeSetup("Jan","127.0.0.1");

        Node testNode = new Node(1,"10.1.1.2");
        setup.setNeighbours(testNode,testNode);

        assert setup.getNext().equals(testNode);
        assert setup.getPrevious().equals(testNode);
    }
}
