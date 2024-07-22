package org.example;

import org.example.statistics.StatisticsType;
import java.util.List;

public record Configuration(List<String> inputFiles,
                            String outputPath,
                            String prefix,
                            boolean appendMode,
                            StatisticsType statisticsType) {
}

