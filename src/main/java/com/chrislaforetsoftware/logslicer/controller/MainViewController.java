package com.chrislaforetsoftware.logslicer.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

public class MainViewController {

    @FXML
    private AnchorPane mainContainer;

    public void handleOpenLog(ActionEvent actionEvent) {
    }

    public void handleClose(ActionEvent actionEvent) {
        Platform.exit();
    }

//    public MainView() {
//        menuBar.prefWidthProperty().bind(mainContainer.prefWidthProperty());
//    }
}
