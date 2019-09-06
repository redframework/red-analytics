package com.red.analytics.project;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static com.red.analytics.project.ProjectController.getProjectView;

public class ProjectBuilderThread implements Runnable {
    @Override
    public void run() {
        // UnZip Tar.Gzip
        File outputDir = new File(ProjectController.getProjectView().getProjectPath().getText());

        File outputFile;

        if (getProjectView().getProjectSDKVersion().getSelectedIndex() == 0){
            outputFile = new File(outputDir, "RedFramework Enterprise SDK.tar");
        } else {
            outputFile = new File(outputDir, "Red Framework Console App Development Kit.tar");
        }

        GZIPInputStream in = null;
        try {
            if (getProjectView().getProjectSDKVersion().getSelectedIndex() == 0){
                in = new GZIPInputStream(ProjectController.class.getResourceAsStream("/com/red/analytics/resources/RedFramework/RedFramework Enterprise SDK.tar.gz"));
            } else {
                in = new GZIPInputStream(ProjectController.class.getResourceAsStream("/com/red/analytics/resources/RedFramework/Red Framework Console App Development Kit.tar.gz"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProjectController.getProjectView().getProgressBar().setValue(20);

        // UnTar it
        List<File> untaredFiles = new LinkedList<File>();

        File tarFile;

        if (getProjectView().getProjectSDKVersion().getSelectedIndex() == 0){
            tarFile = new File(getProjectView().getProjectPath().getText() + "/RedFramework Enterprise SDK.tar");
        } else {
            tarFile = new File(getProjectView().getProjectPath().getText() + "/Red Framework Console App Development Kit.tar");
        }

        InputStream is = null;
        try {
            is = new FileInputStream(tarFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TarArchiveInputStream debInputStream = null;
        try {
            debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
        TarArchiveEntry entry = null;
        while (true) {
            try {
                if (!((entry = (TarArchiveEntry)debInputStream.getNextEntry()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            File TAROutputFile = new File(outputDir, entry.getName());
            if (entry.isDirectory()) {
                if (!TAROutputFile.exists()) {
                    if (!TAROutputFile.mkdirs()) {
                        throw new IllegalStateException(String.format("Create %s Directory Occurs Error.", TAROutputFile.getAbsolutePath()));
                    }
                }
            } else {
                OutputStream outputFileStream = null;
                try {
                    outputFileStream = new FileOutputStream(TAROutputFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    IOUtils.copy(debInputStream, outputFileStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputFileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            untaredFiles.add(outputFile);
        }
        try {
            debInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tarFile.delete();

        File redFrameworkDir;

        if (getProjectView().getProjectSDKVersion().getSelectedIndex() == 0){
            redFrameworkDir = new File(getProjectView().getProjectPath().getText() + "/RedFramework Enterprise SDK");
        } else {
            redFrameworkDir = new File(getProjectView().getProjectPath().getText() + "/Red Framework Console App Development Kit");
        }


        File newPath = new File(getProjectView().getProjectPath().getText() + File.separator + getProjectView().getProjectName().getText());

        redFrameworkDir.renameTo(newPath);

        ProjectController.getProjectView().getProgressBar().setValue(50);

        File redAnalyticsJarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        Path redAnalyticsPath = Path.of(URLDecoder.decode(redAnalyticsJarFile.getPath(), StandardCharsets.UTF_8));


        try {
            Files.copy(redAnalyticsPath, Path.of(newPath.getPath() + File.separator + "Red Analytics.jar"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            e.printStackTrace();
        }

        ProjectController.getProjectView().getProgressBar().setValue(80);

        File environmentFile = new File(getProjectView().getProjectPath().getText() + "/" + getProjectView().getProjectName().getText() + "/Environment.json");

        FileInputStream environmentInputStream = null;
        try {
            environmentInputStream = new FileInputStream(environmentFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONTokener jsonTokener = new JSONTokener(environmentInputStream);


        JSONObject environment = new JSONObject(jsonTokener);

        try {
            environmentInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        environment.getJSONObject("PROJECT").put("Name", getProjectView().getProjectName().getText());
        environment.getJSONObject("PROJECT").put("Programmer", getProjectView().getProgrammer().getText());

        if (getProjectView().getSecretKeyCheckBox().isSelected()){
            String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            StringBuilder secretKey = new StringBuilder();

            while (secretKey.length() < 20){
                secretKey.append(characters.charAt((int) (Math.random() * characters.length())));
            }

            environment.getJSONObject("PROJECT").put("SecretKey", secretKey);
        }

        ProjectController.getProjectView().getProgressBar().setValue(90);

        FileWriter environmentFileWriter = null;
        try {
            environmentFileWriter = new FileWriter(environmentFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            environmentFileWriter.write(environment.toString(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            environmentFileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            environmentFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProjectController.getProjectView().getProgressBar().setValue(100);

        JOptionPane.showMessageDialog(ProjectController.getProjectView().getProjectWindow(), "Project '" + ProjectController.getProjectView().getProjectName().getText() + "' Created Successfully at '" + ProjectController.getProjectView().getProjectPath().getText() + "'\nThanks for Choosing Red Framework", "Red Analytics - New Project Created", JOptionPane.INFORMATION_MESSAGE);

        ProjectController.getProjectView().getProjectWindow().setVisible(false);

        getProjectView().getProjectWindow().dispose();
    }
}
