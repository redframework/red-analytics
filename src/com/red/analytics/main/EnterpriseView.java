package com.red.analytics.main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class EnterpriseView {

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
    private JMenu routeMenu;
    private JMenuItem addRouteMenuItem;
    private JMenuItem analyseRoutesMenuItem;
    private JMenu serverMenu;
    private JMenuItem runServerMenuItem;
    private JMenuItem stopServerMenuItem;
    private JMenuItem maintenanceModeMenuItem;
    private JMenuItem breakModeMenuItem;
    private JMenuItem productionModeMenuItem;
    private JPanel mainContainer;
    private InputStream logoInputStream;
    private JButton generateControllerButton;
    private JButton generateModelButton;
    private JButton generateViewButton;
    private JButton addRouteButton;
    private JButton analyseButton;
    private JButton runServerButton;
    private JButton stopServerButton;
    private JLabel serverStatusLabel;
    private ButtonGroup projectModeButtonGroup;
    private JRadioButton maintenanceRadioButton;
    private JRadioButton productionRadioButton;
    private JRadioButton maintenanceBreakRadioButton;
    private JLabel projectLabel;
    private JLabel routeSectionLabel;


    EnterpriseView(String projectName, int projectMode) {

        // Create window With JFrame and Set Configuration
        window = new JFrame("Red Analytics - " + projectName);
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


        middlewareMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateMiddlewareAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        generateMenu.add(controllerMenuItem);
        generateMenu.add(modelMenuItem);
        generateMenu.add(viewMenuItem);
        generateMenu.add(middlewareMenuItem);

        routeMenu = new JMenu("Route");

        addRouteMenuItem = new JMenuItem("Register Route");

        addRouteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.addRouteAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        analyseRoutesMenuItem = new JMenuItem("Analyse Routes");

        analyseRoutesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    MainController.analyseRouteAction();
            }
        });

        routeMenu.add(addRouteMenuItem);
        routeMenu.add(analyseRoutesMenuItem);

        serverMenu = new JMenu("Server");

        runServerMenuItem = new JMenuItem("Run Red Framework Development Server");

        runServerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.runServerAction();
            }
        });

        stopServerMenuItem = new JMenuItem("Stop Red Framework Development Server");

        stopServerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.stopServerAction();
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

        serverMenu.add(runServerMenuItem);
        serverMenu.add(stopServerMenuItem);
        serverMenu.add(maintenanceModeMenuItem);
        serverMenu.add(breakModeMenuItem);
        serverMenu.add(productionModeMenuItem);

        menuBar.add(projectMenu);
        menuBar.add(generateMenu);
        menuBar.add(routeMenu);
        menuBar.add(serverMenu);

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

        mainContainer.add(new JLabel("Development Server"), GBC);

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

        runServerButton = new JButton("Run");

        runServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    MainController.runServerAction();
            }
        });

        mainContainer.add(runServerButton, GBC);

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

        stopServerButton = new JButton("Stop");

        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.stopServerAction();
            }
        });

        mainContainer.add(stopServerButton, GBC);

        GBC.insets = new Insets(1, 0, 1, 0);
        GBC.gridx = 0;
        GBC.gridy = 4;

        generateViewButton = new JButton("View");

        generateViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.generateViewAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mainContainer.add(generateViewButton, GBC);


        GBC.insets = new Insets(1, 40, 1, 0);
        GBC.gridx = 1;
        GBC.gridy = 4;

        serverStatusLabel = new JLabel("Server Status: STOPPED");

        serverStatusLabel.setForeground(Color.RED);

        mainContainer.add(serverStatusLabel, GBC);


        GBC.insets = new Insets(7, 0, 7, 0);
        GBC.gridx = 0;
        GBC.gridy = 5;

        routeSectionLabel = new JLabel("Route Section");

        mainContainer.add(routeSectionLabel, GBC);


        GBC.insets = new Insets(1, 0, 1, 0);
        GBC.gridx = 0;
        GBC.gridy = 6;

        addRouteButton = new JButton("Register Route");

        addRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainController.addRouteAction();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mainContainer.add(addRouteButton, GBC);


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

        analyseButton = new JButton("Analyse Routes");

        analyseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainController.analyseRouteAction();
            }
        });

        mainContainer.add(analyseButton, GBC);

        GBC.insets = new Insets(1, 30, 1, 0);
        GBC.gridx = 1;
        GBC.gridy = 7;

        mainContainer.add(productionRadioButton, GBC);


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

    public JMenu getRouteMenu() {
        return routeMenu;
    }

    public void setRouteMenu(JMenu routeMenu) {
        this.routeMenu = routeMenu;
    }

    public JMenuItem getAddRouteMenuItem() {
        return addRouteMenuItem;
    }

    public void setAddRouteMenuItem(JMenuItem addRouteMenuItem) {
        this.addRouteMenuItem = addRouteMenuItem;
    }

    public JMenuItem getAnalyseRoutesMenuItem() {
        return analyseRoutesMenuItem;
    }

    public void setAnalyseRoutesMenuItem(JMenuItem analyseRoutesMenuItem) {
        this.analyseRoutesMenuItem = analyseRoutesMenuItem;
    }

    public JMenu getServerMenu() {
        return serverMenu;
    }

    public void setServerMenu(JMenu serverMenu) {
        this.serverMenu = serverMenu;
    }

    public JMenuItem getRunServerMenuItem() {
        return runServerMenuItem;
    }

    public void setRunServerMenuItem(JMenuItem runServerMenuItem) {
        this.runServerMenuItem = runServerMenuItem;
    }

    public JMenuItem getStopServerMenuItem() {
        return stopServerMenuItem;
    }

    public void setStopServerMenuItem(JMenuItem stopServerMenuItem) {
        this.stopServerMenuItem = stopServerMenuItem;
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

    public JButton getAddRouteButton() {
        return addRouteButton;
    }

    public void setAddRouteButton(JButton addRouteButton) {
        this.addRouteButton = addRouteButton;
    }

    public JButton getAnalyseButton() {
        return analyseButton;
    }

    public void setAnalyseButton(JButton analyseButton) {
        this.analyseButton = analyseButton;
    }

    public JButton getRunServerButton() {
        return runServerButton;
    }

    public void setRunServerButton(JButton runServerButton) {
        this.runServerButton = runServerButton;
    }

    public JButton getStopServerButton() {
        return stopServerButton;
    }

    public void setStopServerButton(JButton stopServerButton) {
        this.stopServerButton = stopServerButton;
    }

    public JLabel getServerStatusLabel() {
        return serverStatusLabel;
    }

    public void setServerStatusLabel(JLabel serverStatusLabel) {
        this.serverStatusLabel = serverStatusLabel;
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

    public JLabel getRouteSectionLabel() {
        return routeSectionLabel;
    }

    public void setRouteSectionLabel(JLabel routeSectionLabel) {
        this.routeSectionLabel = routeSectionLabel;
    }
}
