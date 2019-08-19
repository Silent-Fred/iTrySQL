/*
 * Copyright (c) 2016, Michael Kühweg
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Michael Kühweg
 */
public class TextBasedFindActionTest {

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.dialog.action.TextBasedFindAction#find(java.lang.String)}
	 * .
	 */
	@Test
	public void testFind() {
		final TextBasedFindAction findAction = new TextBasedFindAction();
		findAction.find(null);
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
		findAction.find("");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
		findAction.find("A");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());

		findAction.refreshTextContent("");
		findAction.find(null);
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
		findAction.find("");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
		findAction.find("A");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());

		findAction.refreshTextContent("a");
		findAction.find(null);
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
		findAction.find("");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
		findAction.find("A");
		assertEquals(0, findAction.getPositionOfLastRememberedFinding());

		findAction.refreshTextContent("b");
		findAction.find("A");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.dialog.action.TextBasedFindAction#nextOccurrence(java.lang.String)}
	 * .
	 */
	@Test
	public void testNextOccurrence() {
		final TextBasedFindAction findAction = new TextBasedFindAction();
		findAction.refreshTextContent("abcba");
		findAction.nextOccurrence("A");
		assertEquals(0, findAction.getPositionOfLastRememberedFinding());

		findAction.resetSearchPosition();
		findAction.nextOccurrence("A");
		assertEquals(0, findAction.getPositionOfLastRememberedFinding());
		findAction.nextOccurrence("A");
		assertEquals(4, findAction.getPositionOfLastRememberedFinding());
		findAction.nextOccurrence("A");
		assertEquals(4, findAction.getPositionOfLastRememberedFinding());
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.dialog.action.TextBasedFindAction#previousOccurrence(java.lang.String)}
	 * .
	 */
	@Test
	public void testPreviousOccurrence() {
		final TextBasedFindAction findAction = new TextBasedFindAction();
		findAction.refreshTextContent("abcba");
		findAction.previousOccurrence("B");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());

		findAction.resetSearchPosition();
		findAction.nextOccurrence("B");
		findAction.nextOccurrence("B");
		assertEquals(3, findAction.getPositionOfLastRememberedFinding());

		findAction.previousOccurrence("B");
		assertEquals(1, findAction.getPositionOfLastRememberedFinding());

		findAction.previousOccurrence("");
		assertEquals(1, findAction.getPositionOfLastRememberedFinding());
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.dialog.action.TextBasedFindAction#refreshTextContent(java.lang.String)}
	 * .
	 */
	@Test
	public void testRefreshTextContent() {
		final TextBasedFindAction findAction = new TextBasedFindAction();
		findAction.refreshTextContent("a");
		findAction.find("A");
		assertEquals(0, findAction.getPositionOfLastRememberedFinding());
		findAction.refreshTextContent("b");
		findAction.find("A");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.dialog.action.TextBasedFindAction#getPositionOfLastRememberedFinding()}
	 * .
	 */
	@Test
	public void testGetPositionOfLastRememberedFinding() {
		final TextBasedFindAction findAction = new TextBasedFindAction();
		findAction.refreshTextContent("abcba");
		findAction.nextOccurrence("B");
		findAction.nextOccurrence("BB");
		assertEquals(1, findAction.getPositionOfLastRememberedFinding());
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.dialog.action.TextBasedFindAction#resetSearchPosition()}
	 * .
	 */
	@Test
	public void testResetSearchPosition() {
		final TextBasedFindAction findAction = new TextBasedFindAction();
		findAction.refreshTextContent("a");
		findAction.find("A");
		assertEquals(0, findAction.getPositionOfLastRememberedFinding());
		findAction.resetSearchPosition();
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
	}

	/**
	 * Testet das Verhalten, wenn zwischen zwei Aufrufen der Suchmethoden der
	 * Inhalt des zu durchsuchenden Texts derart verändert wurde, dass die
	 * letzte "Fundstelle" außerhalb des noch vorhandenen Textinhalts liegt.
	 */
	@Test
	public void testTextCutOffBetweenCalls() {
		final TextBasedFindAction findAction = new TextBasedFindAction();
		findAction.refreshTextContent("Alles hat ein Ende nur die Wurst hat zwei");
		findAction.find("ZwEi");
		assertEquals(37, findAction.getPositionOfLastRememberedFinding());
		findAction.refreshTextContent("Alles hat ein Ende");
		findAction.nextOccurrence("ZwEi");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());

		findAction.refreshTextContent("Alles hat ein Ende nur die Wurst hat zwei");
		findAction.find("ZwEi");
		assertEquals(37, findAction.getPositionOfLastRememberedFinding());
		findAction.refreshTextContent("Alles hat ein Ende");
		findAction.previousOccurrence("ZwEi");
		assertEquals(-1, findAction.getPositionOfLastRememberedFinding());
	}
}
