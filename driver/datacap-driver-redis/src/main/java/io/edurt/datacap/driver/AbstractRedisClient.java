package io.edurt.datacap.driver;

import io.edurt.datacap.core.Hint;
import io.edurt.datacap.core.Logger;
import io.edurt.datacap.core.Op;
import io.edurt.datacap.core.RedisClient;
import io.edurt.datacap.core.Utils;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.SafeEncoder;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractRedisClient implements RedisClient
{
    public static final Logger LOGGER = new Logger(AbstractRedisClient.class);

    @Override
    public String[] sendCommand(String sql) throws SQLException {
        try {
            Op op = Utils.parseSql(sql);

            Object result = this.sendCommand(op);

            return this.decodeResult(sql, result, op.getHints());
        } catch (Throwable e) {
            throw new SQLException(e);
        }
    }

    protected abstract Object sendCommand(Op op);

    protected Protocol.Command convertCommand(String commandString) {
        return Arrays.stream(Protocol.Command.values())
                .filter(t -> {
                    String string = t.toString();
                    return string.equalsIgnoreCase(commandString);
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("command invalided. commandString = %s", commandString)
                ));
    }


    /**
     * hint:
     * -- decoder:jdk
     * TODO
     *
     * @param sql
     * @param originResult
     * @param hints
     * @return
     */
    protected String[] decodeResult(String sql, Object originResult, List<Hint> hints) {
        String[] decodedResult;
        if (originResult == null) {
            decodedResult = new String[]{null};
        } else if (originResult.getClass().isArray()) {
            String decoded = SafeEncoder.encode((byte[]) originResult);
            decodedResult = Stream.of(decoded)
                    .toArray(String[]::new);

        } else if (originResult instanceof Collection) {
            List<?> list = (List<?>) originResult;
            decodedResult = list.stream()
                    .map(t -> SafeEncoder.encode((byte[]) t))
                    .toArray(String[]::new);

        } else {
            LOGGER.log("cannot decode result. originResult = %s", originResult);
            decodedResult = Stream.of(originResult.toString())
                    .toArray(String[]::new);
        }
        LOGGER.log("decode success. sql = %s, originResult = %s, decodedResult = %s",
                sql, originResult, Utils.toList(decodedResult));
        return decodedResult;
    }
}
