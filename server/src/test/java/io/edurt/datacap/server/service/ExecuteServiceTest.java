package io.edurt.datacap.server.service;

import io.edurt.datacap.server.entity.ExecuteEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(value = SpringRunner.class)
@SpringBootTest
public class ExecuteServiceTest
{
    @Autowired
    private ExecuteService executeService;

    @Test
    @SqlGroup(value = {
            @Sql(value = "classpath:schema/source.sql"),
            @Sql(value = "classpath:data/source.sql")
    })
    public void execute()
    {
        ExecuteEntity configure = new ExecuteEntity();
        configure.setName("MySQL");
        configure.setContent("SHOW TABLES");
        Assert.assertTrue(ObjectUtils.isNotEmpty(executeService.execute(configure)));
    }
}