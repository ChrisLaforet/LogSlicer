package com.chrislaforetsoftware.logslicer.controller;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import com.chrislaforetsoftware.logslicer.parser.IMarkupContent;
import com.chrislaforetsoftware.logslicer.parser.JSONContent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarkupIndexDialog extends Dialog<String> {

    @FXML
    private TableView<IMarkupContent> markupTable;

    @FXML
    private TableColumn<IMarkupContent, String> markupType;

    @FXML
    private TableColumn<IMarkupContent, Integer> startLine;

    @FXML
    private TableColumn<IMarkupContent, Integer> endLine;

    @FXML
    private TableColumn<IMarkupContent, String> rootTag;

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
        }
        catch (IOException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading markup index dialog");
            alert.setContentText("A problem occurred while attempting to create a dialog (" + e.getMessage() + ")");
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    private void showTags() {
        int lineNumber = 0;
        while (lineNumber < content.lineCount()) {
            var tag = content.getTagContentFor(lineNumber);
            if (tag == null) {
                lineNumber++;
                continue;
            }
            markupTable.getItems().add(tag);
            lineNumber = tag.getEndLine() + 1;
        }
    }

    @FXML
    private void initialize() {
        markupType.setCellValueFactory(new PropertyValueFactory<>("markupType"));
        startLine.setCellValueFactory(new PropertyValueFactory<>("startLine"));
        endLine.setCellValueFactory(new PropertyValueFactory<>("endLine"));
        rootTag.setCellValueFactory(new PropertyValueFactory<>("rootTag"));
    }
}
