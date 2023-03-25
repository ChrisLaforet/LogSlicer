package com.chrislaforetsoftware.logslicer.parser;

public class XMLTag {

    public static final char START_BRACKET = '<';
    public static final char END_BRACKET = '>';
    public static final String END_TAG_SLASH = "/";

    private final int lineNumber;
    private final int start;
    private final int end;
    private final String tag;
    private final String content;
    private boolean closed;

    public XMLTag(int lineNumber, int start, int end, String contentInsideBracket, boolean closed) {
        this.lineNumber = lineNumber;
        this.start = start;
        this.end = end;
        this.content = contentInsideBracket.trim();
        this.tag = extractTagFromContent();
        this.closed = closed;
    }

    public XMLTag(int lineNumber, int start, int end, String contentInsideBracket) {
        this(lineNumber, start, end, contentInsideBracket, false);
    }

    private String extractTagFromContent() {
        int index = findWhitespaceBefore(-1, ' ');
        index = findWhitespaceBefore(index, '\t');
        index = findWhitespaceBefore(index, '\r');
        index = findWhitespaceBefore(index, '\n');
        index = findWhitespaceBefore(index, '/');
        index = findWhitespaceBefore(index, '>');

        if (index > 0) {
            return content.substring(0, index).trim();
        }
        return content.trim();
    }

    private int findWhitespaceBefore(int lastIndex, char whitespace) {
        int index = content.indexOf(whitespace);
        if (index < 0) {
            return lastIndex;
        } else if (lastIndex < 0) {
            return index;
        }
        return Math.min(lastIndex, index);
    }

    public void closeTag() {
        this.closed = true;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getMarkup() {
        final StringBuilder sb = new StringBuilder();
        sb.append(START_BRACKET);
        sb.append(content);
        if (closed) {
            sb.append(END_TAG_SLASH);
        }
        sb.append(END_BRACKET);
        return sb.toString();
    }

    public String getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }

    public boolean isClosed() {
        return closed;
    }
}
