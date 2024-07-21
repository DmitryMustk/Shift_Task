package org.example.statistics;

public class StringStatistics implements Statistics {
    int count = 0;
    int minLength = Integer.MAX_VALUE;
    int maxLength = 0;

    @Override
    public void add(String value) {
        count++;
        minLength = Math.min(minLength, value.length());
        maxLength = Math.max(maxLength, value.length());
    }

    @Override
    public String shortStatistics() {
        return "Count: " + count;
    }

    @Override
    public String fullStatistics() {
        return String.format("Count: %d, Min: %d, Max: %d", count, minLength, maxLength);
    }
}
