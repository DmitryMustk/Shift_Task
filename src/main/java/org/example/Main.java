package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration cfg = CliArgsParser.getConfigurationFromCli(args);
            FileFilterUtility filterUtility = new FileFilterUtility();
            filterUtility.processFiles(cfg);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}