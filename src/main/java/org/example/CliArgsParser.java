package org.example;

import org.apache.commons.cli.*;
import org.example.exceptions.CliParsingException;
import org.example.statistics.StatisticsType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CliArgsParser {
    private static final String DEFAULT_OUTPUT_PATH = ".";
    private static final String DEFAULT_PREFIX = "";
    private static final boolean DEFAULT_APPEND_MODE = false;
    private static final StatisticsType DEFAULT_STATISTICS_TYPE = StatisticsType.SHORT;

    private static Configuration configuration;


    public static Configuration getConfigurationFromCli(String[] args) {
        parse(args);
        return configuration;
    }

    private static void parse(String[] args) throws CliParsingException {
        Options options = new Options();
        addOptions(options);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            String outputPath = cmd.getOptionValue("o", DEFAULT_OUTPUT_PATH);
            if (!isOutputPathValid(outputPath)) {
                throw new CliParsingException("Invalid output path: " + outputPath);
            }

            String prefix = cmd.getOptionValue("p", DEFAULT_PREFIX);
            boolean appendMode = cmd.hasOption("a") || DEFAULT_APPEND_MODE;
            StatisticsType statisticsType = cmd.hasOption("f") ? StatisticsType.FULL : DEFAULT_STATISTICS_TYPE;

            List<String> inputFiles = Arrays.stream(cmd.getArgs())
                    .filter(CliArgsParser::isInputFileValid)
                    .toList();

            configuration = new Configuration(inputFiles, outputPath, prefix, appendMode, statisticsType);
        } catch (ParseException e) {
            throw new CliParsingException("Failed to parse command line arguments");
        }
    }

    private static void addOptions(Options options) {
        options.addOption("o", "output", true, "output path");
        options.addOption("p", "prefix", true, "prefix for output files");

        options.addOption("a", "append", false, "append mode");
        options.addOption("s", "short statistics", false, "get short statistics");
        options.addOption("f", "full statistics", false, "get full statistics");
    }

    private static boolean isInputFileValid(String filename) {
        Path path = Paths.get(filename);
        return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
    }

    private static boolean isOutputPathValid(String filename) {
        Path path = Paths.get(filename);
        return Files.exists(path) && Files.isDirectory(path);
    }
}
