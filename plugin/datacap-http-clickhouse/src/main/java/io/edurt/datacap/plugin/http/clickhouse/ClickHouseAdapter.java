package io.edurt.datacap.plugin.http.clickhouse;

import io.edurt.datacap.spi.adapter.HttpAdapter;
import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.connection.http.HttpClient;
import io.edurt.datacap.spi.connection.http.HttpMethod;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class ClickHouseAdapter
        extends HttpAdapter
{
    public ClickHouseAdapter(HttpConnection httpConnection)
    {
        super(httpConnection);
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.connection.getResponse();
        HttpConfigure httpConfigure = new HttpConfigure();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                BeanUtils.copyProperties(httpConfigure, this.connection.getConfigure());
                httpConfigure.setAutoConnected(Boolean.FALSE);
                httpConfigure.setRetry(0);
                httpConfigure.setMethod(HttpMethod.POST);
                httpConfigure.setJsonBody(content + " FORMAT TabSeparatedWithNamesAndTypes");
                httpConfigure.setMediaType(MediaType.parse("text/plain; charset=utf-8"));
                HttpConnection httpConnection = new HttpConnection(httpConfigure, new Response());
                HttpClient httpClient = HttpClient.getInstance(httpConfigure, httpConnection);
                String responseBody = httpClient.execute();
                checkArgument(!responseBody.contains("DB::Exception"), responseBody);
                String[] data = responseBody.split("\n");
                for (int i = 0; i < data.length; i++) {
                    if (i == 0) {
                        headers.addAll(Arrays.asList(data[i].split("\t")));
                    }
                    else if (i == 1) {
                        types.addAll(Arrays.asList(data[i].split("\t")));
                    }
                    else {
                        columns.add(Arrays.asList(data[i].split("\t")));
                    }
                }
                response.setIsSuccessful(Boolean.TRUE);
            }
            catch (Exception ex) {
                log.error("Execute content failed content {} exception ", content, ex);
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            }
            finally {
                response.setHeaders(headers);
                response.setTypes(types);
                response.setColumns(handlerFormatter(httpConfigure.getInjector(), httpConfigure.getFormat(), headers, columns));
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }
}
