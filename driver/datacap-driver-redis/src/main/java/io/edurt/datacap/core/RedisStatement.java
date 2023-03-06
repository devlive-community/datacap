package io.edurt.datacap.core;

import java.sql.*;

public class RedisStatement implements Statement {
    private final static Logger LOGGER = new Logger(RedisStatement.class);

    private final RedisConnection connection;
    private final RedisClient redisClient;

    private ResultSet resultSet;
    private boolean isClosed = false;
    private int fetchSize = 1;

    public RedisStatement(RedisConnection connection, RedisClient redisClient) {
        this.connection = connection;
        this.redisClient = redisClient;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        LOGGER.log("executeQuery(%s)", sql);

        this.checkClosed();

        String[] result = this.redisClient.sendCommand(sql);
        return new RedisResultSet(result, this);
    }


    @Override
    public int executeUpdate(String sql) throws SQLException {
        this.checkClosed();

        String[] result = this.redisClient.sendCommand(sql);
        return result.length;
    }

    @Override
    public void close() throws SQLException {
        LOGGER.log("close()");
        if (isClosed) {
            LOGGER.log("Statement has been closed.");
            return;
        }
        if (this.resultSet != null && !this.resultSet.isClosed()) {
            this.resultSet.close();
        }
        isClosed = true;
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        this.checkClosed();
        return Integer.MAX_VALUE;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        this.checkClosed();
        // do nothing
    }

    @Override
    public int getMaxRows() throws SQLException {
        this.checkClosed();
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        this.checkClosed();
        // do nothing
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        this.checkClosed();
        // do nothing
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        this.checkClosed();
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        LOGGER.log("setQueryTimeout..");
    }

    @Override
    public void cancel() throws SQLException {
        throw new SQLFeatureNotSupportedException("cancel not implemented");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        LOGGER.log("getWarnings returns null");
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.checkClosed();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        LOGGER.log("setCursorName not implemented");
        throw new SQLFeatureNotSupportedException("setCursorName not implemented");
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        this.checkClosed();

        String[] result = this.redisClient.sendCommand(sql);
        this.resultSet = new RedisResultSet(result, this);

        return true;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        this.checkClosed();
        return this.resultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        this.checkClosed();
        LOGGER.log("getUpdateCount()");
        // 原因在父类
        return -1;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        this.checkClosed();
        LOGGER.log("getMoreResults()");
        return this.getMoreResults(CLOSE_CURRENT_RESULT);
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        this.checkClosed();
        LOGGER.log("setFetchDirection not implemented");
        throw new SQLFeatureNotSupportedException("setFetchDirection not implemented");
    }

    @Override
    public int getFetchDirection() throws SQLException {
        this.checkClosed();
        return java.sql.ResultSet.FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        this.checkClosed();
        this.fetchSize = rows;
    }

    @Override
    public int getFetchSize() throws SQLException {
        this.checkClosed();
        return this.fetchSize;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        this.checkClosed();
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getResultSetType() throws SQLException {
        this.checkClosed();
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        this.checkClosed();
        LOGGER.log("addBatch not implemented");
        throw new SQLFeatureNotSupportedException("addBatch not implemented");
    }

    @Override
    public void clearBatch() throws SQLException {
        this.checkClosed();
        LOGGER.log("clearBatch not implemented");
        throw new SQLFeatureNotSupportedException("addBatch not implemented");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        this.checkClosed();
        LOGGER.log("executeBatch not implemented");
        throw new SQLFeatureNotSupportedException("executeBatch not implemented");
    }

    @Override
    public Connection getConnection() throws SQLException {
        this.checkClosed();
        return this.connection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        this.checkClosed();
        LOGGER.log("getMoreResults(%s)", current);
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        LOGGER.log("getGeneratedKeys not implemented");
        throw new SQLFeatureNotSupportedException("getGeneratedKeys not implemented");
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        this.executeUpdate(sql);
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return this.executeUpdate(sql, 0);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return this.executeUpdate(sql, 0);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        this.executeUpdate(sql);
        return true;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return this.execute(sql, 0);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return this.execute(sql, 0);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        this.checkClosed();
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        LOGGER.log("Statement isClosed = %s", isClosed);
        return this.isClosed;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        LOGGER.log("setPoolable not implemented");
        throw new SQLFeatureNotSupportedException("setPoolable not implemented");
    }

    @Override
    public boolean isPoolable() throws SQLException {
        this.checkClosed();
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        this.checkClosed();
        LOGGER.log("closeOnCompletion not implemented");
        throw new SQLFeatureNotSupportedException("closeOnCompletion not implemented");
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        this.checkClosed();
        LOGGER.log("isCloseOnCompletion not implemented");
        return false;
    }

    private void checkClosed() throws SQLException {
        if (isClosed()) {
            LOGGER.log("Statement is closed.");
            throw new SQLException("Statement is closed.");
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (ClassCastException cce) {
            LOGGER.log("Unable to unwrap to %s", iface);
            throw new SQLException("Unable to unwrap to " + iface);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
}
