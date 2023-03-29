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

    @Test
    public void showDatabases()
    {
        SqlBaseFormatter formatter = new SqlBaseFormatter("show databases");
        Assert.assertTrue(formatter.getParseResult().getToken().equals("SHOW"));
    }

    @Test
    public void showTables()
    {
        SqlBaseFormatter formatter = new SqlBaseFormatter("show tables");
        Assert.assertTrue(formatter.getParseResult().getChildToken().equals("TABLES"));

        formatter = new SqlBaseFormatter("show tables from " + table);
        Assert.assertEquals(formatter.getParseResult().getTable(), table);
    }
}
