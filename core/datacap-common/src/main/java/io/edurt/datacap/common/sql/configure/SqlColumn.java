package io.edurt.datacap.common.sql.configure;

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
