package com.example.exceptions;

public class IncorrectDirectoryProcessorException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String message;

    public IncorrectDirectoryProcessorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
