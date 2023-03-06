package io.edurt.datacap.driver.conf;

import io.edurt.datacap.core.Logger;
import lombok.*;

import java.net.URI;
import java.util.Properties;

@Data
@EqualsAndHashCode(callSuper = true)
public class RedisConnectionInfo
        extends BaseConnectionInfo
{
    private final static Logger LOGGER = new Logger(RedisConnectionInfo.class);

    private String host;
    private int port;
    private int dbIndex;

    public RedisConnectionInfo(String rawUrl, Properties info)
    {
        super(info);
        try {
            URI uri = new URI(rawUrl);

            String host = uri.getHost() != null ? uri.getHost() : "localhost";
            int port = uri.getPort() > 0 ? uri.getPort() : 6379;

            String path = uri.getPath();
            int dbIndex = 0;
            if (path != null && path.length() > 1) {
                dbIndex = Integer.parseInt(path.replaceAll("/", ""));
            }

            this.host = host;
            this.port = port;
            this.dbIndex = dbIndex;
        }
        catch (Exception e) {
            LOGGER.log("Cannot parse JDBC URL %s", rawUrl);
            throw new RuntimeException("Cannot parse JDBC URL: " + rawUrl, e);
        }
    }
}
