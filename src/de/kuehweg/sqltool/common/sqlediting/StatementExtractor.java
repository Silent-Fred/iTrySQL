/*
 * Copyright (c) 2013-2015, Michael Kühweg
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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Bei längeren Eingaben im Skriptbereich soll jeweils die Anweisung, in der
 * sich der Cursor befindet, ausgeführt werden.
 *
 * @author Michael Kühweg
 */
public class StatementExtractor {

	/**
	 * Ermittelt die SQL-Anweisung, in der sich die Eingabemarkierung aktuell
	 * befindet.
	 *
	 * @param script
	 *            Eingabetext
	 * @param caretPosition
	 *            Position der Eingabemarke innerhalb des Eingabetexts
	 * @return Die SQL Anweisung innerhalb derer sich die Eingabemarkierung
	 *         befindet (inkl. Randbetrachtung, wenn die Eingabemarke z.B.
	 *         hinter der letzte Anweisung steht)
	 */
	public String extractStatementAtCaretPosition(final String script, final int caretPosition) {
		final Iterator<String> statementIterator = splitIntoRawStatements(script).iterator();
		int pos = 0;
		String lastNotEmptyStatement = "";
		while (pos <= caretPosition && statementIterator.hasNext()) {
			final String statement = statementIterator.next();
			if (statement.trim().length() > 0) {
				lastNotEmptyStatement = statement.trim();
			}
			pos += statement.length();
		}
		return lastNotEmptyStatement;
	}

	/**
	 * Extrahiert alle SQL-Anweisungen aus einem Skript.
	 *
	 * @param script
	 *            SQL-Skript
	 * @return Liste der enthaltenen SQL-Anweisungen
	 */
	public List<StatementString> getStatementsFromScript(final String script) {
		final List<StatementString> statements = new LinkedList<>();
		for (final String statement : splitIntoRawStatements(script)) {
			final String trimmed = statement.trim();
			if (trimmed.length() > 0) {
				statements.add(new StatementString(statement));
			}
		}
		return statements;
	}

	private String statementStartingAtCurrentPosition(final ScannerI scanner) {
		StatementExtractionStates state = StatementExtractionStates.START;
		scanner.startToken();
		do {
			state = state.evaluate(scanner);
		} while (state != StatementExtractionStates.STATEMENT_TERMINATION_FOUND);
		return new String(scanner.currentToken());
	}

	private List<String> splitIntoRawStatements(final String script) {
		if (script == null || script.trim().length() == 0) {
			return Collections.emptyList();
		}
		final List<String> statements = new LinkedList<>();
		final ScannerI scanner = new DefaultScanner(script);
		while (scanner.hasMoreElements()) {
			final String statement = statementStartingAtCurrentPosition(scanner);
			if (statement.length() > 0) {
				statements.add(statement);
			}
		}
		return statements;
	}

}
