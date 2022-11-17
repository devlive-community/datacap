package io.edurt.datacap.spi.connection.http;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SuppressFBWarnings(value = {"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class HttpClient
{
    private static final int CONNECTION_TIME_OUT = 200000;
    private static final int SOCKET_TIME_OUT = 20000;
    private static final int MAX_IDLE_CONNECTIONS = 30;
    private static final long KEEP_ALLIVE_TIME = 60000L;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static volatile HttpClient httpClient;
    private final OkHttpClient okHttpClient;
    private final HttpConfigure configure;
    private final HttpConnection httpConnection;

    public HttpClient(HttpConfigure configure, HttpConnection httpConnection)
    {
        this.configure = configure;
        this.httpConnection = httpConnection;
        ConnectionPool connectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALLIVE_TIME, TimeUnit.MILLISECONDS);
        this.okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(configure.getAutoConnected())
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpRetryInterceptor(configure))
                .addNetworkInterceptor(new HttpRetryInterceptor(configure))
                .build();
    }

    public static HttpClient getInstance(HttpConfigure configure, HttpConnection httpConnection)
    {
        httpClient = new HttpClient(configure, httpConnection);
        return httpClient;
    }

    public String execute()
    {
        switch (configure.getMethod()) {
            case GET:
                return this.get();
            case POST:
                return this.post();
            default:
                return null;
        }
    }

    private String get()
    {
        HttpUrl.Builder builder = HttpUrl.parse(httpConnection.formatJdbcUrl()).newBuilder();
        if (ObjectUtils.isNotEmpty(configure.getParams())) {
            for (String key : configure.getParams().keySet()) {
                builder.addQueryParameter(key, configure.getParams().get(key));
            }
        }
        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(builder.build().toString()).build();
        return execute(request);
    }

    private String post()
    {
        RequestBody requestBody = RequestBody.create(JSON, configure.getJsonBody());
        HttpUrl.Builder builder = HttpUrl.parse(httpConnection.formatJdbcUrl()).newBuilder();

        // Adding Path Parameters
        if (ObjectUtils.isNotEmpty(configure.getParams())) {
            for (String key : configure.getParams().keySet()) {
                builder.addQueryParameter(key, configure.getParams().get(key));
            }
        }

        Request request = new Request.Builder().post(requestBody)
                .addHeader("Accept-Encoding", "identity")
                .url(builder.build().toString()).build();
        return execute(request);
    }

    private String execute(Request request)
    {
        String responseBody = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            responseBody = response.body().string();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return responseBody;
    }
}
