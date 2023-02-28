package io.edurt.datacap.spi.connection.http;

import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.model.Response;
import org.junit.Assert;
import org.junit.Test;

public class HttpClientTest
{
    @Test
    public void test()
    {
        HttpConfigure httpConfigure = new HttpConfigure();
        httpConfigure.setAutoConnected(Boolean.FALSE);
        httpConfigure.setRetry(0);
        httpConfigure.setParams(null);
        httpConfigure.setProtocol("https");
        httpConfigure.setHost("datacap.incubator.edurt.io");
        httpConfigure.setPort(443);
        httpConfigure.setMethod(HttpMethod.GET);
        httpConfigure.setPath("blog/index.html");
        HttpConnection httpConnection = new HttpConnection(httpConfigure, new Response());
        HttpClient httpClient = HttpClient.getInstance(httpConfigure, httpConnection);
        String response = httpClient.execute();
        Assert.assertNotNull(response);
    }
}
