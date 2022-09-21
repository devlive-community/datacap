package io.edurt.datacap.spi.record;

import java.util.ArrayList;
import java.util.List;

public class NoneRecord
        extends Record
{
    protected NoneRecord(List<String> headers, List<Object> columns)
    {
        super(headers, columns);
    }

    @Override
    public Object convert()
    {
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            values.add(columns.get(i));
        }
        return values;
    }
}
