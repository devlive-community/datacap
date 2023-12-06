package io.edurt.datacap.plugin.jdbc.dm;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DmConnection
        extends JdbcConnection
{
    public DmConnection(JdbcConfigure jdbcConfigure, Response response)
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
        buffer.append("://");
        buffer.append(jdbcConfigure.getHost());
        buffer.append(":");
        buffer.append(jdbcConfigure.getPort());
        if (jdbcConfigure.getDatabase().isPresent()) {
            buffer.append("?SCHEMA=");
            buffer.append(jdbcConfigure.getDatabase().get());
        }
        if (jdbcConfigure.getSsl().isPresent()) {
            if (!jdbcConfigure.getDatabase().isPresent()) {
                buffer.append(String.format("?ssl=%s", jdbcConfigure.getSsl().get()));
            }
            else {
                buffer.append(String.format("&ssl=%s", jdbcConfigure.getSsl().get()));
            }
        }
        if (jdbcConfigure.getEnv().isPresent()) {
            Map<String, Object> env = jdbcConfigure.getEnv().get();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            if (!jdbcConfigure.getDatabase().isPresent()) {
                buffer.append("?");
            }
            else {
                if (jdbcConfigure.getIsAppendChar()) {
                    buffer.append("&");
                }
            }
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }
}
