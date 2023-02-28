package io.edurt.datacap.spi.parser;

import io.edurt.datacap.sql.SqlBase;

public interface Parser
{
    SqlBase getSqlBase();

    String getExecuteContext();
}
