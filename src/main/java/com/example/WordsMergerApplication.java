package com.example;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.exceptions.FileInpuException;
import com.example.messages.CustomMessages;
import com.example.services.DirectoryProcessor;
import com.example.utils.DirectoryProcessorFactory;

@SpringBootApplication
public class WordsMergerApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                WordsMergerApplication.class);

    private static final String OUTPUT_FILE_PATH = "OUTPUT FILE PATH";
    private static final String SOURCE_DIRECTORY = "SOURCE_DIRECTORY";

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(WordsMergerApplication.class);
    }

    @Override
    public void run(String... args)
        throws Exception {

        try {
            final String sourceDirectory = System.getenv(SOURCE_DIRECTORY);
            if (Strings.isEmpty(sourceDirectory)) {
                if(LOGGER.isErrorEnabled()){
                    LOGGER.error(CustomMessages.INCORRECT_INPUT_DETAILS.concat(
                            CustomMessages.EXCEPTION_MESSAGE_PLACEHOLDER),
                            CustomMessages.SOURCE_DIRECTORY_VALUE_MISSING);
                }
                throw new FileInpuException(
                            CustomMessages.INCORRECT_INPUT_DETAILS.concat(
                                        CustomMessages.SOURCE_DIRECTORY_VALUE_MISSING));
            }
            DirectoryProcessor directoryProcessor = DirectoryProcessorFactory.getDirectoryProcessorInstance();
            Path directoryPath = Paths.get(sourceDirectory);
            File outputFile = directoryProcessor.processDirectory(
                        directoryPath.toFile());
            if (null != outputFile && LOGGER.isInfoEnabled()) {
                LOGGER.info(OUTPUT_FILE_PATH.concat(
                            CustomMessages.EXCEPTION_MESSAGE_PLACEHOLDER),
                            outputFile.getAbsolutePath());
            } else {
                LOGGER.error(CustomMessages.WORDS_MERGE_OPERATION_FAILED);
            }
        } finally {
            System.exit(SpringApplication.exit(context));
        }

    }

}
