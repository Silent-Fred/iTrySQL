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
package de.kuehweg.sqltool.database.metadata.description;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Beschreibung der Tabellen-Metadaten.
 *
 * @author Michael Kühweg
 */
public class TableDescription extends DatabaseObjectDescription {

    private final String tableType;
    private final String remarks;
    private final Set<ColumnDescription> columns;
    private final Set<PrimaryKeyColumnDescription> primaryKeyColumns;
    private final Set<IndexColumnDescription> indexColumns;
    private final Set<ForeignKeyColumnDescription> importedKeyColumns;
    private final Set<ForeignKeyColumnDescription> exportedKeyColumns;

    public TableDescription(final String tableName, final String tableType,
            final String remarks) {
        super(tableName);
        this.tableType = tableType == null ? "" : tableType;
        this.remarks = remarks == null ? "" : remarks;
        columns = new HashSet<>();
        indexColumns = new HashSet<>();
        importedKeyColumns = new HashSet<>();
        exportedKeyColumns = new HashSet<>();
        primaryKeyColumns = new HashSet<>();
    }

    public String getTableType() {
        return tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * Tabellenspalten, sortiert nach Name
     *
     * @return
     */
    public List<ColumnDescription> getColumns() {
        final List<ColumnDescription> result = new ArrayList<>(columns);
        Collections.sort(result);
        return result;
    }

    private void addColumns(final ColumnDescription... cols) {
        if (cols != null) {
            for (final ColumnDescription col : cols) {
                columns.add(col);
            }
        }
    }

    /**
     * Indizes der Tabelle, sortiert nach Indexname
     *
     * @return
     */
    public List<IndexColumnDescription> getIndices() {
        final List<IndexColumnDescription> result = new ArrayList<>(indexColumns);
        Collections.sort(result);
        return result;
    }

    private void addIndexColumns(final IndexColumnDescription... inds) {
        if (inds != null) {
            for (final IndexColumnDescription index : inds) {
                indexColumns.add(index);
            }
        }
    }

    /**
     * ImportedKeys, sortiert nach Foreign Key Name. Die einzelnen Spalten, aus denen der
     * Foreign Key aufgebaut ist, sind nicht sortiert.
     *
     * @return
     */
    public List<ForeignKeyColumnDescription> getImportedKeyColumns() {
        final List<ForeignKeyColumnDescription> result = new ArrayList<>(
                importedKeyColumns);
        Collections.sort(result);
        return result;
    }

    private void addImportedKeyColumns(final ForeignKeyColumnDescription... fks) {
        if (fks != null) {
            for (final ForeignKeyColumnDescription fk : fks) {
                importedKeyColumns.add(fk);
            }
        }
    }

    /**
     * ExportedKeys, sortiert nach Foreign Key Name. Die einzelnen Spalten, aus denen der
     * Foreign Key aufgebaut ist, sind nicht sortiert.
     *
     * @return
     */
    public List<ForeignKeyColumnDescription> getExportedKeyColumns() {
        final List<ForeignKeyColumnDescription> result = new ArrayList<>(
                exportedKeyColumns);
        Collections.sort(result);
        return result;
    }

    private void addExportedKeyColumns(final ForeignKeyColumnDescription... fks) {
        if (fks != null) {
            for (final ForeignKeyColumnDescription fk : fks) {
                exportedKeyColumns.add(fk);
            }
        }
    }

    /**
     * Primärschlüsselspalten. Die einzelnen Spalten, aus denen der Primärschlüssel aufgebaut ist, sind nicht sortiert.
     *
     * @return
     */
    public List<PrimaryKeyColumnDescription> getPrimaryKeyColumns() {
        return new ArrayList<>(primaryKeyColumns);
    }

    private void addPrimaryKeyColumns(
            final PrimaryKeyColumnDescription... primaryKeyColumns) {
        if (primaryKeyColumns != null) {
            for (final PrimaryKeyColumnDescription pk : primaryKeyColumns) {
                this.primaryKeyColumns.add(pk);
            }
        }
    }

    @Override
    protected void appendChild(DatabaseObjectDescription child) {
        if (ColumnDescription.class.isAssignableFrom(child.getClass())) {
            addColumns((ColumnDescription) child);
        } else if (PrimaryKeyColumnDescription.class.isAssignableFrom(child.getClass())) {
            addPrimaryKeyColumns((PrimaryKeyColumnDescription) child);
        } else if (IndexColumnDescription.class.isAssignableFrom(child.getClass())) {
            addIndexColumns((IndexColumnDescription) child);
        } else if (ImportedKeyColumnDescription.class.isAssignableFrom(child.getClass())) {
            addImportedKeyColumns((ImportedKeyColumnDescription) child);
        } else if (ExportedKeyColumnDescription.class.isAssignableFrom(child.getClass())) {
            addExportedKeyColumns((ExportedKeyColumnDescription) child);
        } else {
            super.appendChild(child);
        }
    }
}
