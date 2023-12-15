package io.edurt.datacap.service.body;

import io.edurt.datacap.common.sql.configure.SqlType;
import io.edurt.datacap.sql.model.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ColumnBody
{
    private SqlType type;
    private List<Column> columns;
}
