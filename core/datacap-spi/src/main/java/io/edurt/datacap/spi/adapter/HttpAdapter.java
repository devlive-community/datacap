package io.edurt.datacap.spi.adapter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.formatter.FormatterFactory;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class HttpAdapter
        implements Adapter
{
    protected HttpConnection httpConnection;

    public HttpAdapter(HttpConnection httpConnection)
    {
        this.httpConnection = httpConnection;
    }

    public Object handlerFormatter(FormatType format, List<String> headers, List<Object> columns)
    {
        return FormatterFactory.createFormatter(format, headers, columns).formatter();
    }

    @Override
    public Response handlerExecute(String content)
    {
        return null;
    }
}
