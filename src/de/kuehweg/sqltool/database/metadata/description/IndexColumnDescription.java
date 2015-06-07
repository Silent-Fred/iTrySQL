/*
 * Copyright (c) 2013-2015, Michael Kühweg
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
package de.kuehweg.sqltool.database.metadata.description;

import java.util.Objects;

/**
 * Beschreibung der Index-Metadaten.
 *
 * @author Michael Kühweg
 */
public class IndexColumnDescription extends DatabaseObjectDescription {

    private final String columnName;
    private final int ordinalPosition;
    private final boolean nonUnique;

    public IndexColumnDescription(final String indexName, final String columnName,
            final int ordinalPosition, final boolean nonUnique) {
        super(indexName);
        this.columnName = columnName == null ? "" : columnName;
        this.ordinalPosition = ordinalPosition;
        this.nonUnique = nonUnique;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public boolean isNonUnique() {
        return nonUnique;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 79 * hash + Objects.hashCode(this.columnName);
        hash = 79 * hash + this.ordinalPosition;
        hash = 79 * hash + (this.nonUnique ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (super.equals(obj)) {
            final IndexColumnDescription other = (IndexColumnDescription) obj;
            return Objects.equals(columnName, other.columnName);
        }
        return false;
    }

}
