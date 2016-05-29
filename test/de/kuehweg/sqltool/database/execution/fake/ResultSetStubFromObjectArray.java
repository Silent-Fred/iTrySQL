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
 * ResultSet auf Basis eines zweidimensionalen Object-Arrays. Die erste Zeile
 * enthält die Spaltenüberschriften, die weiteren Zeilen die Testinhalte.
 *
 * @author Michael Kühweg
 */
public class ResultSetStubFromObjectArray extends FakeResultSet {

	private final Object[][] resultSet;

	private boolean closed;

	private int currentRow;

	private int currentColumn;

	public ResultSetStubFromObjectArray(final Object[][] fakeResultSetContent) {
		resultSet = fakeResultSetContent;
	}

	@Override
	public boolean next() throws SQLException {
		// erste Zeile sind die Spaltenüberschriften - überspringen
		currentRow++;
		currentColumn = 0;
		return currentRow < resultSet.length;
	}

	@Override
	public void close() throws SQLException {
		closed = true;
		currentRow = 0;
		currentColumn = 0;
	}

	@Override
	public boolean wasNull() throws SQLException {
		if (resultSet == null || currentRow < 0 || currentRow >= resultSet.length || currentColumn < 0
				|| currentColumn >= resultSet[0].length) {
			throw new SQLException();
		}
		return resultSet[currentRow][currentColumn] == null;
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new ResultSetMetaDataStubFromObjectArray(resultSet);
	}

	@Override
	public Object getObject(final int columnIndex) throws SQLException {
		// columnIndex ist nicht 0 based
		if (resultSet == null || currentRow < 0 || currentRow >= resultSet.length || columnIndex < 1
				|| columnIndex > resultSet[0].length) {
			throw new SQLException();
		}
		currentColumn = columnIndex - 1;
		return resultSet[currentRow][currentColumn];
	}

	@Override
	public boolean isClosed() throws SQLException {
		return closed;
	}

}
