package com.chrislaforetsoftware.logslicer.parser;

public class XMLMarkupContent implements IMarkupContent {

    public static final String OPEN_ANGLE = "<";
    private final String content;
    private final int startLine;
    private final int endLine;

    public XMLMarkupContent(String content, int startLine, int endLine) {
        this.content = content;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    @Override
    public String getMarkupType() {
        return "XML";
    }

    @Override
    public String getRootTag() {
        String rootTag = "";
        int lastIndex = -1;
        while (true) {
            int index = lastIndex >= 0 ? content.indexOf(OPEN_ANGLE, lastIndex + 1) : content.indexOf("<");
            if (index < 0) {
                return rootTag;
            }

            lastIndex = index;
        }
        // find first < and check tag
        // if tag has soap:Envelope, look for soap:Body and get next tag
        // TODO: CML - find first tag after soap body or first tag
        return "<ROOT>";
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
