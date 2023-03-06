package io.edurt.datacap.driver.cluster;

import io.edurt.datacap.core.Logger;
import io.edurt.datacap.core.RedisConnection;
import io.edurt.datacap.driver.conf.RedisClusterConnectionInfo;
import io.edurt.datacap.driver.redis.RedisDriver;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisCluster;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Properties;

public class RedisClusterDriver implements Driver {
    private final static Logger LOGGER = new Logger(RedisDriver.class);

    private static final String REDIS_CLUSTER_JDBC_PREFIX = "jdbc:redis-cluster:";

    static {
        try {
            DriverManager.registerDriver(new RedisClusterDriver());
        } catch (Exception e) {
            LOGGER.log("Can't register driver!");
            throw new RuntimeException("Can't register driver!", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!this.acceptsURL(url)) {
            LOGGER.log("wrong url. url is %s", url);
            return null;
        }
        if (info == null) {
            info = new Properties();
        }

        String rawUrl = url.replaceFirst("jdbc:", "");
        RedisClusterConnectionInfo connectionInfo = new RedisClusterConnectionInfo(rawUrl, info);

        JedisCluster jedisCluster = new JedisCluster(
                connectionInfo.getNodes(),
                connectionInfo.getTimeout(),
                connectionInfo.getTimeout(),
                connectionInfo.getMaxAttempts(),
                connectionInfo.getUsername(),
                connectionInfo.getPassword(),
                null,
                new GenericObjectPoolConfig<>()
        );
        JedisRedisClusterClient jedisRedisClusterClient = new JedisRedisClusterClient(jedisCluster);

        return new RedisConnection(jedisRedisClusterClient, "0", info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.toLowerCase().startsWith(REDIS_CLUSTER_JDBC_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("cluster:///?hosts=localhost:3306");
        System.out.println(uri.getQuery());
    }
}
