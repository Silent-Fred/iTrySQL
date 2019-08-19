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

import java.sql.ResultSet;
import java.sql.SQLException;

import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;

/**
 * Abstrakte Basisklasse für die Metadaten-Aufbereiter.
 *
 * @author Michael Kühweg
 */
public abstract class AbstractMetaDataReader {

	private final DatabaseDescription root;

	public AbstractMetaDataReader(final DatabaseDescription root) {
		this.root = root;
	}

	public void readAndAddDescriptions(final ResultSet metadata) throws SQLException {
		while (metadata.next()) {
			readAndAddDescription(metadata);
		}
	}

	public DatabaseDescription findParent() {
		return root;
	}

	public CatalogDescription findParent(final String catalogName) {
		try {
			return findCatalog(catalogName);
		} catch (final DescriptionParentNotFoundException ex) {
			final CatalogDescription exceptionalObjectDescription = new CatalogDescription(catalogName);
			root.adoptOrphan(exceptionalObjectDescription);
			return exceptionalObjectDescription;
		}
	}

	public SchemaDescription findParent(final String catalogName, final String schemaName) {
		final CatalogDescription parent = findParent(catalogName);
		try {
			return findSchema(schemaName, parent);
		} catch (final DescriptionParentNotFoundException ex) {
			final SchemaDescription exceptionalObjectDescription = new SchemaDescription(schemaName);
			parent.adoptOrphan(exceptionalObjectDescription);
			return exceptionalObjectDescription;
		}
	}

	public TableDescription findParent(final String catalogName, final String schemaName, final String tableName) {
		final SchemaDescription parent = findParent(catalogName, schemaName);
		try {
			return findTable(tableName, parent);
		} catch (final DescriptionParentNotFoundException ex) {
			final TableDescription exceptionalObjectDescription = new TableDescription(tableName, "UNKNOWN",
					"This table's description was built by accident. Some subobject was created first. The description is incomplete.");
			parent.adoptOrphan(exceptionalObjectDescription);
			return exceptionalObjectDescription;
		}
	}

	private CatalogDescription findCatalog(final String catalogName) throws DescriptionParentNotFoundException {
		if (catalogName != null) {
			for (final CatalogDescription catalog : root.getCatalogs()) {
				if (catalog.getName().equals(catalogName)) {
					return catalog;
				}
			}
		}
		throw new DescriptionParentNotFoundException("Parent catalog not found: " + catalogName);
	}

	private SchemaDescription findSchema(final String schemaName, final CatalogDescription catalog)
			throws DescriptionParentNotFoundException {
		if (schemaName != null && catalog != null) {
			for (final SchemaDescription schema : catalog.getSchemas()) {
				if (schema.getName().equals(schemaName)) {
					return schema;
				}
			}
		}
		throw new DescriptionParentNotFoundException("Parent schema not found: "
				+ (catalog != null ? catalog.getQualifiedName() : "NO CATALOG") + "." + schemaName);
	}

	private TableDescription findTable(final String tableName, final SchemaDescription schema)
			throws DescriptionParentNotFoundException {
		if (tableName != null && schema != null) {
			for (final TableDescription table : schema.getTables()) {
				if (table.getName().equals(tableName)) {
					return table;
				}
			}
		}
		throw new DescriptionParentNotFoundException("Parent table not found: "
				+ (schema != null ? schema.getQualifiedName() : "NO SCHEMA") + "." + tableName);
	}

	protected abstract void readAndAddDescription(final ResultSet metadata) throws SQLException;

}
