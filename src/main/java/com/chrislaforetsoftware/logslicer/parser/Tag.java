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

    public String getMarkup() {
// TODO: extract to support xml and json
        final StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(tag);
        if (isClosed) {
            sb.append("/");
        }
        sb.append(">");
        return sb.toString();
    }

    public String getTag() {
        return tag;
    }
}
