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
package de.kuehweg.sqltool.dialog.action;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.database.execution.fake.ConnectionStubWithBasicMetaData;
import de.kuehweg.sqltool.database.execution.fake.DatabaseMetaDataStubWithUrlAndUser;
import de.kuehweg.sqltool.database.execution.fake.FakeStatement;
import de.kuehweg.sqltool.database.execution.fake.FakeStatementThrowingExceptionOnExecute;
import de.kuehweg.sqltool.database.execution.fake.ResultSetStubFromObjectArray;
import de.kuehweg.sqltool.database.execution.fake.StatementStubWithFakeResultSet;

/**
 * @author Michael Kühweg
 */
public class ExecutionTaskTest {

	private static final String URL = "db://localhost";
	private static final String USER_NAME = "john_doe";

	private Object[] columnLabels;
	private Object[] columnContent;

	private DatabaseMetaDataStubWithUrlAndUser metaData;
	private ConnectionStubWithBasicMetaData connection;
	private ResultSetStubFromObjectArray resultSet;
	private FakeStatement statement;

	public ExecutionTaskTest() {
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
		metaData.setURL(URL);
		metaData.setUserName(USER_NAME);
		connection = new ConnectionStubWithBasicMetaData();
		connection.setMetaData(metaData);
		resultSet = new ResultSetStubFromObjectArray(new Object[][] { columnLabels, columnContent });
	}

	@After
	public void tearDown() {
	}

	@Test
	public void noFrillsExecution() throws Exception {
		statement = new StatementStubWithFakeResultSet(connection, resultSet);
		final ExecutionTask execution = new ExecutionTask("select * from wherever;", statement);
		final ExecutionTrackerStub tracker = new ExecutionTrackerStub();
		execution.attach(tracker);
		execution.call();
		assertNull(tracker.getMessage());
		assertTrue(tracker.getBeforeExecutionCalls() == 1);
		assertTrue(tracker.getIntermediateUpdateCalls() == 1);
		assertTrue(tracker.getAfterExecutionCalls() == 1);
		assertTrue(tracker.getErrorOnExecutionCalls() == 0);
	}

	@Test
	public void errorOnExecution() throws Exception {
		statement = new FakeStatementThrowingExceptionOnExecute(connection, resultSet);
		final ExecutionTask execution = new ExecutionTask("select * from wherever;", statement);
		final ExecutionTrackerStub tracker = new ExecutionTrackerStub();
		execution.attach(tracker);
		execution.call();
		assertNotNull(tracker.getMessage());
		assertTrue(tracker.getBeforeExecutionCalls() == 1);
		assertTrue(tracker.getIntermediateUpdateCalls() == 0);
		assertTrue(tracker.getAfterExecutionCalls() == 0);
		assertTrue(tracker.getErrorOnExecutionCalls() == 1);
	}

	@Test
	public void noTrackerExecution() throws Exception {
		statement = new StatementStubWithFakeResultSet(connection, resultSet);
		final ExecutionTask execution = new ExecutionTask("select * from wherever;", statement);
		execution.call();
		// no exception
	}

}
