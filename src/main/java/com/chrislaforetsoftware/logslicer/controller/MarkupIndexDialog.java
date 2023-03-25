package com.chrislaforetsoftware.logslicer.controller;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import com.chrislaforetsoftware.logslicer.parser.IMarkupContent;
import com.chrislaforetsoftware.logslicer.parser.JSONContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarkupIndexDialog extends Dialog<String> {

    @FXML
    private TableView table;
    private LogContent content;

    public MarkupIndexDialog(Window owner, LogContent content) {
        this.content = content;
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

            showTags();
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

    private void showTags() {
        final List<IMarkupContent> markups = new ArrayList<>();
        for (int lineNumber = 0; lineNumber < content.lineCount(); ) {
            var tag = content.getTagContentFor(lineNumber);
            if (tag == null) {
                lineNumber++;
                continue;
            }
            markups.add(tag);
            lineNumber = tag.getEndLine() + 1;
        }

        markups.forEach(tag ->
            table.getItems().add(tag));
    }

    @FXML
    private void initialize() {

    }
}
