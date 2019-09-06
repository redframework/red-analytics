package com.red.analytics.analyse;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class CommandsController {
    private static CommandsView commandsView;
    private static String commandPathInput = null;
    private static String commandActionInput = null;



    public CommandsController() {
        commandsView = new CommandsView();
    }

    public void analyse() {

        int createViewStatus = commandsView.showAnalyseView();


        if (createViewStatus == -1){
            JOptionPane.showMessageDialog(commandsView.getAnalyseWindow(), "Analytics File Not Found ! \nRun Your Web Application in Production Mode Once", "Red Analytics - Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!commandsView.isModelStatus() && createViewStatus == 0){
            JOptionPane.showMessageDialog(commandsView.getAnalyseWindow(), "Analytics File is Empty ! \nRun Your Web Application in Production Mode Once", "Red Analytics - Warning", JOptionPane.WARNING_MESSAGE);
        }


    }

    public static void addCommandAction() throws IOException, URISyntaxException {


        JPanel inputDialog = new JPanel();

        inputDialog.setBounds(new Rectangle(100, 120));

        inputDialog.setLayout(new BoxLayout(inputDialog, BoxLayout.Y_AXIS));

        JTextField commandPathField = new JTextField(6);
        JTextField commandActionField = new JTextField(6);

        if (commandPathInput != null){
            commandPathField.setText(commandPathInput);
        }

        if (commandActionInput != null){
            commandActionField.setText(commandActionInput);
        }





        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Command Path (with Parameters) :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(commandPathField);
        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Command Action :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(commandActionField);
        inputDialog.add(spaceGenerator(0, 15));


        int result = JOptionPane.showConfirmDialog(commandsView.getAnalyseWindow(), inputDialog,
                "Register Command - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION){
            if (commandPathField.getText().length() == 0){
                JOptionPane.showMessageDialog(commandsView.getAnalyseWindow(), "Command Path Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                commandPathInput = commandPathField.getText();
                commandActionInput = commandActionField.getText();
                addCommandAction();
                return;
            } else if (commandActionField.getText().length() == 0){
                JOptionPane.showMessageDialog(commandsView.getAnalyseWindow(), "Command Action Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                commandPathInput = commandPathField.getText();
                commandActionInput = commandActionField.getText();
                addCommandAction();
                return;
            }



            JSONObject newCommandConfiguration = new JSONObject();

            String commandPath = commandPathField.getText();

            String commandPathWithParameters = commandPathField.getText();


            String[] commandParameters = commandPathWithParameters.split(" ");

            commandPath = commandParameters[0];

            int parametersCount;

            if (commandParameters.length > 1){
                commandParameters = Arrays.copyOfRange(commandParameters, 1, commandParameters.length);
                parametersCount = commandParameters.length;
            } else {
                parametersCount = 0;
            }



            if (parametersCount > 0){
                newCommandConfiguration.put("parameters", new JSONArray(commandParameters));
            }




            newCommandConfiguration.put("action", commandActionField.getText());


            new CommandsModel();


            CommandsModel.getAnalytics().put(commandPath, newCommandConfiguration);

            FileWriter commandsFileWriter = new FileWriter(CommandsModel.getAnalyticsFile().getPath());

            commandsFileWriter.write(CommandsModel.getAnalytics().toString(1));
            commandsFileWriter.flush();
            commandsFileWriter.close();

            File commandsPHPFile = new File(CommandsController.class.getResource("/commands/commands.php").toURI());

            FileWriter commandsPHPFileWriter = new FileWriter(commandsPHPFile.getPath(), true);


            commandsPHPFileWriter.write("\nCommander::register(\"" + commandPathField.getText() + "\", \"" + commandActionField.getText() + "\");\n");


            commandsPHPFileWriter.flush();
            commandsPHPFileWriter.close();

            commandPathInput = null;

            commandActionInput = null;


            JOptionPane.showMessageDialog(commandsView.getAnalyseWindow(), "Command has Been Registered to Your Application", "Register Command - Done", JOptionPane.INFORMATION_MESSAGE);

        } else if (result == JOptionPane.CANCEL_OPTION){

            commandPathInput = null;

            commandActionInput = null;

            return;
        }

    }

    public static void refreshAnalyseAction() throws IOException {

        CommandsModel commandsModel = new CommandsModel();

        commandsView.setAnalyseTableModel(commandsModel);

        if (!commandsView.isModelStatus()){
            JOptionPane.showMessageDialog(commandsView.getAnalyseWindow(), "Analytics File is Empty ! \nRun Your Web Application in Production Mode Once", "Red Analytics - Error", JOptionPane.ERROR_MESSAGE);
        }

        commandsView.getAnalyseTable().setModel(commandsView.getAnalyseTableModel());

    }

    public static Component spaceGenerator(int width, int height){
        return Box.createRigidArea(new Dimension(width, height));
    }
}
