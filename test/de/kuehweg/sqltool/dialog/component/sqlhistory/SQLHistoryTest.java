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
package de.kuehweg.sqltool.dialog.component.sqlhistory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.dialog.component.sqlhistory.SqlHistoryEntry;

/**
 * Test für die Verlaufseinträge.
 *
 * @author Michael Kühweg
 */
public class SQLHistoryTest {

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
	public void sqlForDisplayReplaceLinebreaks() {
		Assert.assertEquals("just one  line", new SqlHistoryEntry(" \njust\none\n\nline\n\n\n").getSqlForDisplay());
	}

	@Test
	public void sqlForDisplayReplaceTabs() {
		Assert.assertEquals("just plain  blanks", new SqlHistoryEntry(" \tjust\tplain\n\nblanks\t\t\t").getSqlForDisplay());
	}

	@Test
	public void sqlForDisplayLengthRestricted() {
		// short enough
		Assert.assertEquals("12345678901234567890123456789012345678901234567890123456789012345678901234567890",
				new SqlHistoryEntry("12345678901234567890123456789012345678901234567890123456789012345678901234567890")
						.getSqlForDisplay());
		// far too long
		Assert.assertEquals("12345678901234567890123456789012345678901234567890123456789012345678901234567...",
				new SqlHistoryEntry("123456789012345678901234567890123456789012345678901234567890123456789012345678901")
						.getSqlForDisplay());
	}

	@Test
	public void originalSQL() {
		Assert.assertEquals(" \njust\none\n\nline\n\n\n",
				new SqlHistoryEntry(" \njust\none\n\nline\n\n\n").getOriginalSQL());
		Assert.assertEquals(" \tjust\tplain\n\nblanks\t\t\t",
				new SqlHistoryEntry(" \tjust\tplain\n\nblanks\t\t\t").getOriginalSQL());
	}

	@Test
	public void timestamp() {
		final long before = System.currentTimeMillis();
		final SqlHistoryEntry history = new SqlHistoryEntry("as time goes by...");
		Assert.assertTrue(history.getTimestamp() >= before);
		Assert.assertTrue(history.getTimestamp() <= System.currentTimeMillis());
	}
}
