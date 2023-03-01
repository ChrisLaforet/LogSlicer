package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

public class XMLExtractor {



    public static MarkupContent testAndExtractFrom(LogContent content, int lineNumber) {
        final XMLTag tag = findStartTag(content.getTextFor(lineNumber));
        if (tag != null)
        {
            if (tag.isClosed()) {
                return new MarkupContent(tag.getMarkup(), lineNumber, lineNumber);
            } else {
                final XMLTag endTag = findClosingTag(content.getTextFor(lineNumber), tag.getStart(), tag.getTag());
                if (endTag != null) {
                    return new MarkupContent(content.getTextFor(lineNumber).substring(tag.getStart(), endTag.getEnd() + 1), lineNumber, lineNumber);
                }
            }
        }
        return null;
    }

    private static XMLTag findStartTag(String line) {
        int start = line.indexOf(XMLTag.START_BRACKET);
        if (start < 0) {
            return null;
        }
        int end = line.indexOf(XMLTag.END_BRACKET, start);
        if (end >= 0) {
            String tagString = line.substring(start + 1, end).trim();
            boolean isClosed = false;
            if (tagString.length() >= 1) {
                if (tagString.endsWith(XMLTag.END_TAG_SLASH)) {
                    isClosed = true;
                    tagString = tagString.substring(0, tagString.length() - 1).trim();
                }
                final XMLTag tag = new XMLTag(start, end, tagString);
                if (isClosed) {
                    tag.closeTag();
                }
                return tag;
            }
        }
        return null;
    }

    private static XMLTag findClosingTag(String line, int startOffset, String tag) {
        final StringBuffer sb = new StringBuffer();
        sb.append(XMLTag.START_BRACKET);
        sb.append(XMLTag.END_TAG_SLASH);
        sb.append(tag);
        sb.append(XMLTag.END_BRACKET);

        int start = line.indexOf(sb.toString(), startOffset);
        if (start < 0) {
            return null;
        }
        int end = line.indexOf(XMLTag.END_BRACKET, start);
        if (end >= 0) {
            String tagString = line.substring(start + 1, end).trim();
            if (tagString.length() >= 1) {
                if (tagString.endsWith(XMLTag.END_TAG_SLASH)) {
                    tagString = tagString.substring(0, tagString.length() - 1).trim();
                }
               return new XMLTag(start, end, tagString);
            }
        }
        return null;
    }
}
