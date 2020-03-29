package com.example.configs;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonConfig.class)
public class CommonConfigDefaultTest {

    @Autowired
    CommonConfig commonConfig;

    @Test
    public void testSystemConfigWithDefaultValues() {
        Assert.assertEquals(commonConfig.systemConfigs().getProperty(
                    CommonConfig.EXECUTOR_SIZE), "20");
        Assert.assertEquals(commonConfig.systemConfigs().getProperty(
                    CommonConfig.FILE_MERGE_SIZE), "2");
    }

}
