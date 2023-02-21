package com.chrislaforetsoftware.logslicer;

import javafx.fxml.FXMLLoader;

public class LogSlicer {

    public void run() {
        new FXMLLoader(getClass().getResource("/com/chrislaforetsoftware/logslicer/MainView.fxml"));

    }


    public static void main(String [] args) {
        final LogSlicer logSlicer = new LogSlicer();
        logSlicer.run();
    }
}
