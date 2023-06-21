package io.edurt.datacap.service.body;

import io.edurt.datacap.common.sql.configure.SqlBody;
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
