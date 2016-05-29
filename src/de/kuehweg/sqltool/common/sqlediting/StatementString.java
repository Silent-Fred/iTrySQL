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
package de.kuehweg.sqltool.common.sqlediting;

/**
 * SQL Statement in String-Repräsentation mit einigen Methoden zum einfachen
 * Umgang damit.
 *
 * @author Michael Kühweg
 */
public class StatementString {

	public static final String[] DDL_COMMANDS = new String[] { "CREATE", "ALTER", "DROP", "TRUNCATE" };

	public static final String[] DML_COMMANDS = new String[] { "SELECT", "INSERT", "UPDATE", "DELETE", "MERGE", "CALL",
			"EXPLAIN", "SCRIPT" };

	public static final String[] DCL_COMMANDS = new String[] { "GRANT", "REVOKE" };

	public static final String[] TCL_COMMANDS = new String[] { "COMMIT", "ROLLBACK", "SAVEPOINT" };

	private final String originalStatement;

	public StatementString(final String sql) {
		originalStatement = sql;
	}

	/**
	 * @return Liefert den unveränderten Text der Anweisung.
	 */
	public String originalStatement() {
		return originalStatement;
	}

	/**
	 * @return Anweisung ohne Kommentare. Kommentare werden jeweils durch ein
	 *         Leerzeichen ersetzt.
	 */
	public String uncommentedStatement() {
		String trimmedStatement = originalStatement != null ? originalStatement.trim() : "";
		int oldLength;
		do {
			oldLength = trimmedStatement.length();
			trimmedStatement = removeFirstComment(trimmedStatement);
		} while (oldLength > trimmedStatement.length());
		return trimmedStatement;
	}

	/**
	 * Abfrage auf DDL Statement. Die Anweisung wird dabei <em>nicht</em>
	 * komplett auf korrekte Syntax geprüft, sondern lediglich nach dem
	 * einleitenden Schlüsselwort klassifiziert.
	 *
	 * @return true wenn die Anweisung ein DDL Statement ist.
	 */
	public boolean isDataDefinitionStatement() {
		return isInCommandList(DDL_COMMANDS);
	}

	/**
	 * Abfrage auf DML Statement. Die Anweisung wird dabei <em>nicht</em>
	 * komplett auf korrekte Syntax geprüft, sondern lediglich nach dem
	 * einleitenden Schlüsselwort klassifiziert.
	 *
	 * @return true wenn die Anweisung ein DML Statement ist.
	 */
	public boolean isDataManipulationStatement() {
		return isInCommandList(DML_COMMANDS);
	}

	/**
	 * Abfrage auf DCL Statement. Die Anweisung wird dabei <em>nicht</em>
	 * komplett auf korrekte Syntax geprüft, sondern lediglich nach dem
	 * einleitenden Schlüsselwort klassifiziert.
	 *
	 * @return true wenn die Anweisung ein DCL Statement ist.
	 */
	public boolean isDataControlStatement() {
		return isInCommandList(DCL_COMMANDS);
	}

	/**
	 * Abfrage auf TCL Statement. Die Anweisung wird dabei <em>nicht</em>
	 * komplett auf korrekte Syntax geprüft, sondern lediglich nach dem
	 * einleitenden Schlüsselwort klassifiziert.
	 *
	 * @return true wenn die Anweisung ein TCL Statement ist.
	 */
	public boolean isTransactionControlStatement() {
		return isInCommandList(TCL_COMMANDS);
	}

	/**
	 * Extrahiert das einleitende Schlüsselwort der Anweisung.
	 *
	 * @return Einleitendes Schlüsselwort in Großbuchstaben
	 */
	public String firstKeyword() {
		final String uncommentedAndUpperCase = uncommentedStatement().toUpperCase();
		int to = 0;
		for (final char ch : uncommentedAndUpperCase.toCharArray()) {
			if (ch < 'A' || ch > 'Z') {
				break;
			}
			to++;
		}
		if (uncommentedAndUpperCase.length() > to) {
			return uncommentedStatement().toUpperCase().substring(0, to);
		}
		return uncommentedAndUpperCase;
	}

