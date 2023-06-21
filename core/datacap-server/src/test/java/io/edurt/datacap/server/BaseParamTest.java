package io.edurt.datacap.server;

import io.edurt.datacap.common.enums.ProtocolEnum;
import io.edurt.datacap.service.entity.ExecuteEntity;
import io.edurt.datacap.service.entity.FormatEntity;
import io.edurt.datacap.service.entity.SourceEntity;

public class BaseParamTest
{
    private BaseParamTest()
    {
    }

    public static SourceEntity builderSource()
    {
        SourceEntity source = new SourceEntity();
        source.setName("Test");
        source.setDescription("This is test source");
        source.setHost("localhost");
        source.setPort(3306);
        source.setProtocol(ProtocolEnum.HTTP.name());
        source.setType("MySQL");
        return source;
    }

    public static ExecuteEntity builderExecute()
    {
        ExecuteEntity configure = new ExecuteEntity();
        configure.setName("MySQL");
        configure.setContent("SELECT * FROM information_schema.TABLES LIMIT 100");
        return configure;
    }

    public static FormatEntity builderFormat()
    {
        FormatEntity configure = new FormatEntity();
        configure.setSql("select * from a left join b on a.id = b.id where a.name != 'test' group a.name order by b.age limit 10");
        return configure;
    }
}
