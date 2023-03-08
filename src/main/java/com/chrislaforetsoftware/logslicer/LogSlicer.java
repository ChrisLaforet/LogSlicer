package com.chrislaforetsoftware.logslicer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LogSlicer extends Application {

    // https://edencoding.com/runtime-components-error/

    private static Stage stage;

    @java.lang.Override
    public void start(Stage stage) throws Exception {
        final Parent parent = FXMLLoader.load(getClass().getResource("/com/chrislaforetsoftware/logslicer/MainView.fxml"));
        stage.setTitle("LogSlicer: Parse components from logs");
        stage.setScene(new Scene(parent, 800, 500));


        LogSlicer.stage = stage;
        stage.show();

//CodeArea codeArea = new CodeArea();
//
//IntFunction<Node> numberFactory = LineNumberFactory.get(codeArea);
//IntFunction<Node> buttonFactory = new ButtonFactory(codeArea.currentParagraphProperty());
//IntFunction<Node> graphicFactory = line -> {
//    HBox hbox = new HBox(
//            numberFactory.apply(line),
//            buttonFactory.apply(line)
//            );
//    hbox.setAlignment(Pos.CENTER_LEFT);
//    return hbox;
//};
//codeArea.setParagraphGraphicFactory(graphicFactory);
//codeArea.replaceText("The green arrow will only be on the line where the caret appears.\n\nButton\nTry it.");
//codeArea.moveTo(0, 0);
//
//stage.setScene(new Scene(new StackPane(codeArea), 600, 400));
//stage.show();
    }

    public static Stage getStage() {
        return LogSlicer.stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
