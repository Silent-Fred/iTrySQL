/*
 * Copyright (c) 2014-2015, Michael Kühweg
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
 * @author Michael Kühweg
 */
public class ForeignKeyColumnDescription implements
        Comparable<ForeignKeyColumnDescription> {

    private final String fkCatalog;
    private final String fkSchema;
    private final String fkTableName;
    private final String fkColumnName;
    private final String foreignKeyName;
    private final String pkCatalog;
    private final String pkSchema;
    private final String pkTableName;
    private final String pkColumnName;

    public ForeignKeyColumnDescription(String fkCatalog, String fkSchema,
            String fkTableName,
            String fkColumnName, String foreignKeyName,
            String pkCatalog, String pkSchema, String pkTableName, String pkColumnName) {
        this.fkCatalog = fkCatalog == null ? "" : fkCatalog;
        this.fkSchema = fkSchema == null ? "" : fkSchema;
        this.fkTableName = fkTableName == null ? "" : fkTableName;
        this.fkColumnName = fkColumnName == null ? "" : fkColumnName;
        this.foreignKeyName = foreignKeyName == null ? "" : foreignKeyName;
        this.pkCatalog = pkCatalog == null ? "" : pkCatalog;
        this.pkSchema = pkSchema == null ? "" : pkSchema;
        this.pkTableName = pkTableName == null ? "" : pkTableName;
        this.pkColumnName = pkColumnName == null ? "" : pkColumnName;
    }

    public String getFkCatalog() {
        return fkCatalog;
    }

    public String getFkSchema() {
        return fkSchema;
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public String getFkColumnName() {
        return fkColumnName;
    }

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public String getPkCatalog() {
        return pkCatalog;
    }

    public String getPkSchema() {
        return pkSchema;
    }

    public String getPkTableName() {
        return pkTableName;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    private boolean isDifferentCatalog() {
        return !fkCatalog.equals(pkCatalog);
    }

    private boolean isDifferentSchema() {
        return !fkSchema.equals(pkSchema);
    }

    public boolean isOutside() {
        return isDifferentCatalog() || isDifferentSchema();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.fkCatalog);
        hash = 89 * hash + Objects.hashCode(this.fkSchema);
        hash = 89 * hash + Objects.hashCode(this.fkTableName);
        hash = 89 * hash + Objects.hashCode(this.fkColumnName);
        hash = 89 * hash + Objects.hashCode(this.foreignKeyName);
        hash = 89 * hash + Objects.hashCode(this.pkCatalog);
        hash = 89 * hash + Objects.hashCode(this.pkSchema);
        hash = 89 * hash + Objects.hashCode(this.pkTableName);
        hash = 89 * hash + Objects.hashCode(this.pkColumnName);
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
        final ForeignKeyColumnDescription other = (ForeignKeyColumnDescription) obj;
        if (!Objects.equals(this.fkCatalog, other.fkCatalog)) {
            return false;
        }
        if (!Objects.equals(this.fkSchema, other.fkSchema)) {
            return false;
        }
        if (!Objects.equals(this.fkTableName, other.fkTableName)) {
            return false;
        }
        if (!Objects.equals(this.fkColumnName, other.fkColumnName)) {
            return false;
        }
        if (!Objects.equals(this.foreignKeyName, other.foreignKeyName)) {
            return false;
        }
        if (!Objects.equals(this.pkCatalog, other.pkCatalog)) {
            return false;
        }
        if (!Objects.equals(this.pkSchema, other.pkSchema)) {
            return false;
        }
        if (!Objects.equals(this.pkTableName, other.pkTableName)) {
            return false;
        }
        return Objects.equals(this.pkColumnName, other.pkColumnName);
    }

    @Override
    public int compareTo(final ForeignKeyColumnDescription other) {
        int result = fkCatalog.compareTo(other.fkCatalog);
        if (result == 0) {
            result = fkSchema.compareTo(other.fkSchema);
        }
        if (result == 0) {
            result = fkTableName.compareTo(other.fkTableName);
        }
        if (result == 0) {
            result = fkColumnName.compareTo(other.fkColumnName);
        }
        if (result == 0) {
            result = foreignKeyName.compareTo(other.foreignKeyName);
        }
        if (result == 0) {
            result = pkCatalog.compareTo(other.pkCatalog);
        }
        if (result == 0) {
            result = pkSchema.compareTo(other.pkSchema);
        }
        if (result == 0) {
            result = pkTableName.compareTo(other.pkTableName);
        }
        if (result == 0) {
            result = pkColumnName.compareTo(other.pkColumnName);
        }
        return result;
    }
}
