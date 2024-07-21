package org.example.exceptions;

import org.apache.commons.cli.ParseException;

public class CliParsingException extends ParseException {

    public CliParsingException(String message) {
        super(message);
    }
}
