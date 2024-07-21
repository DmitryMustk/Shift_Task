package org.example.statistics;

public class IntegerStatistics implements Statistics {
    private int count = 0;
    private Long min = Long.MAX_VALUE;
    private Long max = Long.MIN_VALUE;
    private Long sum = 0L;

    @Override
    public void add(String value) {
        Long intVal = Long.parseLong(value);
        count++;
        sum += intVal;
        min = Math.min(intVal, min);
        max = Math.max(intVal, max);
    }

    @Override
    public String shortStatistics() {
        return "Count: " + count;
    }

    @Override
    public String fullStatistics() {
        return String.format("Sum: %d, Min: %d, Max: %d, Avg: %.2f", sum, min, max, (double)sum / count);
    }

}
