package io.edurt.datacap.plugin.natived.alioss;

import io.edurt.datacap.spi.parser.SqlParser;
import io.edurt.datacap.sql.SqlBase;
import io.edurt.datacap.sql.SqlBaseToken;

public class AliossParser
        extends SqlParser
{
    public AliossParser(String content)
    {
        super(content);
    }

    @Override
    public String getExecuteContext()
    {
        SqlBase sqlBase = this.getSqlBase();
        if (sqlBase.getToken() == SqlBaseToken.SHOW) {
            return sqlBase.getTable();
        }
        else if (sqlBase.getToken() == SqlBaseToken.SELECT) {
            return sqlBase.getTable();
        }
        return null;
    }
}
