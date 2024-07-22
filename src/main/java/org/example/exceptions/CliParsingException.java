package org.example.exceptions;


public class CliParsingException extends RuntimeException {
    public CliParsingException(String message) {
        super(message);
    }
}
