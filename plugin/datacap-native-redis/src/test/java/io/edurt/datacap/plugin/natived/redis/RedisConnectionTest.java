package io.edurt.datacap.plugin.natived.redis;

import org.junit.Assert;
import org.junit.Test;

public class RedisConnectionTest
        extends RedisBaseTest
{
    @Test
    public void openConnection()
    {
        RedisConnection connection = new RedisConnection(this.configure, this.response);
        Assert.assertNotNull(connection.getResponse().getConnection());
    }
}