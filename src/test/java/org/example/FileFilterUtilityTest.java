package org.example;

import org.example.statistics.DataType;
import org.example.statistics.StatisticsType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileFilterUtilityTest {

    private FileFilterUtility utility;
    private Configuration config;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        utility = new FileFilterUtility();
        config = new Configuration();
        config.setOutputPath(tempDir.toString());
        config.setPrefix("sample-");
        config.setAppendMode(false);
        config.setStatisticsType(StatisticsType.SHORT);
    }

    @Test
    void testProcessFiles() throws IOException {
        // Создаем временные файлы для входных данных
        Path inputFile1 = Files.createTempFile("in1", ".txt");
        Path inputFile2 = Files.createTempFile("in2", ".txt");

        // Записываем тестовые данные в файлы
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

        // Устанавливаем входные файлы в конфигурации
        config.setInputFiles(Arrays.asList(inputFile1.toString(), inputFile2.toString()));

        // Запускаем обработку файлов
        utility.processFiles(config);

        // Проверяем, что выходные файлы созданы
        Path outputIntegers = Paths.get(config.getOutputPath(), config.getPrefix() + DataType.INTEGER.getFilename());
        Path outputFloats = Paths.get(config.getOutputPath(), config.getPrefix() + DataType.FLOAT.getFilename());
        Path outputStrings = Paths.get(config.getOutputPath(), config.getPrefix() + DataType.STRING.getFilename());

        assertTrue(Files.exists(outputIntegers));
        assertTrue(Files.exists(outputFloats));
        assertTrue(Files.exists(outputStrings));

        // Проверяем содержимое выходных файлов
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
