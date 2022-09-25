package io.edurt.datacap.spi.type.jdbc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.model.Response;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressFBWarnings(value = {"UC_USELESS_OBJECT"},
        justification = "I prefer to suppress these FindBugs warnings")
public class JdbcColumnConvert
{
    private final ResultSet resultSet;

    public JdbcColumnConvert(ResultSet resultSet)
    {
        this.resultSet = resultSet;
    }

    public Response handlerResponse()
            throws SQLException
    {
        Response response = new Response();
        List<String> headers = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<List<Object>> columns = new ArrayList<>();
        boolean isPresent = true;

        while (this.resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Object> _columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String columnType = metaData.getColumnTypeName(i);
                if (isPresent) {
                    headers.add(columnName);
                    types.add(columnType);
                }
                // Obtain the value of the corresponding type based on the data returned in the query result
                if (columnType.equalsIgnoreCase(JdbcColumnType.INTEGER.name())
                        || columnType.equalsIgnoreCase(JdbcColumnType.INT.name())) {
                    _columns.add(resultSet.getInt(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.TINYINT.name())) {
                    _columns.add(resultSet.getByte(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.DOUBLE.name())) {
                    _columns.add(resultSet.getDouble(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.REAL.name())) {
                    _columns.add(resultSet.getFloat(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.BIGINT.name())) {
                    _columns.add(resultSet.getLong(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.SMALLINT.name())) {
                    _columns.add(resultSet.getShort(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.DECIMAL.name())) {
                    _columns.add(resultSet.getBigDecimal(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.BLOB.name())) {
                    _columns.add(resultSet.getBytes(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.VARCHAR.name())) {
                    _columns.add(resultSet.getString(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.BIT.name())) {
                    _columns.add(resultSet.getBoolean(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.DATE.name())) {
                    _columns.add(resultSet.getDate(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.TIME.name())) {
                    _columns.add(resultSet.getTime(i));
                }
                else if (columnType.equalsIgnoreCase(JdbcColumnType.TIMESTAMP.name())) {
                    _columns.add(resultSet.getTimestamp(i));
                }
                else {
                    _columns.add(resultSet.getObject(i));
                }
            }
            isPresent = false;
            columns.add(_columns);
        }
        return response;
    }
}
