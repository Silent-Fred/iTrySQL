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
     * @param caretPosition
     * @return
     */
    public String extractStatementAtCaretPosition(final String script,
            final int caretPosition) {
        final Iterator<String> statementIterator = splitIntoRawStatements(
                script).iterator();
        int pos = 0;
        String statement = "";
        while (pos <= caretPosition && statementIterator.hasNext()) {
            statement = statementIterator.next();
            pos += statement.length();
        }
        return statement.trim();
    }

    /**
     * Extrahiert alle SQL-Anweisungen aus einem Skript.
     *
     * @param script SQL-Skript
     * @return Liste der enthaltenen SQL-Anweisungen
     */
    public List<StatementString> getStatementsFromScript(final String script) {
        final List<StatementString> statements = new LinkedList<>();
        for (String statement : splitIntoRawStatements(script)) {
            String trimmed = statement.trim();
            if (trimmed.length() > 0) {
                statements.add(new StatementString(statement));
            }
        }
        return statements;
    }

    private List<String> splitIntoRawStatements(final String script) {
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
            final String statement = new String(input, lastPos, pos - lastPos);
            lastPos = pos;
            if (statement.length() > 0) {
                statements.add(statement);
            }
            state = StatementExtractionStates.START;
        }
        return statements;
    }

}
