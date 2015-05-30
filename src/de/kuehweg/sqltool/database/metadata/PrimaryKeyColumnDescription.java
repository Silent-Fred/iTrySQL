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
package de.kuehweg.sqltool.database.metadata;

import java.util.Objects;

/**
 * Beschreibung der Primärschlüssel-Metadaten
 *
 * @author Michael Kühweg
 */
public class PrimaryKeyColumnDescription {

    private final String catalog;
    private final String schema;
    private final String tableName;
    private final String primaryKeyName;
    private final String columnName;

    public PrimaryKeyColumnDescription(String catalog, String schema, String tableName,
            String primaryKeyName, String columnName) {
        this.catalog = catalog == null ? "" : catalog;
        this.schema = schema == null ? "" : schema;
        this.tableName = tableName == null ? "" : tableName;
        this.primaryKeyName = primaryKeyName == null ? "" : primaryKeyName;
        this.columnName = columnName == null ? "" : columnName;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.catalog);
        hash = 97 * hash + Objects.hashCode(this.schema);
        hash = 97 * hash + Objects.hashCode(this.tableName);
        hash = 97 * hash + Objects.hashCode(this.primaryKeyName);
        hash = 97 * hash + Objects.hashCode(this.columnName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrimaryKeyColumnDescription other = (PrimaryKeyColumnDescription) obj;
        if (!Objects.equals(this.catalog, other.catalog)) {
            return false;
        }
        if (!Objects.equals(this.schema, other.schema)) {
            return false;
        }
        if (!Objects.equals(this.tableName, other.tableName)) {
            return false;
        }
        if (!Objects.equals(this.primaryKeyName, other.primaryKeyName)) {
            return false;
        }
        return Objects.equals(this.columnName, other.columnName);
    }

}
