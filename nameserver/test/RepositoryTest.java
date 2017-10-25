import be.dist.common.exceptions.NamingServerException;
import be.dist.name.NodeRepository;
import org.junit.Before;
import org.junit.Test;

public class RepositoryTest {

    private NodeRepository test;

    @Test(expected = NamingServerException.class)
    public void testRepository() {
        test = new NodeRepository();
        test.addNode("freddie","192.168.0.7");
        test.addNode("imre","192.168.0.8");

        String result;
        result = test.getNodeIp("freddie");
        assert result.equals("192.168.0.7");
        result = test.getNodeIp("imre");
        assert result.equals("192.168.0.8");

        test.removeNode("freddie");
        assert test.getNodeIp("freddie") == null;

        test.addNode("jefke", "192.168.0.8");
        test.addNode("imre", "192.168.0.10");
        test.printNodes();
    }
}
