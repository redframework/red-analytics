package com.red.analytics.analyse;

import com.red.analytics.main.MainController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class CommandsView {

    private static JFrame analyseWindow = null;
    private JTable analyseTable;
    private CommandsModel analyseTableModel;
    private JPanel analyseContainer;
    private BoxLayout analyseLayout;
    private JButton addCommandButton;
    private JButton refreshAnalyseButton;
    private JScrollPane scrollPane;
    private boolean modelStatus;


    public int showAnalyseView() {

        try {
            analyseTableModel = new CommandsModel();

            if (analyseTableModel.error){
                return -1;
            }

            this.modelStatus = analyseTableModel.isModelStatus();
            analyseTable = new JTable(analyseTableModel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (analyseWindow != null){
            analyseWindow.requestFocus();
            return 0;
        }


        analyseWindow = new JFrame(MainController.getProjectName() + " - Commands Analyse");
        analyseWindow.setBounds(new Rectangle(950, 500));
        analyseWindow.setResizable(false);
        analyseWindow.setLocationRelativeTo(null);

        analyseWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                analyseWindow = null;
            }
        });

        analyseContainer = new JPanel();

        analyseLayout = new BoxLayout(analyseContainer, BoxLayout.Y_AXIS);

        analyseContainer.setLayout(analyseLayout);

        analyseWindow.setContentPane(analyseContainer);


        InputStream headerInputStream = getClass().getResourceAsStream("/com/red/analytics/resources/images/Red_Framework_Logo.png");
        try {
            analyseWindow.setIconImage(ImageIO.read(headerInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            headerInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        analyseTable.setBounds(new Rectangle(30, 40, 300, 350));

        scrollPane = new JScrollPane(analyseTable);

        analyseContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        analyseContainer.add(scrollPane);

        JPanel buttonsContainer = new JPanel();

        buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.X_AXIS));

        addCommandButton = new JButton("Register Commands");

        addCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CommandsController.addCommandAction();
                    CommandsController.refreshAnalyseAction();
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonsContainer.add(addCommandButton);

        buttonsContainer.add(Box.createRigidArea(new Dimension(10, 0)));

        refreshAnalyseButton = new JButton("Refresh Analyse");

        refreshAnalyseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CommandsController.refreshAnalyseAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        buttonsContainer.add(refreshAnalyseButton);

        analyseContainer.add(Box.createRigidArea(new Dimension(5, 15)));
        analyseContainer.add(buttonsContainer);
        analyseContainer.add(Box.createRigidArea(new Dimension(5, 15)));

        analyseWindow.setVisible(true);

        return 1;
    }

    public JFrame getAnalyseWindow() {
        return analyseWindow;
    }


    public JTable getAnalyseTable() {
        return analyseTable;
    }

    public void setAnalyseTable(JTable analyseTable) {
        this.analyseTable = analyseTable;
    }

    public JPanel getAnalyseContainer() {
        return analyseContainer;
    }

    public void setAnalyseContainer(JPanel analyseContainer) {
        this.analyseContainer = analyseContainer;
    }

    public BoxLayout getAnalyseLayout() {
        return analyseLayout;
    }

    public void setAnalyseLayout(BoxLayout analyseLayout) {
        this.analyseLayout = analyseLayout;
    }

    public JButton getAddCommandButton() {
        return addCommandButton;
    }

    public void setAddCommandButton(JButton addCommandButton) {
        this.addCommandButton = addCommandButton;
    }

    public JButton getRefreshAnalyseButton() {
        return refreshAnalyseButton;
    }

    public void setRefreshAnalyseButton(JButton refreshAnalyseButton) {
        this.refreshAnalyseButton = refreshAnalyseButton;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public boolean isModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(boolean modelStatus) {
        this.modelStatus = modelStatus;
    }

    public CommandsModel getAnalyseTableModel() {
        return analyseTableModel;
    }

    public void setAnalyseTableModel(CommandsModel analyseTableModel) {
        this.analyseTableModel = analyseTableModel;
    }

    public static void setAnalyseWindow(JFrame analyseWindow) {
        CommandsView.analyseWindow = analyseWindow;
    }
}
