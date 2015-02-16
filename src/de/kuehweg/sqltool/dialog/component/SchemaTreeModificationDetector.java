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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.component.schematree.SchemaTreeBuilderTask;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.TreeView;

/**
 * Komponente um zu erkennen, ob sich durch SQL-Anweisungen die
 * Datenbankstruktur verändert hat
 *
 * @author Michael Kühweg
 */
public class SchemaTreeModificationDetector implements
        ExecutionTracker {

    private boolean ddlDetected;
    private final TreeView<String> schemaTree;
    private final Connection attachedToConnection;

    public SchemaTreeModificationDetector(final TreeView<String> schemaTree,
            final Connection connection) {
        this.schemaTree = schemaTree;
        this.attachedToConnection = connection;
    }

    @Override
    public void beforeExecution() {
        ddlDetected = false;
    }

    @Override
    public void intermediateUpdate(
            List<StatementExecutionInformation> executionInfos) {
        if (executionInfos != null) {
            Iterator<StatementExecutionInformation> infoIterator
                    = executionInfos.iterator();
            while (!ddlDetected && infoIterator.hasNext()) {
                StatementExecutionInformation info = infoIterator.next();
                ddlDetected = ddlDetected || info.getSql() != null && info.
                        getSql().isDataDefinitionStatement();
            }
        }
    }

    @Override
    public void afterExecution() {
        if (ddlDetected) {
            final Task<Void> refreshTask = new SchemaTreeBuilderTask(
                    attachedToConnection, schemaTree);
            final Thread th = new Thread(refreshTask);
            th.setDaemon(true);
            th.start();
        }
    }

}
