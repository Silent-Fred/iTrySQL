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

/**
 * Standardvorlage für Textausgabe
 *
 * @author Michael Kühweg
 */
public class DefaultTextResultTemplate extends ResultTemplate {

    private static final String DEFAULT_PLACEHOLDER_EXECUTION = "F";

    private static final String DEFAULT_PLACEHOLDER_RESULT_TABLE = "T";

    private static final String DEFAULT_PLACEHOLDER_ROW_COUNT = "W";

    private static final String DEFAULT_PLACEHOLDER_LIMIT_ROWS = "!";

    public DefaultTextResultTemplate() {
        super();
        setPlaceholderExecution(DEFAULT_PLACEHOLDER_EXECUTION);
        setPlaceholderResultTable(DEFAULT_PLACEHOLDER_RESULT_TABLE);
        setPlaceholderRowCount(DEFAULT_PLACEHOLDER_ROW_COUNT);
        setPlaceholderLimitRows(DEFAULT_PLACEHOLDER_LIMIT_ROWS);
        setTemplate(DEFAULT_PLACEHOLDER_EXECUTION + "\n\n"
                + DEFAULT_PLACEHOLDER_RESULT_TABLE + "\n\n" + DEFAULT_PLACEHOLDER_ROW_COUNT
                + "\n" + DEFAULT_PLACEHOLDER_LIMIT_ROWS + "\n");
    }
}
