package io.edurt.datacap.spi.connection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.connection.http.HttpMethod;
import io.edurt.datacap.spi.model.Configure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@EqualsAndHashCode
@SuppressFBWarnings(value = {"EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"},
        justification = "I prefer to suppress these FindBugs warnings")
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
}
