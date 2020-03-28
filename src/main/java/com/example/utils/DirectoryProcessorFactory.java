package com.example.utils;

import java.util.Properties;

import com.example.exceptions.IncorectDirectoryProcessorException;
import com.example.messages.CustomMessages;
import com.example.models.DirectoryProcessorType;
import com.example.services.DirectoryProcessor;
import com.example.services.TwoFilesProcessor;

public class DirectoryProcessorFactory {

    private static final String EXECUTOR_SIZE = "EXECUTOR_SIZE";
    private static final String FILE_MERGE_SIZE = "FILE_MERGE_SIZE";

    private DirectoryProcessorFactory() {
        throw new IllegalStateException(CustomMessages.FACTORY_CLASS);
    }

    public static DirectoryProcessor getDirectoryProcessorInstance() {
        Properties systemConfig = (Properties) BeanUtil.getBean("systemConfig");
        if (DirectoryProcessorType.fromOrdinal(
                    Integer.parseInt(systemConfig.getProperty(
                                FILE_MERGE_SIZE))) == DirectoryProcessorType.TWO_FILES_PROCESSOR) {
            int executorSize = Integer.parseInt(
                        systemConfig.getProperty(EXECUTOR_SIZE));
            return new TwoFilesProcessor(executorSize);
        }
        throw new IncorectDirectoryProcessorException(
                    CustomMessages.INCORRECT_PROCESSOR_TYPE_SPECIFIED);
    }

}
