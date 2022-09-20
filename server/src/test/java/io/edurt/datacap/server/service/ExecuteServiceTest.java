package io.edurt.datacap.server.service;

import io.edurt.datacap.server.entity.ExecuteEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(value = SpringRunner.class)
@SpringBootTest
class ExecuteServiceTest
{
    @Autowired
    private ExecuteService executeService;

    @Test
    @SqlGroup(value = {
            @Sql(value = "classpath:schema/source.sql"),
            @Sql(value = "classpath:data/source.sql")
    })
    void execute()
    {
        ExecuteEntity configure = new ExecuteEntity();
        configure.setPluginName("MySQL");
        configure.setContent("SHOW TABLES");
        executeService.execute(configure);
    }
}