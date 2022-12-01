package io.edurt.datacap.plugin.natived.redis;

import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import org.junit.Before;

public class RedisBaseTest
{
    protected Configure configure;
    protected Response response;

    @Before
    public void before()
    {
        this.configure = new Configure();
        this.configure.setHost("127.0.0.1");
        this.configure.setPort(6379);
        this.response = new Response();
    }
}
