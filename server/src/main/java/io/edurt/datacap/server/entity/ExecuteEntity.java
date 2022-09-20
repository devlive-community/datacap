package io.edurt.datacap.server.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ExecuteEntity
{
    private String pluginName;
    private String content;
    private Map<String, Object> env;
}
