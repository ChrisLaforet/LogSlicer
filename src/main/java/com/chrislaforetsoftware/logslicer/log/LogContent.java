package com.chrislaforetsoftware.logslicer.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogContent {

    private List<LogLine> lines = new ArrayList<>();

    public LogContent() {
    }

    public void addLine(int lineNumber, String line) {
        lines.add(new LogLine(lineNumber, line));
    }

    public int lineCount() {
        return lines.size();
    }

    public String getTextFor(int lineNumber) {
        if (lines.size() < lineNumber) {
            return null;
        }
        Optional<LogLine> match = lines.stream().filter(logLine -> logLine.getLineNumber() == lineNumber).findFirst();
        if (match.isEmpty()) {
            return null;
        }
        return match.get().getLine();
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
