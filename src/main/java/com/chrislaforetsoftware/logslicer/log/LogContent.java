package com.chrislaforetsoftware.logslicer.log;

import com.chrislaforetsoftware.logslicer.parser.IMarkupContent;
import com.chrislaforetsoftware.logslicer.parser.JSONContent;
import com.chrislaforetsoftware.logslicer.parser.XMLMarkupContent;
import com.chrislaforetsoftware.logslicer.parser.XMLTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LogContent {

    public static final String END_LINE = "\n";

    private final List<LogLine> lines = new ArrayList<>();
    private final StringBuilder fullText = new StringBuilder(8192);

    public LogContent() {
        // do nothing
    }

    public void addLine(int lineNumber, String line) {
        lines.add(new LogLine(lineNumber, line));
        fullText.append(line);
        fullText.append(END_LINE);
    }

    public int lineCount() {
        return lines.size();
    }

    public String getTextFor(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isEmpty()) {
            return null;
        }
        return match.get().getLine();
    }

    public List<XMLTag> getXmlStartTagsFor(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isEmpty()) {
            return Collections.emptyList();
        }
        return match.get().getXmlStartTags();
    }

    public List<XMLTag> getXmlEndTagsFor(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isEmpty()) {
            return Collections.emptyList();
        }
        return match.get().getXmlEndTags();
    }

    private Optional<LogLine> getLogLineFor(int lineNumber) {
        if (lines.size() < lineNumber) {
            return Optional.empty();
        }
        return lines.stream().filter(logLine -> logLine.getLineNumber() == lineNumber).findFirst();
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
                sb.append(END_LINE);
            }
            sb.append(lines.get(current).getLine());
        }
        return sb.toString();
    }

    public String getTextInRange(int startLine, int startIndex, int endLine, int endIndex) {
        // endLine and endIndex are inclusive
        if (lines.size() == 0) {
            throw new IllegalStateException("no lines exist in the file");
        }
        if (startLine < 0 || startLine > endLine || startLine >= lines.size()) {
            throw new IndexOutOfBoundsException("startLine is invalid");
        }
        if (endLine >= lines.size()) {
            endLine = lines.size() - 1;
        }

        if (startLine == endLine) {
            final String text = getTextFor(startLine);
            if (endIndex > text.length()) {
                endIndex = text.length();
            }
            return text.substring(startIndex, endIndex);
        }

        final StringBuffer sb = new StringBuffer(4096);
        sb.append(lines.get(startLine).getLine().substring(startIndex));

        for (int current = startLine + 1; current < endLine; current++) {
            sb.append(END_LINE);
            sb.append(lines.get(current).getLine());
        }
        sb.append(END_LINE);
        sb.append(lines.get(endLine).getLine().substring(0, endIndex));

        return sb.toString();
    }

    public boolean hasXml(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isEmpty()) {
            return false;
        }
        return match.get().hasXml();
    }

    public void setXml(int lineNumber, XMLMarkupContent content) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        match.ifPresent(logLine -> logLine.setXml(content));
    }

    public XMLMarkupContent getXmlContentFor(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isPresent()) {
            return match.get().getXmlContent();
        }
        return null;
    }

    public boolean hasJson(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isEmpty()) {
            return false;
        }
        return match.get().hasJson();
    }

    public void setJson(int lineNumber, JSONContent content) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        match.ifPresent(logLine -> logLine.setJson(content));
    }

    public JSONContent getJsonContentFor(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isPresent()) {
            return match.get().getJsonContent();
        }
        return null;
    }

    public IMarkupContent getTagContentFor(int lineNumber) {
        Optional<LogLine> match = getLogLineFor(lineNumber);
        if (match.isPresent()) {
            if (match.get().hasXml()) {
                return match.get().getXmlContent();
            } else if (match.get().hasJson()) {
                return match.get().getJsonContent();
            }
        }
        return null;
    }
}
