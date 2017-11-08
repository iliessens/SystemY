package be.dist.node;

import java.util.InputMismatchException;
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
        int keuze = 0;
        boolean inValid = true;
        while(inValid) {
            try {
                keuze = scanner.nextInt();
                inValid = false;
            } catch (InputMismatchException e) {
                System.out.println("No valid command from user.");
            }
        }

        switch (keuze) {
            case 9: shutdown();
                    break;
            default: System.out.println("No valid command from user.");
        }
    }

    public void shutdown() {
        setup.sendNeighboursShutdown();
        System.exit(0);
    }
}
