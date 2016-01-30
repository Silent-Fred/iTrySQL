/*
 * Copyright (c) 2015-2016, Michael Kühweg
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
package de.kuehweg.sqltool.database.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.sqlediting.StatementString;
import de.kuehweg.sqltool.database.execution.fake.ConnectionStubWithBasicMetaData;
import de.kuehweg.sqltool.database.execution.fake.DatabaseMetaDataStubWithUrlAndUser;
import de.kuehweg.sqltool.database.execution.fake.FakeStatement;
import de.kuehweg.sqltool.database.execution.fake.FakeStatementThrowingExceptionOnExecute;
import de.kuehweg.sqltool.database.execution.fake.FakeStatementUpdateCount42;
import de.kuehweg.sqltool.database.execution.fake.ResultSetStubFromObjectArray;
import de.kuehweg.sqltool.database.execution.fake.StatementStubWithFakeResultSet;

/**
 * @author Michael Kühweg
 */
public class StatementExecutionTest {

	private static final String url = "db://localhost";
	private static final String userName = "john_doe";

	private Object[] columnLabels;
	private Object[] columnContent;

	private DatabaseMetaDataStubWithUrlAndUser metaData;
	private ConnectionStubWithBasicMetaData connection;
	private ResultSetStubFromObjectArray resultSet;
	private FakeStatement statement;

	public StatementExecutionTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		columnLabels = new String[] { "does", "not", "compute" };
		columnContent = new Object[] { " col1 ", null, 42 };
		metaData = new DatabaseMetaDataStubWithUrlAndUser();
		metaData.setURL(url);
		metaData.setUserName(userName);
		connection = new ConnectionStubWithBasicMetaData();
		connection.setMetaData(metaData);
		resultSet = new ResultSetStubFromObjectArray(new Object[][] { columnLabels, columnContent });
	}

	@After
	public void tearDown() {
	}

	@Test
	public void emptyQuery() throws SQLException {
		final StatementExecution execution = new StatementExecution(null);
		statement = new StatementStubWithFakeResultSet(connection, resultSet);
		final StatementExecutionInformation info = execution.execute(statement);
		assertNull(info.getStatementResult());
		assertEquals(DialogDictionary.LABEL_RESULT_ERROR.toString(), info.getSummary());
	}

	@Test
	public void querySuccessfulWithResult() throws SQLException {
		final String sql = "select * from wherever";
		final StatementExecution execution = new StatementExecution(new StatementString(sql));
		statement = new StatementStubWithFakeResultSet(connection, resultSet);
		final StatementExecutionInformation info = execution.execute(statement);
		assertNotNull(info.getStatementResult());
		assertNotSame(DialogDictionary.LABEL_RESULT_ERROR.toString(), info.getSummary());
		assertEquals(url, info.getConnectionDescription());
		assertEquals(userName, info.getExecutedBy());
		assertNotNull(info.getStartOfExecution());
		assertNotNull(info.getEndOfExecution());
		assertEquals(sql, info.getSql().originalStatement());
		for (int col = 0; col < columnLabels.length; col++) {
			assertEquals(columnLabels[col], info.getStatementResult().getHeader().getColumnHeaders()[col]);
		}
		assertEquals(1, info.getStatementResult().getRows().size());
		final ResultRow row = info.getStatementResult().getRows().iterator().next();
		final List<String> resultColumns = row.columnsAsString();
		assertEquals(" col1 ", resultColumns.get(0));
		assertEquals(ResultRow.NULL_STR, resultColumns.get(1));
		assertEquals("42", resultColumns.get(2));
	}

	@Test
	public void maxRows() throws SQLException {
		final String sql = "select * from wherever";
		final StatementExecution execution = new StatementExecution(new StatementString(sql));
		statement = new StatementStubWithFakeResultSet(connection, resultSet);
		StatementExecutionInformation info = execution.execute(statement);
		assertFalse(info.isLimitMaxRowsReached());
		statement.setMaxRows(2);
		info = execution.execute(statement);
		assertFalse(info.isLimitMaxRowsReached());
		statement.setMaxRows(1);
		info = execution.execute(statement);
		assertTrue(info.isLimitMaxRowsReached());
	}

	@Test
	public void closedResultSetAfterExecution() throws SQLException {
		final String sql = "select * from wherever";
		final StatementExecution execution = new StatementExecution(new StatementString(sql));
		statement = new StatementStubWithFakeResultSet(connection, resultSet);
		execution.execute(statement);
		assertTrue(statement.getResultSet().isClosed());
	}

	@Test
	public void exceptionOnFetch() throws SQLException {
		final String sql = "select * from wherever";
		final StatementExecution execution = new StatementExecution(new StatementString(sql));
		statement = new StatementStubWithFakeResultSet(connection, null);
		final StatementExecutionInformation info = execution.execute(statement);
		assertNull(info.getStatementResult());
		assertEquals(DialogDictionary.LABEL_RESULT_ERROR.toString(), info.getSummary());
	}

	@Test(expected = SQLException.class)
	public void exceptionOnExecution() throws SQLException {
		final String sql = "select * from wherever";
		final StatementExecution execution = new StatementExecution(new StatementString(sql));
		statement = new FakeStatementThrowingExceptionOnExecute(connection, resultSet);
		execution.execute(statement);
	}

	@Test
	public void updateCountForDML() throws SQLException {
		for (final String sql : StatementString.DML_COMMANDS) {
			updateCountForDML(sql);
		}
	}

	private void updateCountForDML(final String sql) throws SQLException {
		final StatementExecution execution = new StatementExecution(new StatementString(sql));
		statement = new FakeStatementUpdateCount42(connection);
		final StatementExecutionInformation info = execution.execute(statement);
		assertNull(info.getStatementResult());
		assertEquals(MessageFormat.format(DialogDictionary.PATTERN_UPDATECOUNT.toString(), 42), info.getSummary());
	}

	@Test
	public void summaryForOtherThanDML() throws SQLException {
		for (final String sql : StatementString.DCL_COMMANDS) {
			summaryForOtherThanDML(sql);
		}
		for (final String sql : StatementString.DDL_COMMANDS) {
			summaryForOtherThanDML(sql);
		}
		for (final String sql : StatementString.TCL_COMMANDS) {
			summaryForOtherThanDML(sql);
		}
	}

	private void summaryForOtherThanDML(final String sql) throws SQLException {
		final StatementExecution execution = new StatementExecution(new StatementString(sql));
		statement = new FakeStatementUpdateCount42(connection);
		final StatementExecutionInformation info = execution.execute(statement);
		assertNull(info.getStatementResult());
		assertEquals(MessageFormat.format(DialogDictionary.PATTERN_EXECUTED_STATEMENT.toString(),
				info.getSql().firstKeyword()), info.getSummary());
	}

}
