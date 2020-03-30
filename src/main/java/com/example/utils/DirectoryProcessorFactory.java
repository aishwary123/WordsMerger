package com.example.utils;

import java.util.Properties;

import com.example.configs.CommonConfig;
import com.example.exceptions.IncorrectDirectoryProcessorException;
import com.example.messages.CustomMessages;
import com.example.models.DirectoryProcessorType;
import com.example.services.DirectoryProcessor;
import com.example.services.TwoFilesProcessor;

public class DirectoryProcessorFactory {

    private DirectoryProcessorFactory() {
        throw new IllegalStateException(CustomMessages.FACTORY_CLASS);
    }

    public static DirectoryProcessor getDirectoryProcessorInstance() {
        Properties systemConfigs = (Properties) BeanUtil.getBean(
                    "systemConfigs");
        if (DirectoryProcessorType.fromOrdinal(
                    Integer.parseInt(systemConfigs.getProperty(
                                CommonConfig.FILE_MERGE_SIZE))) == DirectoryProcessorType.TWO_FILES_PROCESSOR) {
            int executorSize = Integer.parseInt(
                        systemConfigs.getProperty(CommonConfig.EXECUTOR_SIZE));
            return new TwoFilesProcessor(executorSize);
        }
        throw new IncorrectDirectoryProcessorException(
                    CustomMessages.INCORRECT_PROCESSOR_TYPE_SPECIFIED);
    }

}
