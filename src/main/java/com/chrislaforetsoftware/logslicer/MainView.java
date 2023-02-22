package com.chrislaforetsoftware.logslicer;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

public class MainView {

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private MenuBar menuBar;

    public MainView() {
        menuBar.prefWidthProperty().bind(mainContainer.prefWidthProperty());
    }
}
