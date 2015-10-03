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
package de.kuehweg.sqltool.database.formatter;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.ResultHeader;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.database.execution.StatementResult;

/**
 * Test für die Textformatierung von Abfrageergebnissen.
 * 
 * @author Michael Kühweg
 */
public class TextResultFormatterTest {

	private StatementExecutionInformation info;

	public TextResultFormatterTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		info = new StatementExecutionInformation();

		info.setExecutedBy("executor");
		info.setConnectionDescription("quelle_connerie");
		info.setStartOfExecution(new GregorianCalendar(1984, Calendar.JANUARY, 24, 9, 30, 13).getTimeInMillis());
		info.setEndOfExecution(new GregorianCalendar(1984, Calendar.JANUARY, 24, 9, 30, 42).getTimeInMillis());
		info.setSummary("all's well that ends well");

		final StatementResult statementResult = new StatementResult();
		statementResult.setHeader(new ResultHeader("col1", "col2", "col3"));
		statementResult.addRow(new ResultRow(1, "1", "ABCDE"));
		statementResult.addRow(new ResultRow(2, "1234567890", "A"));

		info.setStatementResult(statementResult);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void fullTemplate() {

		final ResultTemplate template = new ResultTemplate();
		template.setTemplate("{0}\n{1}\n{2}\n{3}\n");

		final String whenAndWho = "[24.01.1984 09:30:13 executor@quelle_connerie]";
		final String header = "col1 col2       col3 \n---- ---------- -----";
		final String row1 = "1    1          ABCDE";
		final String row2 = "2    1234567890 A    ";
		final String rowCount = MessageFormat.format(DialogDictionary.PATTERN_ROWCOUNT.toString(), 2);
		final String limited = MessageFormat.format(DialogDictionary.PATTERN_MAX_ROWS.toString(), 2);

		final String expectedBasicResult = whenAndWho + "\n" + header + "\n" + row1 + "\n" + row2 + "\n" + rowCount
				+ "\n";
		final String expectedResultUnlimited = expectedBasicResult + "" + "\n";
		Assert.assertEquals(expectedResultUnlimited, new TextResultFormatter(info).format(template));

		info.setLimitMaxRowsReached(true);
		final String expectedResultLimited = expectedBasicResult + limited + "\n";
		Assert.assertEquals(expectedResultLimited, new TextResultFormatter(info).format(template));
	}

	@Test
	public void partialTemplate() {

		final ResultTemplate template = new ResultTemplate();

		final String whenAndWho = "[24.01.1984 09:30:13 executor@quelle_connerie]";
		final String header = "col1 col2       col3 \n---- ---------- -----";
		final String row1 = "1    1          ABCDE";
		final String row2 = "2    1234567890 A    ";
		final String rowCount = MessageFormat.format(DialogDictionary.PATTERN_ROWCOUNT.toString(), 2);
		final String limited = MessageFormat.format(DialogDictionary.PATTERN_MAX_ROWS.toString(), 2);

		template.setTemplate("{0}\n{1}\n");
		String expectedResult = whenAndWho + "\n" + header + "\n" + row1 + "\n" + row2 + "\n";
		Assert.assertEquals(expectedResult, new TextResultFormatter(info).format(template));

		template.setTemplate("{0}\n{2}\n");
		expectedResult = whenAndWho + "\n" + rowCount + "\n";
		Assert.assertEquals(expectedResult, new TextResultFormatter(info).format(template));

		template.setTemplate("{1}\n{2}\n");
		expectedResult = header + "\n" + row1 + "\n" + row2 + "\n" + rowCount + "\n";
		Assert.assertEquals(expectedResult, new TextResultFormatter(info).format(template));

		template.setTemplate("{0}\n{3}\n");
		expectedResult = whenAndWho + "\n\n";
		Assert.assertEquals(expectedResult, new TextResultFormatter(info).format(template));
		info.setLimitMaxRowsReached(true);
		expectedResult = whenAndWho + "\n" + limited + "\n";
		Assert.assertEquals(expectedResult, new TextResultFormatter(info).format(template));
	}

	@Test
	public void unknownUser() {

		final ResultTemplate template = new ResultTemplate();
		template.setTemplate("{0}");

		info.setExecutedBy(null);
		final String whenAndWho = "[24.01.1984 09:30:13 " + DialogDictionary.LABEL_UNKNOWN_USER.toString() + "]";

		Assert.assertEquals(whenAndWho, new TextResultFormatter(info).format(template));
	}

	@Test
	public void emptyResult() {

		info.setStatementResult(null);

		final ResultTemplate template = new ResultTemplate();
		template.setTemplate("{0}\n{1}\n{2}\n{3}\n");

		final String whenAndWho = "[24.01.1984 09:30:13 executor@quelle_connerie]";

		// summary wird an der Position des rowcount eingesetzt - mit
		// vorgegebenem
		// Template also zwei Leerzeilen zwischen allgemeinen Ausführungsdaten
		// und der
		// Zusammenfassung
		final String expectedResult = whenAndWho + "\n\n" + "all's well that ends well" + "\n\n";
		Assert.assertEquals(expectedResult, new TextResultFormatter(info).format(template));
	}
}
