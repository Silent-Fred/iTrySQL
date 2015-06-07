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

import java.sql.SQLException;

/**
 * ResultSet zum Test der Metadatenaufbereitung
 *
 * @author Michael Kühweg
 */
public class ResultSetStubForMetaDataReader extends FakeResultSet {

    public static final String PREFIX_GET = "CALLED-GET-";

    private static final String[] COLUMN_LABELS_RETURNING_INT
            = new String[]{"COLUMN_SIZE", "DECIMAL_DIGITS", "ORDINAL_POSITION"};

    private int count;

    private String columnLabelToAppendAutoIncrease;
    private int autoIncrease = 1;

    public ResultSetStubForMetaDataReader(int count) {
        this(count, null);
    }

    public ResultSetStubForMetaDataReader(int count,
            String columnLabelToAppendAutoIncrease) {
        this.count = count;
        this.columnLabelToAppendAutoIncrease = columnLabelToAppendAutoIncrease;
    }

    @Override
    public boolean next() throws SQLException {
        return --count >= 0;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return PREFIX_GET + String.class.getSimpleName() + columnLabel
                + (columnLabelToAppendAutoIncrease != null
                && columnLabelToAppendAutoIncrease.equals(columnLabel) ? autoIncrease++ : "");
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        for (int i = 0; i < COLUMN_LABELS_RETURNING_INT.length; i++) {
            if (COLUMN_LABELS_RETURNING_INT[i].equals(columnLabel)) {
                return i;
            }
        }
        return 0xC01;
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return true; // Hauptsache nicht Defaultwert für Boolean ;-)
    }

}
