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
package de.kuehweg.sqltool.dialog.component.schematree;

import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;
import de.kuehweg.sqltool.dialog.util.WebViewWithHSQLDBBugfix;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

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
        final TreeItem<String> root = new TreeItem<>();
        treeToUpdate.setRoot(root);

        root.setValue(db.getName());
        Label label = new Label(SchemaTreeConstants.DATABASE);
        label.getStyleClass().add(SchemaTreeConstants.STYLE_DATABASE);
        root.setGraphic(label);
        root.getChildren().clear();
        root.getChildren().add(getDbProductInfo(db));
        for (final CatalogDescription catalog : db.getCatalogs()) {
            final TreeItem<String> catalogItem = new TreeItem<>(
                    catalog.getName());
            catalogItem.getChildren().addAll(getSchemas(catalog));
            root.getChildren().add(catalogItem);
        }

        stateSaver.expandFromSavedState(treeToUpdate);
    }

    private TreeItem<String> getDbProductInfo(final DatabaseDescription db) {
        return new TreeItem<>(db.getDbProductName() + " " + db.
                getDbProductVersion());
    }

    private List<TreeItem<String>> getSchemas(final CatalogDescription catalog) {
        final List<TreeItem<String>> schemaItems = new ArrayList<>();
        for (final SchemaDescription schema : catalog.getSchemas()) {
            Label label = new Label(SchemaTreeConstants.USER);
            label.getStyleClass().add(SchemaTreeConstants.STYLE_USER);
            final TreeItem<String> schemaItem = new TreeItem<>(schema.getName(), label);
            schemaItem.getChildren().addAll(getTables(schema));
            schemaItems.add(schemaItem);
        }
        return schemaItems;
    }

    private List<TreeItem<String>> getTables(final SchemaDescription schema) {
        final List<TreeItem<String>> typeItems = new ArrayList<>();
        for (final String type : schema.getTableTypes()) {
            final TreeItem<String> typeItem = new TreeItem<>(type);
            for (final TableDescription table : schema.getTablesByType(type)) {
                typeItem.getChildren().add(buildTableItem(table));
            }
            typeItems.add(typeItem);
        }
        return typeItems;
    }

    private TreeItem<String> buildTableItem(final TableDescription table) {
        Label label = new Label(SchemaTreeConstants.TABLE);
        label.getStyleClass().add(SchemaTreeConstants.STYLE_TABLE);
        final TreeItem<String> tableItem = new TreeItem<>(table.getName(),
                label);
        if (table.getRemarks() != null
                && table.getRemarks().trim().length() > 0) {
            tableItem.getChildren().add(new TreeItem<>(table.getRemarks()));
        }
        tableItem.getChildren().
                addAll(new SchemaTreeColumnBuilder(table).getColumnNodes());
        tableItem.getChildren().addAll(new SchemaTreeForeignKeyBuilder(table).
                getForeignKeyNodes());
        tableItem.getChildren().addAll(new SchemaTreeIndexBuilder(table).getIndexNode());

        return tableItem;
    }
}
