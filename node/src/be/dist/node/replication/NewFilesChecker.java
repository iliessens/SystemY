package be.dist.node.replication;

public class NewFilesChecker extends Thread{

    public void run() {
        /*
          Periodieke thread die om de 5s de map controleert op
          nieuwe bestanden die de lokale bestanden bevat
          **/
        while(true) {
            FileDiscovery.getInstance().discoverFiles();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("<!> newFilesChecker thread refused sleeping command <!>");
            }
        }
    }
}
