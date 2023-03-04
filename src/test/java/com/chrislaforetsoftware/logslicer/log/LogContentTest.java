package com.chrislaforetsoftware.logslicer.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogContentTest {

    public static final String ZEROTONINE_STRING = "0123456789";

    @Test
    void givenSingleLine_whenGetTextInRangeStartToEnd_thenReturnsCompleteLine() {
        final LogContent content = new LogContent();
        content.addLine(0, ZEROTONINE_STRING);
        assertEquals(ZEROTONINE_STRING, content.getTextInRange(0, 0, 0, 10));
    }

    @Test
    void givenSingleLine_whenGetTextInRangeStartToEndMinus1_thenReturnsTextInRange() {
        final LogContent content = new LogContent();
        content.addLine(0, ZEROTONINE_STRING);
        assertEquals("012345678", content.getTextInRange(0, 0, 0, 9));
    }

    @Test
    void givenTwoLines_whenGetTextInRangeStartFirstLineToEndSecondLine_thenReturnsTextInRange() {
        final LogContent content = new LogContent();
        content.addLine(0, ZEROTONINE_STRING);
        content.addLine(1, ZEROTONINE_STRING);
        assertEquals(ZEROTONINE_STRING + "\n" + ZEROTONINE_STRING, content.getTextInRange(0, 0, 1, 10));
    }

    @Test
    void givenTwoLines_whenGetTextInRangeStartFirstLineSecondCharToEndSecondLineNinethChar_thenReturnsTextInRange() {
        final LogContent content = new LogContent();
        content.addLine(0, ZEROTONINE_STRING);
        content.addLine(1, ZEROTONINE_STRING);
        assertEquals("123456789\n012345678", content.getTextInRange(0, 1, 1, 9));
    }
}