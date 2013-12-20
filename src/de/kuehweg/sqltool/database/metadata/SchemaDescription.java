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
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Beschreibung der Schema-Metadaten. Ein Schema enthält Tabellen.
 * 
 * @author Michael Kühweg
 */
public class SchemaDescription implements Comparable<SchemaDescription> {

	private static final String UNKNOWN_TYPE = "n/a";
	private final String catalog;
	private final String schema;
	private final Set<TableDescription> tables;

	public SchemaDescription(final String catalog, final String schema) {
		this.catalog = catalog == null ? "" : catalog;
		this.schema = schema == null ? "" : schema;
		tables = new HashSet<>(32);

	}

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}

	public List<TableDescription> getTables() {
		final List<TableDescription> result = new ArrayList<>(tables);
		Collections.sort(result);
		return result;
	}

	public void addTables(final TableDescription... tabs) {
		for (final TableDescription table : tabs) {
			tables.add(table);
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(catalog);
		hash = 71 * hash + Objects.hashCode(schema);
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
		final SchemaDescription other = (SchemaDescription) obj;
		if (!Objects.equals(catalog, other.catalog)) {
			return false;
		}
		if (!Objects.equals(schema, other.schema)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final SchemaDescription other) {
		int result = catalog.compareTo(other.catalog);
		if (result == 0) {
			result = schema.compareTo(other.schema);
		}
		return result;
	}

	public List<String> getTableTypes() {
		final Set<String> tableTypes = new HashSet<>(32);
		for (final TableDescription table : tables) {
			if (table.getTableType() == null
					|| table.getTableType().trim().length() == 0) {
				tableTypes.add(UNKNOWN_TYPE);
			} else {
				tableTypes.add(table.getTableType());
			}
		}
		final List<String> result = new ArrayList<>(tableTypes);
		Collections.sort(result);
		return result;
	}

	public List<TableDescription> getTablesByType(final String type) {
		final String wantedType = type == null || type.trim().length() == 0 ? UNKNOWN_TYPE
				: type;
		final List<TableDescription> result = new ArrayList<>(tables.size());
		for (final TableDescription table : getTables()) {
			final String tableType = table.getTableType() == null
					|| table.getTableType().trim().length() == 0 ? UNKNOWN_TYPE
					: table.getTableType();
			if (wantedType.equals(tableType)) {
				result.add(table);
			}
		}
		return result;
	}
}
