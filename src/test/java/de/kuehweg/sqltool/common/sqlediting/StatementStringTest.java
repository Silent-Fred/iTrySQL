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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michael Kühweg
 */
public class StatementStringTest {

	public StatementStringTest() {
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
	public void empty() {
		assertTrue(new StatementString(null).isEmpty());
		assertTrue(new StatementString("").isEmpty());
		assertTrue(new StatementString("     ").isEmpty());
		assertTrue(new StatementString("\t").isEmpty());
		assertTrue(new StatementString("\n").isEmpty());

		assertTrue(new StatementString("--").isEmpty());
		assertTrue(new StatementString("/* empty */").isEmpty());

		assertTrue(new StatementString(" ; ").isEmpty());
	}

	@Test
	public void notEmpty() {
		assertFalse(new StatementString("values").isEmpty());
	}

	@Test
	public void uncomment() {
		assertEquals("select * from table;",
				new StatementString("/* Test */ select * --\nfrom table;").uncommentedStatement());
		assertEquals("select", new StatementString("/* Test */select/* Test */").uncommentedStatement());
		assertEquals("", new StatementString(null).uncommentedStatement());
		assertEquals("", new StatementString("").uncommentedStatement());
	}

	@Test
	public void uncommentWithCommentInsideLiteral() {
		assertEquals("select '/* literally a comment */' from table;",
				new StatementString("/* Test */ select '/* literally a comment */' --\nfrom table;")
						.uncommentedStatement());
	}

	@Test
	public void original() {
		final String sql = "/* Test */ select * --\nfrom table;";
		final StatementString statement = new StatementString(sql);
		assertEquals("select * from table;", statement.uncommentedStatement());
		assertEquals(sql, statement.originalStatement());
	}

	@Test
	public void firstKeyword() {
		assertEquals("SELECT", new StatementString("/* Test */ select * --\nfrom table;").firstKeyword());
		assertEquals("DROP", new StatementString("/* select */ drop --\n table;").firstKeyword());
	}

	@Test
	public void statementType() {
		assertTrue(new StatementString("/* select */ select * from table").isDataManipulationStatement());
		assertTrue(new StatementString("/* select */ select(1+1) from table").isDataManipulationStatement());
		// auch mit falscher Syntax bezogen auf die komplette Anweisung
		assertTrue(new StatementString("/* comment */ insert into whatever").isDataManipulationStatement());

		// auch mit falscher Syntax bezogen auf die komplette Anweisung
		assertTrue(new StatementString("DROP SELECT").isDataDefinitionStatement());
		assertTrue(new StatementString("ALTER TABLE").isDataDefinitionStatement());

		assertTrue(new StatementString("GRANT select").isDataControlStatement());

		assertTrue(new StatementString("COMMIT;").isTransactionControlStatement());
	}

	@Test
	public void notMyStatementType() {
		assertFalse(new StatementString("/* select */ select * from table").isDataControlStatement());
		assertFalse(new StatementString("/* select */ select * from table").isDataDefinitionStatement());
		assertFalse(new StatementString("/* select */ select * from table").isTransactionControlStatement());

		assertFalse(new StatementString("current_date").isDataControlStatement());
		assertFalse(new StatementString("current_date").isDataDefinitionStatement());
		assertFalse(new StatementString("current_date").isDataManipulationStatement());
		assertFalse(new StatementString("current_date").isTransactionControlStatement());
	}
}
