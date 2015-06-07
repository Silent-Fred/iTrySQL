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

import de.kuehweg.sqltool.database.metadata.description.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.Nullability;
import de.kuehweg.sqltool.database.metadata.description.PrimaryKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

/**
 * Aufbau der Spalteninformationen zu einer Tabelle für die Strukturansicht.
 *
 * @author Michael Kühweg
 */
public class SchemaTreeColumnBuilder {

    private final TableDescription table;

    public SchemaTreeColumnBuilder(final TableDescription table) {
        this.table = table;
    }

    public List<TreeItem<String>> getColumnNodes() {
        final List<TreeItem<String>> columnItems = new ArrayList<>(table
                .getColumns().size());
        for (final ColumnDescription column : table.getColumns()) {
            columnItems.add(buildColumnItem(column));
        }
        return columnItems;
    }

    private boolean isPrimaryKeyColumnInTable(final ColumnDescription column) {
        if (column.getParent().equals(table)) {
            for (PrimaryKeyColumnDescription pk : table.getPrimaryKeyColumns()) {
                if (pk.getColumnName().equals(column.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private TreeItem<String> buildColumnItem(final ColumnDescription column) {
        boolean pkColumn = isPrimaryKeyColumnInTable(column);
        Label label = new Label(
                pkColumn ? SchemaTreeConstants.PRIMARY_KEY : SchemaTreeConstants.COLUMN);
        label.getStyleClass().add(
                pkColumn ? SchemaTreeConstants.STYLE_PRIMARY_KEY : SchemaTreeConstants.STYLE_COLUMN);
        final TreeItem<String> columnItem = new TreeItem<>(column.getName(), label);
        columnItem.getChildren().add(new TreeItem<>(column.getType() + "(" + column.
                getSize() + ")"));
        if (column.getNullable() == Nullability.YES) {
            columnItem.getChildren().add(new TreeItem<>("NULLABLE"));
        }
        return columnItem;
    }
}
