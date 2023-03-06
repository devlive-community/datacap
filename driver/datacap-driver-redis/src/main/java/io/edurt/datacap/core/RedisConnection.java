package io.edurt.datacap.core;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class RedisConnection implements Connection {
    private final static Logger LOGGER = new Logger(RedisConnection.class);

    private final RedisClient redisClient;
    private final Properties properties;

    private String dbIndex;

    public RedisConnection(RedisClient redisClient, String dbIndex, Properties properties) {
        this.redisClient = redisClient;
        this.dbIndex = dbIndex;
        this.properties = properties;
    }

    private boolean isClosed = false;


    @Override
    public Statement createStatement() throws SQLException {
        this.checkClosed();
        return new RedisStatement(this, this.redisClient);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        // TODO 暂不实现，感觉意义不大，未来看是否需要实现
        LOGGER.log("prepareStatement not implemented");
        throw new SQLFeatureNotSupportedException("prepareStatement not implemented");
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        // TODO 暂不实现，感觉无意义，因为主要是执行存储过程的，redis没这玩意
        LOGGER.log("prepareCall not implemented");
        throw new SQLFeatureNotSupportedException("prepareCall not implemented");
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        LOGGER.log("nativeSQL not implemented");
        throw new SQLFeatureNotSupportedException("nativeSQL not implemented");
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return true;
    }

    @Override
    public void commit() throws SQLException {
        // TODO 待支持事务
    }

    @Override
    public void rollback() throws SQLException {
        // TODO
    }

    @Override
    public void close() throws SQLException {
        this.redisClient.close();
        LOGGER.log("Connection close");
        this.isClosed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        LOGGER.log("Connection isClosed = %s", isClosed);
        return this.isClosed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return new RedisDatabaseMetadata(this, this.dbIndex);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        // do nothing
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        LOGGER.log("setCatalog(%s)", catalog);
        // do nothing
    }

    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return Connection.TRANSACTION_NONE;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        LOGGER.log("getWarnings returns null");
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return this.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return this.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return this.prepareCall(sql);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        LOGGER.log("getTypeMap not implemented");
        throw new SQLFeatureNotSupportedException("getTypeMap not implemented");
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        LOGGER.log("setTypeMap not implemented");
        throw new SQLFeatureNotSupportedException("setTypeMap not implemented");
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        // do nothing
    }

    @Override
    public int getHoldability() throws SQLException {
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        LOGGER.log("setSavepoint not implemented");
        throw new SQLFeatureNotSupportedException("setSavepoint not implemented");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        LOGGER.log("setSavepoint not implemented");
        throw new SQLFeatureNotSupportedException("setSavepoint not implemented");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        LOGGER.log("rollback not implemented");
        throw new SQLFeatureNotSupportedException("rollback not implemented");
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        LOGGER.log("releaseSavepoint not implemented");
        throw new SQLFeatureNotSupportedException("releaseSavepoint not implemented");
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this.prepareCall(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return this.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return this.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return this.prepareStatement(sql);
    }

    @Override
    public Clob createClob() throws SQLException {
        LOGGER.log("createClob not implemented");
        throw new SQLFeatureNotSupportedException("createClob not implemented");
    }

    @Override
    public Blob createBlob() throws SQLException {
        LOGGER.log("createBlob not implemented");
        throw new SQLFeatureNotSupportedException("createBlob not implemented");
    }

    @Override
    public NClob createNClob() throws SQLException {
        LOGGER.log("createNClob not implemented");
        throw new SQLFeatureNotSupportedException("createNClob not implemented");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        LOGGER.log("createSQLXML not implemented");
        throw new SQLFeatureNotSupportedException("createSQLXML not implemented");
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        LOGGER.log("isValid = %s", isClosed);
        if (this.isClosed) {
            return false;
        }
        // TODO 超时时间
        try {
            this.redisClient.sendCommand("PING");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this.properties.put(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.properties.putAll(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        this.checkClosed();

        String property = this.properties.getProperty(name);
        property = property == null ? "" : property;
        LOGGER.log("getClientInfo(%s) = %s, properties = %s", name, property, properties);
        return property;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        LOGGER.log("getClientInfo() = %s", properties);
        return this.properties;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        LOGGER.log("createArrayOf not implemented");
        throw new SQLFeatureNotSupportedException("createArrayOf not implemented");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        LOGGER.log("createStruct not implemented");
        throw new SQLFeatureNotSupportedException("createStruct not implemented");
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        synchronized (RedisConnection.class) {
            LOGGER.log("setSchema(%s)", schema);
            this.checkClosed();

            this.redisClient.select(Integer.parseInt(schema));

            this.dbIndex = schema;
        }
    }

    @Override
    public String getSchema() throws SQLException {
        synchronized (RedisConnection.class) {
            this.checkClosed();
            LOGGER.log("getSchema() = %s", this.dbIndex);
            return this.dbIndex;
        }
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        LOGGER.log("abort not implemented");
        throw new SQLFeatureNotSupportedException("abort not implemented");
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        LOGGER.log("setNetworkTimeout not implemented");
        throw new SQLFeatureNotSupportedException("setNetworkTimeout not implemented");
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        LOGGER.log("getNetworkTimeout not implemented");
        throw new SQLFeatureNotSupportedException("getNetworkTimeout not implemented");
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
        this.checkClosed();
        return iface.isInstance(this);
    }

    private void checkClosed() throws SQLException {
        if (isClosed()) {
            LOGGER.log("Connection is closed.");
            throw new SQLException("Connection is closed.");
        }
    }
}
