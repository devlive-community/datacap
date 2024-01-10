package io.edurt.datacap.common.sql.configure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Properties;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SqlColumn
{
    private String column;
    private String value;
    private String expression;
    private SqlOperator operator;
    private SqlOrder order;
    private Properties original;
}
