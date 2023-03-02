package io.edurt.datacap.core;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.*;
import java.util.stream.IntStream;

public class RedisDatabaseMetadata implements DatabaseMetaData {
    private final static Logger LOGGER = new Logger(RedisDatabaseMetadata.class);

    private final RedisConnection connection;
    private final String dbIndex;

    public RedisDatabaseMetadata(RedisConnection connection, String dbIndex) {
        this.connection = connection;
        this.dbIndex = dbIndex;
    }

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        return false;
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        return false;
    }

    @Override
    public String getURL() throws SQLException {
        return null;
    }

    @Override
    public String getUserName() throws SQLException {
        return null;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        return false;
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        return "Redis";
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        return this.getVersion().getOrigin();
    }

    private ServerVersion getVersion() throws SQLException {
        Statement statement = connection.createStatement();
        String version = "0.0.1";
        try {
            ResultSet rs = statement.executeQuery("INFO server");
            while (rs.next()) {
                String result = rs.getString(0);
                String versionX = new BufferedReader(new StringReader(result))
                        .lines()
                        .filter(line -> line.startsWith("redis_version"))
                        .findFirst()
                        .map(line -> line.substring(line.indexOf(":") + 1))
                        .orElse(null);
                if (versionX != null) {
                    version = versionX;
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.log("getDatabaseProductVersion exception occurs, " + e.getMessage());
        }
        return new ServerVersion(version);
    }

    @Override
    public String getDriverName() throws SQLException {
        return "Redis JDBC Connector/J";
    }

    @Override
    public String getDriverVersion() throws SQLException {
        return "redis-jdbc-driver 1.0.0";
    }

    @Override
    public int getDriverMajorVersion() {
        return 1;
    }

    @Override
    public int getDriverMinorVersion() {
        return 0;
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        return false;
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        return null;
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        return null;
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        return null;
    }

    @Override
    public String getStringFunctions() throws SQLException {
        return null;
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        return null;
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        return null;
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        return null;
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        return null;
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        return false;
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        return false;
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        return "SCHEMA";
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        return "PROCEDURE";
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        return "CATALOG";
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        return false;
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        return ".";
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return false;
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxConnections() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        return 64;
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        return 100;
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        return 16;
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return false;
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        return 65535;
    }

    @Override
    public int getMaxStatements() throws SQLException {
        return 64;
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        return 64;
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        return 64;
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        return 0;
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        return 0;
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        LOGGER.log("supportsTransactions() = false");
        return false;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        LOGGER.log("supportsTransactionIsolationLevel() = false");
        return false;
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        LOGGER.log("supportsDataDefinitionAndDataManipulationTransactions() = false");
        return false;
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        LOGGER.log("supportsDataManipulationTransactionsOnly() = false");
        return false;
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        LOGGER.log("dataDefinitionCausesTransactionCommit() = false");
        return false;
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        LOGGER.log("dataDefinitionIgnoredInTransactions() = false");
        return false;
    }

    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        LOGGER.log("getProcedures()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        LOGGER.log("getProcedureColumns()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        LOGGER.log("getTables(%s, %s, %s, %s)", catalog, schemaPattern, tableNamePattern, types);
        return new RedisResultSet(new String[]{"haha"}, connection.createStatement());
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        LOGGER.log("getSchemas()");
        ResultSet rs;
        Statement statement = connection.createStatement();

        String[] databases = IntStream.range(0, 16)
                .mapToObj(i -> i + "")
                .toArray(String[]::new);
        rs = new RedisResultSet(databases, statement);
        return rs;
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        LOGGER.log("DatabaseMetadata getCatalogs()");
        return new RedisResultSet(new String[0], connection.createStatement());
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        LOGGER.log("getTableTypes()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        LOGGER.log("getColumns(%s, %s, %s, %s)", catalog, schemaPattern, tableNamePattern, columnNamePattern);
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        LOGGER.log("getColumnPrivileges()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        LOGGER.log("getTablePrivileges()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        LOGGER.log("getBestRowIdentifier()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        LOGGER.log("getVersionColumns()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        LOGGER.log("getPrimaryKeys()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        LOGGER.log("getImportedKeys()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        LOGGER.log("getExportedKeys()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        LOGGER.log("getCrossReference()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        LOGGER.log("getTypeInfo()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        LOGGER.log("getIndexInfo()");
        return new EmptyResultSet();
    }

    @Override
    public boolean supportsResultSetType(int type) throws SQLException {
        LOGGER.log("supportsResultSetType(%s)", type);
        return type == ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        LOGGER.log("supportsResultSetConcurrency(%s, concurrency)", type, concurrency);
        return false;
    }

    @Override
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        LOGGER.log("ownUpdatesAreVisible(%s)", type);
        return false;
    }

    @Override
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        LOGGER.log("ownDeletesAreVisible(%s)", type);
        return false;
    }

    @Override
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        LOGGER.log("ownInsertsAreVisible(%s)", type);
        return false;
    }

    @Override
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        LOGGER.log("othersUpdatesAreVisible(%s)", type);
        return false;
    }

    @Override
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        LOGGER.log("othersDeletesAreVisible(%s)", type);
        return false;
    }

    @Override
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        LOGGER.log("othersInsertsAreVisible(%s)", type);
        return false;
    }

    @Override
    public boolean updatesAreDetected(int type) throws SQLException {
        LOGGER.log("updatesAreDetected(%s)", type);
        return false;
    }

    @Override
    public boolean deletesAreDetected(int type) throws SQLException {
        LOGGER.log("deletesAreDetected(%s)", type);
        return false;
    }

    @Override
    public boolean insertsAreDetected(int type) throws SQLException {
        LOGGER.log("insertsAreDetected(%s)", type);
        return false;
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        LOGGER.log("supportsBatchUpdates()");
        return false;
    }

    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        LOGGER.log("getUDTs()");
        return new EmptyResultSet();
    }

    @Override
    public Connection getConnection() throws SQLException {
        LOGGER.log("getConnection()");
        return this.connection;
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        LOGGER.log("supportsSavepoints()");
        return false;
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        LOGGER.log("supportsNamedParameters()");
        return false;
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        LOGGER.log("supportsMultipleOpenResults()");
        return false;
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        LOGGER.log("supportsGetGeneratedKeys()");
        return false;
    }

    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        LOGGER.log("getSuperTypes()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        LOGGER.log("getSuperTables()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        LOGGER.log("getAttributes()");
        return new EmptyResultSet();
    }

    @Override
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        LOGGER.log("supportsResultSetHoldability()");
        return holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        LOGGER.log("getResultSetHoldability()");
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        LOGGER.log("getDatabaseMajorVersion()");
        return this.getVersion().getMajor();
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        LOGGER.log("getDatabaseMinorVersion()");
        return this.getVersion().getMinor();
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
        LOGGER.log("getJDBCMajorVersion()");
        return 4;
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
        LOGGER.log("getJDBCMinorVersion()");
        return 0;
    }

    @Override
    public int getSQLStateType() throws SQLException {
        LOGGER.log("getSQLStateType()");
        return DatabaseMetaData.sqlStateSQL;
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        LOGGER.log("locatorsUpdateCopy()");
        return false;
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        LOGGER.log("supportsStatementPooling()");
        return false;
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        LOGGER.log("getRowIdLifetime()");
        return null;
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        LOGGER.log("getSchemas(%s, %s)", catalog, schemaPattern);
        if (schemaPattern.equals("%")) {
            return this.getSchemas();
        }

        schemaPattern = Utils.isNumber(schemaPattern) ? schemaPattern : dbIndex;

        return new RedisResultSet(new String[]{schemaPattern}, this.connection.createStatement());
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        LOGGER.log("supportsStoredFunctionsUsingCallSyntax()");
        return false;
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        LOGGER.log("autoCommitFailureClosesAllResultSets()");
        return false;
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        LOGGER.log("autoCommitFailureClosesAllResultSets()");
        //TODO https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html#setClientInfo-java.lang.String-java.lang.String-
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        LOGGER.log("getFunctions()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        LOGGER.log("getFunctionColumns()");
        return new EmptyResultSet();
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        LOGGER.log("getPseudoColumns()");
        return new EmptyResultSet();
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        LOGGER.log("generatedKeyAlwaysReturned()");
        return false;
    }

    @Override
    public long getMaxLogicalLobSize() throws SQLException {
        LOGGER.log("getMaxLogicalLobSize()");
        return DatabaseMetaData.super.getMaxLogicalLobSize();
    }

    @Override
    public boolean supportsRefCursors() throws SQLException {
        LOGGER.log("supportsRefCursors()");
        return DatabaseMetaData.super.supportsRefCursors();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            LOGGER.log("unwrap()");
            return iface.cast(this);
        } catch (ClassCastException cce) {
            LOGGER.log("Unable to unwrap to %s", iface);
            throw new SQLException("Unable to unwrap to " + iface);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        LOGGER.log("isWrapperFor()");
        return iface.isInstance(this);
    }
}
