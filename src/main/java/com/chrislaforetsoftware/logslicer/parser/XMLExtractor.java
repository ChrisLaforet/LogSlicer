package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

public class XMLExtractor {

    public static IMarkupContent testAndExtractFrom(LogContent content, int lineNumber) {

        final List<XMLTag> possibleTags = new ArrayList<>();
        XMLTag firstEmptyTag = null;
        for (var currentTag : content.getXmlStartTagsFor(lineNumber)) {
            if (currentTag.isClosed()) {
                if (firstEmptyTag == null) {
                    firstEmptyTag = currentTag;
                }
                continue;
            }

            // TODO: CML - first matching start/end can be queued if it is not superceded by a multiline xml
            final XMLTag endTag = findClosingTag(content.getTextFor(lineNumber), lineNumber, currentTag.getStart(), currentTag.getTag());
            if (endTag != null) {
                return new XMLMarkupContent(content.getTextInRange(lineNumber, currentTag.getStart(), lineNumber, endTag.getEnd() + 1), lineNumber, lineNumber);
            }

            addOrReplacePossibleTag(possibleTags, currentTag);
        }

        if (!possibleTags.isEmpty()) {
            var multilineXML = testAndExtractMultilineXML(possibleTags, content, lineNumber);
            if (multilineXML != null) {
                return multilineXML;
            }
        }
        if (firstEmptyTag != null) {
            return new XMLMarkupContent(firstEmptyTag.getMarkup(), lineNumber, lineNumber);
        }
        return null;
    }

    private static void addOrReplacePossibleTag(List<XMLTag> possibleTags, XMLTag tag) {
        int index = 0;
        for (var current : possibleTags) {
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

        for (var currentTag : possibleTags) {
            int endLineNumber = lineNumber + 1;

            while (endLineNumber < content.lineCount()) {
                final var endTag = content.getXmlEndTagsFor(endLineNumber).stream()
                        .filter(tag -> tag.getTag().equals(currentTag.getTag()))
                        .findFirst();
                if (endTag.isPresent()) {
                    return new XMLMarkupContent(content.getTextInRange(currentTag.getLineNumber(),
                            currentTag.getStart(),
                            endLineNumber,
                            endTag.get().getEnd() + 1), currentTag.getLineNumber(), endLineNumber);
                }

                ++endLineNumber;
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
