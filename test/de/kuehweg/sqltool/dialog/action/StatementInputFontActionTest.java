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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.common.UserPreferencesI;
import de.kuehweg.sqltool.common.UserPreferencesStub;
import de.kuehweg.sqltool.dialog.base.FontResizer;

/**
 *
 * @author Michael Kühweg
 */
public class StatementInputFontActionTest {

	private FontAction action;
	private FontResizer fontResizer;
	private UserPreferencesI userPreferences;

	public StatementInputFontActionTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		fontResizer = new FontResizerStub();
		userPreferences = new UserPreferencesStub();
		action = new StatementInputFontAction(null);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void increaseFontSize() {
		fontResizer.setFontSize(FontAction.MAX_FONT_SIZE - 1);
		action.setAlternativeFontResizer(fontResizer);
		action.setDiff(1);
		action.handleFontAction();
		Assert.assertEquals(FontAction.MAX_FONT_SIZE, fontResizer.getFontSize());
	}

	@Test
	public void maxFontSize() {
		fontResizer.setFontSize(FontAction.MAX_FONT_SIZE - 1);
		action.setAlternativeFontResizer(fontResizer);
		action.setDiff(1);
		action.handleFontAction();
		// hier dürfte nicht mehr weiter vergößert werden
		action.handleFontAction();
		Assert.assertEquals(FontAction.MAX_FONT_SIZE, fontResizer.getFontSize());
	}

	@Test
	public void decreaseFontSize() {
		fontResizer.setFontSize(FontAction.MIN_FONT_SIZE + 1);
		action.setAlternativeFontResizer(fontResizer);
		action.setDiff(-1);
		action.handleFontAction();
		Assert.assertEquals(FontAction.MIN_FONT_SIZE, fontResizer.getFontSize());
	}

	@Test
	public void minFontSize() {
		fontResizer.setFontSize(FontAction.MIN_FONT_SIZE + 1);
		action.setAlternativeFontResizer(fontResizer);
		action.setDiff(-1);
		action.handleFontAction();
		// hier dürfte nicht mehr weiter verkleinert werden
		action.handleFontAction();
		Assert.assertEquals(FontAction.MIN_FONT_SIZE, fontResizer.getFontSize());
	}

	@Test(expected = NullPointerException.class)
	public void defaultResizerWithoutTextArea() {
		action.setDiff(-1);
		// boom!
		action.handleFontAction();
	}

	@Test
	public void storeToPreferences() {
		fontResizer.setFontSize(FontAction.MAX_FONT_SIZE + 1);
		action.setAlternativeFontResizer(fontResizer);
		action.setDiff(1);
		action.setUserPreferences(userPreferences);
		action.handleFontAction();
		Assert.assertEquals(fontResizer.getFontSize(),
				userPreferences.getFontSizeStatementInput());
		action.handleFontAction();
		Assert.assertEquals(fontResizer.getFontSize(),
				userPreferences.getFontSizeStatementInput());
	}

}
