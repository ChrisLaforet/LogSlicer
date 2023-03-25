package com.chrislaforetsoftware.logslicer.controller;

import com.chrislaforetsoftware.logslicer.parser.JSONContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;

public class MarkupIndexDialog extends Dialog<String> {

    public MarkupIndexDialog(Window owner) {
        show(owner);
    }

    private void show(Window owner) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/chrislaforetsoftware/logslicer/MarkupIndex.fxml"));
            loader.setController(this);

            final DialogPane dialogPane = loader.load();

            initOwner(owner);
            initModality(Modality.NONE);

            setResizable(true);
            setTitle("List markup tags in log");
            setDialogPane(dialogPane);

           // setOnShowing(dialogEvent -> Platform.runLater(() -> contentText.requestFocus()));
        }
        catch (IOException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading content dialog");
            alert.setContentText("A problem occurred while attempting to create a dialog (" + e.getMessage() + ")");
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {

    }
}
