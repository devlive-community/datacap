package io.edurt.datacap.plugin.http.cratedb;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CrateDBAdapter
        extends HttpAdapter
{
    public CrateDBAdapter(HttpConnection httpConnection)
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
                httpConfigure.setPath("_sql?types");
                httpConfigure.setMethod(HttpMethod.POST);
                Map<String, String> body = new HashMap<>();
                body.put("stmt", content);
                httpConfigure.setJsonBody(JSON.toJSON(body));
                HttpConnection httpConnection = new HttpConnection(httpConfigure, new Response());
                HttpClient httpClient = HttpClient.getInstance(httpConfigure, httpConnection);
                String responseBody = httpClient.execute();
                CrateDBResponse applyResponse = JSON.objectmapper.readValue(responseBody, CrateDBResponse.class);
                if (ObjectUtils.isNotEmpty(applyResponse.getError())) {
                    throw new Exception(String.join(":", applyResponse.getError().getCode(), applyResponse.getError().getMessage()));
                }
                else {
                    headers.addAll(applyResponse.getHeaders());
                    types.addAll(applyResponse.getTypes());
                    for (Object column : applyResponse.getColumns()) {
                        List<Object> _columns = (List<Object>) column;
                        columns.add(handlerFormatter(httpConfigure.getFormat(), headers, _columns));
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
                response.setColumns(columns);
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }
}
