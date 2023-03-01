package com.chrislaforetsoftware.logslicer.parser;

public class MarkupContent {
    private String content;
    private int startLine;
    private int endLine;

    public MarkupContent(String content, int startLine, int endLine) {
        this.content = content;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public String getContent() {
        return content;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }
}
