package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.configure.IConfigure;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class PluginEntity
{
    private String name;
    private String description;
    private String type;
    private IConfigure configure;
}
