package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XMLExtractorTest {

    static private final String SAMPLE_XML_IN_ONE_LINE = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><Testing></Testing></SOAP-ENV:Body></SOAP-ENV:Envelope>";
    public static final String SIMPLE_JSON = "{\"name\":\"John\", \"age\":30, \"car\":null}";

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
        content.addLine(0, SIMPLE_JSON);
        assertNull(XMLExtractor.testAndExtractFrom(content, 0));
    }

    @Test
    void givenLogLine_whenContainsSingleClosedTag_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "<Testing />");
        final IMarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertNotNull(xml);
        assertEquals("<Testing/>", xml.getContent());
    }

    @Test
    void givenLogLine_whenContainsWrappedSingleClosedTag_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "prefix<Testing /> suffix");
        final IMarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertNotNull(xml);
        assertEquals("<Testing/>", xml.getContent());
    }

    @Test
    void givenLogLine_whenContainsOpenAndClosedTag_thenReturnsXML() {
        final LogContent content = new LogContent();
        content.addLine(0, "<Testing> </Testing>");
        final IMarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertNotNull(xml);
        assertEquals("<Testing> </Testing>", xml.getContent());
    }

    @Test
    void givenLogLine_whenContainsXMLBetweenOpenAndClosedTags_thenReturnsXML() {
        final LogContent content = new LogContent();
        content.addLine(0, SAMPLE_XML_IN_ONE_LINE);
        final IMarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertNotNull(xml);
        assertEquals(SAMPLE_XML_IN_ONE_LINE, xml.getContent());
    }

    @Test
    void givenLogLine_whenContainsWrappedXMLBetweenOpenAndClosedTags_thenReturnsXML() {
        final LogContent content = new LogContent();
        content.addLine(0, "This is a test with " + SAMPLE_XML_IN_ONE_LINE + " xml content");
        final IMarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertNotNull(xml);
        assertEquals(SAMPLE_XML_IN_ONE_LINE, xml.getContent());
    }

    @Test
    void givenLogLine_whenContainsXMLBetweenOpenAndClosedTagsWithFalseStartTag_thenReturnsXML() {
        final LogContent content = new LogContent();
        content.addLine(0, "<FalseTag>" + SAMPLE_XML_IN_ONE_LINE);
        final IMarkupContent xml = XMLExtractor.testAndExtractFrom(content, 0);
        assertNotNull(xml);
        assertEquals(SAMPLE_XML_IN_ONE_LINE, xml.getContent());
    }
}