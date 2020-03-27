package com.example.messages;

public class CustomMessages {

    private CustomMessages() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

    public static final String INCORRECT_INPUT_DETAILS = "Incorrect file input details";
    public static final String EXCEPTION_DURING_FILE_MERGER_INSTANCE_CREATION = "Exception during file merger instance creation";
    public static final String EXCEPTION_DURING_FILE_CHUNK_CREATION = "Exception during file chunk creation";
    public static final String EXCEPTION_DURING_FILE_CHUNKING_INVOCATION = "Exception during file chunk process invocation";
    public static final String EXCEPTION_DURING_FILE_MERGER_OPERATION = "Exception during file merger operation";
    public static final String EXCEPTION_DURING_FILE_PROCESSING = "Exception during file processing";
    public static final String EXCEPTION_DURING_FILE_CLOSE_OPERATION = "Exception during file close operation";
    public static final String EXCEPTION_DURING_FILE_DELETE_OPERATION = "Exception during file delete operation";
    public static final String INCORRECT_DIRECTORY_SPECIFIED = "Incorrect directory specified";
    public static final String EXCEPTION_MESSAGE_PLACEHOLDER = ":{}";
    public static final String UTILITY_CLASS = "Utility Class: Instance can not be created";
}
