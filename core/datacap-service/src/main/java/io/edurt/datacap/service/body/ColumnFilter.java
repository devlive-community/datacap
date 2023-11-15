package io.edurt.datacap.service.body;

import com.google.common.collect.Lists;
import io.edurt.datacap.common.sql.configure.SqlColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ColumnFilter
{
    private String condition = "AND";
    private List<SqlColumn> filters = Lists.newArrayList();
}
