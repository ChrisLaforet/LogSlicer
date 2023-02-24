package com.chrislaforetsoftware.logslicer.controller;

import com.chrislaforetsoftware.logslicer.LogSlicer;
import com.chrislaforetsoftware.logslicer.log.LogContent;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class MainViewController {

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TextArea logLineNumber;
    @FXML
    private TextArea logText;

    @FXML
    private Label statusMessage;

    @FXML
    private Label totalLines;

    @FXML
    private ProgressBar progressStatus;

    private LogContent logContent;

    public void handleScroll(ActionEvent actionEvent) {
    }

    public void handleOpenLog(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open log file");
        final File file = fileChooser.showOpenDialog(LogSlicer.getStage());
        if (file != null) {
            Task<LogContent> loadFileTask = loadLogContent(file);
            progressStatus.progressProperty().bind(loadFileTask.progressProperty());
            loadFileTask.run();
        }
    }

    private Task<LogContent> loadLogContent(File file) {
        final Task<LogContent> loaderTask = new Task<>() {
            @Override
            protected LogContent call() throws Exception {
                final BufferedReader reader = new BufferedReader(new FileReader(file));

                long lineCount;
                try (Stream<String> stream = Files.lines(file.toPath())) {
                    lineCount = stream.count();
                }

                final LogContent log = new LogContent(file);
                String line;
                long linesLoaded = 0;
                while ((line = reader.readLine()) != null) {
                    log.addLine(line);
                    updateProgress(++linesLoaded, lineCount);
                }
                return log;
            }
        };

        loaderTask.setOnSucceeded(workerStateEvent -> {
            try {
                logContent = loaderTask.get();
                statusMessage.setText("Loaded " + file.getName());
                logText.setText(logContent.getText());

                final ScrollBar scrollBar = (ScrollBar)logLineNumber.lookup(".scroll-bar:vertical");
                scrollBar.setDisable(true);
                scrollBar.setVisible(false);
                //logLineNumber.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n");

                totalLines.setText(logContent.lineCount() + " lines");
            } catch (InterruptedException | ExecutionException e) {
                logText.setText("Could not load file from: " + file.getAbsolutePath());
                statusMessage.setText("Error showing file");
            }
        });

        loaderTask.setOnFailed(workerStateEvent -> {
            logText.setText("Could not load file from: " + file.getAbsolutePath());
            statusMessage.setText("Failed to load file");
            totalLines.setText("");

            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading file " + file.getName());
            alert.setContentText("A problem occurred while loading file " + file.getName() +
                    "\r\nPath to file:" + file.getAbsolutePath());
            alert.showAndWait();
        });
        return loaderTask;
    }

    public void handleClose(ActionEvent actionEvent) {
        Platform.exit();
    }

//    public MainView() {
//        menuBar.prefWidthProperty().bind(mainContainer.prefWidthProperty());
//    }
}
