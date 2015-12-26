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

	private final String sql;

	public StatementString(final String sql) {
		super();
		this.sql = sql;
	}

	/**
	 * Unveränderter Text der Anweisung.
	 *
	 * @return
	 */
	public String originalStatement() {
		return sql;
	}

	/**
	 * Liefert die Anweisung ohne Kommentare zurück. Kommentare werden jeweils
	 * durch ein Leerzeichen ersetzt.
	 *
	 * @return
	 */
	public String uncommentedStatement() {
		if (sql == null || sql.trim().length() == 0) {
			return "";
		}
		String trimmedStatement = sql.trim();
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

	public boolean isEmpty() {
		return sql == null || sql.trim().isEmpty() || trimAndRemoveTrailingSemicolons(uncommentedStatement()).isEmpty();
	}

	private int findFirstCommentStartIndex(final String statement) {
		if (statement == null || statement.trim().length() == 0) {
			return -1;
		}
		final ScannerI scanner = new DefaultScanner(statement);
		scanner.startToken();
		StatementExtractionStates state = StatementExtractionStates.START;
		while (scanner.hasMoreElements()) {
			state = state.evaluate(scanner);
			switch (state) {
			case INSIDE_BLOCK_COMMENT:
			case INSIDE_LINE_COMMENT:
				scanner.pushback(2);
				final char[] token = scanner.currentToken();
				return token == null ? 0 : token.length - 1;
			default:
				break;
			}
		}
		return -1;
	}

	private int findFirstCommentEndIndex(final String statement) {
		if (statement == null || statement.trim().length() == 0) {
			return -1;
		}
		final ScannerI scanner = new DefaultScanner(statement);
		scanner.startToken();
		StatementExtractionStates state = StatementExtractionStates.START;
		boolean inside = false;
		while (scanner.hasMoreElements()) {
			state = state.evaluate(scanner);
			switch (state) {
			case INSIDE_BLOCK_COMMENT:
			case INSIDE_LINE_COMMENT:
				inside = true;
				break;
			case START:
				if (inside) {
					scanner.pushback();
					final char[] token = scanner.currentToken();
					return token == null ? 0 : token.length;
				}
				break;
			default:
				break;
			}
		}
		return inside ? statement.length() - 1 : -1;
	}

	private String removeFirstComment(final String statement) {
		if (statement == null || statement.trim().length() == 0) {
			return "";
		}
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
