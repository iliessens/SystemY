import be.dist.common.NamingServerInt;
import be.dist.name.NamingRMI;
import org.junit.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMITest {

    private NamingServerInt rmi;;

    @Test
    public void startServer() {
        new NamingRMI("127.0.0.1");
    }

    @Test
    public void bindRMI() throws Exception {
         if (System.getSecurityManager() == null) {
         System.setProperty("java.security.policy","file:src/server.policy");
           System.setSecurityManager(new SecurityManager());
         }
        String name = "NamingServer";
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        rmi = (NamingServerInt) registry.lookup(name);
    }

    //@Test
    public void getIp() throws Exception {
       assert  rmi.getNodeIp("blabla") == null;
    }

}
