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
public abstract class FakeResultSetMetaData implements ResultSetMetaData {

    @Override
    public int getColumnCount() throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int isNullable(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getScale(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getTableName(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        throw new SQLException();
    }

    @Override
    public <T> T
            unwrap(Class< T> iface) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isWrapperFor(
            Class<?> iface) throws SQLException {
        throw new SQLException();
    }

}
