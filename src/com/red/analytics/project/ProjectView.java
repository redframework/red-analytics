package com.red.analytics.project;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ProjectView {

    private JFrame projectWindow;
    private JPanel projectContainer;
    private GridBagLayout projectLayout;
    private GridBagConstraints GBC;
    private JFileChooser fileChooser = null;
    private JButton createButton;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private JButton choosePathButton;
    private JCheckBox secretKeyCheckBox;
    private JTextField projectName;
    private JTextField programmer;
    private JTextField projectPath;
    private JComboBox projectSDKVersion;

    ProjectView(){

        projectWindow = new JFrame();

        projectWindow.setTitle("Red Analytics - New Project");

        projectWindow.setBounds(new Rectangle(550, 525));
        projectWindow.setResizable(false);
        projectWindow.setLocationRelativeTo(null);
        projectWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        InputStream iconInputStream = getClass().getResourceAsStream("/com/red/analytics/resources/images/Red_Framework_Logo.png");

        try {
            projectWindow.setIconImage(ImageIO.read(iconInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            iconInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        projectContainer = new JPanel();

        projectContainer.setBounds(new Rectangle(100, 120));

        projectContainer.setLayout(new BoxLayout(projectContainer, BoxLayout.Y_AXIS));

        projectWindow.setContentPane(projectContainer);

        projectLayout = new GridBagLayout();

        GBC = new GridBagConstraints();

        projectContainer.setLayout(projectLayout);

        projectName = new JTextField(6);
        programmer = new JTextField(6);
        projectPath = new JTextField(6);
        choosePathButton = new JButton("Choose");
        projectSDKVersion = new JComboBox(new String[]{"Red Framework 1.0 Enterprise", "Red Framework 1.0 Console App Development Kit"});
        secretKeyCheckBox = new JCheckBox("Generate New Secret Key");
        secretKeyCheckBox.setSelected(true);


        GBC.insets = new Insets(0, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 0;
        GBC.fill = GridBagConstraints.BOTH;

        InputStream logoInputStream = getClass().getResourceAsStream("/com/red/analytics/resources/images/project_icon.png");

        JLabel headerLabel = null;
        try {
            headerLabel = new JLabel(new ImageIcon(logoInputStream.readAllBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        headerLabel.setHorizontalAlignment(JLabel.CENTER);

        projectContainer.add(headerLabel, GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 1;
        GBC.fill = GridBagConstraints.BOTH;

        projectContainer.add(new JLabel("Project Name :"), GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 2;

        projectContainer.add(projectName, GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 3;

        projectContainer.add(new JLabel("Programmer :"), GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 4;

        projectContainer.add(programmer, GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 5;

        projectContainer.add(new JLabel("Project Path :"), GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 6;

        projectContainer.add(projectPath, GBC);

        GBC.insets = new Insets(0, 20, 0, 0);
        GBC.gridx = 1;
        GBC.gridy = 6;

        projectContainer.add(choosePathButton, GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 7;

        projectContainer.add(new JLabel("Target SDK Version :"), GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 8;

        projectContainer.add(projectSDKVersion, GBC);

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 9;

        projectContainer.add(secretKeyCheckBox, GBC);


        choosePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (fileChooser == null){
                    fileChooser = new JFileChooser();

                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setCurrentDirectory(new File("."));
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    fileChooser.setDialogTitle("Red Analytics - New Project Path");
                }

                int result = fileChooser.showSaveDialog(projectWindow);

                if (result == JFileChooser.APPROVE_OPTION){
                    projectPath.setText(fileChooser.getSelectedFile().toString());
                }
            }
        });

        createButton = new JButton("Create");
        cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                projectWindow.setVisible(false);
            }
        });

        GBC.insets = new Insets(10, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 10;

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    ProjectController.createProjectAction();
            }
        });

        projectContainer.add(createButton, GBC);

        GBC.insets = new Insets(10, 20, 0, 0);
        GBC.gridx = 1;
        GBC.gridy = 10;

        projectContainer.add(cancelButton, GBC);

        progressBar = new JProgressBar();

        projectWindow.setVisible(true);
    }

    public JFrame getProjectWindow() {
        return projectWindow;
    }

    public void setProjectWindow(JFrame projectWindow) {
        this.projectWindow = projectWindow;
    }

    public JPanel getProjectContainer() {
        return projectContainer;
    }

    public void setProjectContainer(JPanel projectContainer) {
        this.projectContainer = projectContainer;
    }

    public GridBagLayout getProjectLayout() {
        return projectLayout;
    }

    public void setProjectLayout(GridBagLayout projectLayout) {
        this.projectLayout = projectLayout;
    }

    public GridBagConstraints getGBC() {
        return GBC;
    }

    public void setGBC(GridBagConstraints GBC) {
        this.GBC = GBC;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    public JButton getCreateButton() {
        return createButton;
    }

    public void setCreateButton(JButton createButton) {
        this.createButton = createButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(JButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JButton getChoosePathButton() {
        return choosePathButton;
    }

    public void setChoosePathButton(JButton choosePathButton) {
        this.choosePathButton = choosePathButton;
    }

    public JCheckBox getSecretKeyCheckBox() {
        return secretKeyCheckBox;
    }

    public void setSecretKeyCheckBox(JCheckBox secretKeyCheckBox) {
        this.secretKeyCheckBox = secretKeyCheckBox;
    }

    public JTextField getProjectName() {
        return projectName;
    }

    public void setProjectName(JTextField projectName) {
        this.projectName = projectName;
    }

    public JTextField getProgrammer() {
        return programmer;
    }

    public void setProgrammer(JTextField programmer) {
        this.programmer = programmer;
    }

    public JTextField getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(JTextField projectPath) {
        this.projectPath = projectPath;
    }

    public JComboBox getProjectSDKVersion() {
        return projectSDKVersion;
    }

    public void setProjectSDKVersion(JComboBox projectSDKVersion) {
        this.projectSDKVersion = projectSDKVersion;
    }
}
