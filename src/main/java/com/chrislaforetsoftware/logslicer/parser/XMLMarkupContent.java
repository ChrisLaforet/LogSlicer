package com.chrislaforetsoftware.logslicer.parser;

public class XMLMarkupContent implements IMarkupContent {

    public static final String OPEN_ANGLE = "<";
    public static final String CLOSE_ANGLE = ">";
    private final String content;
    private final int startLine;
    private final int endLine;

    private String rootTag;

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
        if (this.rootTag == null) {
            this.rootTag = loadRootTag();
        }
        return this.rootTag;
    }

    private String loadRootTag() {
        String rootTag = null;
        int lastIndex = -1;
        boolean inSoapEnvelope = false;
        boolean inSoapBody = false;
        while (true) {
            int index = lastIndex >= 0 ? content.indexOf(OPEN_ANGLE, lastIndex + 1) : content.indexOf("<");
            if (index < 0) {
                return rootTag;
            }

            int closeIndex = content.indexOf(CLOSE_ANGLE, index + 1);
            if (closeIndex < 0) {
                return rootTag;
            }

            final var xmlTag = new XMLTag(0, index, closeIndex, content.substring(index + 1, closeIndex));
            if (!xmlTag.getTag().startsWith("/")) {
                if (rootTag == null) {
                    rootTag = xmlTag.getTag();
                }

                if (xmlTag.getTag().contains(":Envelope")) {
                    inSoapEnvelope = true;
                } else if (!inSoapEnvelope || inSoapBody) {
                    return xmlTag.getTag();
                } else if (xmlTag.getTag().contains(":Body")) {
                    inSoapBody = true;
                }
            }

            lastIndex = closeIndex;
        }
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
