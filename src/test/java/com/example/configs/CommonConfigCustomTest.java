package com.example.configs;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonConfig.class, properties = { "EXECUTOR_SIZE=10",
            "FILE_MERGE_SIZE=4" })
public class CommonConfigCustomTest {

    @Autowired
    CommonConfig commonConfig;

    @Test
    public void testSystemConfigWithCustomValues() {
        Properties p = commonConfig.systemConfigs();
        Assert.assertEquals(commonConfig.systemConfigs().getProperty(
                    CommonConfig.EXECUTOR_SIZE), "10");
        Assert.assertEquals(commonConfig.systemConfigs().getProperty(
                    CommonConfig.FILE_MERGE_SIZE), "4");
    }

}
