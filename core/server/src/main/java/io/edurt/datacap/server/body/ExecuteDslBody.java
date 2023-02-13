package io.edurt.datacap.server.body;

import io.edurt.datacap.plugin.builder.sql.configure.SqlBody;
import io.edurt.datacap.spi.FormatType;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ExecuteDslBody
{
    private String name;
    private SqlBody configure;
    private Map<String, Object> env;
    private FormatType format = FormatType.NONE;
}
