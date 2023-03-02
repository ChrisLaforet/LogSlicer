package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

public class JSONExtractor {
    public static IMarkupContent testAndExtractFrom(LogContent content, int lineNumber) {

        final String line = content.getTextFor(lineNumber);
        int openBraceOffset = findOpenBrace(line);
        if (openBraceOffset >= 0) {
            int closeBraceOffset = findNextClosingBraceAfter(line, openBraceOffset);
            while (closeBraceOffset >= 0) {
                final String possibleJson = line.substring(openBraceOffset, closeBraceOffset + 1);

                if (JSONValidator.isValid(possibleJson)) {
                    return new JSONContent(possibleJson, lineNumber, lineNumber);
                }
                closeBraceOffset = findNextClosingBraceAfter(line, closeBraceOffset + 1);
            }
        }
        return null;
    }

    private static int findOpenBrace(String line) {
        return line.indexOf('{');
    }

    private static int findNextClosingBraceAfter(String line, int start) {
        return line.indexOf('}', start);
    }
}
