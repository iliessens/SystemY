import be.dist.node.replication.FileIO;
import be.dist.node.replication.FileInformation;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class FileIOTest {

    @Test
    public void testListing() {
        FileIO io = new FileIO();
        io.getLocalFiles();
        List<String> lijst = io.getLocalFiles();
        System.out.println(lijst);
        assert lijst.contains("testfile.txt");
    }

    @Test
    public void testMap() {
        FileIO io = new FileIO();
        Map<String,FileInformation> map= io.getMap();
        FileInformation info = map.get("testfile.txt");
        assert info != null;
        assert info.getLocal(); // should be local
        assert info.getOwner() == null; // is still unknown
        assert info.getFileName().equals("testfile.txt");

        assert map == io.getMap();
    }

}
