package org.example;

import org.example.statistics.StatisticsType;
import org.junit.jupiter.api.Test;

import javax.naming.ConfigurationException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @Test
    void testParseDefaultValues() {
        String[] args = new String[]{};

        Configuration cfg = CliArgsParser.getConfigurationFromCli(args);

        assertEquals(".", cfg.outputPath());
        assertEquals("", cfg.prefix());
        assertFalse(cfg.appendMode());
        assertEquals(StatisticsType.SHORT, cfg.statisticsType());
        assertTrue(cfg.inputFiles().isEmpty());
    }

    @Test
    void testParseWithValidInputFiles() throws Exception {
        Path tempFile1 = Files.createTempFile("testfile1", ".txt");
        Path tempFile2 = Files.createTempFile("testfile2", ".txt");

        String[] args = new String[]{tempFile1.toString(), tempFile2.toString()};

        Configuration cfg = CliArgsParser.getConfigurationFromCli(args);

        assertEquals(List.of(tempFile1.toString(), tempFile2.toString()), cfg.inputFiles());

        Files.delete(tempFile1);
        Files.delete(tempFile2);
    }

    @Test
    void testParseWithOutputOption() {
        String[] args = new String[]{"-o", "/valid/output/path"};

        Configuration cfg = CliArgsParser.getConfigurationFromCli(args);

        assertEquals("/valid/output/path", cfg.outputPath());
    }

    @Test
    void testParseWithPrefixOption() {
        String[] args = new String[]{"-p", "prefix_"};

        Configuration cfg = CliArgsParser.getConfigurationFromCli(args);

        assertEquals("prefix_", cfg.prefix());
    }

    @Test
    void testParseWithAppendFlag() {
        String[] args = new String[]{"-a"};

        Configuration cfg = CliArgsParser.getConfigurationFromCli(args);

        assertTrue(cfg.appendMode());
    }

    @Test
    void testParseWithShortStatisticsFlag() {
        String[] args = new String[]{"-s"};

        Configuration cfg = CliArgsParser.getConfigurationFromCli(args);

        assertEquals(StatisticsType.SHORT, cfg.statisticsType());
    }

    @Test
    void testParseWithFullStatisticsFlag() {
        String[] args = new String[]{"-f"};

        Configuration cfg = CliArgsParser.getConfigurationFromCli(args);

        assertEquals(StatisticsType.FULL, cfg.statisticsType());
    }
}