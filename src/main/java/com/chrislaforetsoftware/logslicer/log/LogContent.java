package com.chrislaforetsoftware.logslicer.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LogContent {

    private File file;
    private List<LogLine> lines = new ArrayList<>();

    public LogContent(File file) {
        this.file = file;
    }

    public void addLine(String line) {
        lines.add(new LogLine(line));
    }

    public int lineCount() {
        return lines.size();
    }

    public String getText() {
        final StringBuffer sb = new StringBuffer(4096);
        lines.forEach(line -> {
            sb.append(line.getLine());
            sb.append("\r\n");
        });
        return sb.toString();
    }
}
