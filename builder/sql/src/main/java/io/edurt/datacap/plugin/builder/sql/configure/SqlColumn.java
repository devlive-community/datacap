package io.edurt.datacap.plugin.builder.sql.configure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SqlColumn
{
    private String column;
    private SqlOrder order;
}
