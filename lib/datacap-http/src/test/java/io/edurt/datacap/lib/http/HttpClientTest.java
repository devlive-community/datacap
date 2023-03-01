package io.edurt.datacap.lib.http;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpClientTest
{
    private HttpConfigure configure;

    @Before
    public void before()
    {
        configure = new HttpConfigure();
        configure.setAutoConnected(Boolean.FALSE);
        configure.setRetry(0);
        configure.setParams(null);
        configure.setProtocol("https");
        configure.setHost("datacap.edurt.io");
        configure.setPort(443);
        configure.setMethod(HttpMethod.GET);
    }

    @Test
    public void testExecute()
    {
        HttpClient client = new HttpClient(this.configure);
        Assert.assertNotNull(client.execute());
    }
}
