package org.example;

import org.example.statistics.DataType;
import org.example.statistics.Statistics;
import org.example.statistics.StatisticsFactory;
import org.example.statistics.StatisticsType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileFilterUtility {
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("^-?\\d*\\.\\d+$|^-?\\d+\\.\\d*[eE][+-]?\\d+$");

    public void processFiles(Configuration config) throws IOException {
        Map<DataType, List<String>> dataMap = new EnumMap<>(DataType.class);
        config.getInputFiles().forEach(file -> readAndClassify(file, dataMap));

        Map<DataType, Statistics> statisticsMap = collectStatistics(dataMap);
        writeOutputFiles(config, dataMap);
        printStatistics(config, statisticsMap);
    }

    private void writeOutputFiles(Configuration config, Map<DataType, List<String>> dataMap) {
        for (Map.Entry<DataType, List<String>> entry : dataMap.entrySet()) {
            DataType type = entry.getKey();
            List<String> data = entry.getValue();
            Path outputPath = Paths.get(config.getOutputPath(), config.getPrefix() + type.getFilename());
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath, config.isAppendMode() ? StandardOpenOption.APPEND : StandardOpenOption.CREATE, StandardOpenOption.CREATE)) {
                for (String line : data) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Failed to write to file " + outputPath + ": " + e.getMessage());
            }
        }
    }

    private void printStatistics(Configuration config, Map<DataType, Statistics> statsMap) {
        statsMap.forEach((type, stats) -> {
            System.out.println(type.getLabel() + " Statistics:");
            if (config.getStatisticsType() == StatisticsType.FULL) {
                System.out.println(stats.fullStatistics());
            } else {
                System.out.println(stats.shortStatistics());
            }
        });
    }

    private Map<DataType, Statistics> collectStatistics(Map<DataType, List<String>> dataMap) {
        Map<DataType, Statistics> statisticsMap = new EnumMap<>(DataType.class);
        dataMap.forEach((type, data) -> {
            Statistics statistics = StatisticsFactory.createStatistics(type);
            data.forEach(statistics::add);
            statisticsMap.put(type, statistics);
        });
        return statisticsMap;
    }

    private void readAndClassify(String file, Map<DataType, List<String>> dataMap) {
        try (Stream<String> lines = Files.lines(Paths.get(file))) {
            lines.forEach(line -> classifyLine(line, dataMap));
        } catch (IOException e) {
            System.err.println("Failed to read file " + file + ": " + e.getMessage());
        }
    }

    private void classifyLine(String line, Map<DataType, List<String>> dataMap) {
        if (INTEGER_PATTERN.matcher(line).matches()) {
            dataMap.computeIfAbsent(DataType.INTEGER, k -> new ArrayList<>()).add(line);
        } else if (FLOAT_PATTERN.matcher(line).matches()) {
            dataMap.computeIfAbsent(DataType.FLOAT, k -> new ArrayList<>()).add(line);
        } else {
            dataMap.computeIfAbsent(DataType.STRING, k -> new ArrayList<>()).add(line);
        }
    }
}
