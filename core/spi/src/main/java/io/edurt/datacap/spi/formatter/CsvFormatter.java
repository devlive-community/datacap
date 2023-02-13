package io.edurt.datacap.spi.formatter;

import java.util.List;
import java.util.stream.Collectors;

public class CsvFormatter
        extends Formatter
{
    private CharSequence sequence = ",";

    protected CsvFormatter(List<String> headers, List<Object> columns)
    {
        super(headers, columns);
    }

    @Override
    public Object formatter()
    {
        return String.join(sequence, this.columns.stream().map(String::valueOf).collect(Collectors.toList()));
    }
}
