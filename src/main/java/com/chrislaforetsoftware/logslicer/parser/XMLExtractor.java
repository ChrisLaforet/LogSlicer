package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

public class XMLExtractor {

    public static IMarkupContent testAndExtractFrom(LogContent content, int lineNumber) {
        final List<XMLTag> possibleTags = new ArrayList<>();
        XMLTag tag = findStartTag(content.getTextFor(lineNumber),  lineNumber,0);
        while (tag != null)
        {
            addOrReplacePossibleTag(possibleTags, tag);
            if (tag.isClosed()) {
                return new XMLMarkupContent(tag.getMarkup(), lineNumber, lineNumber);
            }

            final XMLTag endTag = findClosingTag(content.getTextFor(lineNumber), lineNumber, tag.getStart(), tag.getTag());
            if (endTag != null) {
                return new XMLMarkupContent(content.getTextInRange(lineNumber, tag.getStart(), lineNumber, endTag.getEnd() + 1), lineNumber, lineNumber);
            }

            tag = findStartTag(content.getTextFor(lineNumber), lineNumber, tag.getEnd());
        }

        if (possibleTags.isEmpty()) {
            return null;
        }

        return testAndExtractMultilineXML(possibleTags, content, lineNumber);
    }

    private static void addOrReplacePossibleTag(List<XMLTag> possibleTags, XMLTag tag) {
        int index = 0;
        for (XMLTag current : possibleTags) {
            if (current.getTag().equals(tag.getTag())) {
                possibleTags.set(index, tag);
                return;
            }
            ++index;
        }
        possibleTags.add(tag);
    }

    private static IMarkupContent testAndExtractMultilineXML(List<XMLTag> possibleTags, LogContent content, int lineNumber) {
        // for each tag, check for close tag on future line - ensure that there is not another open tag on a future line/stop scanning then

        // TODO: CML - prescan/dynamically cache tags and save in lookup list for LogContent?

        for (XMLTag currentTag : possibleTags) {
            final String closeTag = formatEndTagFor(currentTag.getTag());
            int endLineNumber = lineNumber + 1;

            while (endLineNumber < content.lineCount()) {
                final String line = content.getTextFor(endLineNumber);
                int currentIndex = line.indexOf(currentTag.getTag());
                int endIndex = line.indexOf(closeTag);
                if (currentIndex >= 0) {
                    if (endIndex < 0 || endIndex >= 0 && endIndex > currentIndex) {
                        break;
                    }
                }
                if (endIndex >= 0) {
                    return new XMLMarkupContent(content.getTextInRange(currentTag.getLineNumber(), currentTag.getStart(), endLineNumber, endIndex + closeTag.length()), currentTag.getLineNumber(), endLineNumber);
                }

                ++endLineNumber;
            }
        }
        return null;
    }

    private static XMLTag findStartTag(String line, int lineNumber, int fromIndex) {
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
                final XMLTag tag = new XMLTag(lineNumber, start, end, tagString);
                if (isClosed) {
                    tag.closeTag();
                }
                return tag;
            }
        }
        return null;
    }

    public static String formatEndTagFor(String tag) {
        final StringBuffer sb = new StringBuffer();
        sb.append(XMLTag.START_BRACKET);
        sb.append(XMLTag.END_TAG_SLASH);
        sb.append(tag);
        sb.append(XMLTag.END_BRACKET);
        return sb.toString();
    }

    private static XMLTag findClosingTag(String line, int lineNumber, int startOffset, String tag) {
        final String endTag = formatEndTagFor(tag);
        int start = line.indexOf(endTag, startOffset);
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
               return new XMLTag(lineNumber, start, end, tagString);
            }
        }
        return null;
    }
}
