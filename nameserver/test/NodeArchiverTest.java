import be.dist.name.NodeArchiver;
import org.junit.Test;

import java.util.TreeMap;

public class NodeArchiverTest {

    @Test
    public void writeTest() {
        NodeArchiver archiver = new NodeArchiver();

        TreeMap<Integer,String> testMap = new TreeMap<>();
        testMap.put(1,"192.168.0.1");
        testMap.put(2,"192.168.0.2");

        archiver.save(testMap);

    }
}
