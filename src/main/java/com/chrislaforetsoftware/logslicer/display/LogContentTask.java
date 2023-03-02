package com.chrislaforetsoftware.logslicer.display;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import javafx.concurrent.Task;

public abstract class LogContentTask extends Task<LogContent> {

    @Override
    protected LogContent call() throws Exception {
        return this.doCall();
    }

    protected abstract LogContent doCall() throws Exception;

    public void showProgress(int processed, int total) {
        updateProgress(processed, total);
    }
}
