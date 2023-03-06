package io.edurt.datacap.core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

@SuppressFBWarnings(value = {"DM_DEFAULT_ENCODING", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class RedisResultSet
        implements ResultSet
{
    private final static Logger LOGGER = new Logger(RedisResultSet.class);

    private final String[] result;
    private final Statement owningStatement;

    private int position = -1;
    private boolean isClosed = false;

    public RedisResultSet(final String[] result, final Statement owningStatement)
    {
        this.result = result;
        this.owningStatement = owningStatement;
    }

    private void checkClosed()
            throws SQLException
    {
        if (isClosed()) {
            LOGGER.log("ResultSet is closed.");
            throw new SQLException("RedisSet is closed.");
        }
    }

    @Override
    public boolean next()
            throws SQLException
    {
        this.checkClosed();

        if (position < result.length - 1) {
            position++;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void close()
            throws SQLException
    {
        LOGGER.log("ResultSet close");
        this.isClosed = true;
    }

    @Override
    public boolean wasNull()
            throws SQLException
    {
        this.checkClosed();
        return result[position] == null;
    }

    @Override
    public String getString(int columnIndex)
            throws SQLException
    {
        this.checkClosed();
        LOGGER.log("getString(%s)", columnIndex);
        return result[position];
    }

    @Override
    public boolean getBoolean(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String r = this.getString(0);

        if ("0".equals(r) || "false".equals(r)) {
            return false;
        }
        else if ("1".equals(r) || "true".equals(r)) {
            return true;
        }
        else {
            LOGGER.log("Cannot convert " + r + " into a boolean.");
            throw new SQLException("Cannot convert " + r + " into a boolean.");
        }
    }

    @Override
    public byte getByte(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);

        if (string == null) {
            return 0;
        }
        return string.getBytes()[0];
    }

    @Override
    public short getShort(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);
        if (string == null) {
            return 0;
        }

        return Short.parseShort(string);
    }

    @Override
    public int getInt(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);
        if (string == null) {
            return 0;
        }

        return Integer.parseInt(string);
    }

    @Override
    public long getLong(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);
        if (string == null) {
            return 0;
        }

        return Long.parseLong(string);
    }

    @Override
    public float getFloat(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);
        if (string == null) {
            return 0;
        }

        return Float.parseFloat(string);
    }

    @Override
    public double getDouble(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);
        if (string == null) {
            return 0;
        }

        return Double.parseDouble(string);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException
    {
        return this.getBigDecimal(0);
    }

    @Override
    public byte[] getBytes(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);
        if (string == null) {
            return null;
        }

        return string.getBytes();
    }

    @Override
    public Date getDate(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getDate not implemented");
        throw new SQLFeatureNotSupportedException("getDate not implemented");
    }

    @Override
    public Time getTime(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getTime not implemented");
        throw new SQLFeatureNotSupportedException("getTime not implemented");
    }

    @Override
    public Timestamp getTimestamp(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getTimestamp not implemented");
        throw new SQLFeatureNotSupportedException("getTimestamp not implemented");
    }

    @Override
    public InputStream getAsciiStream(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("getAsciiStream not implemented");
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("getAsciiStream not implemented");
    }

    @Override
    public InputStream getBinaryStream(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("getAsciiStream not implemented");
    }

    @Override
    public String getString(String columnLabel)
            throws SQLException
    {
        return this.getString(0);
    }

    @Override
    public boolean getBoolean(String columnLabel)
            throws SQLException
    {
        return this.getBoolean(0);
    }

    @Override
    public byte getByte(String columnLabel)
            throws SQLException
    {
        return this.getByte(0);
    }

    @Override
    public short getShort(String columnLabel)
            throws SQLException
    {
        return this.getShort(0);
    }

    @Override
    public int getInt(String columnLabel)
            throws SQLException
    {
        return this.getInt(0);
    }

    @Override
    public long getLong(String columnLabel)
            throws SQLException
    {
        return this.getLong(0);
    }

    @Override
    public float getFloat(String columnLabel)
            throws SQLException
    {
        return this.getFloat(0);
    }

    @Override
    public double getDouble(String columnLabel)
            throws SQLException
    {
        return this.getDouble(0);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale)
            throws SQLException
    {
        return this.getBigDecimal(0);
    }

    @Override
    public byte[] getBytes(String columnLabel)
            throws SQLException
    {
        return this.getBytes(0);
    }

    @Override
    public Date getDate(String columnLabel)
            throws SQLException
    {
        return this.getDate(0);
    }

    @Override
    public Time getTime(String columnLabel)
            throws SQLException
    {
        return this.getTime(0);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel)
            throws SQLException
    {
        return this.getTimestamp(0);
    }

    @Override
    public InputStream getAsciiStream(String columnLabel)
            throws SQLException
    {
        return this.getAsciiStream(0);
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel)
            throws SQLException
    {
        return this.getUnicodeStream(0);
    }

    @Override
    public InputStream getBinaryStream(String columnLabel)
            throws SQLException
    {
        return this.getBinaryStream(0);
    }

    @Override
    public SQLWarning getWarnings()
            throws SQLException
    {
        return null;
    }

    @Override
    public void clearWarnings()
            throws SQLException
    {
    }

    @Override
    public String getCursorName()
            throws SQLException
    {
        LOGGER.log("getCursorName not implemented");
        throw new SQLFeatureNotSupportedException("getCursorName not implemented");
    }

    @Override
    public ResultSetMetaData getMetaData()
            throws SQLException
    {
        return new RedisResultSetMetaData();
    }

    @Override
    public Object getObject(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getObject not implemented");
        throw new SQLFeatureNotSupportedException("getObject not implemented");
    }

    @Override
    public Object getObject(String columnLabel)
            throws SQLException
    {
        return this.getObject(0);
    }

    @Override
    public int findColumn(String columnLabel)
            throws SQLException
    {
        return 0;
    }

    @Override
    public Reader getCharacterStream(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("getCharacterStream not implemented");
    }

    @Override
    public Reader getCharacterStream(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("getCharacterStream not implemented");
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex)
            throws SQLException
    {
        this.checkClosed();

        String string = this.getString(0);
        if (string == null) {
            return null;
        }

        return new BigDecimal(string);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel)
            throws SQLException
    {
        return this.getBigDecimal(0);
    }

    @Override
    public boolean isBeforeFirst()
            throws SQLException
    {
        this.checkClosed();
        return position < 0;
    }

    @Override
    public boolean isAfterLast()
            throws SQLException
    {
        this.checkClosed();
        return position >= result.length;
    }

    @Override
    public boolean isFirst()
            throws SQLException
    {
        this.checkClosed();
        return position == 0;
    }

    @Override
    public boolean isLast()
            throws SQLException
    {
        this.checkClosed();
        return position == result.length - 1;
    }

    @Override
    public void beforeFirst()
            throws SQLException
    {
        this.checkClosed();
        position = -1;
    }

    @Override
    public void afterLast()
            throws SQLException
    {
        this.checkClosed();
        position = result.length;
    }

    @Override
    public boolean first()
            throws SQLException
    {
        this.checkClosed();
        position = 0;
        return result.length > 0;
    }

    @Override
    public boolean last()
            throws SQLException
    {
        this.checkClosed();
        position = result.length - 1;
        return result.length > 0;
    }

    @Override
    public int getRow()
            throws SQLException
    {
        this.checkClosed();
        return this.position + 1;
    }

    @Override
    public boolean absolute(int row)
            throws SQLException
    {
        // TODO
        return false;
    }

    @Override
    public boolean relative(int rows)
            throws SQLException
    {
        LOGGER.log("relative not implemented");
        throw new SQLFeatureNotSupportedException("relative not implemented");
    }

    @Override
    public boolean previous()
            throws SQLException
    {
        LOGGER.log("previous not implemented");
        throw new SQLFeatureNotSupportedException("previous not implemented");
    }

    @Override
    public void setFetchDirection(int direction)
            throws SQLException
    {
        LOGGER.log("setFetchDirection not implemented");
        throw new SQLFeatureNotSupportedException("setFetchDirection not implemented");
    }

    @Override
    public int getFetchDirection()
            throws SQLException
    {
        this.checkClosed();
        return FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows)
            throws SQLException
    {
        LOGGER.log("setFetchSize not implemented");
        throw new SQLFeatureNotSupportedException("setFetchSize not implemented");
    }

    @Override
    public int getFetchSize()
            throws SQLException
    {
        this.checkClosed();
        return result.length;
    }

    @Override
    public int getType()
            throws SQLException
    {
        this.checkClosed();
        return TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency()
            throws SQLException
    {
        this.checkClosed();
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated()
            throws SQLException
    {
        LOGGER.log("rowUpdated not implemented");
        throw new SQLFeatureNotSupportedException("rowUpdated not implemented");
    }

    @Override
    public boolean rowInserted()
            throws SQLException
    {
        LOGGER.log("rowInserted not implemented");
        throw new SQLFeatureNotSupportedException("rowInserted not implemented");
    }

    @Override
    public boolean rowDeleted()
            throws SQLException
    {
        LOGGER.log("rowDeleted not implemented");
        throw new SQLFeatureNotSupportedException("rowDeleted not implemented");
    }

    @Override
    public void updateNull(int columnIndex)
            throws SQLException
    {
        LOGGER.log("updateNull not implemented");
        throw new SQLFeatureNotSupportedException("updateNull not implemented");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x)
            throws SQLException
    {
        LOGGER.log("updateBoolean not implemented");
        throw new SQLFeatureNotSupportedException("updateBoolean not implemented");
    }

    @Override
    public void updateByte(int columnIndex, byte x)
            throws SQLException
    {
        LOGGER.log("updateByte not implemented");
        throw new SQLFeatureNotSupportedException("updateByte not implemented");
    }

    @Override
    public void updateShort(int columnIndex, short x)
            throws SQLException
    {
        LOGGER.log("updateShort not implemented");
        throw new SQLFeatureNotSupportedException("updateShort not implemented");
    }

    @Override
    public void updateInt(int columnIndex, int x)
            throws SQLException
    {
        LOGGER.log("updateInt not implemented");
        throw new SQLFeatureNotSupportedException("updateInt not implemented");
    }

    @Override
    public void updateLong(int columnIndex, long x)
            throws SQLException
    {
        LOGGER.log("updateLong not implemented");
        throw new SQLFeatureNotSupportedException("updateLong not implemented");
    }

    @Override
    public void updateFloat(int columnIndex, float x)
            throws SQLException
    {
        LOGGER.log("updateFloat not implemented");
        throw new SQLFeatureNotSupportedException("updateFloat not implemented");
    }

    @Override
    public void updateDouble(int columnIndex, double x)
            throws SQLException
    {
        LOGGER.log("updateDouble not implemented");
        throw new SQLFeatureNotSupportedException("updateDouble not implemented");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException
    {
        LOGGER.log("updateBigDecimal not implemented");
        throw new SQLFeatureNotSupportedException("updateBigDecimal not implemented");
    }

    @Override
    public void updateString(int columnIndex, String x)
            throws SQLException
    {
        LOGGER.log("updateString not implemented");
        throw new SQLFeatureNotSupportedException("updateString not implemented");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x)
            throws SQLException
    {
        LOGGER.log("updateBytes not implemented");
        throw new SQLFeatureNotSupportedException("updateBytes not implemented");
    }

    @Override
    public void updateDate(int columnIndex, Date x)
            throws SQLException
    {
        LOGGER.log("updateDate not implemented");
        throw new SQLFeatureNotSupportedException("updateDate not implemented");
    }

    @Override
    public void updateTime(int columnIndex, Time x)
            throws SQLException
    {
        LOGGER.log("updateTime not implemented");
        throw new SQLFeatureNotSupportedException("updateTime not implemented");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException
    {
        LOGGER.log("updateTimestamp not implemented");
        throw new SQLFeatureNotSupportedException("updateTimestamp not implemented");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException
    {
        LOGGER.log("updateAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("updateAsciiStream not implemented");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException
    {
        LOGGER.log("updateBinaryStream not implemented");
        throw new SQLFeatureNotSupportedException("updateBinaryStream not implemented");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException
    {
        LOGGER.log("updateCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateCharacterStream not implemented");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength)
            throws SQLException
    {
        LOGGER.log("updateObject not implemented");
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }

    @Override
    public void updateObject(int columnIndex, Object x)
            throws SQLException
    {
        LOGGER.log("updateObject not implemented");
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }

    @Override
    public void updateNull(String columnLabel)
            throws SQLException
    {
        LOGGER.log("updateNull not implemented");
        throw new SQLFeatureNotSupportedException("updateNull not implemented");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x)
            throws SQLException
    {
        LOGGER.log("updateBoolean not implemented");
        throw new SQLFeatureNotSupportedException("updateBoolean not implemented");
    }

    @Override
    public void updateByte(String columnLabel, byte x)
            throws SQLException
    {
        LOGGER.log("updateByte not implemented");
        throw new SQLFeatureNotSupportedException("updateByte not implemented");
    }

    @Override
    public void updateShort(String columnLabel, short x)
            throws SQLException
    {
        LOGGER.log("updateShort not implemented");
        throw new SQLFeatureNotSupportedException("updateShort not implemented");
    }

    @Override
    public void updateInt(String columnLabel, int x)
            throws SQLException
    {
        LOGGER.log("updateInt not implemented");
        throw new SQLFeatureNotSupportedException("updateInt not implemented");
    }

    @Override
    public void updateLong(String columnLabel, long x)
            throws SQLException
    {
        LOGGER.log("updateLong not implemented");
        throw new SQLFeatureNotSupportedException("updateLong not implemented");
    }

    @Override
    public void updateFloat(String columnLabel, float x)
            throws SQLException
    {
        LOGGER.log("updateFloat not implemented");
        throw new SQLFeatureNotSupportedException("updateFloat not implemented");
    }

    @Override
    public void updateDouble(String columnLabel, double x)
            throws SQLException
    {
        LOGGER.log("updateDouble not implemented");
        throw new SQLFeatureNotSupportedException("updateDouble not implemented");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x)
            throws SQLException
    {
        LOGGER.log("updateBigDecimal not implemented");
        throw new SQLFeatureNotSupportedException("updateBigDecimal not implemented");
    }

    @Override
    public void updateString(String columnLabel, String x)
            throws SQLException
    {
        LOGGER.log("updateString not implemented");
        throw new SQLFeatureNotSupportedException("updateString not implemented");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x)
            throws SQLException
    {
        LOGGER.log("updateBytes not implemented");
        throw new SQLFeatureNotSupportedException("updateBytes not implemented");
    }

    @Override
    public void updateDate(String columnLabel, Date x)
            throws SQLException
    {
        LOGGER.log("updateDate not implemented");
        throw new SQLFeatureNotSupportedException("updateDate not implemented");
    }

    @Override
    public void updateTime(String columnLabel, Time x)
            throws SQLException
    {
        LOGGER.log("updateTime not implemented");
        throw new SQLFeatureNotSupportedException("updateTime not implemented");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x)
            throws SQLException
    {
        LOGGER.log("updateTimestamp not implemented");
        throw new SQLFeatureNotSupportedException("updateTimestamp not implemented");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length)
            throws SQLException
    {
        LOGGER.log("updateAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("updateAsciiStream not implemented");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length)
            throws SQLException
    {
        LOGGER.log("updateBinaryStream not implemented");
        throw new SQLFeatureNotSupportedException("updateBinaryStream not implemented");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length)
            throws SQLException
    {
        LOGGER.log("updateCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateCharacterStream not implemented");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength)
            throws SQLException
    {
        LOGGER.log("updateObject not implemented");
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }

    @Override
    public void updateObject(String columnLabel, Object x)
            throws SQLException
    {
        LOGGER.log("updateObject not implemented");
        throw new SQLFeatureNotSupportedException("updateObject not implemented");
    }

    @Override
    public void insertRow()
            throws SQLException
    {
        LOGGER.log("insertRow not implemented");
        throw new SQLFeatureNotSupportedException("insertRow not implemented");
    }

    @Override
    public void updateRow()
            throws SQLException
    {
        LOGGER.log("updateRow not implemented");
        throw new SQLFeatureNotSupportedException("updateRow not implemented");
    }

    @Override
    public void deleteRow()
            throws SQLException
    {
        LOGGER.log("deleteRow not implemented");
        throw new SQLFeatureNotSupportedException("deleteRow not implemented");
    }

    @Override
    public void refreshRow()
            throws SQLException
    {
        LOGGER.log("refreshRow not implemented");
        throw new SQLFeatureNotSupportedException("refreshRow not implemented");
    }

    @Override
    public void cancelRowUpdates()
            throws SQLException
    {
        LOGGER.log("cancelRowUpdates not implemented");
        throw new SQLFeatureNotSupportedException("cancelRowUpdates not implemented");
    }

    @Override
    public void moveToInsertRow()
            throws SQLException
    {
        LOGGER.log("moveToInsertRow not implemented");
        throw new SQLFeatureNotSupportedException("moveToInsertRow not implemented");
    }

    @Override
    public void moveToCurrentRow()
            throws SQLException
    {
        LOGGER.log("moveToCurrentRow not implemented");
        throw new SQLFeatureNotSupportedException("moveToCurrentRow not implemented");
    }

    @Override
    public Statement getStatement()
            throws SQLException
    {
        this.checkClosed();
        return this.owningStatement;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map)
            throws SQLException
    {
        LOGGER.log("getObject not implemented");
        throw new SQLFeatureNotSupportedException("getObject not implemented");
    }

    @Override
    public Ref getRef(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getRef not implemented");
        throw new SQLFeatureNotSupportedException("getRef not implemented");
    }

    @Override
    public Blob getBlob(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getBlob not implemented");
        throw new SQLFeatureNotSupportedException("getBlob not implemented");
    }

    @Override
    public Clob getClob(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getClob not implemented");
        throw new SQLFeatureNotSupportedException("getClob not implemented");
    }

    @Override
    public Array getArray(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getArray not implemented");
        throw new SQLFeatureNotSupportedException("getArray not implemented");
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map)
            throws SQLException
    {
        LOGGER.log("getObject not implemented");
        throw new SQLFeatureNotSupportedException("getObject not implemented");
    }

    @Override
    public Ref getRef(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getRef not implemented");
        throw new SQLFeatureNotSupportedException("getRef not implemented");
    }

    @Override
    public Blob getBlob(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getBlob not implemented");
        throw new SQLFeatureNotSupportedException("getBlob not implemented");
    }

    @Override
    public Clob getClob(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getClob not implemented");
        throw new SQLFeatureNotSupportedException("getClob not implemented");
    }

    @Override
    public Array getArray(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getArray not implemented");
        throw new SQLFeatureNotSupportedException("getArray not implemented");
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal)
            throws SQLException
    {
        LOGGER.log("getDate not implemented");
        throw new SQLFeatureNotSupportedException("getDate not implemented");
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal)
            throws SQLException
    {
        LOGGER.log("getDate not implemented");
        throw new SQLFeatureNotSupportedException("getDate not implemented");
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal)
            throws SQLException
    {
        LOGGER.log("getTime not implemented");
        throw new SQLFeatureNotSupportedException("getTime not implemented");
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal)
            throws SQLException
    {
        LOGGER.log("getTime not implemented");
        throw new SQLFeatureNotSupportedException("getTime not implemented");
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException
    {
        LOGGER.log("getTimestamp not implemented");
        throw new SQLFeatureNotSupportedException("getTimestamp not implemented");
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal)
            throws SQLException
    {
        LOGGER.log("getTimestamp not implemented");
        throw new SQLFeatureNotSupportedException("getTimestamp not implemented");
    }

    @Override
    public URL getURL(int columnIndex)
            throws SQLException
    {
        this.checkClosed();
        String string = this.getString(columnIndex);
        if (string == null) {
            return null;
        }

        try {
            return new URL(string);
        }
        catch (MalformedURLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public URL getURL(String columnLabel)
            throws SQLException
    {
        return this.getURL(0);
    }

    @Override
    public void updateRef(int columnIndex, Ref x)
            throws SQLException
    {
        LOGGER.log("updateRef not implemented");
        throw new SQLFeatureNotSupportedException("updateRef not implemented");
    }

    @Override
    public void updateRef(String columnLabel, Ref x)
            throws SQLException
    {
        LOGGER.log("updateRef not implemented");
        throw new SQLFeatureNotSupportedException("updateRef not implemented");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x)
            throws SQLException
    {
        LOGGER.log("updateBlob not implemented");
        throw new SQLFeatureNotSupportedException("updateBlob not implemented");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x)
            throws SQLException
    {
        LOGGER.log("updateBlob not implemented");
        throw new SQLFeatureNotSupportedException("updateBlob not implemented");
    }

    @Override
    public void updateClob(int columnIndex, Clob x)
            throws SQLException
    {
        LOGGER.log("updateClob not implemented");
        throw new SQLFeatureNotSupportedException("updateClob not implemented");
    }

    @Override
    public void updateClob(String columnLabel, Clob x)
            throws SQLException
    {
        LOGGER.log("updateClob not implemented");
        throw new SQLFeatureNotSupportedException("updateClob not implemented");
    }

    @Override
    public void updateArray(int columnIndex, Array x)
            throws SQLException
    {
        LOGGER.log("updateArray not implemented");
        throw new SQLFeatureNotSupportedException("updateArray not implemented");
    }

    @Override
    public void updateArray(String columnLabel, Array x)
            throws SQLException
    {
        LOGGER.log("updateArray not implemented");
        throw new SQLFeatureNotSupportedException("updateArray not implemented");
    }

    @Override
    public RowId getRowId(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getRowId not implemented");
        throw new SQLFeatureNotSupportedException("getRowId not implemented");
    }

    @Override
    public RowId getRowId(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getRowId not implemented");
        throw new SQLFeatureNotSupportedException("getRowId not implemented");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x)
            throws SQLException
    {
        LOGGER.log("updateRowId not implemented");
        throw new SQLFeatureNotSupportedException("updateRowId not implemented");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x)
            throws SQLException
    {
        LOGGER.log("updateRowId not implemented");
        throw new SQLFeatureNotSupportedException("updateRowId not implemented");
    }

    @Override
    public int getHoldability()
            throws SQLException
    {
        LOGGER.log("getHoldability not implemented");
        throw new SQLFeatureNotSupportedException("getHoldability not implemented");
    }

    @Override
    public boolean isClosed()
            throws SQLException
    {
        LOGGER.log("isClosed = %s", isClosed);
        return this.isClosed;
    }

    @Override
    public void updateNString(int columnIndex, String nString)
            throws SQLException
    {
        LOGGER.log("updateNString not implemented");
        throw new SQLFeatureNotSupportedException("updateNString not implemented");
    }

    @Override
    public void updateNString(String columnLabel, String nString)
            throws SQLException
    {
        LOGGER.log("updateNString not implemented");
        throw new SQLFeatureNotSupportedException("updateNString not implemented");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob)
            throws SQLException
    {
        LOGGER.log("updateNClob not implemented");
        throw new SQLFeatureNotSupportedException("updateNClob not implemented");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob)
            throws SQLException
    {
        LOGGER.log("updateNClob not implemented");
        throw new SQLFeatureNotSupportedException("updateNClob not implemented");
    }

    @Override
    public NClob getNClob(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getNClob not implemented");
        throw new SQLFeatureNotSupportedException("getNClob not implemented");
    }

    @Override
    public NClob getNClob(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getNClob not implemented");
        throw new SQLFeatureNotSupportedException("getNClob not implemented");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getSQLXML not implemented");
        throw new SQLFeatureNotSupportedException("getSQLXML not implemented");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getSQLXML not implemented");
        throw new SQLFeatureNotSupportedException("getSQLXML not implemented");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject)
            throws SQLException
    {
        LOGGER.log("updateSQLXML not implemented");
        throw new SQLFeatureNotSupportedException("updateSQLXML not implemented");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject)
            throws SQLException
    {
        LOGGER.log("updateSQLXML not implemented");
        throw new SQLFeatureNotSupportedException("updateSQLXML not implemented");
    }

    @Override
    public String getNString(int columnIndex)
            throws SQLException
    {
        this.checkClosed();
        return result[position];
    }

    @Override
    public String getNString(String columnLabel)
            throws SQLException
    {
        return this.getNString(0);
    }

    @Override
    public Reader getNCharacterStream(int columnIndex)
            throws SQLException
    {
        LOGGER.log("getNCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("getNCharacterStream not implemented");
    }

    @Override
    public Reader getNCharacterStream(String columnLabel)
            throws SQLException
    {
        LOGGER.log("getNCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("getNCharacterStream not implemented");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException
    {
        LOGGER.log("updateNCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateNCharacterStream not implemented");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length)
            throws SQLException
    {
        LOGGER.log("updateNCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateNCharacterStream not implemented");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length)
            throws SQLException
    {
        LOGGER.log("updateAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("updateAsciiStream not implemented");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length)
            throws SQLException
    {
        LOGGER.log("updateBinaryStream not implemented");
        throw new SQLFeatureNotSupportedException("updateBinaryStream not implemented");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException
    {
        LOGGER.log("updateCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateCharacterStream not implemented");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length)
            throws SQLException
    {
        LOGGER.log("updateAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("updateAsciiStream not implemented");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length)
            throws SQLException
    {
        LOGGER.log("updateBinaryStream not implemented");
        throw new SQLFeatureNotSupportedException("updateBinaryStream not implemented");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length)
            throws SQLException
    {
        LOGGER.log("updateCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateCharacterStream not implemented");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length)
            throws SQLException
    {
        LOGGER.log("updateBlob not implemented");
        throw new SQLFeatureNotSupportedException("updateBlob not implemented");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length)
            throws SQLException
    {
        LOGGER.log("updateBlob not implemented");
        throw new SQLFeatureNotSupportedException("updateBlob not implemented");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length)
            throws SQLException
    {
        LOGGER.log("updateClob not implemented");
        throw new SQLFeatureNotSupportedException("updateClob not implemented");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length)
            throws SQLException
    {
        LOGGER.log("updateClob not implemented");
        throw new SQLFeatureNotSupportedException("updateClob not implemented");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length)
            throws SQLException
    {
        LOGGER.log("updateNClob not implemented");
        throw new SQLFeatureNotSupportedException("updateNClob not implemented");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length)
            throws SQLException
    {
        LOGGER.log("updateNClob not implemented");
        throw new SQLFeatureNotSupportedException("updateNClob not implemented");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x)
            throws SQLException
    {
        LOGGER.log("updateNCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateNCharacterStream not implemented");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader)
            throws SQLException
    {
        LOGGER.log("updateNCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateNCharacterStream not implemented");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x)
            throws SQLException
    {
        LOGGER.log("updateAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("updateAsciiStream not implemented");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x)
            throws SQLException
    {
        LOGGER.log("updateBinaryStream not implemented");
        throw new SQLFeatureNotSupportedException("updateBinaryStream not implemented");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x)
            throws SQLException
    {
        LOGGER.log("updateCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateCharacterStream not implemented");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x)
            throws SQLException
    {
        LOGGER.log("updateAsciiStream not implemented");
        throw new SQLFeatureNotSupportedException("updateAsciiStream not implemented");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x)
            throws SQLException
    {
        LOGGER.log("updateBinaryStream not implemented");
        throw new SQLFeatureNotSupportedException("updateBinaryStream not implemented");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader)
            throws SQLException
    {
        LOGGER.log("updateCharacterStream not implemented");
        throw new SQLFeatureNotSupportedException("updateCharacterStream not implemented");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream)
            throws SQLException
    {
        LOGGER.log("updateBlob not implemented");
        throw new SQLFeatureNotSupportedException("updateBlob not implemented");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream)
            throws SQLException
    {
        LOGGER.log("updateBlob not implemented");
        throw new SQLFeatureNotSupportedException("updateBlob not implemented");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader)
            throws SQLException
    {
        LOGGER.log("updateClob not implemented");
        throw new SQLFeatureNotSupportedException("updateClob not implemented");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader)
            throws SQLException
    {
        LOGGER.log("updateClob not implemented");
        throw new SQLFeatureNotSupportedException("updateClob not implemented");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader)
            throws SQLException
    {
        LOGGER.log("updateNClob not implemented");
        throw new SQLFeatureNotSupportedException("updateNClob not implemented");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader)
            throws SQLException
    {
        LOGGER.log("updateNClob not implemented");
        throw new SQLFeatureNotSupportedException("updateNClob not implemented");
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type)
            throws SQLException
    {
        LOGGER.log("getObject not implemented");
        throw new SQLFeatureNotSupportedException("getObject not implemented");
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type)
            throws SQLException
    {
        LOGGER.log("getObject not implemented");
        throw new SQLFeatureNotSupportedException("getObject not implemented");
    }

    @Override
    public <T> T unwrap(Class<T> iface)
            throws SQLException
    {
        try {
            return iface.cast(this);
        }
        catch (ClassCastException cce) {
            LOGGER.log("Unable to unwrap to %s" + iface);
            throw new SQLException("Unable to unwrap to " + iface);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
            throws SQLException
    {
        return iface.isInstance(this);
    }
}
