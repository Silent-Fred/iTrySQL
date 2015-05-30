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
package de.kuehweg.sqltool.database.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasse zum Auslesen der Metadaten einer Datenbank.
 *
 * @author Michael Kühweg
 */
public class MetaDataReader {

    /**
     * Liest die Metadaten der übergebenen Datenbankverbindung aus und liefert sie in
     * aufbereiteter Form als DatabaseDescription zurück.
     *
     * @param connection
     * @return
     */
    public DatabaseDescription readMetaData(final Connection connection) {
        DatabaseDescription db;
        try {
            final DatabaseMetaData metaData = connection.getMetaData();
            final String dbName = metaData.getUserName() + "@"
                    + metaData.getURL();
            final String dbProductName = metaData.getDatabaseProductName();
            final String dbProductVersion = metaData
                    .getDatabaseProductVersion();
            db = new DatabaseDescription(dbName, dbProductName,
                    dbProductVersion);

            final Collection<TableDescription> tableDescriptions
                    = readCompleteTableDescriptions(connection);

            db.addCatalogs(buildCatalogDescriptionsFromSchemaDescriptions(
                    buildSchemaDescriptionsFromTableDescriptions(tableDescriptions)).
                    toArray(new CatalogDescription[0]));
        } catch (final SQLException ex) {
            Logger.getLogger(MetaDataReader.class.getName()).log(Level.SEVERE,
                    null, ex);
            db = new DatabaseDescription();
        }
        return db;
    }

    private Collection<TableDescription> readRawTableDescriptions(
            final Connection connection) throws SQLException {
        try (ResultSet tables = connection.getMetaData().getTables(null, null, null, null)) {
            return new ArrayList<>(new TableMetaDataReader().buildDescriptions(tables));
        }
    }

    private Collection<TableDescription> readCompleteTableDescriptions(
            final Connection connection) throws SQLException {
        final Collection<TableDescription> tableDescriptions = readRawTableDescriptions(
                connection);
        for (TableDescription table : tableDescriptions) {
            readColumnDescriptions(connection, table);
            readPrimaryKeyDescriptions(connection, table);

            readIndexDescriptions(connection, table);

            readForeignKeyDescriptions(connection, table);
            readReferencedByDescriptions(connection, table);
        }
        return tableDescriptions;
    }

    private void readPrimaryKeyDescriptions(final Connection connection,
            TableDescription table) throws SQLException {
        try (ResultSet pks = connection.getMetaData().getPrimaryKeys(table.
                getCatalog(), table.getSchema(), table.getTableName())) {
            table.
                    addPrimaryKeys(new PrimaryKeyMetaDataReader().buildDescriptions(
                                    pks));
        }
    }

    private void readColumnDescriptions(final Connection connection,
            TableDescription table) throws SQLException {
        try (ResultSet columns = connection.getMetaData().getColumns(table.
                getCatalog(), table.getSchema(), table.getTableName(), null)) {
            table.
                    addColumns(new ColumnMetaDataReader().buildDescriptions(
                                    columns));
        }
    }

    private void readIndexDescriptions(final Connection connection,
            TableDescription table) throws SQLException {
        try (ResultSet idx = connection.getMetaData().getIndexInfo(table.
                getCatalog(), table.getSchema(), table.getTableName(), false, false)) {
            table.addIndices(new IndexMetaDataReader().buildDescriptions(idx));
        }
    }

    private void readForeignKeyDescriptions(final Connection connection,
            TableDescription table) throws SQLException {
        try (ResultSet fks = connection.getMetaData().getImportedKeys(table.
                getCatalog(), table.getSchema(), table.getTableName())) {
            table.
                    addForeignKeys(new ForeignKeyMetaDataReader().buildDescriptions(
                                    fks));
        }
    }

    private void readReferencedByDescriptions(final Connection connection,
            TableDescription table) throws SQLException {
        try (ResultSet fks = connection.getMetaData().getExportedKeys(table.
                getCatalog(), table.getSchema(), table.getTableName())) {
            table.addReferencedBy(new ForeignKeyMetaDataReader().
                    buildDescriptions(fks));
        }
    }

    protected Collection<SchemaDescription> buildSchemaDescriptionsFromTableDescriptions(
            final Collection<TableDescription> tables) {
        Map<SchemaDescription, SchemaDescription> schemas = new HashMap<>();
        if (tables != null) {
            for (TableDescription table : tables) {
                SchemaDescription schema = new SchemaDescription(table.getCatalog(),
                        table.getSchema());
                if (schemas.containsKey(schema)) {
                    schema = schemas.get(schema);
                } else {
                    schemas.put(schema, schema);
                }
                schema.addTables(table);
            }
        }
        return schemas.keySet();
    }

    protected Collection<CatalogDescription> buildCatalogDescriptionsFromSchemaDescriptions(
            final Collection<SchemaDescription> schemas) {
        Map<CatalogDescription, CatalogDescription> catalogs = new HashMap<>();
        if (schemas != null) {
            for (SchemaDescription schema : schemas) {
                CatalogDescription catalog = new CatalogDescription(schema.getCatalog());
                if (catalogs.containsKey(catalog)) {
                    catalog = catalogs.get(catalog);
                } else {
                    catalogs.put(catalog, catalog);
                }
                catalog.addSchemas(schema);
            }
        }
        return catalogs.keySet();
    }

}
