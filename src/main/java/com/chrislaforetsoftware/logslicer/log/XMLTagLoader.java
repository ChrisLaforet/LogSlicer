package com.chrislaforetsoftware.logslicer.log;

import com.chrislaforetsoftware.logslicer.parser.XMLTag;

import java.util.List;

class XMLTagLoader {

    private static final String END_TAG_SIGNATURE = XMLTag.START_BRACKET + XMLTag.END_TAG_SLASH;

    private XMLTagLoader() {}

    public static void discoverStartAndEndXMLTagsIn(String line, int lineNumber, List<XMLTag> startTags, List<XMLTag> endTags) {
        XMLTag tag = findStartTag(line, lineNumber, 0);
        while (tag != null) {
            addOrReplaceStartTag(startTags, tag);
            tag = findStartTag(line, lineNumber, tag.getEnd());
        }

        tag = findEndTag(line, lineNumber, 0);
        while (tag != null) {
             endTags.add(tag);
            tag = findEndTag(line, lineNumber, tag.getEnd());
        }
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
            if (tagString.length() >= 1 && !tagString.startsWith(XMLTag.END_TAG_SLASH)) {
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

    private static void addOrReplaceStartTag(List<XMLTag> startTags, XMLTag tag) {
        int index = 0;
        for (XMLTag current : startTags) {
            if (current.getTag().equals(tag.getTag())) {
                startTags.set(index, tag);
                return;
            }
            ++index;
        }
        startTags.add(tag);
    }

    private static XMLTag findEndTag(String line, int lineNumber, int startOffset) {

        int index = line.indexOf(END_TAG_SIGNATURE, startOffset);
        if (index >= 0) {
            int end = line.indexOf(XMLTag.END_BRACKET, index);
            if (end >= 0) {
                final String tagString = line.substring(index + END_TAG_SIGNATURE.length(), end).trim();
                return new XMLTag(lineNumber, index, end, tagString);
            }
        }
        return null;
    }
}
