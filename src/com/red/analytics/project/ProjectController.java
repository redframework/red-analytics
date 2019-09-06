package com.red.analytics.project;

import javax.swing.*;
import java.awt.*;


public class ProjectController {
    private static ProjectView projectView;

    public ProjectController(){
        projectView = new ProjectView();
    }

    public static void createProjectAction() {

        if (projectView.getProjectName().getText().length() == 0){
            JOptionPane.showMessageDialog(projectView.getProjectWindow(), "Project Name Should Not be Empty", "Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (projectView.getProgrammer().getText().length() == 0){
            JOptionPane.showMessageDialog(projectView.getProjectWindow(), "Programmer Should Not be Empty", "Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (projectView.getProjectPath().getText().length() == 0){
            JOptionPane.showMessageDialog(projectView.getProjectWindow(), "Project Path Should Not be Empty", "Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
            return;
        }

        projectView.getGBC().insets = new Insets(30, 0, 20, 0);
        projectView.getGBC().gridx = 0;
        projectView.getGBC().gridy = 11;

        projectView.getProjectContainer().add(projectView.getProgressBar(), projectView.getGBC());

        projectView.getChoosePathButton().setEnabled(false);
        projectView.getSecretKeyCheckBox().setEnabled(false);
        projectView.getProjectName().setEnabled(false);
        projectView.getProgrammer().setEnabled(false);
        projectView.getProjectPath().setEnabled(false);
        projectView.getCreateButton().setEnabled(false);
        projectView.getCancelButton().setEnabled(false);
        projectView.getProjectSDKVersion().setEnabled(false);

        projectView.getProjectWindow().setContentPane(projectView.getProjectContainer());

        Thread projectBuilderThread = new Thread(new ProjectBuilderThread());
        projectBuilderThread.start();



    }

    public static ProjectView getProjectView() {
        return projectView;
    }

    public static void setProjectView(ProjectView projectView) {
        ProjectController.projectView = projectView;
    }
}
