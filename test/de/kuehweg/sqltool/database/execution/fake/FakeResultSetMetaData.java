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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * ResultSetMetaData Implementierung für Tests. Wirft für jede Operation eine
 * {@link SQLException}, kann also z.B. zur Abischerung korrekter
 * Fehlerbehandlung in aufrufenden Klassen verwendet werden.
 * 
 * @author Michael Kühweg
 */
public abstract class FakeResultSetMetaData implements ResultSetMetaData {

	@Override
	public int getColumnCount() throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isAutoIncrement(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isCaseSensitive(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isSearchable(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isCurrency(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public int isNullable(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isSigned(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public int getColumnDisplaySize(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public String getColumnLabel(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public String getColumnName(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public String getSchemaName(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public int getPrecision(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public int getScale(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public String getTableName(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public String getCatalogName(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public int getColumnType(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public String getColumnTypeName(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isReadOnly(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isWritable(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isDefinitelyWritable(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public String getColumnClassName(final int column) throws SQLException {
		throw new SQLException();
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new SQLException();
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new SQLException();
	}

}
