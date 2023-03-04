package com.chrislaforetsoftware.logslicer.parser;

public class XMLTag {

    public static final char START_BRACKET = '<';
    public static final char END_BRACKET = '>';
    public static final String END_TAG_SLASH = "/";

    private int lineNumber;
    private int start;
    private int end;
    private String tag;
    private String content;
    private boolean closed;

    public XMLTag(int lineNumber, int start, int end, String content) {
        this.lineNumber = lineNumber;
        this.start = start;
        this.end = end;
        this.content = content.trim();
        this.tag = extractTagFromContent();
    }

    private String extractTagFromContent() {
        int index = findWhitespaceBefore(-1, ' ');
        index = findWhitespaceBefore(index, '\t');
        index = findWhitespaceBefore(index, '\r');
        index = findWhitespaceBefore(index, '\n');
        if (index > 0) {
            return content.substring(0, index);
        }
        return content;
    }

    private int findWhitespaceBefore(int lastIndex, char whitespace) {
        int index = content.indexOf(whitespace);
        if (index < 0) {
            return lastIndex;
        } else if (lastIndex < 0) {
            return index;
        }
        return lastIndex < index ? lastIndex : index;
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
// TODO: extract to support xml and json
        final StringBuffer sb = new StringBuffer();
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
