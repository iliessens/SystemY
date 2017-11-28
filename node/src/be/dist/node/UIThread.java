package be.dist.node;

import be.dist.common.Node;

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
        while (true) {
            System.out.println("Press 1 to show current neighbours");
            System.out.println("Press 9 to quit: ");
            int keuze = 0;
            boolean inValid = true;
            while (inValid) {
                try {
                    keuze = scanner.nextInt();
                    inValid = false;
                } catch (InputMismatchException e) {
                    System.out.println("No valid command from user.");
                    scanner.nextLine(); // empty buffer
                }
            }

            switch (keuze) {
                case 1:
                    printNeighbours();
                    break;
                case 9:
                    shutdown();
                    break;
                default:
                    System.out.println("No valid command from user.");
            }
        }
    }

    public void shutdown() {
        setup.sendNeighboursShutdown();
        System.exit(0);
    }

    public void printNeighbours() {
        Node previous = setup.getPrevious();
        Node next = setup.getPrevious();
        System.out.println("Previous: " + (previous != null ? previous.getIp() : "Not set"));
        System.out.println("Next: " + (next != null ? next.getIp() : "Not set"));
    }
}
