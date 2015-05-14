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
package de.kuehweg.sqltool.database.execution.fake;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Michael Kühweg
 */
public class ResultSetMetaDataStubFromObjectArray extends FakeResultSetMetaData {

    private final Object[][] resultSet;

    public ResultSetMetaDataStubFromObjectArray(final Object[][] fakeResultSetContent) {
        super();
        this.resultSet = fakeResultSetContent;
    }

    @Override
    public int getColumnCount() throws SQLException {
        if (resultSet == null || resultSet.length == 0) {
            throw new SQLException();
        }
        return resultSet[0].length;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        // nullable, wenn im Fake-Result in einer der Zeilen in der Spalte ein NULL-Wert enthalten ist
        if (resultSet == null || column < 0 || column >= resultSet[0].length) {
            throw new SQLException();
        }
        for (Object[] row : resultSet) {
            if (row[column] == null) {
                return ResultSetMetaData.columnNullable;
            }
        }
        return ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        if (resultSet == null || resultSet.length == 0 || column < 1 || column
                > resultSet[0].length) {
            throw new SQLException();
        }
        return resultSet[0][column - 1].toString();
    }

}
