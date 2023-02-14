package io.edurt.datacap.common.sql.configure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SqlBody
{
    private String database;
    private String table;
    private List<SqlColumn> columns;
    private List<SqlColumn> orders;
    private int limit = 10;
    private int offset = 1;
    private SqlType type;
}
