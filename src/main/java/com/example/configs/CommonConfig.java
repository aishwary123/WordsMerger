package com.example.configs;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.exceptions.VariableNotFoundException;
import com.example.utils.SystemUtils;

@Configuration
public class CommonConfig {

    private static final String EXECUTOR_SIZE = "EXECUTOR_SIZE";
    private static final String FILE_MERGE_SIZE = "FILE_MERGE_SIZE";

    @Value("${EXECUTOR_SIZE}")
    private String defaultExecutorSize;

    @Value("${FILE_MERGE_SIZE}")
    private String defaultFileMergeSize;

    @Bean
    public Properties systemConfig()
        throws VariableNotFoundException {

        final String executorSize = SystemUtils.getSystemVariableOrDefault(
                    EXECUTOR_SIZE, defaultExecutorSize);
        final String fileMergeSize = SystemUtils.getSystemVariableOrDefault(
                    FILE_MERGE_SIZE, defaultFileMergeSize);
        Properties systemConfig = new Properties();
        systemConfig.setProperty(EXECUTOR_SIZE, executorSize);
        systemConfig.setProperty(FILE_MERGE_SIZE, fileMergeSize);
        return systemConfig;
    }

}
