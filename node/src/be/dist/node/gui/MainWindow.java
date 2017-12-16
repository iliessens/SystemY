package be.dist.node.gui;

import be.dist.common.NodeRMIInt;
import be.dist.node.agents.AgentFile;
import be.dist.node.agents.LocalFileList;
import be.dist.node.agents.LocalIP;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

public class MainWindow {
    private JPanel panel1;
    private JList fileList;
    private JButton deleteButton;
    private JButton deleteLocalButton;
    private JButton downloadButton;

    private AgentFile selectedFile;
    private Map<String,AgentFile> fileMap;

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

        LocalFileList.addObserver(this::setFileList);

        addButtonlisteners();
        selectedFile = null;

        fileList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                // might be run multiple times
                int i = listSelectionEvent.getFirstIndex();
                JList lsm = (JList) listSelectionEvent.getSource();
                String selectedFilename = (String) lsm.getModel().getElementAt(i);

                    deleteButton.setEnabled(true);
                    downloadButton.setEnabled(true);

                    selectedFile = fileMap.get(selectedFilename);
                    if (selectedFile != null) {
                        if(new File("files/downloads/"+selectedFile.getFileName()).exists()) {
                            // file is present in downloads folder
                            deleteLocalButton.setEnabled(true);
                        }
                    }
                }
        });
    }

    private void addButtonlisteners() {
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NodeRMIInt nodeRMIInt = getRemoteSetup(selectedFile.getOwnerIP());
                try {
                    nodeRMIInt.sendFileTo(LocalIP.getLocalIP(),selectedFile.getFileName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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
                new File("files/downloads/"+selectedFile.getFileName()).delete();
            }
        });
    }

    private void setFileList(Map<String,AgentFile> filenames) {
        fileMap = filenames;
        DefaultListModel<String> model = new DefaultListModel<>();
        // Fill list with items
        filenames.forEach((key, value) -> model.addElement(key));

        fileList.setModel(model);
        // enable list
        fileList.setEnabled(true);
    }

    public void openGUI() {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private NodeRMIInt getRemoteSetup(String ip) {
        NodeRMIInt remoteSetup = null;
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            remoteSetup = (NodeRMIInt) registry.lookup("nodeSetup");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return remoteSetup;
    }
}
