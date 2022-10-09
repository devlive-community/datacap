package io.edurt.datacap.spi.formatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CsvFormatterTest
{
    private List<String> headers;
    private List<Object> columns;

    @Before
    public void before()
    {
        this.headers = new ArrayList<>();
        this.headers.add("name");
        this.headers.add("age");
        this.columns = new ArrayList<>();
        this.columns.add("datacap");
        this.columns.add("1");
    }

    @Test
    public void formatter()
    {
        Formatter formatter = new CsvFormatter(this.headers, this.columns);
        Assert.assertEquals(formatter.formatter(), "datacap,1");
    }
}
