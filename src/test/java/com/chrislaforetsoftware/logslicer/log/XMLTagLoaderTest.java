package com.chrislaforetsoftware.logslicer.log;

import com.chrislaforetsoftware.logslicer.parser.XMLTag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLTagLoaderTest {

    public static final String TEXT_WITHOUT_XML = "The quick brown fox jumped over the lazy dog";
    public static final String TEXT_WITH_ONE_START_XML = "The quick brown fox <hops>jumped over the lazy dog";
    public static final String TEXT_WITH_ONE_START_AND_ONE_END_XML = "The quick brown fox <hops>jumped</hops> over the lazy dog";
    public static final String TEXT_WITH_TWO_START_AND_TWO_END_XML = "</test>The quick brown fox <hops>jumped</hops> over the lazy dog<newtest>";


    @Test
    void givenText_whenNotContainsXML_thenReturnsNoStartTags() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITHOUT_XML, 0, startTags, endTags);
        assertTrue(startTags.isEmpty());
    }

    @Test
    void givenText_whenNotContainsXML_thenReturnsNoEndTags() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITHOUT_XML, 0, startTags, endTags);
        assertTrue(endTags.isEmpty());
    }

    @Test
    void givenText_whenContainsOneXMLStartTag_thenReturnsOneStartTag() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITH_ONE_START_XML, 0, startTags, endTags);
        assertEquals(1, startTags.size());
    }

    @Test
    void givenText_whenContainsOneXMLStartTag_thenReturnsNoEndTags() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITH_ONE_START_XML, 0, startTags, endTags);
        assertTrue(endTags.isEmpty());
    }

    @Test
    void givenText_whenContainsOneXMLStartAndOneEndTag_thenReturnsOneStartTag() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITH_ONE_START_AND_ONE_END_XML, 0, startTags, endTags);
        assertEquals(1, startTags.size());
    }

    @Test
    void givenText_whenContainsOneXMLStartAndOneEndTag_thenReturnsOneEndTag() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITH_ONE_START_AND_ONE_END_XML, 0, startTags, endTags);
        assertEquals(1, endTags.size());
    }

    @Test
    void givenText_whenContainsTeoXMLStartAndTwoEndTags_thenReturnsTwoStartTags() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITH_TWO_START_AND_TWO_END_XML, 0, startTags, endTags);
        assertEquals(2, startTags.size());
    }

    @Test
    void givenText_whenContainsTeoXMLStartAndTwoEndTags_thenReturnsTwoEndTags() {
        final List<XMLTag> startTags = new ArrayList<>();
        final List<XMLTag> endTags = new ArrayList<>();
        XMLTagLoader.discoverStartAndEndXMLTagsIn(TEXT_WITH_TWO_START_AND_TWO_END_XML, 0, startTags, endTags);
        assertEquals(2, endTags.size());
    }
}