package be.dist.node.replication;

import java.util.List;

public class NewFilesChecker extends Thread{

    private List<String> checkedFiles;
    private FileIO fileIO;
    private FileDiscovery discovery;

    public NewFilesChecker() {
        fileIO = new FileIO();
        checkedFiles = fileIO.getLocalFiles();
    }

    public void run() {
        /*
          Periodieke thread die om de 5s de map controleert op
          nieuwe bestanden die de lokale bestanden bevat
          **/
        while(true) {
            List<String> files = fileIO.getLocalFiles();
            // removes the already checked files.
            files.removeAll(checkedFiles);

            for (String newFile : files) {
                discovery.fileCheck(newFile);
            }

            // Files zijn in orde en moeten volgende keer niet gechecked worden.
            checkedFiles.addAll(files);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("<!> newFilesChecker thread refused sleeping command <!>");
            }
        }
    }
}
