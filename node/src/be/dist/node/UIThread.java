package be.dist.node;

import java.util.Scanner;

public class UIThread extends Thread {
    private Scanner scanner;
    private NodeSetup setup;

    public UIThread(NodeSetup setup) {
        scanner = new Scanner(System.in);
        this.setup = setup;
    }

    public void run() {
        System.out.println("Press 9 to quit: ");
        int keuze = scanner.nextInt();
        switch (keuze) {
            case 9: shutdown();
                    break;
        }
    }

    public void shutdown() {
        setup.sendNeighboursShutdown();
        System.exit(0);
    }
}
