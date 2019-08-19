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
package de.kuehweg.sqltool.common.sqlediting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test für die Zerlegung der Eingabe in Anweisungen.
 *
 * @author Michael Kühweg
 */
public class StatementExtractorTest {

	public StatementExtractorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void splitWithComments() {
		assertEquals(1,
				new StatementExtractor()
						.getStatementsFromScript(
								"/* start here... with a confusing ';' */\ncreate table test (test numeric(1));\n")
						.size());
		assertEquals(1, new StatementExtractor()
				.getStatementsFromScript("-- select * from test;\nselect ';' from test;").size());
	}

	@Test
	public void splitEmpty() {
		// Kommentare nach einer Anweisung beginnen laut derzeitiger Definition
		// ein neues Statement
		assertEquals(2,
				new StatementExtractor()
						.getStatementsFromScript(
								"-- select * from test;\nselect ';' from test; -- an open end as empty statement")
						.size());
		assertTrue(new StatementExtractor()
				.getStatementsFromScript(
						"-- select * from test;\nselect ';' from test; -- an open end as empty statement")
				.get(1).isEmpty());
		// nur Leerzeichen liefern nach Ende kein neues Statement
		assertEquals(1, new StatementExtractor().getStatementsFromScript("select ';' from test; ").size());
		assertEquals(2, new StatementExtractor().getStatementsFromScript("select ';' from test;;").size());
		assertTrue(new StatementExtractor().getStatementsFromScript("select ';' from test;;").get(1).isEmpty());
	}

	@Test
	public void insideSpecial() {
		assertEquals(1, new StatementExtractor()
				.getStatementsFromScript("-- select * from test;\nselect ';' /* ; */ from test;").size());
	}

	@Test
	public void positioning() {
		final String firstStatement = "-- select * from test;\nselect ';' /* ; */ from test;";
		final String secondStatement = "values(current_date)   ";
		final String script = firstStatement + secondStatement;
		assertEquals(firstStatement.trim(), new StatementExtractor().extractStatementAtCaretPosition(script, 0));
		assertEquals(firstStatement.trim(), new StatementExtractor().extractStatementAtCaretPosition(script, 1));
		assertEquals(firstStatement.trim(),
				new StatementExtractor().extractStatementAtCaretPosition(script, firstStatement.length() - 1));

		assertEquals(secondStatement.trim(),
				new StatementExtractor().extractStatementAtCaretPosition(script, firstStatement.length()));
		assertEquals(secondStatement.trim(),
				new StatementExtractor().extractStatementAtCaretPosition(script, script.length() - 1));
	}

	@Test
	public void emptyStatement() {
		assertNotNull(new StatementExtractor().getStatementsFromScript(null));
		assertTrue(new StatementExtractor().getStatementsFromScript(null).isEmpty());
		assertTrue(new StatementExtractor().getStatementsFromScript("").isEmpty());
		assertTrue(new StatementExtractor().getStatementsFromScript(" ").isEmpty());
	}
}
