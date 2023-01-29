package io.edurt.datacap.plugin.natived.redis;

import io.edurt.datacap.spi.adapter.Adapter;
import org.junit.Test;
import io.edurt.datacap.spi.model.Response;

public class RedisAdapterTest
        extends RedisBaseTest {
    @Test
    public void handlerExecute() {
        RedisConnection connection = new RedisConnection(this.configure, this.response);
        Adapter adapter = new RedisAdapter(connection);
        Response cc = adapter.handlerExecute("keys asd");
        System.out.println(cc);
    }
}