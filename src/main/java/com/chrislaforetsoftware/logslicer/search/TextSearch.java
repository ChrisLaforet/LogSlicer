package com.chrislaforetsoftware.logslicer.search;

import com.chrislaforetsoftware.logslicer.log.LogContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextSearch {

    private final String searchString;
    private final List<Location> locations = new ArrayList<>();

    public TextSearch(LogContent content, String searchString, boolean caseInsensitive) {
        this.searchString = searchString;
        if (caseInsensitive) {
            searchString = searchString.toUpperCase();
        }

        for (int currentLine = 0; currentLine < content.lineCount(); currentLine++) {
            String haystack = content.getTextFor(currentLine);
            if (caseInsensitive) {
                haystack = haystack.toUpperCase();
            }
            int column = 0;
            while (true) {
                int offset = haystack.indexOf(searchString, column);
                if (offset < 0) {
                    break;
                }
                locations.add(new Location(currentLine, offset));
                column = offset + 1;
            }
        }
    }

    public String getSearchString() {
        return searchString;
    }

    public int matchCount() {
        return locations.size();
    }

    public Optional<Location> getFirstMatch() {
        if (locations.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(locations.get(0));
    }

    public Optional<Location> getPreviousMatchTo(Location index) {
        for (int offset = locations.size() - 1; offset >= 0; offset--) {
            final Location location = locations.get(offset);
            if (location.line() > index.line()) {
                continue;
            }
            if ((location.line() == index.line() &&
                    location.column() < index.column()) ||
                    (location.line() < index.line())) {
                return Optional.of(location);
            }
        }

        return Optional.empty();
    }

    public Optional<Location> getNextMatchTo(Location index) {
        for (var location : locations) {
            if (location.line() < index.line()) {
                continue;
            }
            if ((location.line() == index.line() &&
                    location.column() > index.column()) ||
                    (location.line() > index.line())) {
                return Optional.of(location);
            }
        }

    return Optional.empty();
    }
}
