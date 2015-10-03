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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Statement Implementierung für Tests. Wirft für jede Operation eine
 * {@link SQLException}, kann also z.B. zur Absicherung korrekter
 * Fehlerbehandlung in aufrufenden Klassen verwendet werden.
 * 
 * @author Michael Kühweg
 */
public abstract class FakeStatement implements Statement {

	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int executeUpdate(final String sql) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void close() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setMaxFieldSize(final int max) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getMaxRows() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setMaxRows(final int max) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setEscapeProcessing(final boolean enable) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setQueryTimeout(final int seconds) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void cancel() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setCursorName(final String name) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean execute(final String sql) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getUpdateCount() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getResultSetType() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void addBatch(final String sql) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void clearBatch() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int[] executeBatch() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public Connection getConnection() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean getMoreResults(final int current) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean execute(final String sql, final String[] columnNames) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean isClosed() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void setPoolable(final boolean poolable) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean isPoolable() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new SQLException("Fake-Statement");
	}

}
