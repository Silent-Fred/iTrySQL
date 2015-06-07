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
package de.kuehweg.sqltool.dialog.component.schematree;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.metadata.description.ForeignKeyColumnByColumnName;
import de.kuehweg.sqltool.database.metadata.description.ForeignKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

/**
 * Aufbau der INformationen zu Fremdschlüsseln für die Strukturansicht.
 *
 * @author Michael Kühweg
 */
public class SchemaTreeForeignKeyBuilder {

    private final TableDescription table;

    private enum ForeignKeyType {

        IMPORTED, EXPORTED
    };

    public SchemaTreeForeignKeyBuilder(final TableDescription table) {
        this.table = table;
    }

    public List<TreeItem<String>> getForeignKeyNodes() {
        final List<TreeItem<String>> foreignKeyNodes = new ArrayList<>(2);

        final List<TreeItem<String>> importedNode = getForeignKeyNode(
                ForeignKeyType.IMPORTED);
        if (!importedNode.isEmpty()) {
            foreignKeyNodes.addAll(importedNode);
        }

        final List<TreeItem<String>> exportedNode = getForeignKeyNode(
                ForeignKeyType.EXPORTED);
        if (!exportedNode.isEmpty()) {
            foreignKeyNodes.addAll(exportedNode);
        }

        return foreignKeyNodes;
    }

    private List<TreeItem<String>> getForeignKeyNode(ForeignKeyType type) {
        final List<TreeItem<String>> nodeItems = getForeignKeyNodeItems(type);

        if (nodeItems.isEmpty()) {
            return Collections.emptyList();
        }

        final List<TreeItem<String>> keyCollection = new ArrayList<>(1);
        Label keyCollectionLabel = new Label(labelTextByType(type));
        keyCollectionLabel.getStyleClass().add(styleClassByType(type));
        final String keyCollectionTitle = collectionTitleByType(type);
        final TreeItem<String> keyCollectionTitleTitem = new TreeItem<>(
                keyCollectionTitle, keyCollectionLabel);
        keyCollectionTitleTitem.getChildren().addAll(nodeItems);
        keyCollection.add(keyCollectionTitleTitem);

        return keyCollection;
    }

    private String labelTextByType(ForeignKeyType type) {
        return type == ForeignKeyType.EXPORTED ? SchemaTreeConstants.FOREIGN_KEY_CONSTRAINT : SchemaTreeConstants.REFERENCES;
    }

    private String styleClassByType(ForeignKeyType type) {
        return type == ForeignKeyType.EXPORTED ? SchemaTreeConstants.STYLE_FOREIGN_KEY_CONSTRAINT : SchemaTreeConstants.STYLE_REFERENCES;
    }

    private String collectionTitleByType(ForeignKeyType type) {
        return (type == ForeignKeyType.EXPORTED ? DialogDictionary.LABEL_TREE_REFERENCED_BY : DialogDictionary.LABEL_TREE_REFERENCES).
                toString();
    }

    private List<ForeignKeyColumnDescription> relevantForeignKeyColumnDescriptions(
            ForeignKeyType type) {
        return type == ForeignKeyType.EXPORTED ? table.getExportedKeyColumns() : table.
                getImportedKeyColumns();
    }

    private List<ForeignKeyColumnDescription> fkColumnsForFkNameSortedByColumnName(
            final String fkName, List<ForeignKeyColumnDescription> fkColumns) {
        List<ForeignKeyColumnDescription> sortedFkColumns = new ArrayList<>(10);
        for (final ForeignKeyColumnDescription fkColumn : fkColumns) {
            if (fkColumn.getName().equals(fkName)) {
                sortedFkColumns.add(fkColumn);
            }
        }
        Collections.sort(sortedFkColumns, new ForeignKeyColumnByColumnName());
        return sortedFkColumns;
    }

    private Set<String> extractForeignKeyNames(List<ForeignKeyColumnDescription> fkColumns) {
        final Set<String> foreignKeyNames = new HashSet<>(10);
        for (final ForeignKeyColumnDescription fkColumn : fkColumns) {
            foreignKeyNames.add(fkColumn.getName());
        }
        return foreignKeyNames;
    }

    private List<TreeItem<String>> getForeignKeyNodeItems(ForeignKeyType type) {
        final List<ForeignKeyColumnDescription> relevantFkColumns
                = relevantForeignKeyColumnDescriptions(type);
        final List<TreeItem<String>> fkItems = new ArrayList<>(
                relevantFkColumns.size());
        final List<String> fkNames = new ArrayList<>(extractForeignKeyNames(
                relevantFkColumns));
        Collections.sort(fkNames);
        for (String fkName : fkNames) {
            Label label = new Label(labelTextByType(type));
            label.getStyleClass().add(styleClassByType(type));
            final TreeItem<String> fkItem = new TreeItem<>(fkName, label);
            for (ForeignKeyColumnDescription column
                    : fkColumnsForFkNameSortedByColumnName(fkName, relevantFkColumns)) {
                fkItem.getChildren().add(new TreeItem<>(fkColumnNameForTreeView(column,
                        type)));
            }
            fkItems.add(fkItem);
        }
        return fkItems;
    }

    private String fkColumnNameForTreeView(final ForeignKeyColumnDescription fkColumn,
            ForeignKeyType type) {
        StringBuilder description = new StringBuilder(fkColumn.getInsideColumnName());
        if (type == ForeignKeyType.EXPORTED) {
            description.append(" <- ");
        } else {
            description.append(" -> ");
        }
        description.append(fkColumn.referencesFarOutside() ? fkColumn.
                getFullyQualifiedOutsideColumn() : fkColumn.getQualifiedOutsideColumn());
        return description.toString();
    }
}
