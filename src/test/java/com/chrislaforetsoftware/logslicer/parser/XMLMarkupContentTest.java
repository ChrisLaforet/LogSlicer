package com.chrislaforetsoftware.logslicer.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XMLMarkupContentTest {

    @Test
    void givenContentWithSingleClosedTag_whenGetRootTag_thenReturnsTag() {
        final var content = new XMLMarkupContent("<SingleClosedTag />", 0, 0);
        assertEquals("SingleClosedTag", content.getRootTag());
    }

    @Test
    void givenContentWithSoapEnvelopeOnly_whenGetRootTag_thenReturnsSoapEnvelopeAsTag() {
        final var content = new XMLMarkupContent("<soap:Envelope />", 0, 0);
        assertEquals("soap:Envelope", content.getRootTag());
    }

    @Test
    void givenContentWithSoapEnvelopeAndBodyWithTag_whenGetRootTag_thenReturnsTag() {
        final var content = new XMLMarkupContent("<soap:Envelope><soap:Body><Testing /></soap:Body></soap:Envelope>", 0, 0);
        assertEquals("Testing", content.getRootTag());
    }

    @Test
    void givenContentWithSoapEnvEnvelopeAndBodyWithTag_whenGetRootTag_thenReturnsTag() {
        final var content = new XMLMarkupContent("<SOAP-ENV:Envelope><SOAP-ENV:Body><Testing /></SOAP-ENV:Body></SOAP-ENV:Envelope>", 0, 0);
        assertEquals("Testing", content.getRootTag());
    }
}