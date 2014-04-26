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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Beschreibung der Tabellen-Metadaten. Eine Tabelle enthält Spalten und hat
 * Indizes.
 * 
 * @author Michael Kühweg
 */
public class TableDescription implements Comparable<TableDescription> {

	private final String catalog;
	private final String schema;
	private final String tableName;
	private final String tableType;
	private final String remarks;
	private final Set<ColumnDescription> columns;
	private final Set<IndexDescription> indices;
	private final Set<ForeignKeyDescription> foreignKeys;
	private final Set<ForeignKeyDescription> referencedBy;
    private final List<String> primaryKey;

	public TableDescription(final String catalog, final String schema,
			final String tableName, final String tableType, final String remarks) {
		this.catalog = catalog == null ? "" : catalog;
		this.schema = schema == null ? "" : schema;
		this.tableName = tableName == null ? "" : tableName;
		this.tableType = tableType;
		this.remarks = remarks;
		columns = new HashSet<>();
		indices = new HashSet<>();
        foreignKeys = new HashSet<>();
        referencedBy = new HashSet<>();
        primaryKey = new LinkedList<>();
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

	public String getTableType() {
		return tableType;
	}

	public String getRemarks() {
		return remarks;
	}

	public List<ColumnDescription> getColumns() {
		final List<ColumnDescription> result = new ArrayList<>(columns);
		Collections.sort(result);
		return result;
	}

	public void addColumns(final ColumnDescription... cols) {
		for (final ColumnDescription col : cols) {
			columns.add(col);
		}
	}

	public List<IndexDescription> getIndices() {
		final List<IndexDescription> result = new ArrayList<>(indices);
		Collections.sort(result);
		return result;
	}

	public void addIndices(final IndexDescription... inds) {
		for (final IndexDescription index : inds) {
			indices.add(index);
		}
	}

	public List<ForeignKeyDescription> getForeignKeys() {
		final List<ForeignKeyDescription> result = new ArrayList<>(foreignKeys);
		Collections.sort(result);
		return result;
	}

	public void addForeignKeys(final ForeignKeyDescription... fks) {
		for (final ForeignKeyDescription fk : fks) {
			foreignKeys.add(fk);
		}
	}

	public List<ForeignKeyDescription> getReferencedBy() {
		final List<ForeignKeyDescription> result = new ArrayList<>(referencedBy);
		Collections.sort(result);
		return result;
	}

	public void addReferencedBy(final ForeignKeyDescription... refs) {
		for (final ForeignKeyDescription ref : refs) {
			referencedBy.add(ref);
		}
	}

    public List<String> getPrimaryKey() {
        return primaryKey;
    }
    
    public void setPrimaryKey(final String... primaryKeyColumn) {
        primaryKey.clear();
        for (final String pkCol : primaryKeyColumn) {
            primaryKey.add(pkCol);
        }
    }
    
    public void addPrimaryKeyColumn(final String primaryKeyColumn) {
        if (!primaryKey.contains(primaryKeyColumn)) {
            primaryKey.add(primaryKeyColumn);
        }
    }
    
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + Objects.hashCode(catalog);
		hash = 97 * hash + Objects.hashCode(schema);
		hash = 97 * hash + Objects.hashCode(tableName);
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
		final TableDescription other = (TableDescription) obj;
		if (!Objects.equals(catalog, other.catalog)) {
			return false;
		}
		if (!Objects.equals(schema, other.schema)) {
			return false;
		}
		if (!Objects.equals(tableName, other.tableName)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final TableDescription other) {
		int result = catalog.compareTo(other.catalog);
		if (result == 0) {
			result = schema.compareTo(other.schema);
		}
		if (result == 0) {
			result = tableName.compareTo(other.tableName);
		}
		return result;
	}
}
