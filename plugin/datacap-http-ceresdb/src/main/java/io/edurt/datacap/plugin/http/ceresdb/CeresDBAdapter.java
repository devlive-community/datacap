package io.edurt.datacap.plugin.http.ceresdb;

import io.edurt.datacap.spi.adapter.HttpAdapter;
import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.connection.http.HttpClient;
import io.edurt.datacap.spi.connection.http.HttpMethod;
import io.edurt.datacap.spi.json.JSON;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class CeresDBAdapter
        extends HttpAdapter
{
    public CeresDBAdapter(HttpConnection connection)
    {
        super(connection);
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.httpConnection.getResponse();
        HttpConfigure configure = new HttpConfigure();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                BeanUtils.copyProperties(configure, this.httpConnection.getConfigure());
                configure.setAutoConnected(Boolean.FALSE);
                configure.setRetry(0);
                configure.setMethod(HttpMethod.POST);
                configure.setPath("sql");
                Map<String, String> map = new ConcurrentHashMap<>();
                map.put("query", content);
                configure.setJsonBody(JSON.toJSON(map));
                configure.setMediaType(MediaType.parse("application/json; charset=utf-8"));
                HttpConnection httpConnection = new HttpConnection(configure, new Response());
                HttpClient httpClient = HttpClient.getInstance(configure, httpConnection);
                String body = httpClient.execute();
                CeresDBResponse ceresDBResponse = JSON.objectmapper.readValue(body, CeresDBResponse.class);
                if (ceresDBResponse.getCode() == 0 || ObjectUtils.isNotEmpty(ceresDBResponse.getRows())) {
                    response.setIsSuccessful(true);
                    for (int i = ceresDBResponse.getRows().size() - 1; i >= 0; i--) {
                        Map<String, String> row = ceresDBResponse.getRows().get(i);
                        if (i == 0) {
                            headers.addAll(row.keySet());
                            // TODO: check type
                        }
                        List<Object> _columns = row.entrySet()
                                .stream()
                                .map(entry -> entry.getValue())
                                .collect(Collectors.toList());
                        columns.add(handlerFormatter(configure.getFormat(), headers, _columns));
                    }
                }
                else {
                    response.setIsSuccessful(Boolean.FALSE);
                    response.setMessage(ceresDBResponse.getMessage());
                }
            }
            catch (Exception ex) {
                log.error("Execute content failed content {} exception ", content, ex);
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            }
            finally {
                response.setHeaders(headers);
                response.setTypes(types);
                response.setColumns(columns);
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }
}
