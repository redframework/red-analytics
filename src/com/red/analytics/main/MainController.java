package com.red.analytics.main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.red.analytics.analyse.*;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;
import com.red.analytics.project.ProjectController;

public class MainController {

    public static Process php_service = null;
    private static EnterpriseView enterpriseView;
    private static ConsoleView consoleView;
    private static JSONObject environment;
    private static String projectName;
    private static String projectSDK;
    private static File environmentFile;
    private static String routePathInput = null;
    private static String routeActionInput = null;
    private static String routeMethodInput = null;
    private static String middlewareInput = null;
    private static ProjectController projectController;
    private static JFrame mainWindow;

    public static final String enterpriseSDK = "Red Framework Enterprise Application";
    public static final String consoleSDK = "Red Framework Console Application";

    private static String commandPathInput = null;
    private static String commandActionInput = null;

    public MainController() throws IOException {

        try {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            File jarFile = new File(path);
            String environmentPath = jarFile.getParent() + File.separator + "Environment.json";
            environmentPath = URLDecoder.decode(environmentPath, StandardCharsets.UTF_8);
            environmentFile = new File(environmentPath);
        } catch (Exception e){
            int result = JOptionPane.showConfirmDialog(null, "Would You Like to Create Your New Project Based On Red Framework ?", "Red Analytics - No Project Module Found", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (result == JOptionPane.OK_OPTION){
                projectController = new ProjectController();
                ProjectController.getProjectView().getProjectWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            return;
        }

        if (!environmentFile.exists()){
            int result = JOptionPane.showConfirmDialog(null, "Would You Like to Create Your New Project Based On Red Framework ?", "Red Analytics - No Project Module Found", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (result == JOptionPane.OK_OPTION){
                projectController = new ProjectController();
                ProjectController.getProjectView().getProjectWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            return;
        }

        FileInputStream environmentInputStream = new FileInputStream(environmentFile);

        JSONTokener jsonTokener = new JSONTokener(environmentInputStream);

        environment = new JSONObject(jsonTokener);

        environmentInputStream.close();

        String projectState = environment.getJSONObject("PROJECT").getString("State");

        projectState = projectState.toLowerCase();

        projectName = environment.getJSONObject("PROJECT").getString("Name");

        int projectMode;

        switch (projectState) {
            case "maintenance":
                projectMode = 0;
                break;
            case "production":
                projectMode = 1;
                break;
            case "break":
                projectMode = 2;
                break;
            default:
                projectMode = -1;
                break;
        }

        projectSDK = environment.getJSONObject("PROJECT").getString("SDK");

        if (projectSDK.equals(enterpriseSDK)){
            enterpriseView = new EnterpriseView(projectName, projectMode);
            mainWindow = enterpriseView.getWindow();
        } else if (projectSDK.equals(consoleSDK)){
            consoleView = new ConsoleView(projectName, projectMode);
            mainWindow = consoleView.getWindow();
        } else {
            JOptionPane.showMessageDialog(null, "SDK is Not Valid", "Framework SDK", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void runServerAction() {

        if (php_service != null){
            JOptionPane.showMessageDialog(mainWindow, "Development Server is Already Running at http://localhost:927", "Development Server is Running", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            php_service = Runtime.getRuntime().exec("php -S localhost:927 Server.php");

            JLabel serverStatusLabel = enterpriseView.getServerStatusLabel();
            serverStatusLabel.setText("Server Status: RUNNING");
            serverStatusLabel.setForeground(Color.green);

            JOptionPane.showMessageDialog(enterpriseView.getWindow(), "Development Server is Running at http://localhost:927", "Development Server is Running", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(enterpriseView.getWindow(), "Error ! Make Sure You Have installed PHP on Your Computer.", "Error on Running Development Server", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void stopServerAction(){
        if (php_service == null){
            JOptionPane.showMessageDialog(mainWindow, "Development Server is Already Stopped", "Development Server is Stopped", JOptionPane.WARNING_MESSAGE);
        } else {
            php_service.destroy();
            php_service = null;
            JLabel serverStatusLabel = enterpriseView.getServerStatusLabel();
            serverStatusLabel.setText("Server Status: STOPPED");
            serverStatusLabel.setForeground(Color.RED);
        }
    }

    public static void runConsoleAppAction() {


        if (php_service != null){
            JOptionPane.showMessageDialog(mainWindow, "Application is Already Running", "Application is Running", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File currentPath = new File(MainController.class.getProtectionDomain().getCodeSource().getLocation().getPath());


        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("cmd.exe"+ " /c "+ currentPath.getParent().substring(0, 1).toUpperCase() + ":"+ " cd \""+ URLDecoder.decode(currentPath.getParent(), StandardCharsets.UTF_8)+ "\" & start cmd.exe /k"+ " \"php application\"");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainWindow, "Error ! Make Sure You Have installed PHP on Your Computer.", "Error on Running Development Server", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void changeModeAction(int mode) throws IOException {


        switch (mode){
            case 0:
                environment.getJSONObject("PROJECT").put("State", "Maintenance");
                JOptionPane.showMessageDialog(mainWindow, "Project State Works Well if you Use Environment.json as Your Config \nNew App Mode: Maintenance", "Important Notice", JOptionPane.WARNING_MESSAGE);
                break;

            case 1:
                environment.getJSONObject("PROJECT").put("State", "Production");
                JOptionPane.showMessageDialog(mainWindow, "Project State Works Well if you Use Environment.json as Your Config \nNew App Mode: Production", "Important Notice", JOptionPane.WARNING_MESSAGE);
                break;

            case 2:
                environment.getJSONObject("PROJECT").put("State", "Break");
                JOptionPane.showMessageDialog(mainWindow, "Project State Works Well if you Use Environment.json as Your Config \nNew App Mode: Maintenance Break", "Important Notice", JOptionPane.WARNING_MESSAGE);
                break;
        }

        FileWriter environmentFileWriter = new FileWriter(environmentFile.getPath());

        environmentFileWriter.write(environment.toString(1));
        environmentFileWriter.flush();
        environmentFileWriter.close();

    }

    public static void addRouteAction() throws IOException {


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


        int result = JOptionPane.showConfirmDialog(mainWindow, inputDialog,
                "Register Route - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION){
            if (routePathField.getText().length() == 0){
                JOptionPane.showMessageDialog(mainWindow, "Route Path Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                routePathInput = routePathField.getText();
                routeActionInput = routeActionField.getText();
                routeMethodInput = routeMethodField.getText();
                middlewareInput = middlewaresField.getText();
                addRouteAction();
                return;
            } else if (routeActionField.getText().length() == 0){
                JOptionPane.showMessageDialog(mainWindow, "Route Action Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                routePathInput = routePathField.getText();
                routeActionInput = routeActionField.getText();
                routeMethodInput = routeMethodField.getText();
                middlewareInput = middlewaresField.getText();
                addRouteAction();
                return;
            } else if (routeMethodField.getText().length() == 0){
                JOptionPane.showMessageDialog(mainWindow, "Route Method Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
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

            List<String> routeParameters = new ArrayList<>();

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


            File routesPHPFile = new File("routes/routes.php");

            FileWriter routesPHPFileWriter = new FileWriter(routesPHPFile.getPath(), true);

            if (middlewares.length() < 1){
                routesPHPFileWriter.write("\nRouter::register(\"" + routePathField.getText() + "\", \"" + method + "\", \"" + routeActionField.getText() + "\");\n");
            } else {
                routesPHPFileWriter.write("\nRouter::register(\"" + routePathField.getText() + "\", \"" + method + "\", \"" + routeActionField.getText() + "\", " + new JSONArray(middlewaresAsArray).toString() + ");\n");
            }

            routesPHPFileWriter.flush();
            routesPHPFileWriter.close();

            new RoutesView();

            routePathInput = null;

            routeActionInput = null;

            routeMethodInput = null;

            middlewareInput = null;

            RoutesModel routesModel = new RoutesModel();

            if (routesModel.error){
                JOptionPane.showMessageDialog(mainWindow, "Analytics File Not Found ! \nRun Your Web Application in Production Mode Once", "Red Analytics - Error", JOptionPane.ERROR_MESSAGE);
            } else {
                RoutesModel.getAnalytics().put(routeAction, newRouteConfiguration);

                FileWriter routesFileWriter = new FileWriter(RoutesModel.getAnalyticsFile().getPath());

                routesFileWriter.write(RoutesModel.getAnalytics().toString(1));
                routesFileWriter.flush();
                routesFileWriter.close();

                JOptionPane.showMessageDialog(mainWindow, "Route has Been Registered to Your Application", "Register Route - Done", JOptionPane.INFORMATION_MESSAGE);

            }


        } else if (result == JOptionPane.CANCEL_OPTION){

            routePathInput = null;

            routeActionInput = null;

            routeMethodInput = null;

            middlewareInput = null;

            return;
        }

    }

    public static void addCommandAction() throws IOException {


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


        int result = JOptionPane.showConfirmDialog(mainWindow, inputDialog,
                "Register Command - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION){
            if (commandPathField.getText().length() == 0){
                JOptionPane.showMessageDialog(mainWindow, "Command Path Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                commandPathInput = commandPathField.getText();
                commandActionInput = commandActionField.getText();
                addCommandAction();
                return;
            } else if (commandActionField.getText().length() == 0){
                JOptionPane.showMessageDialog(mainWindow, "Command Action Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
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



            File commandsPHPFile = new File("commands/commands.php");

            FileWriter commandsPHPFileWriter = new FileWriter(commandsPHPFile.getPath(), true);


            commandsPHPFileWriter.write("\nCommander::register(\"" + commandPathField.getText() + "\", \"" + commandActionField.getText() + "\");\n");


            commandsPHPFileWriter.flush();
            commandsPHPFileWriter.close();

            commandPathInput = null;

            commandActionInput = null;


            JOptionPane.showMessageDialog(mainWindow, "Command has Been Registered to Your Application", "Register Command - Done", JOptionPane.INFORMATION_MESSAGE);

        } else if (result == JOptionPane.CANCEL_OPTION){

            commandPathInput = null;

            commandActionInput = null;

            return;
        }

    }

    public static void analyseRouteAction(){
        RoutesController routesController = new RoutesController();
        routesController.analyse();
    }

    public static Component spaceGenerator(int width, int height){
        return Box.createRigidArea(new Dimension(width, height));
    }

    public static void generateNewSecretKey() throws IOException {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder secretKey = new StringBuilder();

        while (secretKey.length() < 20){
            secretKey.append(characters.charAt((int) (Math.random() * characters.length())));
        }

        environment.getJSONObject("PROJECT").put("SecretKey", secretKey);

        FileWriter environmentFileWriter = new FileWriter(environmentFile.getPath());

        environmentFileWriter.write(environment.toString(1));
        environmentFileWriter.flush();
        environmentFileWriter.close();


        JOptionPane.showMessageDialog(mainWindow, "New Secret Key for " + projectName + " has Been Generated", "New Secret Key Generated", JOptionPane.INFORMATION_MESSAGE);

    }

    public static void generateControllerAction() throws IOException {

        JPanel inputDialog = new JPanel();

        inputDialog.setBounds(new Rectangle(100, 120));

        inputDialog.setLayout(new BoxLayout(inputDialog, BoxLayout.Y_AXIS));

        JTextField controllerName = new JTextField(6);
        JCheckBox generateModelCheckBox = new JCheckBox("Generate Model");


        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Controller Name (without 'Controller' Suffix) :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(controllerName);
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(generateModelCheckBox);


        int result = JOptionPane.showConfirmDialog(mainWindow, inputDialog,
                "Generate Controller - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (controllerName.getText().length() == 0) {
                JOptionPane.showMessageDialog(mainWindow, "Controller Name Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                generateControllerAction();
                return;
            }

            String controllerNameString = controllerName.getText();
            controllerNameString = controllerNameString.substring(0,1).toUpperCase() + controllerNameString.substring(1);

            LocalDateTime localDateTime = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


            File newControllerFile = new File("app" + File.separator + "Controllers" + File.separator + controllerNameString + "Controller.php");

            if (!newControllerFile.exists()){
                newControllerFile.createNewFile();
            }

            FileWriter newControllerFileWriter = new FileWriter(newControllerFile.getAbsolutePath(), false);


            if (generateModelCheckBox.isSelected()){

                newControllerFileWriter.write("<?php\n" +
                        "/** Red Framework Controller\n" +
                        " * Generated By Red Analytics\n" +
                        " * \n" +
                        " * Date: " + localDateTime.format(dateFormatter) + "\n" +
                        " * Time: " + localDateTime.format(timeFormatter) + "\n" +
                        " * @author " + environment.getJSONObject("PROJECT").get("Programmer") + "\n" +
                        " */\n" +
                        "\n" +
                        "namespace App\\Controllers;\n" +
                        "\n" +
                        "\n" +
                        "use Red\\Base\\Controller;\n" +
                        "use App\\Models\\" + controllerNameString + "Model;\n" +
                        "\n" +
                        "class " + controllerNameString + "Controller extends Controller\n" +
                        "{\n" +
                        "\n    /**\n" +
                        "     * @var " + controllerNameString + "Model $this->model\n" +
                        "     */" +
                        "\n    protected $model;" +
                        "\n" +
                        "}");

                newControllerFileWriter.flush();
                newControllerFileWriter.close();

                generateModelAction(controllerNameString);
                JOptionPane.showMessageDialog(mainWindow, "Controller and Model '" + controllerNameString + "' Generated Successfully", "Generate Controller - Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
                newControllerFileWriter.write("<?php\n" +
                        "/** Red Framework Controller\n" +
                        " * Generated By Red Analytics\n" +
                        " * \n" +
                        " * Date: " + localDateTime.format(dateFormatter) + "\n" +
                        " * Time: " + localDateTime.format(timeFormatter) + "\n" +
                        " * @author " + environment.getJSONObject("PROJECT").get("Programmer") + "\n" +
                        " */\n" +
                        "\n" +
                        "namespace App\\Controllers;\n" +
                        "\n" +
                        "\n" +
                        "use Red\\Base\\Controller;\n" +
                        "\n" +
                        "class " + controllerNameString + "Controller extends Controller\n" +
                        "{\n" +
                        "\n" +
                        "}");

                newControllerFileWriter.flush();
                newControllerFileWriter.close();
                JOptionPane.showMessageDialog(mainWindow, "Controller '" + controllerNameString + "' Generated Successfully", "Generate Controller - Done", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    public static void generateModelAction() throws IOException {

        JPanel inputDialog = new JPanel();

        inputDialog.setBounds(new Rectangle(100, 120));

        inputDialog.setLayout(new BoxLayout(inputDialog, BoxLayout.Y_AXIS));

        JTextField modelName = new JTextField(6);


        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Model Name (without 'Model' Suffix) :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(modelName);
        inputDialog.add(spaceGenerator(0, 5));

        int result = JOptionPane.showConfirmDialog(mainWindow, inputDialog,
                "Generate Model - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (modelName.getText().length() == 0) {
                JOptionPane.showMessageDialog(mainWindow, "Model Name Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                generateModelAction();
                return;
            }

            String modelNameString = modelName.getText();
            modelNameString = modelNameString.substring(0,1).toUpperCase() + modelNameString.substring(1);

            LocalDateTime localDateTime = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            File newModelFile = new File("app" + File.separator + "Models" + File.separator + modelNameString + "Model.php");

            if (!newModelFile.exists()){
                newModelFile.createNewFile();
            }

            FileWriter newModelFileWriter = new FileWriter(newModelFile.getAbsolutePath(), false);

            newModelFileWriter.write("<?php\n" +
                    "/** Red Framework Model\n" +
                    " * Generated By Red Analytics\n" +
                    " * \n" +
                    " * Date: " + localDateTime.format(dateFormatter) + "\n" +
                    " * Time: " + localDateTime.format(timeFormatter) + "\n" +
                    " * @author " + environment.getJSONObject("PROJECT").get("Programmer") + "\n" +
                    " */\n" +
                    "\n" +
                    "namespace App\\Models;\n" +
                    "\n" +
                    "\n" +
                    "use Red\\Base\\Model;\n" +
                    "\n" +
                    "class " + modelNameString + "Model extends Model\n" +
                    "{\n" +
                    "\n" +
                    "}");

            newModelFileWriter.flush();
            newModelFileWriter.close();

            JOptionPane.showMessageDialog(mainWindow, "Model '" + modelNameString + "' Generated Successfully", "Generate Model - Done", JOptionPane.INFORMATION_MESSAGE);


        }
    }

    public static void generateModelAction(String modelNameString) throws IOException {

        LocalDateTime localDateTime = LocalDateTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        File newModelFile = new File("app" + File.separator + "Models" + File.separator + modelNameString + "Model.php");

        if (!newModelFile.exists()){
            newModelFile.createNewFile();
        }

        FileWriter newModelFileWriter = new FileWriter(newModelFile.getAbsolutePath(), false);

        newModelFileWriter.write("<?php\n" +
                "/** Red Framework Model\n" +
                " * Generated By Red Analytics\n" +
                " * \n" +
                " * Date: " + localDateTime.format(dateFormatter) + "\n" +
                " * Time: " + localDateTime.format(timeFormatter) + "\n" +
                " * @author " + environment.getJSONObject("PROJECT").get("Programmer") + "\n" +
                " */\n" +
                "\n" +
                "namespace App\\Models;\n" +
                "\n" +
                "\n" +
                "use Red\\Base\\Model;\n" +
                "\n" +
                "class " + modelNameString + "Model extends Model\n" +
                "{\n" +
                "\n" +
                "}");

        newModelFileWriter.flush();
        newModelFileWriter.close();

    }

    public static void generateViewAction() throws IOException {

        JPanel inputDialog = new JPanel();

        inputDialog.setBounds(new Rectangle(100, 120));

        inputDialog.setLayout(new BoxLayout(inputDialog, BoxLayout.Y_AXIS));

        JTextField viewName = new JTextField(6);
        JTextField viewDirectory = new JTextField(6);


        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("View Name :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(viewName);
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(new JLabel("View Directory (Leave it Blank If it is EnterpriseView) :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(viewDirectory);
        inputDialog.add(spaceGenerator(0, 5));


        int result = JOptionPane.showConfirmDialog(mainWindow, inputDialog,
                "Generate View - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (viewName.getText().length() == 0) {
                JOptionPane.showMessageDialog(mainWindow, "View Name Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                generateViewAction();
                return;
            }

            String viewNameString = viewName.getText();
            viewNameString = viewNameString.substring(0,1).toUpperCase() + viewNameString.substring(1);

            LocalDateTime localDateTime = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            File newViewFile = null;

            if (viewDirectory.getText().length() > 0){
                File directory = new File("views" + File.separator + viewDirectory.getText());

                if (!directory.exists()) {
                    directory.mkdir();
                }

                newViewFile = new File("views" + File.separator + viewDirectory.getText() + File.separator + viewNameString + ".twig");
            } else {
                newViewFile = new File("views" + File.separator + viewNameString + ".twig");
            }

            if (!newViewFile.exists()){
                newViewFile.createNewFile();
            }

            FileWriter newViewFileWriter = new FileWriter(newViewFile.getAbsolutePath(), false);

            newViewFileWriter.write("{## Red Framework View\n" +
                    " # Generated By Red Analytics\n" +
                    " # \n" +
                    " # Date: " + localDateTime.format(dateFormatter) + "\n" +
                    " # Time: " + localDateTime.format(timeFormatter) + "\n" +
                    " # author: " + environment.getJSONObject("PROJECT").get("Programmer") + "\n" +
                    " #}\n" +
                    "\n");

            newViewFileWriter.flush();
            newViewFileWriter.close();


            if (viewDirectory.getText().length() > 0){
                JOptionPane.showMessageDialog(mainWindow, "View '" + viewNameString + "' Generated Successfully in Directory '" + viewDirectory.getText() + "'", "Generate View - Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainWindow, "View '" + viewNameString + "' Generated Successfully", "Generate View - Done", JOptionPane.INFORMATION_MESSAGE);
            }



        }
    }

    public static void generateMiddlewareAction() throws IOException {

        JPanel inputDialog = new JPanel();

        inputDialog.setBounds(new Rectangle(100, 120));

        inputDialog.setLayout(new BoxLayout(inputDialog, BoxLayout.Y_AXIS));

        JTextField middlewareName = new JTextField(6);


        inputDialog.add(spaceGenerator(0, 15));
        inputDialog.add(new JLabel("Middleware Name :"));
        inputDialog.add(spaceGenerator(0, 5));
        inputDialog.add(middlewareName);
        inputDialog.add(spaceGenerator(0, 5));


        int result = JOptionPane.showConfirmDialog(mainWindow, inputDialog,
                "Generate Middleware - Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (middlewareName.getText().length() == 0) {
                JOptionPane.showMessageDialog(mainWindow, "Middleware Name Should not Be Empty", "Please Fill All Necessary Fields !", JOptionPane.ERROR_MESSAGE);
                generateModelAction();
                return;
            }

            String middlewareNameString = middlewareName.getText();
            middlewareNameString = middlewareNameString.substring(0,1).toUpperCase() + middlewareNameString.substring(1);

            LocalDateTime localDateTime = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            File newMiddlewareFile = new File("app" + File.separator + "Middlewares" + File.separator + middlewareNameString + ".php");

            if (!newMiddlewareFile.exists()) {
                newMiddlewareFile.createNewFile();
            }

            FileWriter newMiddlewareFileWriter = new FileWriter(newMiddlewareFile.getAbsolutePath(), false);

            newMiddlewareFileWriter.write("<?php\n" +
                    "/** Red Framework Middleware\n" +
                    " * Generated By Red Analytics\n" +
                    " * \n" +
                    " * Date: " + localDateTime.format(dateFormatter) + "\n" +
                    " * Time: " + localDateTime.format(timeFormatter) + "\n" +
                    " * @author " + environment.getJSONObject("PROJECT").get("Programmer") + "\n" +
                    " */\n" +
                    "\n" +
                    "namespace App\\Middlewares;\n" +
                    "\n" +
                    "\n" +
                    "use Red\\Base\\Middleware;\n" +
                    "\n" +
                    "class " + middlewareNameString + " extends Middleware\n" +
                    "{\n" +
                    "    public function run(... $parameters)\n" +
                    "    {\n" +
                    "        // TODO: Implement run() method.\n" +
                    "    }\n" +
                    "}");

            newMiddlewareFileWriter.flush();
            newMiddlewareFileWriter.close();

            JOptionPane.showMessageDialog(mainWindow, "Middleware '" + middlewareNameString + "' Generated Successfully", "Generate Middleware - Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void mainFrameCloseEvent(){
        if (MainController.php_service != null) {


            int result = 0;
            if (!MainController.getEnvironment().getJSONObject("PROJECT").getString("SDK").equals("Red Framework Console Application")){
                result = JOptionPane.showConfirmDialog(mainWindow, "Development Server is Running, Do you Want to Stop and Exit ?", "Development Server is Running", JOptionPane.YES_NO_OPTION);
            }

            if (result == 0){
                MainController.php_service.destroy();
                System.exit(0);
            }

        } else {
            System.exit(0);
        }
    }

    public static void newProject() {
        projectController = new ProjectController();
    }

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        MainController.projectName = projectName;
    }

    public static JSONObject getEnvironment() {
        return environment;
    }

    public static void setEnvironment(JSONObject environment) {
        MainController.environment = environment;
    }
}
