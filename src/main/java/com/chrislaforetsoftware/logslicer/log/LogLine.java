package com.chrislaforetsoftware.logslicer.log;

import com.chrislaforetsoftware.logslicer.parser.XMLTag;

import java.util.ArrayList;
import java.util.List;

public class LogLine {

    private String line;
    private int lineNumber;

    // TODO: CML - add XML content here for all affected lines

    private List<XMLTag> xmlStartTags = new ArrayList<>();
    private List<XMLTag> xmlEndTags = new ArrayList<>();

    public LogLine(int lineNumber, String line) {
        this.lineNumber = lineNumber;
        this.line = line;
        XMLTagLoader.discoverStartAndEndXMLTagsIn(line, lineNumber, xmlStartTags, xmlEndTags);
    }

    public String getLine() {
        return this.line;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public List<XMLTag> getXmlStartTags() {
        return xmlStartTags;
    }

    public List<XMLTag> getXmlEndTags() {
        return xmlEndTags;
    }
}
