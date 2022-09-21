package io.edurt.datacap.spi.record;

import io.edurt.datacap.spi.FormatType;

import java.util.List;

public class RecordFactory
{
    private RecordFactory()
    {}

    public static Record createRecord(FormatType format, List<String> headers, List<Object> columns)
    {
        Record instance;
        if (format.equals(FormatType.JSON)) {
            instance = new JsonRecord(headers, columns);
        }
        else {
            instance = new NoneRecord(headers, columns);
        }
        return instance;
    }
}
