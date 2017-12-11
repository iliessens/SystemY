package be.dist.node.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
    private JPanel panel1;
    private JList list1;
    private JButton deleteButton;
    private JButton deleteLocalButton;
    private JButton downloadButton;

    private String selectedFilename;

    public static void main(String args[]) {
        MainWindow gui = new MainWindow();
        gui.openGUI();
    }

    public MainWindow() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        addButtonlisteners();
        selectedFilename = null;

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                // might be run multiple times
                int i = listSelectionEvent.getFirstIndex();
                JList lsm = (JList) listSelectionEvent.getSource();
                selectedFilename = (String) lsm.getModel().getElementAt(i);

                if(selectedFilename != null) {
                    deleteButton.setEnabled(true);
                    //deleteLocalButton.setEnabled(true);
                    downloadButton.setEnabled(true);
                }
            }
        });
    }

    private void addButtonlisteners() {
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // TODO do download action
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // TODO do delete action
            }
        });

        deleteLocalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // TODO do local delete
            }
        });
    }

    public void openGUI() {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
