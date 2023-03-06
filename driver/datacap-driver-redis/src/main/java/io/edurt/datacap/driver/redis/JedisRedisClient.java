package io.edurt.datacap.driver.redis;

import io.edurt.datacap.driver.AbstractRedisClient;
import io.edurt.datacap.core.Logger;
import io.edurt.datacap.core.Op;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

public class JedisRedisClient
        extends AbstractRedisClient
{
    public static final Logger LOGGER = new Logger(JedisRedisClient.class);

    private final Jedis jedis;

    public JedisRedisClient(Jedis jedis)
    {
        this.jedis = jedis;
    }

    @Override
    protected synchronized Object sendCommand(Op op)
    {
        String rawSql = op.getOriginSql();
        String commandString = op.getCommand();
        String[] params = op.getParams();

        int db = -1;
        try {
            db = jedis.getDB();
            Protocol.Command command = this.convertCommand(commandString);

            Object result;
            if (params == null || params.length == 0) {
                result = this.jedis.sendCommand(command);
            }
            else {
                result = this.jedis.sendCommand(command, params);
            }
            return result;
        }
        catch (Throwable e) {
            LOGGER.log("command on db %s `%s` cannot execute.", db, rawSql);
            throw new RuntimeException(String.format("command on db %s `%s` cannot execute.", db, rawSql));
        }
    }

    @Override
    public synchronized void select(int dbIndex)
    {
        this.jedis.select(dbIndex);
    }

    @Override
    public synchronized void close()
    {
        LOGGER.log("close()");
        this.jedis.close();
    }
}
