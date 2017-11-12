import be.dist.common.exceptions.NamingServerException;
import be.dist.name.NodeRepository;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class RepositoryTest {

    private NodeRepository test;

    @Before
    public void setup() {
        test = new NodeRepository("10.2.1.5");
        test.clearNodes();
        test.addNode("freddie","192.168.0.7");
        test.addNode("imre","192.168.0.8");
    }

    @Test
    public void testAdd() {
        test.addNode("Jannes","192.168.0.254");
    }

    @Test
    public void testget() {
        String result;
        result = test.getNodeIp("freddie");
        assert result.equals("192.168.0.7");

        result = test.getNodeIp("imre");
        assert result.equals("192.168.0.8");
    }

    @Test
    public void testRemove() {
        test.removeNode("freddie");
        assert test.getNodeIp("freddie") == null;
    }



    @Test
    public void testDoubles() {

        try {
            test.addNode("jefke", "192.168.0.8");
            fail("Double IP was allowed!");
        }
        catch(NamingServerException e){
        }

        try {
            test.addNode("imre", "192.168.0.10");
            fail("Double name was allowed!");
        }
        catch(NamingServerException e){
        }
    }
    @Test
    public void getOwnerTest(){

        test.addNode("bob","192.168.0.9");
        test.addNode("jannes","192.168.0.10");
        assert ((String)test.getOwner("0")).equals("192.168.0.9");
        assert ((String)test.getOwner("jannes")).equals("192.168.0.7");
        assert ((String)test.getOwner("Robbe")).equals("192.168.0.8");
        assert ((String)test.getOwner("freddie")).equals("192.168.0.9");
    }

}
