package io.edurt.datacap.spi.adapter;

import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.formatter.FormatterFactory;
import io.edurt.datacap.spi.model.Response;

import java.util.List;

public interface Adapter
{
    Response handlerExecute(String content);

    default Object handlerFormatter(FormatType format, List<String> headers, List<Object> columns)
    {
        return FormatterFactory.createFormatter(format, headers, columns).formatter();
    }
}
