package io.edurt.datacap.spi.formatter;

import java.util.List;

public abstract class Formatter
{
    protected final List<String> headers;
    protected final List<Object> columns;

    protected Formatter(List<String> headers, List<Object> columns)
    {
        this.headers = headers;
        this.columns = columns;
    }

    public abstract Object formatter();
}
