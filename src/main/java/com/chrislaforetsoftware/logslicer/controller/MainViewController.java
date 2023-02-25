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
import java.util.Optional;
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

    private VirtualizedScrollPane virtualizedScrollPane;

    private CodeArea codeArea;

    private String searchFor;

    public void initialize() {
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        // codeArea.setContextMenu( new DefaultContextMenu() );

        virtualizedScrollPane = new VirtualizedScrollPane<>(codeArea);
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

    public void handleSearch(ActionEvent actionEvent) {
        if (logContent == null) {
            return;
        }

        final TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Find text");
        dialog.setHeaderText("Search for text within this log file");
        dialog.setContentText("Search for:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(searchString -> {
            searchFor = searchString;
//            System.out.println("Your name: " + searchString)
        });
    }

    public void handleSearchNext(ActionEvent actionEvent) {
        if (searchFor == null || searchFor.isEmpty()) {
            handleSearch(actionEvent);
        } else {
            // flesh in this logic
        }
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

    public void handleGoToLine(ActionEvent actionEvent) {
        if (logContent == null) {
            return;
        }

        final TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Go to line");
        dialog.setHeaderText("Jump to the specified line number (1 to " + logContent.lineCount() + ")");
        dialog.setContentText("Line number:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(lineString -> {
            try {
                int lineNumber = Integer.parseInt(lineString.trim());
                if (lineNumber > 0 && lineNumber <= logContent.lineCount()) {
                    codeArea.getCaretPosition();
                    System.out.println(codeArea.getCaretPosition());
                    codeArea.requestFollowCaret();
org.reactfx.value.Val<Double> val = virtualizedScrollPane.totalHeightEstimateProperty();
System.out.println("Height: " + val.getValue());
System.out.println("Code Height: " + codeArea.getMaxHeight());
System.out.println("Y scale: " + codeArea.getScaleY());

                    //Platform.runLater(() -> codeArea.position(lineNumber, 0).toOffset());
                }
            } catch (Exception e) {

            }
        });
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

Platform.runLater(() -> codeArea.position(1, 0).toOffset());
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
