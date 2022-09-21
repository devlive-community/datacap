package io.edurt.datacap.spi.record;

import java.util.List;

public abstract class Record
{
    protected final List<String> headers;
    protected final List<Object> columns;

    protected Record(List<String> headers, List<Object> columns)
    {
        this.headers = headers;
        this.columns = columns;
    }

    public abstract Object convert();
}
