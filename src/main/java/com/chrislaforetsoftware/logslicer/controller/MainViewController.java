package com.chrislaforetsoftware.logslicer.controller;

import com.chrislaforetsoftware.logslicer.LogSlicer;
import com.chrislaforetsoftware.logslicer.log.LogContent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainViewController {

    @FXML
    private AnchorPane mainContainer;

    private LogContent logContent;

    public void handleOpenLog(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open log file");
        final File file = fileChooser.showOpenDialog(LogSlicer.getStage());
        if (file != null) {
            loadFile(file);
        }
    }

    private void loadFile(File file) {
        try {
            logContent = new LogContent(file);

        } catch (Exception e) {

        }
    }

    public void handleClose(ActionEvent actionEvent) {
        Platform.exit();
    }

//    public MainView() {
//        menuBar.prefWidthProperty().bind(mainContainer.prefWidthProperty());
//    }
}
