package io.edurt.datacap.sql;

import org.junit.Assert;
import org.junit.Test;

public class KafkaParserTest
{
    private String table = "aa";

    @Test
    public void showTopic()
    {
        SqlBaseFormatter formatter = new SqlBaseFormatter("show topics");
        Assert.assertTrue(formatter.getParseResult().isSuccessful());
    }

    @Test
    public void showConsumers()
    {
        SqlBaseFormatter formatter = new SqlBaseFormatter("show Consumers");
        Assert.assertTrue(formatter.getParseResult().isSuccessful());

        formatter = new SqlBaseFormatter("show Consumers from " + table);
        Assert.assertEquals(formatter.getParseResult().getTable(), table);
    }
}
