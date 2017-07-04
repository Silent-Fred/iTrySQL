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
import java.util.logging.Level;
import java.util.logging.Logger;

import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;

/**
 * Klasse zum Auslesen der Metadaten einer Datenbank.
 *
 * @author Michael Kühweg
 */
public class MetaDataReader {

	/**
	 * Liest die Metadaten der übergebenen Datenbankverbindung aus und liefert
	 * sie in aufbereiteter Form als DatabaseDescription zurück.
	 *
	 * @param connection
	 * @return
	 */
	public DatabaseDescription readMetaData(final Connection connection) {
		DatabaseDescription db;
		try {
			final DatabaseMetaData metaData = connection.getMetaData();
			final String dbName = metaData.getUserName() + "@" + metaData.getURL();
			final String dbProductName = metaData.getDatabaseProductName();
			final String dbProductVersion = metaData.getDatabaseProductVersion();
			db = new DatabaseDescription(dbName, dbProductName, dbProductVersion);

			readCatalogDescriptions(db, connection);
			readSchemaDescriptions(db, connection);
			readTableDescriptions(db, connection);
			readTableSubObjects(db, connection);

		} catch (final SQLException ex) {
			Logger.getLogger(MetaDataReader.class.getName()).log(Level.SEVERE, null, ex);
			db = new DatabaseDescription();
		}
		return db;
	}

	private void readCatalogDescriptions(final DatabaseDescription db, final Connection connection)
			throws SQLException {
		try (ResultSet catalogs = connection.getMetaData().getCatalogs()) {
			new CatalogMetaDataReader(db).readAndAddDescriptions(catalogs);
		}
	}

	private void readSchemaDescriptions(final DatabaseDescription db, final Connection connection) throws SQLException {
		try (ResultSet schemas = connection.getMetaData().getSchemas()) {
			new SchemaMetaDataReader(db).readAndAddDescriptions(schemas);
		}
	}

	private void readTableDescriptions(final DatabaseDescription db, final Connection connection) throws SQLException {
		try (ResultSet tables = connection.getMetaData().getTables(null, null, null, null)) {
			new TableMetaDataReader(db).readAndAddDescriptions(tables);
		}
	}

	private void readTableSubObjects(final DatabaseDescription db, final Connection connection) throws SQLException {
		for (final CatalogDescription catalog : db.getCatalogs()) {
			for (final SchemaDescription schema : catalog.getSchemas()) {
				for (final TableDescription table : schema.getTables()) {
					readColumns(db, connection, catalog.getName(), schema.getName(), table.getName());
					readPrimaryKeyColumns(db, connection, catalog.getName(), schema.getName(), table.getName());
					readIndices(db, connection, catalog.getName(), schema.getName(), table.getName());
					readExportedKeys(db, connection, catalog.getName(), schema.getName(), table.getName());
					readImportedKeys(db, connection, catalog.getName(), schema.getName(), table.getName());
				}
			}
		}
	}

	private void readColumns(final DatabaseDescription db, final Connection connection, final String catalog,
			final String schema, final String table) throws SQLException {
		try (ResultSet columns = connection.getMetaData().getColumns(catalog, schema, table, null)) {
			new ColumnMetaDataReader(db).readAndAddDescriptions(columns);
		}
	}

	private void readPrimaryKeyColumns(final DatabaseDescription db, final Connection connection, final String catalog,
			final String schema, final String table) throws SQLException {
		try (ResultSet columns = connection.getMetaData().getPrimaryKeys(catalog, schema, table)) {
			new PrimaryKeyMetaDataReader(db).readAndAddDescriptions(columns);
		}
	}

	private void readIndices(final DatabaseDescription db, final Connection connection, final String catalog,
			final String schema, final String table) throws SQLException {
		try (ResultSet idx = connection.getMetaData().getIndexInfo(catalog, schema, table, false, false)) {
			new IndexMetaDataReader(db).readAndAddDescriptions(idx);
		}
	}

	private void readExportedKeys(final DatabaseDescription db, final Connection connection, final String catalog,
			final String schema, final String table) throws SQLException {
		try (ResultSet idx = connection.getMetaData().getExportedKeys(catalog, schema, table)) {
			new ExportedKeyMetaDataReader(db).readAndAddDescriptions(idx);
		}
	}

	private void readImportedKeys(final DatabaseDescription db, final Connection connection, final String catalog,
			final String schema, final String table) throws SQLException {
		try (ResultSet idx = connection.getMetaData().getImportedKeys(catalog, schema, table)) {
			new ImportedKeyMetaDataReader(db).readAndAddDescriptions(idx);
		}
	}

}
