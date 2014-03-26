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
package de.kuehweg.sqltool.dialog.action;

import de.kuehweg.sqltool.database.metadata.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.IndexDescription;
import de.kuehweg.sqltool.database.metadata.Nullability;
import de.kuehweg.sqltool.database.metadata.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.TableDescription;
import de.kuehweg.sqltool.dialog.images.ImagePack;
import de.kuehweg.sqltool.dialog.util.WebViewWithHSQLDBBugfix;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

/**
 * Baumansicht der Datenbankstruktur
 *
 * @author Michael Kühweg
 */
public class SchemaTreeBuilder implements Runnable {

    private final DatabaseDescription db;
    private final TreeView<String> treeToUpdate;

    public SchemaTreeBuilder(final DatabaseDescription db,
            final TreeView<String> treeToUpdate) {
        this.db = db;
        this.treeToUpdate = treeToUpdate;
    }

    @Override
    public void run() {
        refreshSchemaTree(db, treeToUpdate);
        // FIXME
        WebViewWithHSQLDBBugfix.fix();
    }

    private void refreshSchemaTree(final DatabaseDescription db,
            final TreeView<String> treeToUpdate) {
        final SchemaTreeExpandedStateSaver stateSaver = new SchemaTreeExpandedStateSaver();
        stateSaver.readExpandedStateFrom(treeToUpdate);
        final TreeItem<String> root = new TreeItem<String>();
        treeToUpdate.setRoot(root);
        if (db != null) {
            root.setValue(db.getName());
            root.setGraphic(new ImageView(ImagePack.TREE_DATABASE.getAsImage()));
            root.getChildren().clear();
            for (final CatalogDescription catalog : db.getCatalogs()) {
                final TreeItem<String> catalogItem = new TreeItem<String>(
                        catalog.getCatalog());
                catalogItem.getChildren().addAll(getSchemas(catalog));
                root.getChildren().add(catalogItem);
            }
        }
        stateSaver.expandFromSavedState(treeToUpdate);
    }

    private List<TreeItem<String>> getSchemas(final CatalogDescription catalog) {
        final List<TreeItem<String>> schemaItems = new ArrayList<>();
        for (final SchemaDescription schema : catalog.getSchemas()) {
            final TreeItem<String> schemaItem = new TreeItem<String>(
                    schema.getSchema(), new ImageView(
                    ImagePack.TREE_SCHEMA.getAsImage()));
            schemaItem.getChildren().addAll(getTables(schema));
            schemaItems.add(schemaItem);
        }
        return schemaItems;
    }

    private List<TreeItem<String>> getTables(final SchemaDescription schema) {
        final List<TreeItem<String>> typeItems = new ArrayList<>();
        for (final String type : schema.getTableTypes()) {
            final TreeItem<String> typeItem = new TreeItem<String>(type);
            for (final TableDescription table : schema.getTablesByType(type)) {
                final TreeItem<String> tableItem = new TreeItem<String>(
                        table.getTableName(), new ImageView(
                        ImagePack.TREE_TABLE.getAsImage()));
                if (table.getRemarks() != null
                        && table.getRemarks().trim().length() > 0) {
                    tableItem.getChildren().add(
                            new TreeItem<String>(table.getRemarks()));
                }
                tableItem.getChildren().addAll(getColumns(table));
                tableItem.getChildren().addAll(getIndices(table));
                typeItem.getChildren().add(tableItem);
            }
            typeItems.add(typeItem);
        }
        return typeItems;
    }

    private List<TreeItem<String>> getColumns(final TableDescription table) {
        final List<TreeItem<String>> columnItems = new ArrayList<>(table
                .getColumns().size());
        for (final ColumnDescription column : table.getColumns()) {
            final TreeItem<String> columnItem = new TreeItem<String>(
                    column.getColumnName(), new ImageView(
                    table.getPrimaryKey().contains(column.
                    getColumnName()) ? ImagePack.TREE_PRIMARY_KEY.getAsImage() : ImagePack.TREE_COLUMN.
                    getAsImage()));
            columnItem.getChildren().add(
                    new TreeItem<String>(column.getType() + "("
                    + column.getSize() + ")"));
            if (column.getNullable() == Nullability.YES) {
                columnItem.getChildren().add(new TreeItem<String>("NULLABLE"));
            }
            columnItems.add(columnItem);
        }
        return columnItems;
    }

    private List<TreeItem<String>> getIndices(final TableDescription table) {
        final List<TreeItem<String>> indexItems = new ArrayList<>(table
                .getIndices().size());
        for (final IndexDescription index : table.getIndices()) {
            final TreeItem<String> indexItem = new TreeItem<String>(
                    index.getIndexName(), new ImageView(
                    ImagePack.TREE_INDEX.getAsImage()));
            indexItem.getChildren().add(
                    new TreeItem<String>(index.getColumnName()));
            if (index.isNonUnique()) {
                indexItem.getChildren().add(new TreeItem<String>("NON UNIQUE"));
            }
            indexItems.add(indexItem);
        }
        return indexItems;
    }
}
