package io.edurt.datacap.plugin.jdbc.sqlserver;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlServerConnection
        extends JdbcConnection
{
    public SqlServerConnection(JdbcConfigure jdbcConfigure, Response response)
    {
        super(jdbcConfigure, response);
    }

    /**
     * check version
     * SQL Server 2000：
     * jdbc:microsoft:sqlserver://<host>:<port>;DatabaseName=<database>
     * SQL Server 2005 and above:
     * jdbc:sqlserver://<host>:<port>;databaseName=<database>
     */
    @Override
    protected String formatJdbcUrl()
    {
        JdbcConfigure jdbcConfigure = (JdbcConfigure) this.getConfigure();
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:");
        buffer.append(jdbcConfigure.getJdbcType());
        buffer.append("://");
        buffer.append(jdbcConfigure.getHost());
        buffer.append(":");
        buffer.append(jdbcConfigure.getPort());
        buffer.append(";");
        if (jdbcConfigure.getDatabase().isPresent()) {
            buffer.append("databaseName=");
            buffer.append(jdbcConfigure.getDatabase().get());
            buffer.append(";");
        }
        if (jdbcConfigure.getSsl().isPresent()) {
            buffer.append(String.format("ssl=%s", jdbcConfigure.getSsl().get()));
            buffer.append(";");
            buffer.append("trustServerCertificate=true;");
        }
        if (jdbcConfigure.getEnv().isPresent()) {
            Map<String, Object> env = jdbcConfigure.getEnv().get();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            buffer.append(String.join(";", flatEnv));
        }
        return buffer.toString();
    }
}
