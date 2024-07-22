package org.example;

import org.example.statistics.DataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileFilterUtilityTest { ;
    @Test
    void testProcessFiles(@TempDir Path tempDir) throws IOException {
        Path inputFile1 = Files.createTempFile("in1", ".txt");
        Path inputFile2 = Files.createTempFile("in2", ".txt");

        List<String> lines1 = Arrays.asList(
                "Lorem ipsum dolor sit amet",
                "45",
                "Пример",
                "3.1415",
                "consectetur adipiscing",
                "-0.001",
                "тестовое задание",
                "100500"
        );

        List<String> lines2 = Arrays.asList(
                "Нормальная форма числа с плавающей запятой",
                "1.528535047E-25",
                "Long",
                "1234567890123456789"
        );

        Files.write(inputFile1, lines1);
        Files.write(inputFile2, lines2);

        FileFilterUtility utility = new FileFilterUtility();
        String[] args = {"-o", tempDir.toAbsolutePath().toString(), "-p", "sample", "-s", inputFile1.toString(), inputFile2.toString()};
        Configuration config = CliArgsParser.getConfigurationFromCli(args);


        utility.processFiles(config);

        Path outputIntegers = Paths.get(config.outputPath(), config.prefix() + DataType.INTEGER.getFilename());
        Path outputFloats = Paths.get(config.outputPath(), config.prefix() + DataType.FLOAT.getFilename());
        Path outputStrings = Paths.get(config.outputPath(), config.prefix() + DataType.STRING.getFilename());

        assertTrue(Files.exists(outputIntegers));
        assertTrue(Files.exists(outputFloats));
        assertTrue(Files.exists(outputStrings));

        List<String> expectedIntegers = Arrays.asList("45", "100500", "1234567890123456789");
        List<String> expectedFloats = Arrays.asList("3.1415", "-0.001", "1.528535047E-25");
        List<String> expectedStrings = Arrays.asList(
                "Lorem ipsum dolor sit amet",
                "Пример",
                "consectetur adipiscing",
                "тестовое задание",
                "Нормальная форма числа с плавающей запятой",
                "Long"
        );

        assertLinesMatch(expectedIntegers, Files.readAllLines(outputIntegers));
        assertLinesMatch(expectedFloats, Files.readAllLines(outputFloats));
        assertLinesMatch(expectedStrings, Files.readAllLines(outputStrings));
    }
}
