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
package de.kuehweg.sqltool.database.metadata.description;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Beschreibung der Schema-Metadaten. Ein Schema enthält Tabellen.
 *
 * @author Michael Kühweg
 */
public class SchemaDescription extends DatabaseObjectDescription {

    private static final String UNKNOWN_TYPE = "n/a";
    private final Set<TableDescription> tables;

    public SchemaDescription(final String schema) {
        super(schema);
        tables = new HashSet<>();
    }

    /**
     * Tabellen im Schema, sortiert nach Tabellenname.
     *
     * @return
     */
    public List<TableDescription> getTables() {
        final List<TableDescription> result = new ArrayList<>(tables);
        Collections.sort(result);
        return result;
    }

    private void addTables(final TableDescription... tabs) {
        for (final TableDescription table : tabs) {
            tables.add(table);
        }
    }

    /**
     * Typen von "Tabellen" (u.a. auch Views), alphabetisch nach Name des Typs sortiert.
     *
     * @return
     */
    public List<String> getTableTypes() {
        final Set<String> tableTypes = new HashSet<>(32);
        for (final TableDescription table : tables) {
            if (table.getTableType() == null
                    || table.getTableType().trim().length() == 0) {
                tableTypes.add(UNKNOWN_TYPE);
            } else {
                tableTypes.add(table.getTableType());
            }
        }
        final List<String> result = new ArrayList<>(tableTypes);
        Collections.sort(result);
        return result;
    }

    /**
     * Tabellen oder Views o.ä. - gefiltert nach dem übergebenen Typ. Das Ergebnis ist
     * sortiert nach Tabellenname.
     *
     * @param type
     * @return
     */
    public List<TableDescription> getTablesByType(final String type) {
        final String wantedType = type == null || type.trim().length() == 0 ? UNKNOWN_TYPE
                : type;
        final List<TableDescription> result = new ArrayList<>(tables.size());
        for (final TableDescription table : getTables()) {
            final String tableType = table.getTableType() == null
                    || table.getTableType().trim().length() == 0 ? UNKNOWN_TYPE
                            : table.getTableType();
            if (wantedType.equals(tableType)) {
                result.add(table);
            }
        }
        Collections.sort(result);
        return result;
    }

    @Override
    protected void appendChild(DatabaseObjectDescription child) {
        if (TableDescription.class.isAssignableFrom(child.getClass())) {
            addTables((TableDescription) child);
        } else {
            super.appendChild(child);
        }
    }
}