	/**
	 * @param commands
	 *            Liste von Schlüsselwörtern
	 * @return true wenn das Statement mit einem Schlüsselwort aus der
	 *         übergebenen Liste beginnt.
	 */
	private boolean isInCommandList(final String[] commands) {
		final String firstKeyword = firstKeyword();
		if (!firstKeyword.isEmpty()) {
			for (final String command : commands) {
				if (command.equals(firstKeyword)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return Liefert true, wenn die Anweisung leer ist, bzw. nur aus
	 *         Kommentaren besteht.
	 */
	public boolean isEmpty() {
		return originalStatement == null || originalStatement.trim().isEmpty()
				|| trimAndRemoveTrailingSemicolons(uncommentedStatement()).isEmpty();
	}

	/**
	 * @param statement
	 *            Text der SQL-Anweisung
	 * @return Startposition des ersten Kommentars in der Anweisung. Wenn kein
	 *         Kommentar enthalten ist, dann -1
	 */
	private int findFirstCommentStartIndex(final String statement) {
		int startIndexOfFirstComment = -1; // kein Kommentar
		final ScannerI scanner = new DefaultScanner(statement);
		scanner.startToken();
		final StatementExtractionStates state = scanForwardUntilInsideFirstComment(scanner);
		if (insideCommentState(state)) {
			scanner.pushback(2);
			final char[] token = scanner.currentToken();
			// entweder ganz am Anfang oder abhängig von der Länge des
			// vorangegangenen Abschnitts
			startIndexOfFirstComment = token == null ? 0 : token.length;
		}
		return startIndexOfFirstComment;
	}

	/**
	 * @param statement
	 *            Text der SQL-Anweisung
	 * @return Endposition des ersten Kommentars in der Anweisung. Wenn kein
	 *         Kommentar enthalten ist, dann -1. Die Endposition ist inklusive
	 *         zu betrachten, gehört also noch zum Kommentar.
	 */
	private int findFirstCommentEndIndex(final String statement) {
		int endIndexOfFirstComment = -1; // kein Kommentar
		final ScannerI scanner = new DefaultScanner(statement);
		scanner.startToken();
		StatementExtractionStates state = scanForwardUntilInsideFirstComment(scanner);
		if (insideCommentState(state)) {
			state = scanForwardToNextStartStateOrEndOfInput(scanner, state);
			if (state != StatementExtractionStates.START) {
				endIndexOfFirstComment = statement.length() - 1;
			} else {
				scanner.pushback();
				final char[] token = scanner.currentToken();
				endIndexOfFirstComment = token == null ? 0 : token.length;
			}
		}
		return endIndexOfFirstComment;
	}

	/**
	 * Setzt den übergebenen Scanner weiter, bis die lexikalische Analyse
	 * erkennt, dass sie sich innerhalb eines Kommentars befindet oder bis keine
	 * weiteren Zeichen mehr vom Scanner verarbeitet werden können.
	 *
	 * @param scanner
	 *            Vorbereiteter Scanner, der auf Anfangsposition für das neu zu
	 *            lesende Token steht.
	 * @return Gefundener Zustand, der zum Ende des Scanvorgangs geführt hat.
	 */
	private StatementExtractionStates scanForwardUntilInsideFirstComment(final ScannerI scanner) {
		StatementExtractionStates state = StatementExtractionStates.START;
		while (scanner.hasMoreElements() && !insideCommentState(state)) {
			state = state.evaluate(scanner);
		}
		return state;
	}

	/**
	 * Setzt den übergebenen Scanner weiter, bis die lexikalische Analyse
	 * erkennt, dass sie einen neuen START-Zustand erreicht hat oder bis keine
	 * weiteren Zeichen mehr vom Scanner verarbeitet werden können.
	 *
	 * @param scanner
	 *            Vorbereiteter Scanner, der an der Position steht, an der ein
	 *            Kommentar erkannt wurde.
	 * @param startState
	 *            Zustand, der beim Erreichen des Kommentars eingenommen wurde.
	 *            (zur Unterscheidung, um welche Art von Kommentar es sich
	 *            handelt)
	 * @return Gefundener Zustand, der zum Ende des Scanvorgangs geführt hat.
	 */
	private StatementExtractionStates scanForwardToNextStartStateOrEndOfInput(final ScannerI scanner,
			final StatementExtractionStates startState) {
		StatementExtractionStates state = startState;
		while (scanner.hasMoreElements() && state != StatementExtractionStates.START) {
			state = state.evaluate(scanner);
		}
		return state;
	}

	private boolean insideCommentState(final StatementExtractionStates state) {
		return state == StatementExtractionStates.INSIDE_BLOCK_COMMENT
				|| state == StatementExtractionStates.INSIDE_LINE_COMMENT;
	}

	/**
	 * Entfernt den ersten Kommentar aus der übergebenen Anweisung. Kommentare
	 * am Anfang oder Ende der Anweisung werden komplett entfernt, Kommentare
	 * innerhalb der Anweisung werden durch ein Leerzeichen ersetzt. (da sie
	 * syntaktisch als Trennelement fungieren)
	 *
	 * @param statement
	 *            Text der SQL-Anweisung.
	 * @return Anweisung ohne den ersten Kommentar.
	 */
	private String removeFirstComment(final String statement) {
		final StringBuilder builder = new StringBuilder();
		final int firstCommentStartIndex = findFirstCommentStartIndex(statement);
		if (firstCommentStartIndex < 0) {
			builder.append(statement);
		} else {
			builder.append(statement.substring(0, firstCommentStartIndex).trim());
			final int firstCommentEndIndex = findFirstCommentEndIndex(statement);
			if (firstCommentEndIndex < statement.length() - 1) {
				// Kommentar ist ein Trennelement - wird ersetzt durch einen
				// anderen Trenner, damit nicht evtl. die Syntax zerstört wird
				builder.append(" ");
				builder.append(statement.substring(firstCommentEndIndex + 1).trim());
			}
		}
		return builder.toString().trim();
	}

	private String trimAndRemoveTrailingSemicolons(final String statement) {
		String trimmed = statement.trim();
		while (trimmed.endsWith(";")) {
			trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
		}
		return trimmed;
	}
}
