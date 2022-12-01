package io.edurt.datacap.plugin.natived.redis;

import io.edurt.datacap.spi.adapter.Adapter;
import org.junit.Test;

public class RedisAdapterTest
        extends RedisBaseTest
{
    @Test
    public void handlerExecute()
    {
        RedisConnection connection = new RedisConnection(this.configure, this.response);
        Adapter adapter = new RedisAdapter(connection);
        adapter.handlerExecute("GET key");
    }
}