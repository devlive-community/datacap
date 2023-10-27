package io.edurt.datacap.service.body;

import io.edurt.datacap.common.sql.configure.SqlColumn;
import io.edurt.datacap.common.sql.configure.SqlType;
import io.edurt.datacap.spi.model.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Properties;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TableFilter
{
    private Pagination pagination;
    private List<OrderFilter> orders;
    private SqlType type = SqlType.SELECT;
    // If it is in preview mode, only the executed SQL is returned, and the specific content is executed
    private boolean preview = false;
    // If it is UPDATE | INSERT uses the following configuration
    private List<SqlColumn> columns;
    private Properties original;
}
