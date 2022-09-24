package io.edurt.datacap.spi.connection;

import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JdbcConnection
{
    private final JdbcConfigure jdbcConfigure;
    private final Response response;
    private Connection connection;

    public JdbcConnection(JdbcConfigure jdbcConfigure, Response response)
    {
        this.jdbcConfigure = jdbcConfigure;
        this.response = response;
        this.openConnection();
    }

    private String formatJdbcUrl()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:");
        buffer.append(this.jdbcConfigure.getJdbcType());
        buffer.append("://");
        buffer.append(this.jdbcConfigure.getHost());
        buffer.append(":");
        buffer.append(this.jdbcConfigure.getPort());
        if (this.jdbcConfigure.getDatabase().isPresent()) {
            buffer.append("/");
            buffer.append(this.jdbcConfigure.getDatabase().get());
        }
        if (this.jdbcConfigure.getEnv().isPresent()) {
            Map<String, Object> env = this.jdbcConfigure.getEnv().get();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            buffer.append("?");
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }

    private Connection openConnection()
    {
        try {
            Class.forName(this.jdbcConfigure.getJdbcDriver());
            String url = formatJdbcUrl();
            log.info("Connection driver {}", this.jdbcConfigure.getJdbcDriver());
            log.info("Connection url {}", url);
            if (this.jdbcConfigure.getUsername().isPresent() || this.jdbcConfigure.getPassword().isPresent()) {
                log.info("Connection username with {} password with {}", this.jdbcConfigure.getUsername().get(), "***");
                this.connection = DriverManager.getConnection(url, this.jdbcConfigure.getUsername().get(), this.jdbcConfigure.getPassword().get());
            }
            else {
                log.info("Connection username and password not present");
                this.connection = DriverManager.getConnection(url);
            }
            response.setIsConnected(Boolean.TRUE);
        }
        catch (SQLException | ClassNotFoundException ex) {
            log.error("Connection failed ", ex);
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return this.connection;
    }

    public Connection getConnection()
    {
        return this.connection;
    }

    public Response getResponse()
    {
        return this.response;
    }

    public JdbcConfigure getConfigure()
    {
        return this.jdbcConfigure;
    }

    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            try {
                this.connection.close();
            }
            catch (SQLException ex) {
                log.error("Connection close failed ", ex);
            }
        }
    }
}
