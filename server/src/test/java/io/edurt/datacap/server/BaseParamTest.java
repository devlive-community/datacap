package io.edurt.datacap.server;

import io.edurt.datacap.server.common.ProtocolEnum;
import io.edurt.datacap.server.entity.ExecuteEntity;
import io.edurt.datacap.server.entity.SourceEntity;

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
        source.setProtocol(ProtocolEnum.HTTP);
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
}
