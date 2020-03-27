package com.example.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.exceptions.FileChunkingException;
import com.example.exceptions.FileInpuException;
import com.example.messages.CustomMessages;

public class FileChunker {

    private static final int FILE_SIZE_LIMIT = 10 * 1024 * 1024; // 10MB limit
    private static final String SPLIT_FILE_LOCATION_PATH = "SplitFileTemp";
    private static final String SPLIT_FILE_EXTENSION = ".split";
    private Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean validate(final File file) {
        if (!file.exists()) {
            throw new FileInpuException(CustomMessages.INCORRECT_INPUT_DETAILS);
        } else if (file.length() <= FILE_SIZE_LIMIT) {
            return false;
        }
        return true;
    }

    public File createChunk(File file) {

        if (!validate(file)) {
            // If file size is less than limit, we won't split it.
            return file;
        }
        long linescount = 0;

        try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(file))) {
            while (bufferedReader.readLine() != null) {
                linescount++;
            }
        } catch (IOException ioException) {
            throw new FileChunkingException(
                        CustomMessages.EXCEPTION_DURING_FILE_CHUNK_CREATION);
        }

        long numberOfSplit = file.length() / FILE_SIZE_LIMIT;
        long numberOfLinesPerSplit = linescount / numberOfSplit;
        long numberOfLinesInLastSplit = numberOfLinesPerSplit
                    + (linescount % numberOfSplit);
        try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(file))) {
            Path tempDirectory = Files.createTempDirectory(
                        SPLIT_FILE_LOCATION_PATH);
            long fileOffset = 0;
            for (; fileOffset < numberOfSplit - 1; fileOffset++) {
                Path splitFilePath = Files.createFile(
                            Paths.get(tempDirectory.toString(),
                                        String.valueOf(fileOffset).concat(
                                                    SPLIT_FILE_EXTENSION)));
                try (BufferedWriter bufferedWriter = new BufferedWriter(
                            new FileWriter(splitFilePath.toFile()))) {
                    int lineOffset = 0;
                    while (lineOffset < numberOfLinesPerSplit) {
                        String line = bufferedReader.readLine();
                        bufferedWriter.write(line);
                    }
                }

            }

            if (numberOfLinesInLastSplit > 0) {
                Path splitFilePath = Files.createFile(
                            Paths.get(tempDirectory.toString(),
                                        String.valueOf(fileOffset).concat(
                                                    SPLIT_FILE_EXTENSION)));
                try (BufferedWriter bufferedWriter = new BufferedWriter(
                            new FileWriter(splitFilePath.toFile()))) {
                    int lineOffset = 0;
                    while (lineOffset < numberOfLinesInLastSplit) {
                        String line = bufferedReader.readLine();
                        bufferedWriter.write(line);
                    }
                }
            }
            return tempDirectory.toFile();

        } catch (IOException ioException) {
            logger.error(
                        CustomMessages.EXCEPTION_DURING_FILE_CHUNK_CREATION.concat(
                                    CustomMessages.EXCEPTION_MESSAGE_PLACEHOLDER),
                        ioException.getMessage());
            throw new FileChunkingException(
                        CustomMessages.EXCEPTION_DURING_FILE_CHUNK_CREATION);
        }

    }

}
