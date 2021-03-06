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
package de.kuehweg.sqltool.database.execution.fake;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Connection, die beim Aufruf jeglicher Operationen die deklarierte Exception
 * wirft.
 *
 * @author Michael Kühweg
 */
public abstract class FakeConnection implements Connection {

	@Override
	public Statement createStatement() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public String nativeSQL(final String sql) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void commit() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void rollback() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void close() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public boolean isClosed() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setCatalog(final String catalog) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public String getCatalog() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setHoldability(final int holdability) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public int getHoldability() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Clob createClob() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Blob createBlob() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public NClob createNClob() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public boolean isValid(final int timeout) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		throw new SQLClientInfoException();
	}

	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		throw new SQLClientInfoException();
	}

	@Override
	public String getClientInfo(final String name) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setSchema(final String schema) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public String getSchema() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void abort(final Executor executor) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new SQLException("Fake-Connection");
	}

}
