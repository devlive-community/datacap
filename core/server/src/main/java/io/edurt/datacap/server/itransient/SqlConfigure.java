package io.edurt.datacap.server.itransient;

import io.edurt.datacap.server.common.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SqlConfigure
{
    private String column;
    private Type type;
    private String expression;
}
