package com.chrislaforetsoftware.logslicer.parser;

public class Tag {

    private int start;
    private String tag;
    private boolean isClosed;

    public Tag(int start, String tag) {
        this.start = start;
        this.tag = tag;
    }

    public void closeTag() {
        this.isClosed = true;
    }

    public int getStart() {
        return start;
    }

    public String getTag() {
        return tag;
    }
}
