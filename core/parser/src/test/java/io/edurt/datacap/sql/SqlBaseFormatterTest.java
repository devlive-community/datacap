package io.edurt.datacap.sql;

import org.junit.Assert;
import org.junit.Test;

public class SqlBaseFormatterTest
{
    @Test
    public void getParseResult()
    {
        String sql = "SELECT * FROM a";
        SqlBaseFormatter formatter = new SqlBaseFormatter(sql);
        Assert.assertTrue(formatter.getParseResult().isSuccessful());
    }
}