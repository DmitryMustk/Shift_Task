package org.example.statistics;

public class StatisticsFactory {
    public static Statistics createStatistics(DataType type) {
        switch (type) {
            case INTEGER:
                return new IntegerStatistics();
            case FLOAT:
                return new FloatStatistics();
            case STRING:
                return new StringStatistics();
            default:
                throw new IllegalArgumentException("Unsupported data type: " + type);
        }
    }
}
