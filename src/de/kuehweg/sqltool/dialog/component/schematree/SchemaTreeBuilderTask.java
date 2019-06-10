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

import java.sql.Connection;

import de.kuehweg.sqltool.database.metadata.MetaDataReader;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TreeView;

/**
 * Strukturansicht aktualisieren. Als Task implementiert, da das Auslesen der Metadaten
 * vergleichsweise lang dauern kann (könnte) und die Oberfläche zwischenzeitlich nicht
 * blockiert sein soll.
 *
 * @author Michael Kühweg
 */
public class SchemaTreeBuilderTask extends Task<Void> {

    private final Connection connection;
    private final TreeView<String> treeToUpdate;

    public SchemaTreeBuilderTask(final Connection connection,
            final TreeView<String> treeToUpdate) {
        this.connection = connection;
        this.treeToUpdate = treeToUpdate;
    }

    @Override
    protected Void call() throws Exception {
        final DatabaseDescription db = connection != null ? new MetaDataReader().
                readMetaData(connection) : new DatabaseDescription();
        Platform.runLater(new SchemaTreeBuilder(db, treeToUpdate));
        return null;
    }
}
