package org.example.statistics;


public enum DataType {
    INTEGER("integers.txt", "Integer"),
    FLOAT("floats.txt", "Float"),
    STRING("strings.txt", "String"),
    ;

    private final String fileName;
    private final String label;

    DataType(String fileName, String label) {
        this.fileName = fileName;
        this.label = label;
    }

    public String getFilename() {
        return fileName;
    }

    public String getLabel() {
        return label;
    }
}
