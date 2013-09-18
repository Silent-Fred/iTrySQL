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
package de.kuehweg.sqltool.database;

import de.kuehweg.sqltool.common.DialogDictionary;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Michael Kühweg
 */
public class ResultFormatter {

    private static final String NULL_STR = "[null]";
    private Object[] header;
    private List<Object[]> rows;
    private boolean initialised;
    private boolean headOnly;
    private String executedBy;
    private Date executedAt;

    public ResultFormatter() {
    }

    private void setHeader(final Object[] headRow) {
        header = Arrays.copyOf(headRow, headRow.length);
    }

    private void addRow(final Object[] row) {
        rows.add(Arrays.copyOf(row, row.length));
    }

    public void fillFromStatementResult(final Statement statement) throws SQLException {
        initialised = true;
        setHeader(new String[]{DialogDictionary.LABEL_RESULT_EXECUTED.
            toString()});
        rows = new LinkedList<>();
        executedAt = new Date();
        if (statement == null) {
            return;
        }
        ResultSet resultSet = null;
        try {
            executedBy = statement.getConnection().getMetaData().getUserName()
                    + "@" + statement.getConnection().getMetaData().getURL();
            resultSet = statement.getResultSet();
            if (resultSet == null) {
                headOnly = true;
            } else {
                headOnly = false;
                final ResultSetMetaData metaData = resultSet.getMetaData();
                final int col = metaData.getColumnCount();
                final Object[] headRow = new Object[col];
                for (int i = 1; i <= col; i++) {
                    headRow[i - 1] = metaData.getColumnLabel(i);
                }
                setHeader(headRow);
                final Object[] row = new Object[col];
                while (resultSet.next()) {
                    for (int i = 1; i <= col; i++) {
                        row[i - 1] = resultSet.getObject(i);
                        if (resultSet.wasNull()) {
                            row[i - 1] = NULL_STR;
                        }
                    }
                    addRow(row);
                }
            }
        } catch (SQLException ex) {
            setHeader(new String[]{DialogDictionary.LABEL_RESULT_ERROR.
                toString()});
            rows.clear();
        }
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            setHeader(new String[]{DialogDictionary.LABEL_RESULT_ERROR.
                toString()});
            addRow(new String[]{DialogDictionary.LABEL_EMPTY.toString()});
        }
    }

    public boolean isHeadOnly() {
        return headOnly;
    }

    public Date getExecutedAt() {
        return executedAt != null ? executedAt : new Date();
    }

    public String getExecutedBy() {
        return executedBy != null ? executedBy : "unknown";
    }

    public String[] getHeader() {
        if (!initialised) {
            setHeader(new String[]{DialogDictionary.LABEL_RESULT_ERROR.
                toString()});
            addRow(new String[]{DialogDictionary.LABEL_EMPTY.toString()});
        }
        String[] result = new String[header.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = header[i].toString();
        }
        return result;
    }

    public List<List<String>> getRows() {
        if (!initialised) {
            setHeader(new String[]{DialogDictionary.LABEL_RESULT_ERROR.
                toString()});
            addRow(new String[]{DialogDictionary.LABEL_EMPTY.toString()});
        }
        List<List<String>> result = new ArrayList<>(rows.size());
        if (!headOnly) {
            for (final Object[] row : rows) {
                List<String> oneRow = new ArrayList<>(header.length);
                for (Object col : row) {
                    oneRow.add(col != null ? col.toString() : "");
                }
                result.add(oneRow);
            }
        }
        return result;
    }
}
