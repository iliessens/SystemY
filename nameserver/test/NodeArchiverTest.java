import be.dist.name.NodeArchiver;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class NodeArchiverTest {

    @Test
    public void archiveTest() {
        //write test
        NodeArchiver archiver = new NodeArchiver();

        TreeMap<Integer,String> testMap = new TreeMap<>();
        testMap.put(1,"192.168.0.1");
        testMap.put(2,"192.168.0.2");

        archiver.save(testMap);

        // Read test
        Map<Integer,String> map = archiver.read();

        assert "192.168.0.1".equals(map.get(1));
        assert "192.168.0.2".equals(map.get(2));
    }

}
