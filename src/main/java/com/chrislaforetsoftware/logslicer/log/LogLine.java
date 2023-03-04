package com.chrislaforetsoftware.logslicer.log;

import com.chrislaforetsoftware.logslicer.parser.XMLTag;

import java.util.ArrayList;
import java.util.List;

public class LogLine {

    private String line;
    private int lineNumber;

    private List<XMLTag> xmlTagList = new ArrayList<>();

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
