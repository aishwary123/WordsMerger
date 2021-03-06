package com.example.configs;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.utils.SystemUtils;

/**
 * ALL THE COMMON CONFIGURATION WILL BE MAINTAINED HERE.
 * 
 * @author aishwaryt
 */
@Configuration
public class CommonConfig {

    public static final String EXECUTOR_SIZE = "EXECUTOR_SIZE";
    public static final String FILE_MERGE_SIZE = "FILE_MERGE_SIZE";

    @Value("${EXECUTOR_SIZE}")
    private String defaultExecutorSize;

    @Value("${FILE_MERGE_SIZE}")
    private String defaultFileMergeSize;

    @Bean
    public Properties systemConfigs() {

        final String executorSize = SystemUtils.getSystemVariableOrDefault(
                    EXECUTOR_SIZE, defaultExecutorSize);
        final String fileMergeSize = SystemUtils.getSystemVariableOrDefault(
                    FILE_MERGE_SIZE, defaultFileMergeSize);
        Properties systemConfigs = new Properties();
        systemConfigs.setProperty(EXECUTOR_SIZE, executorSize);
        systemConfigs.setProperty(FILE_MERGE_SIZE, fileMergeSize);
        return systemConfigs;
    }

}
