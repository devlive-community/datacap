package io.edurt.datacap.plugin.jdbc.clickhouse;

import io.edurt.datacap.spi.model.Configure;
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
public class ClickHouseConnection
{
    private static String DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";

    private final Configure configure;
    private final Response response;
    private Connection connection;

    public ClickHouseConnection(Configure configure, Response response)
    {
        this.configure = configure;
        this.response = response;
    }

    private String appendURL()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:clickhouse://");
        buffer.append(configure.getHost());
        buffer.append(":");
        buffer.append(configure.getPort());
        if (configure.getDatabase().isPresent()) {
            buffer.append("/");
            buffer.append(configure.getDatabase().get());
        }
        if (configure.getEnv().isPresent()) {
            Map<String, Object> env = configure.getEnv().get();
            List<String> flatEnv = env.entrySet().stream().map(value -> String.format("%s=%s", value.getKey(), value.getValue())).collect(Collectors.toList());
            buffer.append("?");
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }

    public Connection openConnection()
    {
        try {
            Class.forName(DRIVER);
            String url = appendURL();
            log.info("Connection driver {}", DRIVER);
            log.info("Connection url {}", url);
            if (configure.getUsername().isPresent() || configure.getPassword().isPresent()) {
                log.info("Connection username with {} password with {}", configure.getUsername().get(), "***");
                this.connection = DriverManager.getConnection(url, configure.getUsername().get(), configure.getPassword().get());
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

    public Configure getConfigure()
    {
        return this.configure;
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
