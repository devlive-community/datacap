package io.edurt.datacap.spi.connection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.connection.http.HttpMethod;
import io.edurt.datacap.spi.model.Configure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import okhttp3.MediaType;

import java.util.Map;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@SuppressFBWarnings(value = {"EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC", "EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class HttpConfigure
        extends Configure
{
    private String protocol = "http";
    private String path;
    private HttpMethod method;
    private Integer retry = 0;
    private Boolean autoConnected = Boolean.FALSE;
    private Map<String, String> params;
    private String jsonBody;
    private boolean isDecoded = false;
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
}
