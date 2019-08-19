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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Test für die Ausgabeaufbereitung.
 *
 * @author Michael Kühweg
 */
public class ResultTemplateTest {

	private static final String BASE_TEMPLATE_TEXT = "Test: {0} {1} {2} {3}.";

	private ResultTemplate template;

	@Before
	public void setUp() {
		template = new ResultTemplate(BASE_TEMPLATE_TEXT);
	}

	@Test
	public void emptyTemplate() {
		final ResultTemplate emptyTemplate = new ResultTemplate();
		assertEquals("", emptyTemplate.buildWithTemplate());
	}

	@Test
	public void nothingToFillIn() {
		assertEquals("Test:    .", template.buildWithTemplate());
	}

	@Test
	public void emptyResult() {
		template.setExecutionInformation("happily executed");
		template.setRowCount("happy rows counted");
		assertEquals("Test: happily executed  happy rows counted .", template.buildWithTemplate());
	}

	@Test
	public void withResult() {
		template.setExecutionInformation("happily executed");
		template.setResultTable("happy results");
		template.setRowCount("happy rows counted");
		assertEquals("Test: happily executed happy results happy rows counted .", template.buildWithTemplate());
	}

	@Test
	public void placeholderInReplacedText() {
		template.setExecutionInformation("{3}");
		template.setResultTable("{0}");
		template.setRowCount("{2}");
		template.setLimitedRows("{1}");
		assertEquals("Test: {3} {0} {2} {1}.", template.buildWithTemplate());
	}

	@Test
	public void withResultAndLimit() {
		template.setExecutionInformation("happily executed");
		template.setResultTable("happy results");
		template.setRowCount("happy rows counted");
		template.setLimitedRows("happy limitations");
		assertEquals("Test: happily executed happy results happy rows counted happy limitations.",
				template.buildWithTemplate());
	}

	@Test
	public void damagedTemplate() {

		template.setTemplate("Test: {0} {1} {2 {3}.");

		template.setExecutionInformation("happily executed");
		template.setResultTable("happy results");
		template.setRowCount("happy rows counted");
		template.setLimitedRows("happy limitations");

		assertEquals(template.buildWithFallbackTemplate(), template.buildWithTemplate());
	}
}
