package com.chrislaforetsoftware.logslicer.parser;

public class JSONContent implements IMarkupContent {

    private final String content;
    private final int startLine;
    private final int endLine;

    public JSONContent(String content, int startLine, int endLine) {
        this.content = content;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    @Override
    public String getMarkupType() {
        return "JSON";
    }

    @Override
    public String getRootTag() {
        return "";
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    @Override
    public int getEndLine() {
        return endLine;
    }
}
