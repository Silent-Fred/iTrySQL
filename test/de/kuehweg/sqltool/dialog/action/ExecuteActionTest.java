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
package de.kuehweg.sqltool.dialog.action;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.fake.ConnectionStubWithBasicMetaData;

/**
 * Test der Ausführung von SQL-Anweisungen
 *
 * @author Michael Kühweg
 */
public class ExecuteActionTest {

	private ExecuteAction action;

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		action = new ExecuteAction();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void handlingProblemsInGeneral() throws SQLException {
		Assert.assertNotNull(action.startExecution(null, null));
		Assert.assertNotNull(action.startExecution("select * from wherever;",
				null));
	}

	@Test
	public void specificMessages() throws SQLException {
		Assert.assertEquals(DialogDictionary.MSG_NO_STATEMENT_TO_EXECUTE,
				action.startExecution(null, null));
		Assert.assertEquals(DialogDictionary.MSG_NO_STATEMENT_TO_EXECUTE,
				action.startExecution("   \n   \t ", null));
		Assert.assertEquals(DialogDictionary.MSG_NO_STATEMENT_TO_EXECUTE,
				action.startExecution("   \n   \t ",
						new ConnectionStubWithBasicMetaData()));
		Assert.assertEquals(DialogDictionary.MSG_NO_DB_CONNECTION,
				action.startExecution("select * from wherever ", null));
	}

	@Test(expected = SQLException.class)
	public void handlingException() throws SQLException {
		action.startExecution("select * from wherever;",
				new ConnectionStubWithBasicMetaData());
	}

}
