package com.chrislaforetsoftware.logslicer.log;

import com.chrislaforetsoftware.logslicer.parser.IMarkupContent;
import com.chrislaforetsoftware.logslicer.parser.JSONContent;
import com.chrislaforetsoftware.logslicer.parser.XMLMarkupContent;
import com.chrislaforetsoftware.logslicer.parser.XMLTag;

import java.util.ArrayList;
import java.util.List;

public class LogLine {

    private String line;
    private int lineNumber;

    // TODO: CML - add XML content here for all affected lines

    private List<XMLTag> xmlStartTags = new ArrayList<>();
    private List<XMLTag> xmlEndTags = new ArrayList<>();

    private XMLMarkupContent xmlContent;
    private JSONContent jsonContent;

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

    public boolean hasXml() {
        return xmlContent != null;
    }

    public void setXml(XMLMarkupContent xmlContent) {
        this.xmlContent = xmlContent;
    }

    public XMLMarkupContent getXmlContent() {
        return this.xmlContent;
    }

    public boolean hasJson() {
        return jsonContent != null;
    }

    public void setJson(JSONContent jsonContent) {
        this.jsonContent = jsonContent;
    }
}
