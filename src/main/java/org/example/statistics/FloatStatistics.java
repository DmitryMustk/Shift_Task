package org.example.statistics;

public class FloatStatistics implements Statistics {
    private int count = 0;
    private float min = Float.MAX_VALUE;
    private float max = Float.MIN_VALUE;
    private float sum = 0;

    @Override
    public void add(String value) {
        float floatValue = Float.parseFloat(value);
        count++;
        sum += floatValue;
        min = Math.min(floatValue, min);
        max = Math.max(floatValue, max);
    }

    @Override
    public String shortStatistics() {
        return "Count: " + count;
    }

    @Override
    public String fullStatistics() {
        return String.format("Count: %d, Min: %s, Max: %s, Avg: %.2f", count, min, max, sum / count);
    }
}
