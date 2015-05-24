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
package de.kuehweg.sqltool.dialog.component.schematree;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.metadata.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.ForeignKeyDescription;
import de.kuehweg.sqltool.database.metadata.IndexDescription;
import de.kuehweg.sqltool.database.metadata.Nullability;
import de.kuehweg.sqltool.database.metadata.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.TableDescription;
import de.kuehweg.sqltool.dialog.util.WebViewWithHSQLDBBugfix;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        if (db != null) {
            root.setValue(db.getName());
            Label label = new Label(SchemaTreeConstants.DATABASE);
            label.getStyleClass().add(SchemaTreeConstants.STYLE_DATABASE);
            root.setGraphic(label);
            root.getChildren().clear();
            root.getChildren().add(getDbProductInfo(db));
            for (final CatalogDescription catalog : db.getCatalogs()) {
                final TreeItem<String> catalogItem = new TreeItem<>(
                        catalog.getCatalog());
                catalogItem.getChildren().addAll(getSchemas(catalog));
                root.getChildren().add(catalogItem);
            }
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
            final TreeItem<String> schemaItem = new TreeItem<>(schema.getSchema(), label);
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
                Label label = new Label(SchemaTreeConstants.TABLE);
                label.getStyleClass().add(SchemaTreeConstants.STYLE_TABLE);
                final TreeItem<String> tableItem = new TreeItem<>(table.getTableName(),
                        label);
                if (table.getRemarks() != null
                        && table.getRemarks().trim().length() > 0) {
                    tableItem.getChildren().add(
                            new TreeItem<>(table.getRemarks()));
                }
                tableItem.getChildren().addAll(getColumns(table));
                tableItem.getChildren().addAll(getForeignKeys(table, table.
                        getForeignKeys(), false));
                tableItem.getChildren().addAll(getForeignKeys(table, table.
                        getReferencedBy(), true));
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
            Label label = new Label(
                    table.getPrimaryKey().contains(column.getColumnName()) ? SchemaTreeConstants.PRIMARY_KEY : SchemaTreeConstants.COLUMN);
            label.getStyleClass().add(table.getPrimaryKey().contains(column.
                    getColumnName()) ? SchemaTreeConstants.STYLE_PRIMARY_KEY : SchemaTreeConstants.STYLE_COLUMN);
            final TreeItem<String> columnItem = new TreeItem<>(column.getColumnName(),
                    label);
            columnItem.getChildren().add(
                    new TreeItem<>(column.getType() + "("
                            + column.getSize() + ")"));
            if (column.getNullable() == Nullability.YES) {
                columnItem.getChildren().add(new TreeItem<>("NULLABLE"));
            }
            columnItems.add(columnItem);
        }
        return columnItems;
    }

    private List<TreeItem<String>> getIndices(final TableDescription table) {
        final List<TreeItem<String>> indexItems = new ArrayList<>(table
                .getIndices().size());
        for (final IndexDescription index : table.getIndices()) {
            Label label = new Label(SchemaTreeConstants.INDEX);
            label.getStyleClass().add(SchemaTreeConstants.STYLE_INDEX);
            final TreeItem<String> indexItem = new TreeItem<>(index.getIndexName(), label);
            indexItem.getChildren().add(
                    new TreeItem<>(index.getColumnName()));
            if (index.isNonUnique()) {
                indexItem.getChildren().add(new TreeItem<>("NON UNIQUE"));
            }
            indexItems.add(indexItem);
        }
        if (indexItems.isEmpty()) {
            return Collections.emptyList();
        }
        final List<TreeItem<String>> indexCollection = new ArrayList<>(1);
        Label label = new Label(SchemaTreeConstants.INDEX);
        label.getStyleClass().add(SchemaTreeConstants.STYLE_INDEX);
        final TreeItem<String> indexTitle = new TreeItem<>(
                DialogDictionary.LABEL_TREE_INDICES.toString(), label);
        indexCollection.add(indexTitle);
        indexTitle.getChildren().addAll(indexItems);
        return indexCollection;
    }

    private List<TreeItem<String>> getForeignKeys(
            final TableDescription tableDescription,
            final Collection<ForeignKeyDescription> foreignKeyDescriptions,
            final boolean outsideView) {
        final List<TreeItem<String>> foreignKeyItems = new ArrayList<>(
                foreignKeyDescriptions.size());
        for (final Map.Entry<String, Collection<String>> fk
                : buildForeignKeyColumnsByForeignKeyNameMap(tableDescription,
                        foreignKeyDescriptions).entrySet()) {
            final Label label = new Label(
                    outsideView ? SchemaTreeConstants.FOREIGN_KEY_CONSTRAINT : SchemaTreeConstants.REFERENCES);
            label.getStyleClass().add(
                    outsideView ? SchemaTreeConstants.STYLE_FOREIGN_KEY_CONSTRAINT : SchemaTreeConstants.STYLE_REFERENCES);
            final TreeItem<String> foreignKeyItem = new TreeItem<>(fk.getKey(), label);
            final List<String> fkColumns = new ArrayList<>(fk.getValue());
            Collections.sort(fkColumns);
            for (final String column : fkColumns) {
                foreignKeyItem.getChildren().add(new TreeItem<>(column));
            }
            foreignKeyItems.add(foreignKeyItem);
        }
        if (foreignKeyItems.isEmpty()) {
            return Collections.emptyList();
        }
        final Label collectionLabel = new Label(
                outsideView ? SchemaTreeConstants.FOREIGN_KEY_CONSTRAINT : SchemaTreeConstants.REFERENCES);
        collectionLabel.getStyleClass().add(
                outsideView ? SchemaTreeConstants.STYLE_FOREIGN_KEY_CONSTRAINT : SchemaTreeConstants.STYLE_REFERENCES);
        final DialogDictionary collectionTitle
                = outsideView ? DialogDictionary.LABEL_TREE_REFERENCED_BY : DialogDictionary.LABEL_TREE_REFERENCES;
        final List<TreeItem<String>> foreignKeyCollection = new ArrayList<>(1);
        final TreeItem<String> collectionTitleTitem = new TreeItem<>(collectionTitle.
                toString(), collectionLabel);
        foreignKeyCollection.add(collectionTitleTitem);
        collectionTitleTitem.getChildren().addAll(foreignKeyItems);
        return foreignKeyCollection;
    }

    private String getForeignKeyFKTableColumnNameQualified(
            final ForeignKeyDescription foreignKeyDescription) {
        final StringBuilder qualifiedNameBuilder = new StringBuilder();
        if (foreignKeyDescription.isOutside()) {
            qualifiedNameBuilder.append(foreignKeyDescription.getFkCatalog()).
                    append(".").append(foreignKeyDescription.getFkSchema()).
                    append(".");
        }
        qualifiedNameBuilder.append(foreignKeyDescription.getFkTableName()).
                append(".").append(foreignKeyDescription.getFkColumnName());
        return qualifiedNameBuilder.toString();
    }

    private String getForeignKeyPKTableColumnNameQualified(
            final ForeignKeyDescription foreignKeyDescription) {
        final StringBuilder qualifiedNameBuilder = new StringBuilder();
        if (foreignKeyDescription.isOutside()) {
            qualifiedNameBuilder.append(foreignKeyDescription.getPkCatalog()).
                    append(".").append(foreignKeyDescription.getPkSchema()).
                    append(".");
        }
        qualifiedNameBuilder.append(foreignKeyDescription.getPkTableName()).
                append(".").append(foreignKeyDescription.getPkColumnName());
        return qualifiedNameBuilder.toString();
    }

    private String getForeignKeyNameQualified(
            final ForeignKeyDescription referencedByDescription) {
        final StringBuilder qualifiedNameBuilder = new StringBuilder();
        if (referencedByDescription.isOutside()) {
            qualifiedNameBuilder.append(referencedByDescription.getFkCatalog()).
                    append(".").append(referencedByDescription.getFkSchema()).
                    append(".");
        }
        qualifiedNameBuilder.append(referencedByDescription.getForeignKeyName());
        return qualifiedNameBuilder.toString();
    }

    private boolean isTableOwnerOfForeignKey(
            final TableDescription tableDescription,
            final ForeignKeyDescription foreignKeyDescription) {
        return Objects.equals(tableDescription.getCatalog(),
                foreignKeyDescription.getFkCatalog())
                && Objects.equals(tableDescription.getSchema(),
                        foreignKeyDescription.getFkSchema())
                && Objects.equals(tableDescription.getTableName(),
                        foreignKeyDescription.getFkTableName());
    }

    private String getForeignKeyColumn(
            final TableDescription tableDescriptionBeingEvaluated,
            final ForeignKeyDescription foreignKey) {
        if (foreignKey == null) {
            return "";
        }
        return isTableOwnerOfForeignKey(tableDescriptionBeingEvaluated,
                foreignKey)
                        ? foreignKey.getFkColumnName() + " -> "
                        + getForeignKeyPKTableColumnNameQualified(
                                foreignKey)
                        : getForeignKeyFKTableColumnNameQualified(
                                foreignKey);
    }

    private Map<String, Collection<String>> buildForeignKeyColumnsByForeignKeyNameMap(
            final TableDescription tableDescriptionBeingEvaluated,
            final Collection<ForeignKeyDescription> foreignKeyDescriptions) {
        final Map<String, Collection<String>> fkColumnsByFK = new HashMap<>();
        for (final ForeignKeyDescription foreignKey : foreignKeyDescriptions) {
            final String foreignKeyName = getForeignKeyNameQualified(foreignKey);
            Collection<String> fkColumns = fkColumnsByFK.get(foreignKeyName);
            if (fkColumns == null) {
                fkColumns = new HashSet<>();
                fkColumnsByFK.put(foreignKeyName, fkColumns);
            }
            fkColumns.add(getForeignKeyColumn(tableDescriptionBeingEvaluated,
                    foreignKey));
        }
        return fkColumnsByFK;
    }
}
