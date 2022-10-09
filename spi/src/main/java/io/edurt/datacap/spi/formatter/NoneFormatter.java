package io.edurt.datacap.spi.formatter;

import java.util.ArrayList;
import java.util.List;

public class NoneFormatter
        extends Formatter
{
    protected NoneFormatter(List<String> headers, List<Object> columns)
    {
        super(headers, columns);
    }

    @Override
    public Object formatter()
    {
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            values.add(columns.get(i));
        }
        return values;
    }
}
