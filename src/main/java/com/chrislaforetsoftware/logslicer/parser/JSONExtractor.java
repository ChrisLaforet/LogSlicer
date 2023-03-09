package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

public class JSONExtractor {
    public static IMarkupContent testAndExtractFrom(LogContent content, int lineNumber) {
System.err.println(lineNumber + " - " + content.getTextFor(lineNumber));
        final String line = content.getTextFor(lineNumber);
        int openBraceOffset = findOpenBraceAfter(line, 0);
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
        return null;
    }

    private static int findOpenBraceAfter(String line, int start) {
        return line.indexOf('{', start);
    }

    private static int findNextClosingBraceAfter(String line, int start) {
        return line.indexOf('}', start);
    }
}
