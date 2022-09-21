package io.edurt.datacap.server.service;

import io.edurt.datacap.server.BaseParamTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(value = SpringRunner.class)
@SpringBootTest
public class SourceServiceTest
{
    @Autowired
    private SourceService sourceService;

    @Test
    @Sql(value = "classpath:schema/source.sql")
    public void saveOrUpdate()
    {
        assertThat(this.sourceService.saveOrUpdate(BaseParamTest.builderSource()).getStatus());
    }

    @Test
    public void testConnection()
    {
        Assert.assertNotNull(this.sourceService.testConnection(BaseParamTest.builderSource()));
    }
}