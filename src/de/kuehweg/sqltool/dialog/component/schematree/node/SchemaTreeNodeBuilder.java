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
package de.kuehweg.sqltool.dialog.component.schematree.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseObjectDescription;
import de.kuehweg.sqltool.database.metadata.description.ForeignKeyColumnByColumnName;
import de.kuehweg.sqltool.database.metadata.description.ForeignKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.IndexColumnByOrdinalPosition;
import de.kuehweg.sqltool.database.metadata.description.IndexColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.Nullability;
import de.kuehweg.sqltool.database.metadata.description.PrimaryKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;

/**
 * Aufbau der kompletten Strukturansicht, ohne direkten Bezug zu
 * Oberfkächenklassen.
 *
 * @author Michael Kühweg
 */
public class SchemaTreeNodeBuilder {

	private final SchemaTreeNode root;

	public SchemaTreeNodeBuilder(final DatabaseDescription db) {
		root = buildDatabaseNode(db);
	}

	public SchemaTreeNode getRootOfPopulatedTree() {
		return root;
	}

	private SchemaTreeNode buildDatabaseNode(final DatabaseDescription db) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.DATABASE, db.getName());
		node.appendChild(new SchemaTreeNode(SchemaTreeNodeType.PLAIN, getDbProductInfo(db)));
		for (final CatalogDescription catalog : db.getCatalogs()) {
			node.appendChild(buildCatalogNode(catalog));
		}
		return node;
	}

	private String getDbProductInfo(final DatabaseDescription db) {
		return db.getDbProductName() + " " + db.getDbProductVersion();
	}

	private SchemaTreeNode buildCatalogNode(final CatalogDescription catalog) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.CATALOG, catalog.getName());
		for (final SchemaDescription schema : catalog.getSchemas()) {
			node.appendChild(buildSchemaNode(schema));
		}
		return node;
	}

	private SchemaTreeNode buildSchemaNode(final SchemaDescription schema) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.SCHEMA, schema.getName());
		for (final String tableType : schema.getTableTypes()) {
			final SchemaTreeNode typeNode = new SchemaTreeNode(SchemaTreeNodeType.TABLE_TYPE, tableType);
			node.appendChild(typeNode);
			for (final TableDescription table : schema.getTablesByType(tableType)) {
				typeNode.appendChild(buildTableNode(table));
			}
		}
		return node;
	}

	private SchemaTreeNode buildTableNode(final TableDescription table) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.TABLE, table.getName());
		if (table.getRemarks() != null && !table.getRemarks().isEmpty()) {
			node.appendChild(new SchemaTreeNode(SchemaTreeNodeType.PLAIN, table.getRemarks()));
		}
		for (final ColumnDescription column : table.getColumns()) {
			node.appendChild(buildColumnNode(column));
		}
		final SchemaTreeNode importedKeys = buildImportedKeysNode(table);
		if (!importedKeys.getChildren().isEmpty()) {
			node.appendChild(importedKeys);
		}
		final SchemaTreeNode exportedKeys = buildExportedKeysNode(table);
		if (!exportedKeys.getChildren().isEmpty()) {
			node.appendChild(exportedKeys);
		}
		final SchemaTreeNode indices = buildIndicesNode(table);
		if (!indices.getChildren().isEmpty()) {
			node.appendChild(indices);
		}
		return node;
	}

	private SchemaTreeNode buildColumnNode(final ColumnDescription column) {
		SchemaTreeNode node;
		if (isPrimaryKeyColumnInTable(column)) {
			node = new SchemaTreeNode(SchemaTreeNodeType.PRIMARY_KEY_COLUMN, column.getName());
		} else {
			node = new SchemaTreeNode(SchemaTreeNodeType.COLUMN, column.getName());
		}
		if (column.getRemarks() != null && !column.getRemarks().isEmpty()) {
			node.appendChild(new SchemaTreeNode(SchemaTreeNodeType.PLAIN, column.getRemarks()));
		}
		node.appendChild(new SchemaTreeNode(SchemaTreeNodeType.PLAIN, dataTypeDescription(column)));
		if (column.getNullable() == Nullability.YES) {
			node.appendChild(new SchemaTreeNode(SchemaTreeNodeType.PLAIN, "NULLABLE"));
		}
		if (column.getDefaultValue() != null && !column.getDefaultValue().isEmpty()) {
			node.appendChild(new SchemaTreeNode(SchemaTreeNodeType.PLAIN, defaultDescription(column)));
		}
		return node;
	}

	private boolean isPrimaryKeyColumnInTable(final ColumnDescription column) {
		final TableDescription table = (TableDescription) column.getParent();
		for (final PrimaryKeyColumnDescription pk : table.getPrimaryKeyColumns()) {
			if (pk.getColumnName().equals(column.getName())) {
				return true;
			}
		}
		return false;
	}

	private String dataTypeDescription(final ColumnDescription column) {
		return column.getType() + dataTypeLengthDescription(column);
	}

	private String dataTypeLengthDescription(final ColumnDescription column) {
		if (column.getSize() != 0 || column.getDecimalDigits() != 0) {
			return "(" + column.getSize() + (column.getDecimalDigits() != 0 ? "," + column.getDecimalDigits() : "")
					+ ")";
		}
		return "";
	}

	private String defaultDescription(final ColumnDescription column) {
		return "DEFAULT: " + column.getDefaultValue();
	}

	private SchemaTreeNode buildImportedKeysNode(final TableDescription table) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.IMPORTED_KEYS,
				DialogDictionary.LABEL_TREE_REFERENCES.toString());
		for (final String fkName : extractAndSortNames(table.getImportedKeyColumns())) {
			node.appendChild(buildImportedKeyNode(extractAndSortByColumnName(table.getImportedKeyColumns(), fkName)));
		}
		return node;
	}

	private SchemaTreeNode buildExportedKeysNode(final TableDescription table) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.EXPORTED_KEYS,
				DialogDictionary.LABEL_TREE_REFERENCED_BY.toString());
		for (final String fkName : extractAndSortNames(table.getExportedKeyColumns())) {
			node.appendChild(buildExportedKeyNode(extractAndSortByColumnName(table.getExportedKeyColumns(), fkName)));
		}
		return node;
	}

	private SchemaTreeNode buildImportedKeyNode(final List<ForeignKeyColumnDescription> fkColumns) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.IMPORTED_KEY,
				foreignKeyDescription(fkColumns));
		for (final ForeignKeyColumnDescription fkColumn : fkColumns) {
			node.appendChild(
					new SchemaTreeNode(SchemaTreeNodeType.IMPORTED_KEY_COLUMN, importedKeyColumnDescription(fkColumn)));
		}
		return node;
	}

	private SchemaTreeNode buildExportedKeyNode(final List<ForeignKeyColumnDescription> fkColumns) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.EXPORTED_KEY,
				foreignKeyDescription(fkColumns));
		for (final ForeignKeyColumnDescription fkColumn : fkColumns) {
			node.appendChild(
					new SchemaTreeNode(SchemaTreeNodeType.EXPORTED_KEY_COLUMN, exportedKeyColumnDescription(fkColumn)));
		}
		return node;
	}

	private String foreignKeyDescription(final List<ForeignKeyColumnDescription> fkColumns) {
		return fkColumns.isEmpty() ? "n/a" : fkColumns.iterator().next().getName();
	}

	private String importedKeyColumnDescription(final ForeignKeyColumnDescription fkColumn) {
		final StringBuilder description = new StringBuilder(fkColumn.getInsideColumnName());
		description.append(" -> ");
		description.append(fkColumn.referencesFarOutside() ? fkColumn.getFullyQualifiedOutsideColumn()
				: fkColumn.getQualifiedOutsideColumn());
		return description.toString();
	}

	private String exportedKeyColumnDescription(final ForeignKeyColumnDescription fkColumn) {
		final StringBuilder description = new StringBuilder(fkColumn.getInsideColumnName());
		description.append(" <- ");
		description.append(fkColumn.referencesFarOutside() ? fkColumn.getFullyQualifiedOutsideColumn()
				: fkColumn.getQualifiedOutsideColumn());
		return description.toString();
	}

	private List<String> extractAndSortNames(final List<? extends DatabaseObjectDescription> objs) {
		final Set<String> filtered = new HashSet<>();
		for (final DatabaseObjectDescription obj : objs) {
			filtered.add(obj.getName());
		}
		final List<String> sorted = new ArrayList<>(filtered);
		Collections.sort(sorted);
		return sorted;
	}

	private <TYPE extends DatabaseObjectDescription> List<TYPE> extractByName(final List<TYPE> objs,
			final String name) {
		final List<TYPE> filtered = new ArrayList<>(objs.size());
		for (final TYPE obj : objs) {
			if (obj.getName().equals(name)) {
				filtered.add(obj);
			}
		}
		return filtered;
	}

	private List<ForeignKeyColumnDescription>
			extractAndSortByColumnName(final List<ForeignKeyColumnDescription> fkColumns, final String fkName) {
		final List<ForeignKeyColumnDescription> sorted = new ArrayList<>(extractByName(fkColumns, fkName));
		Collections.sort(sorted, new ForeignKeyColumnByColumnName());
		return sorted;
	}

	private SchemaTreeNode buildIndicesNode(final TableDescription table) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.INDICES,
				DialogDictionary.LABEL_TREE_INDICES.toString());
		for (final String idxName : extractAndSortNames(table.getIndices())) {
			node.appendChild(buildIndexNode(extractAndSortByOrdinalPosition(table.getIndices(), idxName), idxName));
		}
		return node;
	}

	private SchemaTreeNode buildIndexNode(final List<IndexColumnDescription> idxColumns, final String idxName) {
		final SchemaTreeNode node = new SchemaTreeNode(SchemaTreeNodeType.INDEX, indexDescription(idxColumns));
		for (final IndexColumnDescription idxColumn : idxColumns) {
			node.appendChild(new SchemaTreeNode(SchemaTreeNodeType.INDEX_COLUMN, idxColumn.getColumnName()));
		}
		return node;
	}

	private String indexDescription(final List<IndexColumnDescription> idxColumns) {
		final String baseName = idxColumns.isEmpty() ? "n/a" : idxColumns.iterator().next().getName();
		return isNonUniqueIndex(idxColumns) ? baseName + " (NON UNIQUE)" : baseName;
	}

	private boolean isNonUniqueIndex(final List<IndexColumnDescription> idxColumns) {
		return !idxColumns.isEmpty() && idxColumns.iterator().next().isNonUnique();
	}

	private List<IndexColumnDescription> extractAndSortByOrdinalPosition(final List<IndexColumnDescription> idxColumns,
			final String idxName) {
		final List<IndexColumnDescription> sorted = new ArrayList<>(extractByName(idxColumns, idxName));
		Collections.sort(sorted, new IndexColumnByOrdinalPosition());
		return sorted;
	}

}
