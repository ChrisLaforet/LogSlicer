package com.chrislaforetsoftware.logslicer.controller;

import com.chrislaforetsoftware.logslicer.LogSlicer;
import com.chrislaforetsoftware.logslicer.log.LogContent;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class MainViewController {

    @FXML
    private StackPane anchorPane;

    @FXML
    private Label statusMessage;

    @FXML
    private Label totalLines;

    @FXML
    private ProgressBar progressStatus;

    private LogContent logContent;

    private CodeArea codeArea;

    public void initialize() {
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        // codeArea.setContextMenu( new DefaultContextMenu() );

        final VirtualizedScrollPane virtualizedScrollPane = new VirtualizedScrollPane<>(codeArea);
        virtualizedScrollPane.autosize();
//        virtualizedScrollPane.setMaxHeight(Double.MAX_VALUE);
//        virtualizedScrollPane.setMaxWidth(Double.MAX_VALUE);
        anchorPane.getChildren().add(virtualizedScrollPane);

//        new VirtualizedScrollPane<>(codeArea);
//        scene.getStylesheets().add(JavaKeywordsAsyncDemo.class.getResource("java-keywords.css").toExternalForm());
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Java Keywords Demo");
//        primaryStage.show();
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
                codeArea.clear();
                codeArea.replaceText(0, 0, logContent.getText());
                totalLines.setText(logContent.lineCount() + " lines");

Platform.runLater(() -> codeArea.position(0, 0).toOffset());
            } catch (InterruptedException | ExecutionException e) {
                codeArea.clear();
                codeArea.replaceText(0, 0, "Could not load file from: " + file.getAbsolutePath());
                statusMessage.setText("Error showing file");
            }
        });

        loaderTask.setOnFailed(workerStateEvent -> {
            codeArea.clear();
            codeArea.replaceText(0, 0, "Could not load file from: " + file.getAbsolutePath());
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
