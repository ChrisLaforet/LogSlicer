package com.chrislaforetsoftware.logslicer.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LogContent {

    private List<LogLine> lines = new ArrayList<>();

    public LogContent(File file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(new LogLine(line));
                line = reader.readLine();
            }
        }
    }

    public int lineCount() {
        return lines.size();
    }
}
