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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasse zum Auslesen der Metadaten einer Datenbank.
 * 
 * @author Michael Kühweg
 */
public class MetaDataReader {

	private MetaDataReader() {
		// utility class
	}

	/**
	 * Liest die Metadaten der übergebenen Datenbankverbindung aus und liefert
	 * sie in aufbereiteter Form als DatabaseDescription zurück.
	 * 
	 * @param connection
	 * @return
	 */
	public static DatabaseDescription readMetaData(final Connection connection) {
		String dbName = "DB";
		DatabaseDescription db = null;
		final Map<SchemaDescription, SchemaDescription> schemas = new HashMap<>();
		try {
			final DatabaseMetaData metaData = connection.getMetaData();
			dbName = metaData.getUserName() + "@" + metaData.getURL();
			db = new DatabaseDescription(dbName);
			final Collection<TableDescription> tableDescriptions = new HashSet<>();
			final ResultSet tables = connection.getMetaData().getTables(null,
					null, null, null);
			while (tables.next()) {
				final TableDescription tableDescription = new TableDescription(
						tables.getString("TABLE_CAT"),
						tables.getString("TABLE_SCHEM"),
						tables.getString("TABLE_NAME"),
						tables.getString("TABLE_TYPE"),
						tables.getString("REMARKS"));
				tableDescriptions.add(tableDescription);
				final SchemaDescription key = new SchemaDescription(
						tableDescription.getCatalog(),
						tableDescription.getSchema());
				SchemaDescription value = schemas.get(key);
				if (value == null) {
					value = key;
					schemas.put(key, key);
				}
				value.addTables(tableDescription);
			}
			tables.close();
			readColumnsForGivenTables(metaData, tableDescriptions);
			readIndicesForGivenTables(metaData, tableDescriptions);
		} catch (final SQLException ex) {
			Logger.getLogger(MetaDataReader.class.getName()).log(Level.SEVERE,
					null, ex);
			db = new DatabaseDescription(dbName);
		} finally {
			final Map<CatalogDescription, CatalogDescription> catalogs = new HashMap<>();
			for (final SchemaDescription schema : schemas.values()) {
				final CatalogDescription key = new CatalogDescription(
						schema.getCatalog());
				CatalogDescription value = catalogs.get(key);
				if (value == null) {
					value = key;
					catalogs.put(key, key);
				}
				value.addSchemas(schema);
			}
			if (db != null) {
				for (final CatalogDescription catalog : catalogs.values()) {
					db.addCatalogs(catalog);
				}
			}
		}
		return db;
	}

	/**
	 * Liest die Indexdaten für einer Menge von Tabellen nach und trägt sie in
	 * den Tabellenbeschreibungen ein.
	 * 
	 * @param metaData
	 * @param tables
	 */
	private static void readIndicesForGivenTables(
			final DatabaseMetaData metaData,
			final Collection<TableDescription> tables) {
		for (final TableDescription table : tables) {
			ResultSet indices;
			try {
				indices = metaData.getIndexInfo(table.getCatalog(),
						table.getSchema(), table.getTableName(), false, false);
				while (indices.next()) {
					final IndexDescription indexDescription = new IndexDescription(
							indices.getString("TABLE_CAT"),
							indices.getString("TABLE_SCHEM"),
							indices.getString("TABLE_NAME"),
							indices.getString("INDEX_NAME"),
							indices.getString("COLUMN_NAME"),
							indices.getInt("ORDINAL_POSITION"),
							indices.getBoolean("NON_UNIQUE"));
					table.addIndices(indexDescription);
				}
				indices.close();
			} catch (final SQLException ex) {
				Logger.getLogger(MetaDataReader.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Liest die Spalten für eine Menge von Tabellen nach und trägt sie in den
	 * Tabellenbeschreibungen ein.
	 * 
	 * @param metaData
	 * @param tables
	 */
	private static void readColumnsForGivenTables(
			final DatabaseMetaData metaData,
			final Collection<TableDescription> tables) {
		final Map<TableDescription, TableDescription> tablesMapping = new HashMap<>();
		for (final TableDescription table : tables) {
			tablesMapping.put(table, table);
		}
		try {
			final ResultSet columns = metaData.getColumns(null, null, null,
					null);
			while (columns.next()) {

				final String nullableMeta = columns.getString("IS_NULLABLE");
				Nullability nullabilityToUse = Nullability.MAYBE;
				switch (nullableMeta) {
				case "YES":
					nullabilityToUse = Nullability.YES;
					break;
				case "NO":
					nullabilityToUse = Nullability.NO;
					break;
				}
				final ColumnDescription columnDescription = new ColumnDescription(
						columns.getString("TABLE_CAT"),
						columns.getString("TABLE_SCHEM"),
						columns.getString("TABLE_NAME"),
						columns.getString("COLUMN_NAME"),
						columns.getString("TYPE_NAME"),
						columns.getInt("COLUMN_SIZE"),
						columns.getInt("DECIMAL_DIGITS"), nullabilityToUse,
						columns.getString("REMARKS"),
						columns.getString("COLUMN_DEF"));
				final TableDescription key = new TableDescription(
						columnDescription.getCatalog(),
						columnDescription.getSchema(),
						columnDescription.getTableName(), null, null);
				final TableDescription value = tablesMapping.get(key);
				// nur f��r die ��bergebenen Tabellen wird erg��nzt
				if (value != null) {
					value.addColumns(columnDescription);
				}
			}
			columns.close();
		} catch (final SQLException ex) {
			Logger.getLogger(MetaDataReader.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
