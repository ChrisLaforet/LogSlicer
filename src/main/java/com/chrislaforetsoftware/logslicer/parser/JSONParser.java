package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;

class JSONParser {

    public static final char DOUBLE_QUOTE = '"';
    public static final char CLOSE_CURLY_BRACE = '}';
    public static final char OPEN_CURLY_BRACE = '{';
    public static final char OPEN_ANGLE_BRACKET = '<';
    public static final String NEWLINE = "\n";

    private LogContent content;
    private int startLineNumber;
    private int endLineNumber;
    private String jsonContent;

    public JSONParser(LogContent content, int startLineNumber) {
        this.content = content;
        this.startLineNumber = startLineNumber;
    }

    public boolean attemptExtraction() {
        int openBraces = 0;
        int closeBraces = 0;
        int currentLineNumber = startLineNumber;
        int startOffset = 0;
        String line = content.getTextFor(currentLineNumber);
        while (line != null) {
            boolean isInQuote = false;
            for (int currentColumn = 0; currentColumn < line.length(); currentColumn++) {
                char ch = line.charAt(currentColumn);
                if (ch == OPEN_CURLY_BRACE) {
                    if (openBraces == 0) {
                        startOffset = currentColumn;
                    }
                    ++openBraces;
                } else if (openBraces > 0 && ch == CLOSE_CURLY_BRACE) {
                    ++closeBraces;

                    if (openBraces == closeBraces) {
                        return extractAndTestJson(startOffset, currentLineNumber, currentColumn);
                    }
                } else if (ch == DOUBLE_QUOTE) {
                    isInQuote = !isInQuote;
                } else if (ch == OPEN_ANGLE_BRACKET && !isInQuote) {
                    // possible XML, so bail!
                    return false;
                }
            }

            if (openBraces == 0) {
                break;
            }

            line = content.getTextFor(++currentLineNumber);
        }
        return false;
    }

    public boolean hasJson() {
        return jsonContent != null;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public String getJson() {
        return jsonContent;
    }
    private boolean extractAndTestJson(int startOffset, int endLineNumber, int endOffset) {
        String possibleJson;
        if (startLineNumber == endLineNumber) {
            possibleJson = content.getTextFor(startLineNumber).substring(startOffset, endOffset + 1);
        } else {
            final StringBuffer sb = new StringBuffer(2048);
            for (int currentLineNumber = startLineNumber; currentLineNumber <= endLineNumber; ++currentLineNumber) {
                final String line = content.getTextFor(currentLineNumber);
                if (currentLineNumber != startLineNumber) {
                    sb.append(NEWLINE);
                }
                sb.append(line);
            }
            possibleJson = sb.toString();
        }

        if (!JSONValidator.isValid(possibleJson)) {
            return false;
        }
        jsonContent = possibleJson;
        this.endLineNumber = endLineNumber;
        return true;
    }
}
