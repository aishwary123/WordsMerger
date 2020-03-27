package com.example.exceptions;

public class FileChunkingException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String message;

    public FileChunkingException(String message) {
        this.message = message;
    }

}
