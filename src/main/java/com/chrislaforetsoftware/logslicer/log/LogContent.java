package com.chrislaforetsoftware.logslicer.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogContent {

    private final List<LogLine> lines = new ArrayList<>();

    private final StringBuilder fullText = new StringBuilder(8192);

    public LogContent() {
    }

    public void addLine(int lineNumber, String line) {
        lines.add(new LogLine(lineNumber, line));
        fullText.append(line);
        fullText.append("\r\n");
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
        return fullText.toString();
    }
}
