package com.jfreechart.controllers;

/**
 *
 * @author mayanton
 */

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class JChartEditorController {
    private File loadedFileReference;
    private FileTime lastModifiedTime;

    public Label statusMessage;
    public ProgressBar progressBar;
    public Button loadChangesButton;
    public TextArea textArea;

    public void initialize() {
        loadChangesButton.setVisible(false);
    }

    public void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //only allow text files to be selected using chooser
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
        );

        //set initial directory somewhere user will recognise
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        //let user select file
        File fileToLoad = fileChooser.showOpenDialog(null);

        //if file has been chosen, load it using asynchronous method (define later)
        if (fileToLoad != null) {
         //   loadFileToTextArea(fileToLoad);
        }
    }
    public void loadChanges(ActionEvent event) {
       // loadFileToTextArea(loadedFileReference);
        loadChangesButton.setVisible(false);
    }
    public void saveFile(ActionEvent event) {
        try {
            FileWriter myWriter = new FileWriter(loadedFileReference);
            myWriter.write(textArea.getText());
            myWriter.close();
            lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(SEVERE, null, e);
        }
    }
}