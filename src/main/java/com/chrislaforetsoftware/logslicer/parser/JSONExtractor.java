package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

public class JSONExtractor {

    private JSONExtractor() {
        // maintains class as static-only class
    }

    public static IMarkupContent testAndExtractFrom(LogContent content, int lineNumber) {
        final JSONParser parser = new JSONParser(content, lineNumber);
        if (parser.attemptExtraction()) {
            return new JSONContent(parser.getJson(), parser.getStartLineNumber(), parser.getEndLineNumber());
        }
        return null;
    }
}
