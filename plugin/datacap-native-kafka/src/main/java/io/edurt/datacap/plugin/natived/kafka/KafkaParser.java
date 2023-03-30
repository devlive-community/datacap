package io.edurt.datacap.plugin.natived.kafka;

import io.edurt.datacap.spi.parser.SqlParser;
import io.edurt.datacap.sql.SqlBase;

public class KafkaParser
        extends SqlParser
{
    public KafkaParser(String content)
    {
        super(content);
    }

    @Override
    public String getExecuteContext()
    {
        SqlBase sqlBase = this.getSqlBase();
        if (sqlBase.getToken().equalsIgnoreCase("SHOW")) {
            return sqlBase.getTable();
        }
        else if (sqlBase.getToken().equalsIgnoreCase("SELECT")) {
            return sqlBase.getTable();
        }
        return null;
    }
}
