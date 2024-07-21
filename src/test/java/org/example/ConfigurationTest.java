package org.example;

import org.example.exceptions.ConfigurationException;
import org.example.statistics.StatisticsType;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @Test
    void testParseDefaultValues() throws ConfigurationException {
        String[] args = new String[]{};

        Configuration cfg = Configuration.parse(args);

        assertEquals(".", cfg.getOutputPath());
        assertEquals("", cfg.getPrefix());
        assertFalse(cfg.isAppendMode());
        assertEquals(StatisticsType.SHORT, cfg.getStatisticsType());
        assertTrue(cfg.getInputFiles().isEmpty());
    }

    @Test
    void testParseWithValidInputFiles() throws Exception {
        Path tempFile1 = Files.createTempFile("testfile1", ".txt");
        Path tempFile2 = Files.createTempFile("testfile2", ".txt");

        String[] args = new String[]{tempFile1.toString(), tempFile2.toString()};

        Configuration cfg = Configuration.parse(args);

        assertEquals(List.of(tempFile1.toString(), tempFile2.toString()), cfg.getInputFiles());

        Files.delete(tempFile1);
        Files.delete(tempFile2);
    }

    @Test
    void testParseWithOutputOption() throws ConfigurationException {
        String[] args = new String[]{"-o", "/valid/output/path"};

        Configuration cfg = Configuration.parse(args);

        assertEquals("/valid/output/path", cfg.getOutputPath());
    }

    @Test
    void testParseWithPrefixOption() throws ConfigurationException {
        String[] args = new String[]{"-p", "prefix_"};

        Configuration cfg = Configuration.parse(args);

        assertEquals("prefix_", cfg.getPrefix());
    }

    @Test
    void testParseWithAppendFlag() throws ConfigurationException {
        String[] args = new String[]{"-a"};

        Configuration cfg = Configuration.parse(args);

        assertTrue(cfg.isAppendMode());
    }

    @Test
    void testParseWithShortStatisticsFlag() throws ConfigurationException {
        String[] args = new String[]{"-s"};

        Configuration cfg = Configuration.parse(args);

        assertEquals(StatisticsType.SHORT, cfg.getStatisticsType());
    }

    @Test
    void testParseWithFullStatisticsFlag() throws ConfigurationException {
        String[] args = new String[]{"-f"};

        Configuration cfg = Configuration.parse(args);

        assertEquals(StatisticsType.FULL, cfg.getStatisticsType());
    }
}