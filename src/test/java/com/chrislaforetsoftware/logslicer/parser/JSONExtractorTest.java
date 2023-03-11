package com.chrislaforetsoftware.logslicer.parser;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class JSONExtractorTest {

    static private final String SIMPLE_XML = "<Testing></Testing>";
    static public final String VALID_SINGLE_LINE_JSON = "{\"menu\":{\"id\":\"file\",\"value\":\"File\",\"popup\":{\"menuitem\":[{\"value\":\"New\",\"onclick\":\"CreateNewDoc()\"},{\"value\":\"Open\",\"onclick\":\"OpenDoc()\"},{\"value\":\"Close\",\"onclick\":\"CloseDoc()\"}]}}}";
    static public final String INVALID_SINGLE_LINE_JSON = "{\"id\":\"file\"  \"value\":\"File\"}";
    static private final String LIVE_SINGLE_LINE_JSON = "{\"search\":{\"filter\":true,\"family\":[{\"age\":33,\"children\":1,\"disabilities\":[\"NONE\"]}],\"familyCodes\":[\"MARRIED\",\"INSURED\"],\"nextBirthday\":{\"date\":\"2022-10-26T0:00\",\"cakeOption\":{\"code\":\"CHOC_GANACHE\",\"type\":\"12_INCH_ROUND\"},\"iceCreamOption\":{\"code\":\"VAN_SWIRL\",\"type\":\"RASPBERRY_SWIRL\"}}}}";
    static public final String LIVE_MULTILINE_JSON = "{\"search\": {\n" + " \"filter\": true,\n" + " \"family\": [{\n" + "  \"age\": 33,\n" + "  \"children\": 1,\n" + "  \"disabilities\": [\"NONE\"]\n" + " }],\n" + " \"familyCodes\": [\n" + "  \"MARRIED\",\n" + "  \"INSURED\"\n" + " ],\n" + " \"nextBirthday\": {\n" + "  \"date\": \"2022-10-26T0:00\",\n" + "  \"cakeOption\": {\n" + "   \"code\": \"CHOC_GANACHE\",\n" + "   \"type\": \"12_INCH_ROUND\"\n" + "  },\n" + "  \"iceCreamOption\": {\n" + "   \"code\": \"VAN_SWIRL\",\n" + "   \"type\": \"RASPBERRY_SWIRL\"\n" + "  }\n" + " }\n" + "}}";
    static public final String INVALID_JSON_WITH_XML = "{\"code\":\"1239801A\",<xmltag>\"states\":[\"TX\",\"CA\",\"AK\"]}";

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
    void givenLogLine_whenContainsValidSingleLineJSON_thenReturnsTag() {
        final LogContent content = new LogContent();
        content.addLine(0, VALID_SINGLE_LINE_JSON);
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

    @Test
    void givenLogLines_whenContainsFormedMultilineJSON_thenReturnsTag() {
        final LogContent content = createMultilineContent(LIVE_MULTILINE_JSON);
        final IMarkupContent json = JSONExtractor.testAndExtractFrom(content, 0);
        assertNotNull(json);
        assertEquals(LIVE_MULTILINE_JSON, json.getContent());
    }

    public static LogContent createMultilineContent(String multiline) {
        final LogContent content = new LogContent();
        try (BufferedReader reader = new BufferedReader(new StringReader(multiline))) {
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                content.addLine(currentLine++, line);
            }
        } catch (Exception e) {
            // do nothing
        }
        return content;
    }
}