package com.red.analytics.main;

import com.red.analytics.analyse.CommandsController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class ConsoleView {

    private JFrame window;
    private GridBagLayout mainLayout;
    private GridBagConstraints GBC;
    private JMenuBar menuBar;
    private JMenu projectMenu;
    private JMenuItem newProjectMenuItem;
    private JMenuItem newSecretKeyMenuItem;
    private JMenu generateMenu;
    private JMenuItem controllerMenuItem;
    private JMenuItem modelMenuItem;
    private JMenuItem viewMenuItem;
    private JMenuItem middlewareMenuItem;
    private JMenu commandMenu;
    private JMenuItem addCommandMenuItem;
    private JMenuItem analyseCommandMenuItem;
    private JMenu applicationMenu;
    private JMenuItem runApplicationMenuItem;
    private JMenuItem stopApplicationMenuItem;
    private JMenuItem maintenanceModeMenuItem;
    private JMenuItem breakModeMenuItem;
    private JMenuItem productionModeMenuItem;
    private JPanel mainContainer;
    private InputStream logoInputStream;
    private JButton generateControllerButton;
    private JButton generateModelButton;
    private JButton generateViewButton;
    private JButton addCommandButton;
    private JButton analyseButton;
    private JButton runApplicationButton;
    private JButton stopApplicationButton;
    private JLabel applicationStatusLabel;
    private ButtonGroup projectModeButtonGroup;
    private JRadioButton maintenanceRadioButton;
    private JRadioButton productionRadioButton;
    private JRadioButton maintenanceBreakRadioButton;
    private JLabel projectLabel;
    private JLabel commandSectionLabel;


    ConsoleView(String projectName, int projectMode) {

        // Create window With JFrame and Set Configuration
        window = new JFrame("Red Analytics - " + projectName + " (Console Application)");
        window.setBounds(new Rectangle(600, 560));
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        InputStream headerInputStream = getClass().getResourceAsStream("/com/red/analytics/resources/images/Red_Framework_Logo.png");
        try {
            window.setIconImage(ImageIO.read(headerInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            headerInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                MainController.mainFrameCloseEvent();
            }
        });


        // Create and Set Layout to window

        mainLayout = new GridBagLayout();

        GBC = new GridBagConstraints();

        window.setLayout(mainLayout);

        // Create JPanel

        mainContainer = new JPanel();

        mainContainer.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        mainContainer.setAlignmentY(JPanel.CENTER_ALIGNMENT);


        mainContainer.setLayout(mainLayout);

        window.setContentPane(mainContainer);


        logoInputStream = getClass().getResourceAsStream("/com/red/analytics/resources/images/Red_Framework_Logo.png");

        JLabel headerLabel = null;
        try {
            headerLabel = new JLabel(new ImageIcon(logoInputStream.readAllBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            logoInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        headerLabel.setHorizontalAlignment(JLabel.CENTER);


        menuBar = new JMenuBar();

        projectMenu = new JMenu("Project");

        newProjectMenuItem = new JMenuItem("New Project");

        newProjectMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.newProject();
            }
        });

        newSecretKeyMenuItem = new JMenuItem("New Secret Key for " + projectName);

        newSecretKeyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateNewSecretKey();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        projectMenu.add(newProjectMenuItem);
        projectMenu.add(newSecretKeyMenuItem);

        generateMenu = new JMenu("Generate");

        controllerMenuItem = new JMenuItem("Controller");

        controllerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateControllerAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        modelMenuItem = new JMenuItem("Model");

        modelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateModelAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        viewMenuItem = new JMenuItem("View");

        viewMenuItem.setEnabled(false);

        viewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateViewAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        middlewareMenuItem = new JMenuItem("Middleware");

        middlewareMenuItem.setEnabled(false);

        generateMenu.add(controllerMenuItem);
        generateMenu.add(modelMenuItem);
        generateMenu.add(viewMenuItem);
        generateMenu.add(middlewareMenuItem);

        commandMenu = new JMenu("Commands");

        addCommandMenuItem = new JMenuItem("Register Command");

        addCommandMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.addCommandAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        analyseCommandMenuItem = new JMenuItem("Analyse Commands");

        analyseCommandMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommandsController commandsController = new CommandsController();
                commandsController.analyse();
            }
        });

        commandMenu.add(addCommandMenuItem);
        commandMenu.add(analyseCommandMenuItem);

        applicationMenu = new JMenu("Application");

        runApplicationMenuItem = new JMenuItem("Run Application");

        runApplicationMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.runConsoleAppAction();
            }
        });

        stopApplicationMenuItem = new JMenuItem("Stop Application");

        stopApplicationMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.stopConsoleAppAction();
            }
        });

        maintenanceModeMenuItem = new JMenuItem("Maintenance Mode");

        maintenanceModeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    maintenanceRadioButton.setSelected(true);
                    MainController.changeModeAction(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        breakModeMenuItem = new JMenuItem("Maintenance Break Mode");

        breakModeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    maintenanceBreakRadioButton.setSelected(true);
                    MainController.changeModeAction(2);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        productionModeMenuItem = new JMenuItem("Production Mode");

        productionModeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    productionRadioButton.setSelected(true);
                    MainController.changeModeAction(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        applicationMenu.add(runApplicationMenuItem);
        applicationMenu.add(stopApplicationMenuItem);
        applicationMenu.add(maintenanceModeMenuItem);
        applicationMenu.add(breakModeMenuItem);
        applicationMenu.add(productionModeMenuItem);

        menuBar.add(projectMenu);
        menuBar.add(generateMenu);
        menuBar.add(commandMenu);
        menuBar.add(applicationMenu);

        window.setJMenuBar(menuBar);

        GBC.insets = new Insets(-30, 0, 0, 0);
        GBC.gridx = 0;
        GBC.gridy = 0;

        mainContainer.add(headerLabel, GBC);

        GBC.insets = new Insets(-30, 0, 0, 0);
        GBC.gridx = 1;
        GBC.gridy = 0;

        projectLabel = new JLabel(projectName.toUpperCase() + " PROJECT");

        projectLabel.setForeground(Color.GRAY);

        mainContainer.add(projectLabel, GBC);

        GBC.insets = new Insets(0, 0, 7, 0);
        GBC.gridx = 0;
        GBC.gridy = 1;
        GBC.fill = GridBagConstraints.BOTH;

        mainContainer.add(new JLabel("Generate Section"), GBC);

        GBC.insets = new Insets(0, 30, 7, 0);
        GBC.gridx = 1;
        GBC.gridy = 1;

        mainContainer.add(new JLabel("Application Runtime"), GBC);

        GBC.insets = new Insets(1, 0, 1, 0);

        GBC.gridx = 0;
        GBC.gridy = 2;

        generateControllerButton = new JButton("Controller");

        generateControllerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateControllerAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mainContainer.add(generateControllerButton, GBC);

        GBC.insets = new Insets(1, 30, 1, 0);
        GBC.gridx = 1;
        GBC.gridy = 2;

        runApplicationButton = new JButton("Run");

        runApplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.runConsoleAppAction();
            }
        });

        mainContainer.add(runApplicationButton, GBC);

        GBC.insets = new Insets(1, 0, 1, 0);
        GBC.gridx = 0;
        GBC.gridy = 3;

        generateModelButton = new JButton("Model");

        generateModelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateModelAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mainContainer.add(generateModelButton, GBC);

        GBC.insets = new Insets(1, 30, 1, 0);
        GBC.gridx = 1;
        GBC.gridy = 3;

        stopApplicationButton = new JButton("Stop");

        stopApplicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.stopConsoleAppAction();
            }
        });

        mainContainer.add(stopApplicationButton, GBC);

        GBC.insets = new Insets(1, 0, 1, 0);
        GBC.gridx = 0;
        GBC.gridy = 4;

        generateViewButton = new JButton("View");

        generateViewButton.setEnabled(false);

        mainContainer.add(generateViewButton, GBC);


        GBC.insets = new Insets(1, 40, 1, 0);
        GBC.gridx = 1;
        GBC.gridy = 4;

        applicationStatusLabel = new JLabel("App Status: STOPPED");

        applicationStatusLabel.setForeground(Color.RED);

        mainContainer.add(applicationStatusLabel, GBC);


        GBC.insets = new Insets(7, 0, 7, 0);
        GBC.gridx = 0;
        GBC.gridy = 5;

        commandSectionLabel = new JLabel("Command Section");

        mainContainer.add(commandSectionLabel, GBC);


        GBC.insets = new Insets(1, 0, 1, 0);
        GBC.gridx = 0;
        GBC.gridy = 6;

        addCommandButton = new JButton("Register Command");

        addCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.addCommandAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mainContainer.add(addCommandButton, GBC);


        projectModeButtonGroup = new ButtonGroup();
        maintenanceRadioButton = new JRadioButton("Maintenance");

        maintenanceRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.changeModeAction(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        productionRadioButton = new JRadioButton("Production");

        productionRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.changeModeAction(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        maintenanceBreakRadioButton = new JRadioButton("Break");

        maintenanceBreakRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.changeModeAction(2);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        if (projectMode == 0){
            maintenanceRadioButton.setSelected(true);
        } else if (projectMode == 1){
            productionRadioButton.setSelected(true);
        } else {
            maintenanceBreakRadioButton.setSelected(true);
        }

        projectModeButtonGroup.add(maintenanceRadioButton);
        projectModeButtonGroup.add(productionRadioButton);
        projectModeButtonGroup.add(maintenanceBreakRadioButton);

        GBC.insets = new Insets(1, 30, 1, 0);
        GBC.gridx = 1;
        GBC.gridy = 6;

        mainContainer.add(maintenanceRadioButton, GBC);

        GBC.insets = new Insets(0, 0, 0, 0);
        GBC.gridx = 2;
        GBC.gridy = 6;

        mainContainer.add(maintenanceBreakRadioButton, GBC);

        GBC.insets = new Insets(1, 0, 1, 0);
        GBC.gridx = 0;
        GBC.gridy = 7;

        analyseButton = new JButton("Analyse Commands");

        analyseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommandsController commandsController = new CommandsController();
                commandsController.analyse();
            }
        });

        mainContainer.add(analyseButton, GBC);

        GBC.insets = new Insets(1, 30, 1, 0);
        GBC.gridx = 1;
        GBC.gridy = 7;

        mainContainer.add(productionRadioButton, GBC);

        if (!OsUtils.isWindows()){
            runApplicationMenuItem.setEnabled(false);
            stopApplicationMenuItem.setEnabled(false);
            runApplicationButton.setEnabled(false);
            stopApplicationButton.setEnabled(false);
        }

        window.setVisible(true);
    }

    public JFrame getWindow() {
        return window;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    public GridBagLayout getMainLayout() {
        return mainLayout;
    }

    public void setMainLayout(GridBagLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    public GridBagConstraints getGBC() {
        return GBC;
    }

    public void setGBC(GridBagConstraints GBC) {
        this.GBC = GBC;
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenu getGenerateMenu() {
        return generateMenu;
    }

    public void setGenerateMenu(JMenu generateMenu) {
        this.generateMenu = generateMenu;
    }

    public JMenuItem getControllerMenuItem() {
        return controllerMenuItem;
    }

    public void setControllerMenuItem(JMenuItem controllerMenuItem) {
        this.controllerMenuItem = controllerMenuItem;
    }

    public JMenuItem getModelMenuItem() {
        return modelMenuItem;
    }

    public void setModelMenuItem(JMenuItem modelMenuItem) {
        this.modelMenuItem = modelMenuItem;
    }

    public JMenuItem getViewMenuItem() {
        return viewMenuItem;
    }

    public void setViewMenuItem(JMenuItem viewMenuItem) {
        this.viewMenuItem = viewMenuItem;
    }

    public JMenu getCommandMenu() {
        return commandMenu;
    }

    public void setCommandMenu(JMenu commandMenu) {
        this.commandMenu = commandMenu;
    }

    public JMenuItem getAddCommandMenuItem() {
        return addCommandMenuItem;
    }

    public void setAddCommandMenuItem(JMenuItem addCommandMenuItem) {
        this.addCommandMenuItem = addCommandMenuItem;
    }

    public JMenuItem getAnalyseCommandMenuItem() {
        return analyseCommandMenuItem;
    }

    public void setAnalyseCommandMenuItem(JMenuItem analyseCommandMenuItem) {
        this.analyseCommandMenuItem = analyseCommandMenuItem;
    }

    public JMenu getApplicationMenu() {
        return applicationMenu;
    }

    public void setApplicationMenu(JMenu applicationMenu) {
        this.applicationMenu = applicationMenu;
    }

    public JMenuItem getRunApplicationMenuItem() {
        return runApplicationMenuItem;
    }

    public void setRunApplicationMenuItem(JMenuItem runApplicationMenuItem) {
        this.runApplicationMenuItem = runApplicationMenuItem;
    }

    public JMenuItem getStopApplicationMenuItem() {
        return stopApplicationMenuItem;
    }

    public void setStopApplicationMenuItem(JMenuItem stopApplicationMenuItem) {
        this.stopApplicationMenuItem = stopApplicationMenuItem;
    }

    public JMenuItem getMaintenanceModeMenuItem() {
        return maintenanceModeMenuItem;
    }

    public void setMaintenanceModeMenuItem(JMenuItem maintenanceModeMenuItem) {
        this.maintenanceModeMenuItem = maintenanceModeMenuItem;
    }

    public JMenuItem getBreakModeMenuItem() {
        return breakModeMenuItem;
    }

    public void setBreakModeMenuItem(JMenuItem breakModeMenuItem) {
        this.breakModeMenuItem = breakModeMenuItem;
    }

    public JMenuItem getProductionModeMenuItem() {
        return productionModeMenuItem;
    }

    public void setProductionModeMenuItem(JMenuItem productionModeMenuItem) {
        this.productionModeMenuItem = productionModeMenuItem;
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }

    public void setMainContainer(JPanel mainContainer) {
        this.mainContainer = mainContainer;
    }

    public InputStream getLogoInputStream() {
        return logoInputStream;
    }

    public void setLogoInputStream(InputStream logoInputStream) {
        this.logoInputStream = logoInputStream;
    }

    public JButton getGenerateControllerButton() {
        return generateControllerButton;
    }

    public void setGenerateControllerButton(JButton generateControllerButton) {
        this.generateControllerButton = generateControllerButton;
    }

    public JButton getGenerateModelButton() {
        return generateModelButton;
    }

    public void setGenerateModelButton(JButton generateModelButton) {
        this.generateModelButton = generateModelButton;
    }

    public JButton getGenerateViewButton() {
        return generateViewButton;
    }

    public void setGenerateViewButton(JButton generateViewButton) {
        this.generateViewButton = generateViewButton;
    }

    public JButton getAddCommandButton() {
        return addCommandButton;
    }

    public void setAddCommandButton(JButton addCommandButton) {
        this.addCommandButton = addCommandButton;
    }

    public JButton getAnalyseButton() {
        return analyseButton;
    }

    public void setAnalyseButton(JButton analyseButton) {
        this.analyseButton = analyseButton;
    }

    public JButton getRunApplicationButton() {
        return runApplicationButton;
    }

    public void setRunApplicationButton(JButton runApplicationButton) {
        this.runApplicationButton = runApplicationButton;
    }

    public JButton getStopApplicationButton() {
        return stopApplicationButton;
    }

    public void setStopApplicationButton(JButton stopApplicationButton) {
        this.stopApplicationButton = stopApplicationButton;
    }

    public JLabel getApplicationStatusLabel() {
        return applicationStatusLabel;
    }

    public void setApplicationStatusLabel(JLabel applicationStatusLabel) {
        this.applicationStatusLabel = applicationStatusLabel;
    }

    public ButtonGroup getProjectModeButtonGroup() {
        return projectModeButtonGroup;
    }

    public void setProjectModeButtonGroup(ButtonGroup projectModeButtonGroup) {
        this.projectModeButtonGroup = projectModeButtonGroup;
    }

    public JRadioButton getMaintenanceRadioButton() {
        return maintenanceRadioButton;
    }

    public void setMaintenanceRadioButton(JRadioButton maintenanceRadioButton) {
        this.maintenanceRadioButton = maintenanceRadioButton;
    }

    public JRadioButton getProductionRadioButton() {
        return productionRadioButton;
    }

    public void setProductionRadioButton(JRadioButton productionRadioButton) {
        this.productionRadioButton = productionRadioButton;
    }

    public JRadioButton getMaintenanceBreakRadioButton() {
        return maintenanceBreakRadioButton;
    }

    public void setMaintenanceBreakRadioButton(JRadioButton maintenanceBreakRadioButton) {
        this.maintenanceBreakRadioButton = maintenanceBreakRadioButton;
    }

    public JMenu getProjectMenu() {
        return projectMenu;
    }

    public void setProjectMenu(JMenu projectMenu) {
        this.projectMenu = projectMenu;
    }

    public JMenuItem getNewProjectMenuItem() {
        return newProjectMenuItem;
    }

    public void setNewProjectMenuItem(JMenuItem newProjectMenuItem) {
        this.newProjectMenuItem = newProjectMenuItem;
    }

    public JLabel getProjectLabel() {
        return projectLabel;
    }

    public void setProjectLabel(JLabel projectLabel) {
        this.projectLabel = projectLabel;
    }

    public JMenuItem getNewSecretKeyMenuItem() {
        return newSecretKeyMenuItem;
    }

    public void setNewSecretKeyMenuItem(JMenuItem newSecretKeyMenuItem) {
        this.newSecretKeyMenuItem = newSecretKeyMenuItem;
    }

    public JMenuItem getMiddlewareMenuItem() {
        return middlewareMenuItem;
    }

    public void setMiddlewareMenuItem(JMenuItem middlewareMenuItem) {
        this.middlewareMenuItem = middlewareMenuItem;
    }

    public JLabel getCommandSectionLabel() {
        return commandSectionLabel;
    }

    public void setCommandSectionLabel(JLabel commandSectionLabel) {
        this.commandSectionLabel = commandSectionLabel;
    }
}
