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

/**
 * Beschreibung der Spalten-Metadaten.
 *
 * @author Michael Kühweg
 */
public class ColumnDescription extends DatabaseObjectDescription {

    private final String type;
    private final int size;
    private final int decimalDigits;
    private final Nullability nullable;
    private final String defaultValue;
    private final String remarks;

    public ColumnDescription(final String columnName, final String type,
            final int size, final int decimalDigits,
            final Nullability nullable, final String defaultValue,
            final String remarks) {
        super(columnName);
        this.type = type == null ? "" : type;
        this.size = size;
        this.decimalDigits = decimalDigits;
        this.nullable = nullable;
        this.defaultValue = defaultValue == null ? "" : defaultValue;
        this.remarks = remarks == null ? "" : remarks;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public Nullability getNullable() {
        return nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }
}
