package com.red.analytics.analyse;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoutesController {
    private static RoutesView routesView;
    private static String routePathInput = null;
    private static String routeActionInput = null;
    private static String routeMethodInput = null;
    private static String middlewareInput = null;



    public RoutesController() {
        routesView = new RoutesView();
    }

    public void analyse() {
       int createViewStatus = routesView.showAnalyseView();

       if (createViewStatus == -1){
           JOptionPane.showMessageDialog(routesView.getAnalyseWindow(), "Analytics File Not Found ! \nRun Your Web Application in Production Mode Once", "Red Analytics - Error", JOptionPane.ERROR_MESSAGE);
       }

       if (!routesView.isModelStatus() && createViewStatus == 0){
           JOptionPane.showMessageDialog(routesView.getAnalyseWindow(), "Analytics File is Empty ! \nRun Your Web Application in Production Mode Once", "Red Analytics - Warning", JOptionPane.WARNING_MESSAGE);
       }

    }

    public static void addRouteAction() throws IOException, URISyntaxException {


        JPanel inputDialog = new JPanel();

        inputDialog.setBounds(new Rectangle(100, 120));

        inputDialog.setLayout(new BoxLayout(inputDialog, BoxLayout.Y_AXIS));

        JTextField routePathField = new JTextField(6);
        JTextField routeActionField = new JTextField(6);
        JTextField routeMethodField = new JTextField(6);
        JTextField middlewaresField = new JTextField(6);

        if (routePathInput != null){
            routePathField.setText(routePathInput);
        }

        if (routeActionInput != null){
            routeActionField.setText(routeActionInput);
        }

        if (routeMethodInput != null){
            routeMethodField.setText(routeMethodInput);
        }

        if (middlewareInput != null){
            middlewaresField.setText(middlewareInput);
        }




        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Route Path (with Parameters) :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(routePathField);
        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Route Action :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(routeActionField);
        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Method :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(routeMethodField);
        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Middlewares :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(middlewaresField);
        inputDialog.add(spaceGenerator(0, 15));


        int result = JOptionPane.showConfirmDialog(routesView.getAnalyseWindow(), inputDialog,
                "Register Route - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION){
            if (routePathField.getText().length() == 0){
                JOptionPane.showMessageDialog(routesView.getAnalyseWindow(), "Route Path Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                routePathInput = routePathField.getText();
                routeActionInput = routeActionField.getText();
                routeMethodInput = routeMethodField.getText();
                middlewareInput = middlewaresField.getText();
                addRouteAction();
                return;
            } else if (routeActionField.getText().length() == 0){
                JOptionPane.showMessageDialog(routesView.getAnalyseWindow(), "Route Action Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                routePathInput = routePathField.getText();
                routeActionInput = routeActionField.getText();
                routeMethodInput = routeMethodField.getText();
                middlewareInput = middlewaresField.getText();
                addRouteAction();
                return;
            } else if (routeMethodField.getText().length() == 0){
                JOptionPane.showMessageDialog(routesView.getAnalyseWindow(), "Route Method Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                routePathInput = routePathField.getText();
                routeActionInput = routeActionField.getText();
                routeMethodInput = routeMethodField.getText();
                middlewareInput = middlewaresField.getText();
                addRouteAction();
                return;
            }



            JSONObject newRouteConfiguration = new JSONObject();

            String routeAction = routePathField.getText();

            String routePathWithParameters = routePathField.getText();

            List<String> routeParameters = new ArrayList<String>();

            int parametersCount = 0;
            Pattern pattern = Pattern.compile("[{][a-zA-Z0-9]+[}]");
            Matcher matcher = pattern.matcher(routePathWithParameters);

            while (matcher.find()){
                String param = matcher.group();
                param = param.substring(1, param.length() - 1);
                routeParameters.add(param);
                parametersCount++;
            }

            if (parametersCount > 0){
                routeAction = matcher.replaceAll("");
                newRouteConfiguration.put("parameters", new JSONArray(routeParameters.toArray()));
            }

            String method = routeMethodField.getText().toUpperCase();

            method = method.replaceAll("\\s", "");

            String[] methodasArray = method.split(",");

            String middlewares = middlewaresField.getText();

            middlewares = middlewares.replaceAll("\\s", "");

            String[] middlewaresAsArray = middlewares.split(",");

            newRouteConfiguration.put("action", routeActionField.getText());

            newRouteConfiguration.put("method", new JSONArray(methodasArray));

            if (middlewares.length() < 1) {
                newRouteConfiguration.put("middlewares", "none");
            } else {
                newRouteConfiguration.put("middlewares", new JSONArray(middlewaresAsArray));
            }

            new RoutesModel();


            RoutesModel.getAnalytics().put(routeAction, newRouteConfiguration);

            FileWriter routesFileWriter = new FileWriter(RoutesModel.getAnalyticsFile().getPath());

            routesFileWriter.write(RoutesModel.getAnalytics().toString(1));
            routesFileWriter.flush();
            routesFileWriter.close();

            File routesPHPFile = new File(RoutesController.class.getResource("/routes/routes.php").toURI());

            FileWriter routesPHPFileWriter = new FileWriter(routesPHPFile.getPath(), true);

            if (middlewares.length() < 1){
                routesPHPFileWriter.write("\nRouter::register(\"" + routePathField.getText() + "\", \"" + method + "\", \"" + routeActionField.getText() + "\");\n");
            } else {
                routesPHPFileWriter.write("\nRouter::register(\"" + routePathField.getText() + "\", \"" + method + "\", \"" + routeActionField.getText() + "\", " + new JSONArray(middlewaresAsArray).toString() + ");\n");
            }

            routesPHPFileWriter.flush();
            routesPHPFileWriter.close();

            routePathInput = null;

            routeActionInput = null;

            routeMethodInput = null;

            middlewareInput = null;


            JOptionPane.showMessageDialog(routesView.getAnalyseWindow(), "Route has Been Registered to Your Application", "Register Route - Done", JOptionPane.INFORMATION_MESSAGE);

        } else if (result == JOptionPane.CANCEL_OPTION){

            routePathInput = null;

            routeActionInput = null;

            routeMethodInput = null;

            middlewareInput = null;

            return;
        }

    }

    public static void refreshAnalyseAction() throws IOException {

        RoutesModel routesModel = new RoutesModel();

        routesView.setAnalyseTableModel(routesModel);

        if (!routesView.isModelStatus()){
            JOptionPane.showMessageDialog(routesView.getAnalyseWindow(), "Analytics File is Empty ! \nRun Your Web Application in Production Mode Once", "Red Analytics - Error", JOptionPane.ERROR_MESSAGE);
        }

        routesView.getAnalyseTable().setModel(routesView.getAnalyseTableModel());

    }

    public static Component spaceGenerator(int width, int height){
        return Box.createRigidArea(new Dimension(width, height));
    }
}
