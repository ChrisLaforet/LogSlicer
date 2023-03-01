package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

public class XMLExtractor {

    public static final char START_BRACKET = '<';
    public static final char END_BRACKET = '>';
    public static final String END_TAG_SLASH = "/";

    public static MarkupContent testAndExtractFrom(LogContent content, int lineNumber) {
        final Tag tag = findStartTag(content.getTextFor(lineNumber));
        if (tag != null)
        {
            return new MarkupContent(tag.getMarkup(), lineNumber, lineNumber);
        }
        return null;
    }

    private static Tag findStartTag(String line) {
        int start = line.indexOf(START_BRACKET);
        if (start < 0) {
            return null;
        }
        int end = line.indexOf(END_BRACKET, start);
        if (end >= 0) {
            String tagString = line.substring(start + 1, end).trim();
            boolean isClosed = false;
            if (tagString.length() >= 1) {
                if (tagString.endsWith(END_TAG_SLASH)) {
                    isClosed = true;
                    tagString = tagString.substring(0, tagString.length() - 1).trim();
                }
                final Tag tag = new Tag(start, tagString);
                if (isClosed) {
                    tag.closeTag();
                }
                return tag;
            }
        }
        return null;
    }
}
