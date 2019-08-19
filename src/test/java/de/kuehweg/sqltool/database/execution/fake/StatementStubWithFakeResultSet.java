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

/**
 * Statement Stub für Tests. Liefert ein definiertes, im Konstruktor übergebenes
 * ResultSet für Test zurück.
 *
 * @author Michael Kühweg
 */
public class StatementStubWithFakeResultSet extends FakeStatement {

	private final ResultSet resultSet;

	private final Connection connection;

	private int maxRows;

	public StatementStubWithFakeResultSet(final Connection connection, final ResultSet resultSet) {
		this.connection = connection;
		this.resultSet = resultSet;
	}

	@Override
	public boolean execute(final String sql) throws SQLException {
		return true;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		if (resultSet == null) {
			throw new SQLException();
		}
		return resultSet;
	}

	@Override
	public void close() throws SQLException {
		// ohne Exception
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public void setMaxRows(final int max) throws SQLException {
		maxRows = max;
	}

	@Override
	public int getMaxRows() throws SQLException {
		return maxRows;
	}
}
