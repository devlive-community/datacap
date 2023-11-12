package io.edurt.datacap.spi.column;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Column
{
    protected final ResultSet resultSet;

    public Column(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }

    public abstract Object mappingColumnData(String columnType, Integer columnIndex)
            throws SQLException;

    public abstract String mappingColumnType(String columnName)
            throws SQLException;
}
