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
            if (tag.isClosed()) {
                return new MarkupContent(tag.getMarkup(), lineNumber, lineNumber);
            } else {
                final Tag endTag = findClosingTag(content.getTextFor(lineNumber), tag.getStart(), tag.getTag());
                if (endTag != null) {
                    return new MarkupContent(content.getTextFor(lineNumber).substring(tag.getStart(), endTag.getEnd() + 1), lineNumber, lineNumber);
                }
            }
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
                final Tag tag = new Tag(start, end, tagString);
                if (isClosed) {
                    tag.closeTag();
                }
                return tag;
            }
        }
        return null;
    }

    private static Tag findClosingTag(String line, int startOffset, String tag) {
        final StringBuffer sb = new StringBuffer();
        sb.append(START_BRACKET);
        sb.append(END_TAG_SLASH);
        sb.append(tag);
        sb.append(END_BRACKET);

        int start = line.indexOf(sb.toString(), startOffset);
        if (start < 0) {
            return null;
        }
        int end = line.indexOf(END_BRACKET, start);
        if (end >= 0) {
            String tagString = line.substring(start + 1, end).trim();
            if (tagString.length() >= 1) {
                if (tagString.endsWith(END_TAG_SLASH)) {
                    tagString = tagString.substring(0, tagString.length() - 1).trim();
                }
               return new Tag(start, end, tagString);
            }
        }
        return null;
    }
}
