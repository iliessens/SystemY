import be.dist.common.NamingServerInt;
import be.dist.name.NamingRMI;
import be.dist.name.NodeRepository;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMITest {

    private NamingServerInt rmi;

    public void startServer() {
        NodeRepository nodeRepository = new NodeRepository("10.2.1.5");
        new NamingRMI("127.0.0.1", nodeRepository);
    }

    @Before
    public void bindRMI() throws Exception {
        startServer();
         if (System.getSecurityManager() == null) {
         System.setProperty("java.security.policy","file:src/server.policy");
           System.setSecurityManager(new SecurityManager());
         }
        String name = "NamingServer";
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        rmi = (NamingServerInt) registry.lookup(name);

    }

    @Test
    public void testConcurrency() throws Exception {
        ConcurrencyRunner runner = new ConcurrencyRunner(rmi);

        runner.run();
        runner.run();
    }

    class ConcurrencyRunner implements Runnable{
        private NamingServerInt rmi;

        public ConcurrencyRunner(NamingServerInt rmi) {
            this.rmi = rmi;
        }

        @Override
        public void run() {
            try {
                rmi.getNodeIp("jefke");
            }
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    };

}
