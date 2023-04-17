package io.edurt.datacap.plugin.http.questdb;

import com.google.common.collect.Maps;
import io.edurt.datacap.plugin.http.questdb.response.QuestDBResponse;
import io.edurt.datacap.spi.adapter.HttpAdapter;
import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.connection.http.HttpClient;
import io.edurt.datacap.spi.connection.http.HttpMethod;
import io.edurt.datacap.spi.json.JSON;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class QuestDBAdapter
        extends HttpAdapter
{
    public QuestDBAdapter(HttpConnection connection)
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
                configure.setMethod(HttpMethod.GET);
                configure.setPath("exec");
                Map<String, String> parameters = Maps.newHashMap();
                parameters.put("query", content);
                configure.setParams(parameters);
                configure.setDecoded(true);
                HttpConnection httpConnection = new HttpConnection(configure, new Response());
                HttpClient httpClient = HttpClient.getInstance(configure, httpConnection);
                String body = httpClient.execute();
                QuestDBResponse requestResponse = JSON.objectmapper.readValue(body, QuestDBResponse.class);
                if (ObjectUtils.isNotEmpty(requestResponse.getQuery())) {
                    response.setIsSuccessful(true);
                    if (ObjectUtils.isNotEmpty(requestResponse.getColumns())) {
                        requestResponse.getColumns()
                                .forEach(schema -> {
                                    headers.add(schema.getName());
                                    types.add(schema.getType());
                                });
                    }
                    requestResponse.getDataset()
                            .forEach(record -> columns.add(handlerFormatter(configure.getFormat(), headers, record)));
                }
                else {
                    response.setIsSuccessful(Boolean.FALSE);
                    response.setMessage(requestResponse.getError());
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
