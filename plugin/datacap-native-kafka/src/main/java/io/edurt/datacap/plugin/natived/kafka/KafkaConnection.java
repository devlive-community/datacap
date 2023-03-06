package io.edurt.datacap.plugin.natived.kafka;

import io.edurt.datacap.spi.connection.Connection;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;

@Slf4j
public class KafkaConnection
        extends Connection
{
    private Configure configure;
    private Response response;

    @Getter
    private AdminClient client;

    public KafkaConnection(Configure configure, Response response)
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
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, configure.getHost());
            this.client = AdminClient.create(properties);
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
        this.client.close();
        log.info("Connection close successful");
    }
}
