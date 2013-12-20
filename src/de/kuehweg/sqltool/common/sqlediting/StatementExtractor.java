/*
 * Copyright (c) 2013, Michael Kühweg
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
import java.util.LinkedList;
import java.util.List;

/**
 * Bei längeren Eingaben im Skriptbereich soll jeweils die Anweisung, in der
 * sich der Cursor befindet, ausgeführt werden.
 * 
 * @author Michael Kühweg
 */
public class StatementExtractor {

	private StatementExtractor() {
		// util class has no instances
	}

	/**
	 * Ermittelt die SQL-Anweisung, in der sich die Eingabemarkierung aktuell
	 * befindet.
	 * 
	 * @param script
	 * @param caretPosition
	 * @return
	 */
	public static String extractStatementAtCaretPosition(final String script,
			final int caretPosition) {
		if (script == null || script.trim().length() == 0) {
			return "";
		}
		final char[] input = script.toCharArray();
		// von vorne einzelne Anweisungen suchen und jeweils Anfang und Ende der
		// Anweisung merken
		StatementExtractionStates state = StatementExtractionStates.START;
		int lastPos = 0;
		int pos = 0;
		// Sonderfall ist immer, dass man sich in der letzten Anweisung befindet
		// und diese gar nicht mit Semikolon abgeschlossen ist
		String maybeFinalStatement = "";
		// kompletten Text durchlaufen wenn erforderlich
		while (pos < input.length) {
			// Zeichen auswerten, bis eine fertige Anweisung erkannt wurde oder
			// das Ende der Eingabe erreicht ist
			while (state != StatementExtractionStates.FINAL
					&& pos < input.length) {
				state = state.evaluate(input[pos++]);
			}
			// die Anweisung ist ein möglicher Kandidat, wenn überhaupt was
			// gelesen wurde (außer whitespace)
			final String statement = new String(input, lastPos, pos - lastPos)
					.trim();
			if (statement.length() > 0) {
				maybeFinalStatement = statement;
			}
			// befindet sich die Eingabemarkierung innerhalb der gerade
			// erkannten Anweisung, dann ist die richtige Anweisung gefunden und
			// kann als Ergebnis zurückgegeben werden
			if (lastPos <= caretPosition && pos >= caretPosition) {
				return maybeFinalStatement;
			}
			// ansonsten weiter versuchen mit einer neuen Anweisung
			lastPos = pos;
			state = StatementExtractionStates.START;
		}
		// falls das Ende ohne Semikolon erreicht wurde (oder gar nichts erkannt
		// wurde), dann landen wir hier und geben eben die nicht abgeschlossene
		// Anweisung zurück - ist ja kein Beinbruch
		return maybeFinalStatement;
	}

	/**
	 * Extrahiert alle SQL-Anweisungen aus einem Skript.
	 * 
	 * @param script
	 *            SQL-Skript
	 * @return Liste der enthaltenen SQL-Anweisungen
	 */
	public static List<String> getStatementsFromScript(final String script) {
		if (script == null || script.trim().length() == 0) {
			return Collections.emptyList();
		}
		final char[] input = script.toCharArray();
		final List<String> statements = new LinkedList<>();
		StatementExtractionStates state = StatementExtractionStates.START;
		int lastPos = 0;
		int pos = 0;
		while (pos < input.length) {
			while (state != StatementExtractionStates.FINAL
					&& pos < input.length) {
				state = state.evaluate(input[pos++]);
			}
			final String statement = new String(input, lastPos, pos - lastPos)
					.trim();
			lastPos = pos;
			if (statement.length() > 0) {
				statements.add(statement);
			}
			state = StatementExtractionStates.START;
		}
		return statements;
	}
}
