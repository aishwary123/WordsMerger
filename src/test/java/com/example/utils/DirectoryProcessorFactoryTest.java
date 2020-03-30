package com.example.utils;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.example.configs.CommonConfig;
import com.example.exceptions.IncorrectDirectoryProcessorException;
import com.example.services.DirectoryProcessor;
import com.example.services.TwoFilesProcessor;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BeanUtil.class })
public class DirectoryProcessorFactoryTest {

    @Test
    public void testGetDirectoryProcessorInstance_Success() {
        PowerMockito.mockStatic(BeanUtil.class);
        Properties systemConfig = new Properties();
        systemConfig.put(CommonConfig.FILE_MERGE_SIZE, "2");
        systemConfig.put(CommonConfig.EXECUTOR_SIZE, "10");
        PowerMockito.when(BeanUtil.getBean("systemConfigs")).thenReturn(
                    systemConfig);
        DirectoryProcessor directoryProcessor = DirectoryProcessorFactory.getDirectoryProcessorInstance();
        Assert.assertThat(directoryProcessor,
                    instanceOf(TwoFilesProcessor.class));
    }

    @Test(expected = IncorrectDirectoryProcessorException.class)
    public void testGetDirectoryProcessorInstance_FailureForIncorrectFileMergeSize() {
        PowerMockito.mockStatic(BeanUtil.class);
        Properties systemConfig = new Properties();
        systemConfig.put(CommonConfig.FILE_MERGE_SIZE, "3");
        systemConfig.put(CommonConfig.EXECUTOR_SIZE, "10");
        PowerMockito.when(BeanUtil.getBean("systemConfigs")).thenReturn(
                    systemConfig);
        DirectoryProcessorFactory.getDirectoryProcessorInstance();
    }

}
