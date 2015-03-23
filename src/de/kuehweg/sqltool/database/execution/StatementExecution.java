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
package de.kuehweg.sqltool.database.execution;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.sqlediting.StatementString;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zur Ausführung einer SQL-Anweisung mit Aufbereitung des Ergebnisses
 *
 * @author Michael Kühweg
 */
public class StatementExecution {

    private static final String NULL_STR = "[null]";

    private final StatementExecutionInformation info;

    public StatementExecution(final StatementString sql) {
        info = new StatementExecutionInformation();
        info.setSql(sql);
    }

    public StatementExecutionInformation execute(final Statement statement) throws SQLException {
        info.
                setExecutedBy(statement.getConnection().getMetaData().
                        getUserName());
        info.setConnectionDescription(statement.getConnection().getMetaData().
                getURL());
        info.setStartOfExecution(System.currentTimeMillis());
        if (statement.execute(info.getSql().uncommentedStatement())) {
            retrieveResult(statement);
        } else {
            headOnlyResult(statement.getUpdateCount());
        }
        // als Ende der Ausführung wird der Zeitpunkt betrachtet, ab dem die Ergebnismenge übertragen ist
        info.setEndOfExecution(System.currentTimeMillis());
        return info;
    }

    private void retrieveResult(final Statement statement) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.getResultSet();
            if (resultSet != null) {
                info.setStatementResult(new StatementResult());
                retrieveHeader(resultSet);
                retrieveRows(resultSet);
                info.setSummary(MessageFormat.format(
                        DialogDictionary.PATTERN_ROWCOUNT.toString(),
                        info.getStatementResult().getRows().size()));

            }
        } catch (SQLException ex) {
            erroneousResult();
        }
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (final SQLException ex) {
            erroneousResult();
        }
    }

    private void retrieveHeader(final ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int col = metaData.getColumnCount();

            final String[] header = new String[col];
            for (int i = 1; i <= col; i++) {
                header[i - 1] = metaData.getColumnLabel(i);
            }
            info.getStatementResult().setHeader(new ResultHeader(header));
        }
    }

    private void retrieveRows(final ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int col = metaData.getColumnCount();

            final List<ResultRow> rows = new ArrayList<>();
            final Object[] row = new Object[col];
            while (resultSet.next()) {
                for (int i = 1; i <= col; i++) {
                    row[i - 1] = resultSet.getObject(i);
                    if (resultSet.wasNull()) {
                        row[i - 1] = null;
                    }
                }
                info.getStatementResult().addRow(new ResultRow(row));
            }
        }
    }

    private void headOnlyResult(final int updateCount) {
        info.setStatementResult(null);
        if (info.getSql().isDataManipulationStatement()) {
            info.setSummary(MessageFormat.format(
                    DialogDictionary.PATTERN_UPDATECOUNT.toString(), updateCount));
        } else {
            info.setSummary(MessageFormat.format(
                    DialogDictionary.PATTERN_EXECUTED_STATEMENT.toString(),
                    info.getSql().firstKeyword()));
        }
    }

    private void erroneousResult() {
        info.setStatementResult(null);
        info.setSummary(
                DialogDictionary.LABEL_RESULT_ERROR.toString());
    }
}
