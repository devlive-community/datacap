package io.edurt.datacap.sql;

import org.junit.Assert;
import org.junit.Test;

public class ShowPathsTest
{
    @Test
    public void testShowPaths()
    {
        SqlBaseFormatter formatter = new SqlBaseFormatter("show paths");
        Assert.assertTrue(formatter.getParseResult().isSuccessful());

        formatter = new SqlBaseFormatter("SHOW PATHS");
        Assert.assertTrue(formatter.getParseResult().isSuccessful());
    }
    @Test
    public void testShowPathsFrom()
    {
        SqlBaseFormatter formatter = new SqlBaseFormatter("show paths from aa");
        Assert.assertTrue(formatter.getParseResult().isSuccessful());

        formatter = new SqlBaseFormatter("SHOW PATHS FROM aaa");
        Assert.assertTrue(formatter.getParseResult().isSuccessful());
    }
}