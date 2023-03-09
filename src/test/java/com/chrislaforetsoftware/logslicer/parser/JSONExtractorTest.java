package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONExtractorTest {

    static private final String SIMPLE_XML = "<Testing></Testing>";
    static private final String VALID_SINGLE_LINE_JSON = "{\"menu\":{\"id\":\"file\",\"value\":\"File\",\"popup\":{\"menuitem\":[{\"value\":\"New\",\"onclick\":\"CreateNewDoc()\"},{\"value\":\"Open\",\"onclick\":\"OpenDoc()\"},{\"value\":\"Close\",\"onclick\":\"CloseDoc()\"}]}}}";
    static private final String INVALID_SINGLE_LINE_JSON = "{\"id\":\"file\"  \"value\":\"File\"}";
    static private final String LIVE_SINGLE_LINE_JSON = "{\"search\":{\"filter\":true,\"family\":[{\"age\":33,\"children\":1,\"disabilities\":[\"NONE\"]}],\"familyCodes\":[\"MARRIED\",\"INSURED\"],\"nextBirthday\":{\"date\":\"2022-10-26T0:00\",\"cakeOption\":{\"code\":\"CHOC_GANACHE\",\"type\":\"12_INCH_ROUND\"},\"iceCreamOption\":{\"code\":\"VAN_SWIRL\",\"type\":\"RASPBERRY_SWIRL\"}}}}";

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
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(json);
        assertEquals("{}", json.getContent());
    }

    @Test
    void givenLogLine_whenContainsEmptyJSONPrecededByPrologCharacters_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "Now is the time for{}");
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(json);
        assertEquals("{}", json.getContent());
    }

    @Test
    void givenLogLine_whenContainsEmptyJSONPrecededByFalseAlarms_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "This { is a { red herring {} with a trailer");
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(json);
        assertEquals("{}", json.getContent());
    }

    @Test
    void givenLogLine_whenContainsValidSingleLineJSON_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, VALID_SINGLE_LINE_JSON);
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(json);
        assertEquals(VALID_SINGLE_LINE_JSON, json.getContent());
    }

    @Test
    void givenLogLine_whenContainsValidSingleLineJSONPrecededByFalseAlarms_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, "Testing { With {{ false alarms " + VALID_SINGLE_LINE_JSON + " False }");
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(json);
        assertEquals(VALID_SINGLE_LINE_JSON, json.getContent());
    }

    @Test
    void givenLogLine_whenContainsMalformedSingleLineJSON_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, INVALID_SINGLE_LINE_JSON);
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNull(json);
    }

    @Test
    void givenLogLine_whenContainsFormedSingleLineJSON_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, LIVE_SINGLE_LINE_JSON);
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(json);
        assertEquals(LIVE_SINGLE_LINE_JSON, json.getContent());
    }
}