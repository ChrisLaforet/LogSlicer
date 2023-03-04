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
        fullText.append("\n");
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

    public String getTextInRange(int startLine, int endLine) {
        // endLine is INCLUSIVE
        if (lines.size() == 0) {
            throw new IllegalStateException("no lines exist in the file");
        }
        if (startLine < 0 || startLine > endLine || startLine >= lines.size()) {
            throw new IndexOutOfBoundsException("startLine is invalid");
        }
        final StringBuffer sb = new StringBuffer(4096);
        if (endLine >= lines.size()) {
            endLine = lines.size() - 1;
        }

        for (int current = startLine; current <= endLine; current++) {
            if (current != startLine) {
                sb.append("\n");
            }
            sb.append(lines.get(current));
        }
        return sb.toString();
    }

    public String getTextInRange(int startLine, int startIndex, int endLine, int endIndex) {
        // endLine and endIndex are inclusive
        if (startLine == endLine) {
            return getTextFor(startLine).substring(startIndex, endIndex);
        }

        return "";
    }
}
