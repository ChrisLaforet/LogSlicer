package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

public class XMLExtractor {

    public static IMarkupContent testAndExtractFrom(LogContent content, int lineNumber) {
        XMLTag tag = findStartTag(content.getTextFor(lineNumber), 0);
        while (tag != null)
        {
            if (tag.isClosed()) {
                return new XMLMarkupContent(tag.getMarkup(), lineNumber, lineNumber);
            }

            final XMLTag endTag = findClosingTag(content.getTextFor(lineNumber), tag.getStart(), tag.getTag());
            if (endTag != null) {
                return new XMLMarkupContent(content.getTextFor(lineNumber).substring(tag.getStart(), endTag.getEnd() + 1), lineNumber, lineNumber);
            }

            tag = findStartTag(content.getTextFor(lineNumber), tag.getEnd());
        }
        return null;
    }

    private static XMLTag findStartTag(String line, int fromIndex) {
        int start = line.indexOf(XMLTag.START_BRACKET,fromIndex);
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
