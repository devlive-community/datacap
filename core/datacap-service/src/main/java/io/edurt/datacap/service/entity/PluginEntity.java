package io.edurt.datacap.service.entity;

import io.edurt.datacap.service.configure.IConfigure;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PluginEntity
{
    private String name;
    private String description;
    private String type;
    private IConfigure configure;
}
