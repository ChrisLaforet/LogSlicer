package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONExtractorTest {

    static private final String SIMPLE_XML = "<Testing></Testing>";

    @Test
    void givenLogLine_whenTextEmpty_thenReturnsNull() {
        final LogContent content = new LogContent();
        content.addLine(0, "");
        assertNull(JSONExtractor.testAndExtractFrom(content, 0));
    }

    @Test
    void givenLogLine_whenContainsNonXMLText_thenReturnsNull() {
        final LogContent content = new LogContent();
        content.addLine(0, "This is just a test");
        assertNull(JSONExtractor.testAndExtractFrom(content, 0));
    }

    @Test
    void givenLogLine_whenContainsXMLText_thenReturnsNull() {
        final LogContent content = new LogContent();
        content.addLine(0, SIMPLE_XML);
        assertNull(JSONExtractor.testAndExtractFrom(content, 0));
    }

    @Test
    void givenLogLine_whenContainsEmptyJSON_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "{}");
        final IMarkupContent xml = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(xml);
        assertEquals("<Testing/>", xml.getContent());
    }
}