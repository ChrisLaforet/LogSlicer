package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

public class JSONExtractor {
    public static IMarkupContent testAndExtractFrom(LogContent content, int lineNumber) {
        final String line = content.getTextFor(lineNumber);
        int openBraceOffset = findOpenBraceAfter(line, 0);
        int firstOpenBraceOffset = openBraceOffset;

        while (openBraceOffset >= 0) {
            int closeBraceOffset = findNextClosingBraceAfter(line, openBraceOffset);
            while (closeBraceOffset >= 0) {
                final String possibleJson = line.substring(openBraceOffset, closeBraceOffset + 1);

                if (JSONValidator.isValid(possibleJson)) {
                    return new JSONContent(possibleJson, lineNumber, lineNumber);
                }
                closeBraceOffset = findNextClosingBraceAfter(line, closeBraceOffset + 1);
            }
            openBraceOffset = findOpenBraceAfter(line, openBraceOffset + 1);
        }

//        if (firstOpenBraceOffset >= 0) {
//            // while additional lines do not have xml tags
//            // while next line is not at end of file
//            // track open braces and close braces
//            // if #open == #close, try json parse
//
//            int openBraceCount = 0;
//            int closeBraceCount = 0;
//
//            int endLineNumber = lineNumber + 1;
//
//            while (endLineNumber < content.lineCount()) {
//                final String currentLine = content.getTextFor(lineNumber);
//                int openBraceOffset = findOpenBraceAfter(line, 0);
//
//                final var endTag = content.getXmlEndTagsFor(endLineNumber).stream()
//                        .filter(tag -> tag.getTag().equals(currentTag.getTag()))
//                        .findFirst();
//                if (endTag.isPresent()) {
//                    return new XMLMarkupContent(content.getTextInRange(currentTag.getLineNumber(),
//                                                                       currentTag.getStart(),
//                                                                       endLineNumber,
//                                                                       endTag.get().getEnd() + 1), currentTag.getLineNumber(), endLineNumber);
//                }
//
//                ++endLineNumber;
//            }
//        }

        return null;
    }

    private static int findOpenBraceAfter(String line, int start) {
        return line.indexOf('{', start);
    }

    private static int findNextClosingBraceAfter(String line, int start) {
        return line.indexOf('}', start);
    }

    class BraceCounter {
        private int openBraces;
        private int closeBraces;

        public boolean areBracesMatched() {
            return openBraces == closeBraces;
        }
        public void addOpenBrace() {
            ++openBraces;
        }

        public void addCloseBrace() {
            ++closeBraces;
        }
    }
}
