package org.example.statistics;

public class StatisticsFactory {
    public static Statistics createStatistics(DataType type) {
        return switch (type) {
            case INTEGER -> new IntegerStatistics();
            case FLOAT -> new FloatStatistics();
            case STRING -> new StringStatistics();
        };
    }
}
