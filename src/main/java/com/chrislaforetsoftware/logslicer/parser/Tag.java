package com.chrislaforetsoftware.logslicer.parser;

public class Tag {

    private int start;
    private int end;
    private String tag;
    private boolean closed;

    public Tag(int start, int end, String tag) {
        this.start = start;
        this.end = end;
        this.tag = tag;
    }

    public void closeTag() {
        this.closed = true;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getMarkup() {
// TODO: extract to support xml and json
        final StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(tag);
        if (closed) {
            sb.append("/");
        }
        sb.append(">");
        return sb.toString();
    }

    public String getTag() {
        return tag;
    }

    public boolean isClosed() {
        return closed;
    }
}
