package io.edurt.datacap.plugin.natived.alioss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.connection.Connection;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class AliossConnection
        extends Connection
{
    private Configure configure;
    private Response response;

    @Getter
    private OSS ossClient;

    public AliossConnection(Configure configure, Response response)
    {
        super(configure, response);
    }

    @Override
    protected String formatJdbcUrl()
    {
        return null;
    }

    @Override
    protected java.sql.Connection openConnection()
    {
        try {
            this.configure = getConfigure();
            this.response = getResponse();
            log.info("Connection url {}", formatJdbcUrl());
            this.ossClient = new OSSClientBuilder()
                    .build(configure.getHost(), configure.getUsername().get(), configure.getPassword().get());
            response.setIsConnected(Boolean.TRUE);
        }
        catch (Exception ex) {
            log.error("Connection failed ", ex);
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return null;
    }

    @Override
    public void destroy()
    {
        this.ossClient.shutdown();
        log.info("Connection close successful");
    }
}
