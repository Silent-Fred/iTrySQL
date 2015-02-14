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

    private static final String[] DDL_COMMANDS = new String[]{"CREATE",
        "ALTER", "DROP"};

    private final String sql;

    public StatementString(final String sql) {
        super();
        this.sql = sql;
    }

    public String originalStatement() {
        return sql;
    }

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

    public boolean isDataDefinitionStatement() {
        final String uncommentedStatement = uncommentedStatement();
        for (final String ddl : DDL_COMMANDS) {
            if (uncommentedStatement.toUpperCase().startsWith(ddl)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return sql == null || sql.trim().length() == 0
                || uncommentedStatement().trim().length() == 0;
    }

    private int findFirstCommentStartIndex(final String statement) {
        if (statement == null || statement.trim().length() == 0) {
            return -1;
        }
        final char[] input = statement.toCharArray();
        StatementExtractionStates state = StatementExtractionStates.START;
        int pos = 0;
        while (pos < input.length) {
            state = state.evaluate(input[pos++]);
            switch (state) {
                case INSIDE_BLOCK_COMMENT:
                case INSIDE_LINE_COMMENT:
                    return pos - 2;
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
        final char[] input = statement.toCharArray();
        StatementExtractionStates state = StatementExtractionStates.START;
        int pos = 0;
        boolean inside = false;
        while (pos < input.length) {
            state = state.evaluate(input[pos++]);
            switch (state) {
                case INSIDE_BLOCK_COMMENT:
                case INSIDE_LINE_COMMENT:
                    inside = true;
                    break;
                case START:
                    if (inside) {
                        return pos - 1;
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
            builder.append(statement.substring(0, firstCommentStartIndex)
                    .trim());
            final int firstCommentEndIndex = findFirstCommentEndIndex(statement);
            if (firstCommentEndIndex < statement.length() - 1) {
                // Kommentar ist ein Trennelement - wird ersetzt durch einen
                // anderen Trenner, damit nicht evtl. die Syntax zerstört wird
                builder.append(" ");
                builder.append(statement.substring(firstCommentEndIndex + 1)
                        .trim());
            }
        }
        return builder.toString().trim();
    }
}
