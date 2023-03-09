package com.chrislaforetsoftware.logslicer.controller;

import com.chrislaforetsoftware.logslicer.parser.XMLMarkupContent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class ContentViewDialog extends Dialog<String> {

    @FXML
    private ButtonType copyTextButton;

    @FXML
    private TextArea contentText;

    public ContentViewDialog(Window owner, int lineNumber, XMLMarkupContent content) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/chrislaforetsoftware/logslicer/ContentView.fxml"));
            loader.setController(this);

            final DialogPane dialogPane = loader.load();

            contentText.setText(content.getContent());
            contentText.setEditable(false);

            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);

            setResizable(true);
            setTitle("Embedded content at line " + (lineNumber + 1));
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                if (Objects.equals(ButtonBar.ButtonData.OTHER, buttonType.getButtonData())) {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString(content.getContent());
                    clipboard.setContent(clipboardContent);
                }
                return null;
            });

            setOnShowing(dialogEvent -> Platform.runLater(() -> contentText.requestFocus()));
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
