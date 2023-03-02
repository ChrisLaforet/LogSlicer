package com.chrislaforetsoftware.logslicer.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class PasteLogViewDialog extends Dialog<String> {

    @FXML
    private ButtonType prepareLogButton;

    @FXML
    private TextArea logContentText;

    public PasteLogViewDialog(Window owner) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/chrislaforetsoftware/logslicer/PasteLogView.fxml"));
            loader.setController(this);

            final DialogPane dialogPane = loader.load();
            // no reactiveness to the text area at this time:
            //dialogPane.lookupButton(prepareLogButton).addEventFilter(ActionEvent.ANY, this::onPrepare);

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);

            setResizable(true);
            setTitle("Paste log text");
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                if (!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                    return null;
                }

                return logContentText.getText();
            });

            setOnShowing(dialogEvent -> Platform.runLater(() -> logContentText.requestFocus()));
        }
        catch (IOException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading paste dialog");
            alert.setContentText("A problem occurred while attempting to create a dialog (" + e.getMessage() + ")");
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {

    }

}
