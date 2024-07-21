package org.example;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.exceptions.ConfigurationException;
import org.example.statistics.StatisticsType;
import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Getter
@Setter
public class Configuration {
    private List<String> inputFiles;
    private String outputPath;
    private String prefix;
    private boolean appendMode;
    private StatisticsType statisticsType;

    private static final String DEFAULT_OUTPUT_PATH = ".";
    private static final String DEFAULT_PREFIX = "";
    private static final boolean DEFAULT_APPEND_MODE = false;
    private static final StatisticsType DEFAULT_STATISTICS_TYPE = StatisticsType.SHORT;

    Configuration() {
        this.inputFiles = new ArrayList<>();
        this.outputPath = DEFAULT_OUTPUT_PATH;
        this.prefix = DEFAULT_PREFIX;
        this.appendMode = DEFAULT_APPEND_MODE;
        this.statisticsType = DEFAULT_STATISTICS_TYPE;
    }

    public static Configuration parse(String[] args) {
        Options options = new Options();
        addOptions(options);

        Configuration cfg = new Configuration();
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            processOption(cmd, "o", Configuration::isOutputFileValid, cfg::setOutputPath);
            processOption(cmd, "p", null, cfg::setPrefix);
            processFlag(cmd, "a", cfg::setAppendMode, true);
            processFlag(cmd, "s", (val) -> cfg.setStatisticsType(StatisticsType.SHORT), null);
            processFlag(cmd, "f", (val) -> cfg.setStatisticsType(StatisticsType.FULL), null);

            cfg.setInputFiles(Arrays.stream(cmd.getArgs())
                    .filter(Configuration::isInputFileValid)
                    .toList());

            return cfg;
        } catch (ParseException e) {
            throw new ConfigurationException("Failed to parse command line arguments");
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

    private static boolean isOutputFileValid(String filename) {
        Path path = Paths.get(filename);
        return Files.exists(path) && Files.isRegularFile(path) && Files.isWritable(path);
    }

    private static void processOption(CommandLine cmd, String option, OptionValidator validator, OptionConsumer consumer) throws ConfigurationException {
        if (cmd.hasOption(option)) {
            String value = cmd.getOptionValue(option);
            if (validator != null) {
                validator.validate(value);
            }
            consumer.accept(value);
        }
    }

    private static void processFlag(CommandLine cmd, String option, FlagConsumer consumer, Boolean value) {
        if (cmd.hasOption(option)) {
            consumer.accept(value);
        }
    }


    @FunctionalInterface
    private interface OptionValidator {
        void validate(String value) throws ConfigurationException;
    }

    @FunctionalInterface
    private interface OptionConsumer {
        void accept(String value) throws ConfigurationException;
    }

    @FunctionalInterface
    private interface FlagConsumer {
        void accept(Boolean value) throws ConfigurationException;
    }
}
