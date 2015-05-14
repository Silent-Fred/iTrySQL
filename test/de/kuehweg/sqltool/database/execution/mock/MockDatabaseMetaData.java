/*
 * Copyright (c) 2015, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.kuehweg.sqltool.database.execution.mock;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

/**
 *
 * @author Michael Kühweg
 */
public abstract class MockDatabaseMetaData implements DatabaseMetaData {

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getURL() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getUserName() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getDriverName() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getDriverVersion() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getDriverMajorVersion() {
        return 0;
    }

    @Override
    public int getDriverMinorVersion() {
        return 1;
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getStringFunctions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxConnections() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxStatements() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern,
            String procedureNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern,
            String procedureNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern,
            String tableNamePattern, String[] types) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern,
            String tableNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table,
            String columnNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern,
            String tableNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table,
            int scope, boolean nullable) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getCrossReference(String parentCatalog, String parentSchema,
            String parentTable, String foreignCatalog, String foreignSchema,
            String foreignTable) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getIndexInfo(String catalog, String schema, String table,
            boolean unique, boolean approximate) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsResultSetType(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean updatesAreDetected(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean deletesAreDetected(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean insertsAreDetected(int type) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern,
            int[] types) throws SQLException {
        throw new SQLException();
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern,
            String typeNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern,
            String tableNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern,
            String typeNamePattern, String attributeNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getSQLStateType() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        throw new SQLException();
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern,
            String functionNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern,
            String functionNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern,
            String tableNamePattern, String columnNamePattern) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        throw new SQLException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isWrapperFor(
            Class<?> iface) throws SQLException {
        throw new SQLException();
    }

}
