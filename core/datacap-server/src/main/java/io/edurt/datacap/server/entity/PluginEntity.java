package io.edurt.datacap.server.entity;

import io.edurt.datacap.server.plugin.configure.IConfigure;
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
