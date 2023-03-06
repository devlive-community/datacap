package io.edurt.datacap.driver.cluster;

import io.edurt.datacap.driver.AbstractRedisClient;
import io.edurt.datacap.core.Hint;
import io.edurt.datacap.core.HintKey;
import io.edurt.datacap.core.Op;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Protocol;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class JedisRedisClusterClient extends AbstractRedisClient {
    private final JedisCluster jedisCluster;

    @Override
    protected Object sendCommand(Op op) {
        String rawSql = op.getOriginSql();
        String commandString = op.getCommand();
        String[] params = op.getParams();
        List<Hint> hints = op.getHints();

        try {
            Protocol.Command command = this.convertCommand(commandString);

            String sampleKey = hints.stream()
                    .filter(hint -> hint.getKey().equals(HintKey.sample_key))
                    .findFirst()
                    .map(Hint::getValue)
                    .orElse(null);

            Object result;
            if (params == null || params.length == 0) {
                result = this.jedisCluster.sendCommand(sampleKey, command);
            } else {
                result = this.jedisCluster.sendCommand(sampleKey, command, params);
            }
            return result;
        } catch (Throwable e) {
            LOGGER.log("command `%s` cannot execute.", rawSql);
            throw new RuntimeException(String.format("command `%s` cannot execute.", rawSql));
        }
    }

    @Override
    public void select(int dbIndex) throws SQLException {
        throw new SQLException("Redis Cluster does not support this operation");
    }

    @Override
    public void close() {
        this.jedisCluster.close();
    }
}
