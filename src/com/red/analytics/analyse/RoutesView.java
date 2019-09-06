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

public class RoutesView {

    private static JFrame analyseWindow = null;
    private JTable analyseTable;
    private RoutesModel analyseTableModel;
    private JPanel analyseContainer;
    private BoxLayout analyseLayout;
    private JButton addRouteButton;
    private JButton refreshAnalyseButton;
    private JScrollPane scrollPane;
    private boolean modelStatus;


    public int showAnalyseView() {

        try {
            analyseTableModel = new RoutesModel();

            if (analyseTableModel.error){
                return -1;
            }

            this.modelStatus = analyseTableModel.isModelStatus();
            analyseTable = new JTable(analyseTableModel);
            analyseTable.getColumnModel().getColumn(1).setPreferredWidth(10);
            analyseTable.getColumnModel().getColumn(3).setPreferredWidth(10);
            analyseTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (analyseWindow != null){
            analyseWindow.requestFocus();
            return 0;
        }


        analyseWindow = new JFrame(MainController.getProjectName() + " - Routes Analyse");
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

        addRouteButton = new JButton("Register Route");

        addRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RoutesController.addRouteAction();
                    RoutesController.refreshAnalyseAction();
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonsContainer.add(addRouteButton);

        buttonsContainer.add(Box.createRigidArea(new Dimension(10, 0)));

        refreshAnalyseButton = new JButton("Refresh Analyse");

        refreshAnalyseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RoutesController.refreshAnalyseAction();
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

    public JButton getAddRouteButton() {
        return addRouteButton;
    }

    public void setAddRouteButton(JButton addRouteButton) {
        this.addRouteButton = addRouteButton;
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

    public RoutesModel getAnalyseTableModel() {
        return analyseTableModel;
    }

    public void setAnalyseTableModel(RoutesModel analyseTableModel) {
        this.analyseTableModel = analyseTableModel;
    }

    public static void setAnalyseWindow(JFrame analyseWindow) {
        RoutesView.analyseWindow = analyseWindow;
    }
}
