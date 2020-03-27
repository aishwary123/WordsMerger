package com.example.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.exceptions.FileInpuException;
import com.example.exceptions.FileMergingException;
import com.example.messages.CustomMessages;

public class TwoFileMerger implements IFileMerger {

    protected File file1;
    protected File file2;
    protected String outputPath;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String DEFAULT_FILE_EXTENSION = ".txt";
    private static final String TEMP_FOLDER_NAME = "FileMergeTemp";

    public TwoFileMerger(final File file1,
                         final File file2)
        throws IOException {

        this.file1 = file1;
        this.file2 = file2;
        Path outputFolderPath = Files.createTempDirectory(TEMP_FOLDER_NAME);
        this.outputPath = outputFolderPath.toString();

    }

    public TwoFileMerger(final File file1,
                         final File file2,
                         final String outputFolder)
        throws IOException {
        this.file1 = file1;
        this.file2 = file2;
        this.outputPath = outputFolder;
        Path outputFolderPath = Paths.get(outputFolder);
        if (!outputFolderPath.toFile().exists()) {
            Files.createDirectories(outputFolderPath);
        }

    }

    @Override
    public File mergeFiles() {

        if (null == file1 && null == file2) {
            throw new FileInpuException(CustomMessages.INCORRECT_INPUT_DETAILS);
        } else if (null != file1 && null == file2) {
            return file1;
        } else if (null != file2 && null == file1) {
            return file2;
        }

        try (BufferedReader fileReader1 = new BufferedReader(
                    new FileReader(file1.getAbsolutePath()));
                    BufferedReader fileReader2 = new BufferedReader(
                                new FileReader(file2.getAbsolutePath()))) {
            final String resultFileName = generateFileName(file1, file2);
            Path resultFilePath = Paths.get(outputPath, resultFileName);
            Files.deleteIfExists(resultFilePath);
            Files.createFile(resultFilePath);
            File outputFile = resultFilePath.toFile();
            try (BufferedWriter fileWriter = new BufferedWriter(
                        new FileWriter(outputFile))) {
                String word1 = fileReader1.readLine();
                if (null != word1)
                    word1 = word1.trim();
                String word2 = fileReader2.readLine();
                if (null != word2)
                    word2 = word2.trim();
                while (Strings.isNotEmpty(word1) && Strings.isNotEmpty(word2)) {
                    if (word1.compareTo(word2) < 0) {
                        fileWriter.write(word1);
                        fileWriter.newLine();
                        word1 = fileReader1.readLine();
                        if (null != word1)
                            word1 = word1.trim();

                    } else if (word1.compareTo(word2) > 0) {
                        fileWriter.write(word2);
                        fileWriter.newLine();
                        word2 = fileReader2.readLine();
                        if (null != word2)
                            word2 = word2.trim();
                    } else {
                        fileWriter.write(word1);
                        fileWriter.newLine();
                        fileWriter.write(word2);
                        fileWriter.newLine();
                        word1 = fileReader1.readLine();
                        if (null != word1)
                            word1 = word1.trim();
                        word2 = fileReader2.readLine();
                        if (null != word2)
                            word2 = word2.trim();
                    }
                }
                if (Strings.isEmpty(word1)) {
                    while (Strings.isNotEmpty(word2)) {
                        fileWriter.write(word2);
                        fileWriter.newLine();
                        word2 = fileReader2.readLine();
                        if (null != word2)
                            word2 = word2.trim();
                    }
                }
                if (Strings.isEmpty(word2)) {
                    while (Strings.isNotEmpty(word1)) {
                        fileWriter.write(word1);
                        fileWriter.newLine();
                        word1 = fileReader1.readLine();
                        if (null != word1)
                            word1 = word1.trim();
                    }
                }

                return outputFile;
            }

        } catch (Exception exception) {
            logger.error(
                        CustomMessages.EXCEPTION_DURING_FILE_MERGER_OPERATION.concat(
                                    CustomMessages.EXCEPTION_MESSAGE_PLACEHOLDER),
                        exception.getMessage());
            throw new FileMergingException(
                        CustomMessages.EXCEPTION_DURING_FILE_MERGER_OPERATION);
        }
    }

    public String generateFileName(final File file1,
                                   final File file2) {

        final String file1Name = file1.getName().indexOf(
                    FILE_EXTENSION_SEPARATOR) == -1
                                ? file1.getName()
                                : file1.getName().substring(0,
                                            file1.getName().indexOf(
                                                        FILE_EXTENSION_SEPARATOR));

        final String file2Name = file2.getName().indexOf(
                    FILE_EXTENSION_SEPARATOR) == -1
                                ? file2.getName()
                                : file2.getName().substring(0,
                                            file2.getName().indexOf(
                                                        FILE_EXTENSION_SEPARATOR));

        long num1 = Long.parseLong(file1Name);
        if (num1 == 0)
            num1 = 1000;
        long num2 = Long.parseLong(file2Name);
        num1 = num1 + num2 + System.currentTimeMillis()
                    + new Random().nextInt(1000);
        String fileExtension;

        if (file1.getName().indexOf(FILE_EXTENSION_SEPARATOR) == -1) {
            if (file2.getName().indexOf(FILE_EXTENSION_SEPARATOR) == -1) {
                fileExtension = DEFAULT_FILE_EXTENSION;
            } else {
                fileExtension = file2.getName().substring(
                            file2.getName().indexOf(FILE_EXTENSION_SEPARATOR));
            }
        } else {
            fileExtension = file1.getName().substring(
                        file1.getName().indexOf(FILE_EXTENSION_SEPARATOR));
        }

        return num1 + fileExtension;

    }

    public void closeChannel(Closeable channel)
        throws IOException {
        try {
            if (null != channel)
                channel.close();
        } catch (IOException fileCloseException) {
            logger.error(
                        CustomMessages.EXCEPTION_DURING_FILE_CLOSE_OPERATION.concat(
                                    CustomMessages.EXCEPTION_MESSAGE_PLACEHOLDER),
                        fileCloseException.getMessage());

            throw fileCloseException;
        }
    }
}
