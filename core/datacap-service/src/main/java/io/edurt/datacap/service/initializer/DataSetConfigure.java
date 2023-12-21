package io.edurt.datacap.service.initializer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "datacap.dataset")
public class DataSetConfigure
{
    private String type;
    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
    private String tableDefaultEngine;
    private String tablePrefix;
}
