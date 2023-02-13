package io.edurt.datacap.server.entity;

import io.edurt.datacap.spi.FormatType;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ExecuteEntity
{
    private String name;
    private String content;
    private Map<String, Object> env;
    private FormatType format = FormatType.NONE;
}
