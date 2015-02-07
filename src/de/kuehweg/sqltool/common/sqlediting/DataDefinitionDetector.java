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
 * Klasse dient dazu, DDL-Statements zu erkennen (z.B. für abhängigen Refresh
 * der Schemaansicht)
 *
 * @author Michael Kühweg
 */
public class DataDefinitionDetector {

    private static final String[] DDL_COMMANDS = new String[]{"CREATE",
        "ALTER", "DROP"};

    private final StatementExtractor extractor = new StatementExtractor();

    public boolean containsDataDefinition(final String script) {
        if (script == null || script.trim().length() == 0) {
            return false;
        }
        boolean containsDataDefinition = false;
        for (final String statement : extractor
                .getStatementsFromScript(script)) {
            containsDataDefinition = containsDataDefinition
                    || isDataDefinition(statement);
        }
        return containsDataDefinition;
    }

    private boolean isDataDefinition(final String statement) {
        final String uncommentedStatement = extractor.
                removeComments(
                        statement).toUpperCase();
        for (final String ddl : DDL_COMMANDS) {
            if (uncommentedStatement.startsWith(ddl)) {
                return true;
            }
        }
        return false;
    }

}
