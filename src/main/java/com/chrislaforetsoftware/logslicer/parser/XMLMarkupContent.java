package com.chrislaforetsoftware.logslicer.parser;

public class XMLMarkupContent implements IMarkupContent {

    private final String content;
    private final int startLine;
    private final int endLine;

    public XMLMarkupContent(String content, int startLine, int endLine) {
        this.content = content;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public String getMarkupType() {
        return "XML";
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
