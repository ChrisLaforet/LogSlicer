package com.chrislaforetsoftware.logslicer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class LogSlicer extends Application {

    // https://edencoding.com/runtime-components-error/

    private static Stage stage;

    @java.lang.Override
    public void start(Stage stage) throws Exception {
        final Parent parent = FXMLLoader.load(getClass().getResource("/com/chrislaforetsoftware/logslicer/MainView.fxml"));
        stage.setTitle("LogSlicer: Parse components from logs");
        stage.setScene(new Scene(parent, 800, 500));
        LogSlicer.stage = stage;


        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        // codeArea.setContextMenu( new DefaultContextMenu() );

        new VirtualizedScrollPane<>(codeArea);
        scene.getStylesheets().add(JavaKeywordsAsyncDemo.class.getResource("java-keywords.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Java Keywords Demo");
        primaryStage.show();

        stage.show();
    }

    public static Stage getStage() {
        return LogSlicer.stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
