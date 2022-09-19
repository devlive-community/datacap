package io.edurt.datacap.plugin.jdbc.mysql;

import io.edurt.datacap.spi.model.Configure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MySQLConnection
{
    private static String DRIVER = "com.mysql.jdbc.Driver";

    private final Configure configure;
    private Connection connection;

    public MySQLConnection(Configure configure)
    {
        this.configure = configure;
    }

    private String appendURL()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:mysql://");
        buffer.append(configure.getHost());
        buffer.append(":");
        buffer.append(configure.getPort());
        if (configure.getDatabase().isPresent()) {
            buffer.append("/");
            buffer.append(configure.getDatabase().get());
        }
        if (configure.getEnv().isPresent()) {
            Map<String, Object> env = configure.getEnv().get();
            List<String> flatEnv = env.entrySet().stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
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
        }
        catch (SQLException | ClassNotFoundException ex) {
            log.error("Connection failed ", ex);
        }
        return this.connection;
    }

    public Connection getConnection()
    {
        return this.connection;
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
