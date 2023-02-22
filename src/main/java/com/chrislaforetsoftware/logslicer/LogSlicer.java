package com.chrislaforetsoftware.logslicer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogSlicer extends Application {

    // https://edencoding.com/runtime-components-error/

    @java.lang.Override
    public void start(Stage stage) throws Exception {
        final Parent parent = FXMLLoader.load(getClass().getResource("/com/chrislaforetsoftware/logslicer/MainView.fxml"));
        stage.setTitle("LogSlicer: Parse components from logs");
        stage.setScene(new Scene(parent, 800, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
