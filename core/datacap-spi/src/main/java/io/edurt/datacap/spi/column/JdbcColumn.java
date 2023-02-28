package io.edurt.datacap.spi.column;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcColumn
{
    private final ResultSet resultSet;

    public JdbcColumn(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }

    public Object convert(String columnType, Integer columnIndex)
            throws SQLException
    {
        // Obtain the value of the corresponding type based on the data returned in the query result
        if (columnType.equalsIgnoreCase(JdbcColumnType.INTEGER.name())
                || columnType.equalsIgnoreCase(JdbcColumnType.INT.name())) {
            return resultSet.getInt(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.TINYINT.name())) {
            return resultSet.getByte(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.DOUBLE.name())) {
            return resultSet.getDouble(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.REAL.name())) {
            return resultSet.getFloat(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.BIGINT.name())) {
            return resultSet.getLong(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.SMALLINT.name())) {
            return resultSet.getShort(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.DECIMAL.name())) {
            return resultSet.getBigDecimal(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.BLOB.name())) {
            return resultSet.getBytes(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.VARCHAR.name())
                || String.class.getSimpleName().equals(columnType)) {
            return resultSet.getString(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.BIT.name())) {
            return resultSet.getBoolean(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.DATE.name())) {
            return resultSet.getDate(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.TIME.name())) {
            return resultSet.getTime(columnIndex);
        }
        else if (columnType.equalsIgnoreCase(JdbcColumnType.TIMESTAMP.name())) {
            return resultSet.getTimestamp(columnIndex);
        }
        else {
            return resultSet.getObject(columnIndex);
        }
    }
}
