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
import de.kuehweg.sqltool.database.metadata.description.IndexColumnByOrdinalPosition;
import de.kuehweg.sqltool.database.metadata.description.IndexColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

/**
 * Aufbau der Indexinformationen für die Strukturansicht
 *
 * @author Michael Kühweg
 */
public class SchemaTreeIndexBuilder {

    private final TableDescription table;

    public SchemaTreeIndexBuilder(final TableDescription table) {
        this.table = table;
    }

    public List<TreeItem<String>> getIndexNode() {
        final List<TreeItem<String>> indexItems = getIndices();
        if (indexItems.isEmpty()) {
            return Collections.emptyList();
        }
        final List<TreeItem<String>> indexNode = new ArrayList<>(1);
        Label label = new Label(SchemaTreeConstants.INDEX);
        label.getStyleClass().add(SchemaTreeConstants.STYLE_INDEX);
        final TreeItem<String> indexTitle
                = new TreeItem<>(DialogDictionary.LABEL_TREE_INDICES.toString(), label);
        indexNode.add(indexTitle);
        indexTitle.getChildren().addAll(indexItems);
        return indexNode;
    }

    private boolean isNonUniqueIndex(final String indexName) {
        for (final IndexColumnDescription indexColumn : table.getIndices()) {
            if (indexColumn.getName().equals(indexName)) {
                return indexColumn.isNonUnique();
            }
        }
        return false;
    }

    private List<String> indexColumnNames(final List<IndexColumnDescription> indexColumns) {
        List<String> columnNames = new ArrayList<>(indexColumns.size());
        for (final IndexColumnDescription indexColumn : indexColumns) {
            columnNames.add(indexColumn.getColumnName());
        }
        return columnNames;
    }

    private Set<String> extractIndexNames() {
        final Set<String> indexNames = new HashSet<>(10);
        for (final IndexColumnDescription index : table.getIndices()) {
            indexNames.add(index.getName());
        }
        return indexNames;
    }

    private List<TreeItem<String>> getIndices() {
        final List<TreeItem<String>> indexItems
                = new ArrayList<>(table.getIndices().size());
        final List<String> indexNames = new ArrayList<>(extractIndexNames());
        Collections.sort(indexNames);
        for (String indexName : indexNames) {
            Label label = new Label(SchemaTreeConstants.INDEX);
            label.getStyleClass().add(SchemaTreeConstants.STYLE_INDEX);
            final TreeItem<String> indexItem
                    = new TreeItem<>(indexNameForTreeView(indexName), label);
            for (String columnName : indexColumnNames(indexColumnsSortedByPosition(
                    indexName))) {
                indexItem.getChildren().add(new TreeItem<>(columnName));
            }
            indexItems.add(indexItem);
        }
        return indexItems;
    }

    private List<IndexColumnDescription> indexColumnsSortedByPosition(
            final String indexName) {
        List<IndexColumnDescription> indexColumns = new ArrayList<>(10);
        for (final IndexColumnDescription indexColumn : table.getIndices()) {
            if (indexColumn.getName().equals(indexName)) {
                indexColumns.add(indexColumn);
            }
        }
        Collections.sort(indexColumns, new IndexColumnByOrdinalPosition());
        return indexColumns;
    }

    private String indexNameForTreeView(final String indexName) {
        return isNonUniqueIndex(indexName) ? indexName + " (NON UNIQUE)" : indexName;
    }

}
