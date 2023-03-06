package io.edurt.datacap.core;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class RedisResultSetMetaData
        implements ResultSetMetaData
{
    private final static Logger LOGGER = new Logger(RedisResultSetMetaData.class);

    public static final int MAX_SIZE = 1024;

    @Override
    public <T> T unwrap(Class<T> iface)
            throws SQLException
    {
        try {
            return iface.cast(this);
        }
        catch (ClassCastException cce) {
            LOGGER.log("Unable to unwrap to %s", iface);
            throw new SQLException("Unable to unwrap to " + iface);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
            throws SQLException
    {
        return iface.isInstance(this);
    }

    @Override
    public int getColumnCount()
            throws SQLException
    {
        return 1;
    }

    @Override
    public boolean isAutoIncrement(int column)
            throws SQLException
    {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column)
            throws SQLException
    {
        return true;
    }

    @Override
    public boolean isSearchable(int column)
            throws SQLException
    {
        return false;
    }

    @Override
    public boolean isCurrency(int column)
            throws SQLException
    {
        return false;
    }

    @Override
    public int isNullable(int column)
            throws SQLException
    {
        return ResultSetMetaData.columnNoNulls;
    }

    @Override
    public boolean isSigned(int column)
            throws SQLException
    {
        return false;
    }

    @Override
    public int getColumnDisplaySize(int column)
            throws SQLException
    {
        return MAX_SIZE;
    }

    @Override
    public String getColumnLabel(int column)
            throws SQLException
    {
        return "RESULTS";
    }

    @Override
    public String getColumnName(int column)
            throws SQLException
    {
        return "RESULTS";
    }

    @Override
    public String getSchemaName(int column)
            throws SQLException
    {
        LOGGER.log("getSchemaName(%s)", column);
        return "9";
    }

    @Override
    public int getPrecision(int column)
            throws SQLException
    {
        return MAX_SIZE;
    }

    @Override
    public int getScale(int column)
            throws SQLException
    {
        return 0;
    }

    @Override
    public String getTableName(int column)
            throws SQLException
    {
        return "";
    }

    @Override
    public String getCatalogName(int column)
            throws SQLException
    {
        return "";
    }

    @Override
    public int getColumnType(int column)
            throws SQLException
    {
        return Types.NVARCHAR;
    }

    @Override
    public String getColumnTypeName(int column)
            throws SQLException
    {
        return "String";
    }

    @Override
    public boolean isReadOnly(int column)
            throws SQLException
    {
        return true;
    }

    @Override
    public boolean isWritable(int column)
            throws SQLException
    {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column)
            throws SQLException
    {
        return false;
    }

    @Override
    public String getColumnClassName(int column)
            throws SQLException
    {
        return "java.lang.String";
    }
}
