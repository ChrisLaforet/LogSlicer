package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XMLExtractorTest {

    @Test
    void givenLogLine_whenTextEmpty_thenReturnsNull() {
        final LogContent content = new LogContent();
        content.addLine(0, "");
        assertNull(XMLExtractor.testAndExtractFrom(content, 0));
    }

    @Test
    void givenLogLine_whenContainsNonXMLText_thenReturnsNull() {
        final LogContent content = new LogContent();
        content.addLine(0, "This is just a test");
        assertNull(XMLExtractor.testAndExtractFrom(content, 0));
    }

    @Test
    void givenLogLine_whenContainsJSONText_thenReturnsNull() {
        final LogContent content = new LogContent();
        content.addLine(0, "{\"name\":\"John\", \"age\":30, \"car\":null}");
        assertNull(XMLExtractor.testAndExtractFrom(content, 0));
    }

    @Test
    void givenLogLine_whenContainsSingleClosedTag_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "<Testing />");
        final MarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertEquals("<Testing/>", xml.getContent());
    }

    @Test
    void givenLogLine_whenContainsWrappedSingleClosedTag_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "prefix<Testing /> suffix");
        final MarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertEquals("<Testing/>", xml.getContent());
    }

    @Test
    void givenLogLine_whenContainsOpenAndClosedTag_thenReturnsXML() {
        final LogContent content = new LogContent();
        content.addLine(0, "<Testing> </Testing>");
        final MarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertEquals("<Testing> </Testing>", xml.getContent());
    }
}