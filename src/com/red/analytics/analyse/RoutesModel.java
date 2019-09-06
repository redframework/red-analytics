package com.red.analytics.analyse;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class RoutesModel extends AbstractTableModel {
    private String[] columnNames = {"Path", "Url Parameters", "Action", "Method",
            "Middlewares"};

    private Object[][] data = {};

    private static File analyticsFile;
    private static FileInputStream analyticsFileInputStream;
    private static JSONObject analytics;
    private boolean modelStatus;
    public boolean error;

    public RoutesModel() throws IOException {

        try {
            analyticsFile = new File("storage/Analytics/Routes.json");
        } catch (Exception e){
            error = true;
            return;
        }

        analyticsFileInputStream = new FileInputStream(analyticsFile);

        JSONTokener jsonTokener = new JSONTokener(analyticsFileInputStream);


        analytics = new JSONObject(jsonTokener);
        analyticsFileInputStream.close();


        if(analytics.length() == 0){
            modelStatus = false;
            return;
        } else {
            modelStatus = true;
        }

        Iterator<String> keys = analytics.keys();

        data = new Object[analytics.length()][5];


        int counter = 0;
        while(keys.hasNext()) {
            String routePath = keys.next();
            if (analytics.get(routePath) instanceof JSONObject) {
                data[counter][0] = routePath;

                try {
                    data[counter][1] = analytics.getJSONObject(routePath).getJSONArray("parameters").length();
                } catch (Exception e){
                    data[counter][1] = 0;
                }


                Object action = analytics.getJSONObject(routePath).get("action");
                if (action.toString().equals("{}")){
                    data[counter][2] = "Closure Object";
                } else {
                    data[counter][2] = action;
                }

                data[counter][3] = analytics.getJSONObject(routePath).getJSONArray("method").join(" , ");
                try {
                    data[counter][4] = analytics.getJSONObject(routePath).getJSONArray("middlewares").join(" , ");
                } catch (Exception e){
                    data[counter][4] = "None";
                }
            }
                counter++;
        }

    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public static File getAnalyticsFile() {
        return analyticsFile;
    }

    public static void setAnalyticsFile(File analyticsFile) {
        RoutesModel.analyticsFile = analyticsFile;
    }

    public static FileInputStream getAnalyticsFileInputStream() {
        return analyticsFileInputStream;
    }

    public static void setAnalyticsFileInputStream(FileInputStream analyticsFileInputStream) {
        RoutesModel.analyticsFileInputStream = analyticsFileInputStream;
    }

    public static JSONObject getAnalytics() {
        return analytics;
    }

    public static void setAnalytics(JSONObject analytics) {
        RoutesModel.analytics = analytics;
    }

    public boolean isModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(boolean modelStatus) {
        this.modelStatus = modelStatus;
    }
}
