package com.chrislaforetsoftware.logslicer.log;

public class LogLine {

    private String line;
    private int lineNumber;

    public LogLine(int lineNumber, String line) {
        this.lineNumber = lineNumber;
        this.line = line;
    }

    public String getLine() {
        return this.line;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }
}
