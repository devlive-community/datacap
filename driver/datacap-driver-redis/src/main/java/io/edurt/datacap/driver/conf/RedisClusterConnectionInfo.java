package io.edurt.datacap.driver.conf;

import io.edurt.datacap.core.Logger;
import io.edurt.datacap.core.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RedisClusterConnectionInfo
        extends BaseConnectionInfo
{
    public static final Logger LOGGER = new Logger(RedisClusterConnectionInfo.class);

    private Set<HostAndPort> nodes;
    private int maxAttempts;

    public RedisClusterConnectionInfo(String rawUrl, Properties info)
    {
        super((info));
        try {
            URI uri = new URI(rawUrl);
            Object maxAttemptsString = info.getOrDefault("maxAttempts", JedisCluster.DEFAULT_MAX_ATTEMPTS);
            int maxAttempts = Integer.parseInt(maxAttemptsString.toString());

            String query = uri.getQuery();
            Map<String, String> paramMap = Utils.parseQueryStringToMap(query);
            String host = paramMap.get("host");
            String[] hosts = host.split(";");

            this.nodes = Arrays.stream(hosts)
                    .map(h -> {
                        String[] split = h.split(":");
                        return new HostAndPort(split[0], Integer.parseInt(split[1]));
                    }).collect(Collectors.toSet());

            this.maxAttempts = maxAttempts;
        }
        catch (Exception e) {
            LOGGER.log("Cannot parse JDBC URL %s", rawUrl);
            throw new RuntimeException("Cannot parse JDBC URL: " + rawUrl, e);
        }
    }
}
