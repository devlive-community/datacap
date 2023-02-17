package io.edurt.datacap.spi.parser;

import io.edurt.datacap.sql.SqlBase;
import io.edurt.datacap.sql.SqlBaseFormatter;

public class SqlParser
        implements Parser
{
    private final String content;
    private SqlBaseFormatter formatter;

    public SqlParser(String content)
    {
        this.content = content;
        this.formatter = new SqlBaseFormatter(this.content);
    }

    @Override
    public SqlBase getSqlBase()
    {
        return this.formatter.getParseResult();
    }

    @Override
    public String getExecuteContext()
    {
        return null;
    }
}
