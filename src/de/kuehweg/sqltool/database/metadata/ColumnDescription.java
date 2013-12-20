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
package de.kuehweg.sqltool.database.metadata;

import java.util.Objects;

/**
 * Beschreibung der Spalten-Metadaten.
 * 
 * @author Michael Kühweg
 */
public class ColumnDescription implements Comparable<ColumnDescription> {

	private final String catalog;
	private final String schema;
	private final String tableName;
	private final String columnName;
	private final String type;
	private final int size;
	private final int decimalDigits;
	private final Nullability nullable;
	private final String defaultValue;
	private final String remarks;

	public ColumnDescription(final String catalog, final String schema,
			final String tableName, final String columnName, final String type,
			final int size, final int decimalDigits,
			final Nullability nullable, final String defaultValue,
			final String remarks) {
		this.catalog = catalog == null ? "" : catalog;
		this.schema = schema == null ? "" : schema;
		this.tableName = tableName == null ? "" : tableName;
		this.columnName = columnName == null ? "" : columnName;
		this.type = type;
		this.size = size;
		this.decimalDigits = decimalDigits;
		this.nullable = nullable;
		this.defaultValue = defaultValue;
		this.remarks = remarks;
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

	public String getColumnName() {
		return columnName;
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

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + Objects.hashCode(catalog);
		hash = 37 * hash + Objects.hashCode(schema);
		hash = 37 * hash + Objects.hashCode(tableName);
		hash = 37 * hash + Objects.hashCode(columnName);
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ColumnDescription other = (ColumnDescription) obj;
		if (!Objects.equals(catalog, other.catalog)) {
			return false;
		}
		if (!Objects.equals(schema, other.schema)) {
			return false;
		}
		if (!Objects.equals(tableName, other.tableName)) {
			return false;
		}
		if (!Objects.equals(columnName, other.columnName)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final ColumnDescription other) {
		int result = catalog.compareTo(other.catalog);
		if (result == 0) {
			result = schema.compareTo(other.schema);
		}
		if (result == 0) {
			result = tableName.compareTo(other.tableName);
		}
		if (result == 0) {
			result = columnName.compareTo(other.columnName);
		}
		return result;
	}
}
