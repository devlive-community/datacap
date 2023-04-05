package io.edurt.datacap.plugin.natived.h2;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

public class H2Connection
        extends JdbcConnection
{
    public H2Connection(JdbcConfigure jdbcConfigure, Response response)
    {
        super(jdbcConfigure, response);
    }

    @Override
    protected String formatJdbcUrl()
    {
        JdbcConfigure jdbcConfigure = (JdbcConfigure) this.getConfigure();
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:");
        buffer.append(jdbcConfigure.getJdbcType());
        if (jdbcConfigure.getDatabase().isPresent()) {
            buffer.append(":");
            buffer.append(jdbcConfigure.getDatabase().get());
        }
        return buffer.toString();
    }
}
