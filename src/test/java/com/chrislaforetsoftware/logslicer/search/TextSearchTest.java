package com.chrislaforetsoftware.logslicer.search;

import com.chrislaforetsoftware.logslicer.log.LogContent;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TextSearchTest {

    public static final String SEARCH_STRING = "Test";
    public static final String SINGLE_TEST_LINE = "This is a Test line";

    @Test
    void givenContentToSearch_whenContentIsEmpty_thenMatchCountIsZero() {
        final LogContent content = new LogContent();
        final TextSearch search = new TextSearch(content, SEARCH_STRING, false);
        assertEquals(0, search.matchCount());
    }

    @Test
    void givenContentToSearch_whenContentIsEmpty_thenFirstMatchIsOptionalEmpty() {
        final LogContent content = new LogContent();
        final TextSearch search = new TextSearch(content, SEARCH_STRING, false);
        assertTrue(search.getFirstMatch().isEmpty());
    }

    @Test
    void givenContentToSearch_whenContentContainsOneMatch_thenMatchCountIsOne() {
        final LogContent content = new LogContent();
        content.addLine(0, SINGLE_TEST_LINE);
        final TextSearch search = new TextSearch(content, SEARCH_STRING, false);
        assertEquals(1, search.matchCount());
    }

    @Test
    void givenContentToSearch_whenContentContainsOneMatch_thenFirstMatchIsCorrect() {
        final LogContent content = new LogContent();
        content.addLine(0, SINGLE_TEST_LINE);
        final TextSearch search = new TextSearch(content, SEARCH_STRING, false);
        final Optional<Location> match = search.getFirstMatch();
        assertTrue(match.isPresent());
        assertEquals(0, match.get().line());
        assertEquals(10, match.get().column());
    }

    @Test
    void givenContentToSearch_whenContentContainsOneCaseInsensitiveMatch_thenMatchCountOne() {
        final LogContent content = new LogContent();
        content.addLine(0, SINGLE_TEST_LINE);
        final TextSearch search = new TextSearch(content, SEARCH_STRING.toUpperCase(), true);
        assertEquals(1, search.matchCount());
    }

    @Test
    void givenContentToSearch_whenContentContainsTwoMatches_thenMatchCountIsTwo() {
        final LogContent content = new LogContent();
        content.addLine(0, SINGLE_TEST_LINE);
        content.addLine(1, SINGLE_TEST_LINE);
        final TextSearch search = new TextSearch(content, SEARCH_STRING, false);
        assertEquals(2, search.matchCount());
    }

    @Test
    void givenContentToSearchWithTwoMatches_whenRequestingSecondMatch_thenReturnsMatch() {
        final LogContent content = new LogContent();
        content.addLine(0, SINGLE_TEST_LINE);
        content.addLine(1, "Another " + SINGLE_TEST_LINE);
        final TextSearch search = new TextSearch(content, SEARCH_STRING, false);
        final Optional<Location> first = search.getFirstMatch();
        final Optional<Location> second = search.getNextMatchTo(first.get());
        assertEquals(1, second.get().line());
        assertEquals(18, second.get().column());
    }

    @Test
    void givenContentToSearchWithTwoMatches_whenRequestingPreviousMatchToSecond_thenReturnsMatch() {
        final LogContent content = new LogContent();
        content.addLine(0, SINGLE_TEST_LINE);
        content.addLine(1, "Another " + SINGLE_TEST_LINE);
        final TextSearch search = new TextSearch(content, SEARCH_STRING, false);
        final Optional<Location> first = search.getFirstMatch();
        final Optional<Location> second = search.getNextMatchTo(first.get());

        final Optional<Location> previous = search.getPreviousMatchTo(second.get());

        assertEquals(0, previous.get().line());
        assertEquals(10, previous.get().column());
    }
}