package io.edurt.datacap.driver.redis;

import io.edurt.datacap.core.Logger;
import io.edurt.datacap.core.RedisConnection;
import io.edurt.datacap.driver.conf.RedisConnectionInfo;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.Properties;

public class RedisDriver implements Driver {
    private final static Logger LOGGER = new Logger(RedisDriver.class);

    private static final String REDIS_JDBC_PREFIX = "jdbc:redis:";

    static {
        try {
            DriverManager.registerDriver(new RedisDriver());
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
        RedisConnectionInfo redisConnectionInfo = new RedisConnectionInfo(rawUrl, info);

        String host = redisConnectionInfo.getHost();
        int port = redisConnectionInfo.getPort();
        int dbIndex = redisConnectionInfo.getDbIndex();
        int timeout = redisConnectionInfo.getTimeout();
        boolean ssl = redisConnectionInfo.isSsl();
        String username = redisConnectionInfo.getUsername();
        String password = redisConnectionInfo.getPassword();

        try {
            final Jedis jedis = new Jedis(host, port, timeout, timeout, ssl);
            jedis.connect();

            if (username != null) {
                jedis.auth(username, password);
            } else if (password != null) {
                jedis.auth(password);
            }
            if (dbIndex != 0) {
                jedis.select(dbIndex);
            }
//            if (clientName != null) {
//                jedis.clientSetname(clientName);
//            }

            return new RedisConnection(new JedisRedisClient(jedis), dbIndex + "", info);
        } catch (Exception e) {
            LOGGER.log("Cannot init RedisConnection %s", e);
            throw new SQLException("Cannot init RedisConnection", e);
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.toLowerCase().startsWith(REDIS_JDBC_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
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
        // ref: com.mysql.cj.jdbc.NonRegisteringDriver.getParentLogger
        LOGGER.log("getParentLogger not implemented");
        throw new SQLFeatureNotSupportedException("getParentLogger not implemented");
    }
}
