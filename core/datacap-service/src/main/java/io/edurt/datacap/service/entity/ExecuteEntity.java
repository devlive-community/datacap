package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.enums.QueryMode;
import io.edurt.datacap.spi.FormatType;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ExecuteEntity
{
    private String name;
    private String content;
    private Map<String, Object> env;
    private FormatType format = FormatType.NONE;
    private int limit;
    private QueryMode mode;
}
