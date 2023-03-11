package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONParserTest {

    @Test
    void givenLogContent_whenEmpty_thenReturnsFalse() {
        final LogContent content = new LogContent();
        content.addLine(0, "");
        final JSONParser parser = new JSONParser(content, 0);
        assertFalse(parser.attemptExtraction());
    }

    @Test
    void givenLogContent_whenContainsEmptyJSON_thenReturnsJSON() {
        final LogContent content = new LogContent();
        content.addLine(0, "{}");
        final JSONParser parser = new JSONParser(content, 0);
        assertTrue(parser.attemptExtraction());
        assertEquals("{}", parser.getJson());
    }

    @Test
    void givenLogContent_whenContainsSingleLineValidJSON_thenReturnsJSON() {
        final LogContent content = new LogContent();
        content.addLine(0, JSONExtractorTest.VALID_SINGLE_LINE_JSON);
        final JSONParser parser = new JSONParser(content, 0);
        assertTrue(parser.attemptExtraction());
        assertEquals(JSONExtractorTest.VALID_SINGLE_LINE_JSON, parser.getJson());
    }

    @Test
    void givenLogContent_whenContainsSingleLineInvalidJSON_thenReturnsFalse() {
        final LogContent content = new LogContent();
        content.addLine(0, JSONExtractorTest.INVALID_SINGLE_LINE_JSON);
        final JSONParser parser = new JSONParser(content, 0);
        assertFalse(parser.attemptExtraction());
    }

    @Test
    void givenLogContent_whenContainsFormedMultilineJSON_thenReturnsJSON() {
        final LogContent content = JSONExtractorTest.createMultilineContent(JSONExtractorTest.LIVE_MULTILINE_JSON);
        final JSONParser parser = new JSONParser(content, 0);
        assertTrue(parser.attemptExtraction());
        assertEquals(JSONExtractorTest.LIVE_MULTILINE_JSON, parser.getJson());
    }

    @Test
    void givenLogContent_whenContainsXMLTagInJSON_thenReturnsFalse() {
        final LogContent content = new LogContent();
        content.addLine(0, JSONExtractorTest.INVALID_JSON_WITH_XML);
        final JSONParser parser = new JSONParser(content, 0);
        assertFalse(parser.attemptExtraction());
    }
}