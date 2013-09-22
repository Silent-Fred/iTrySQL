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

import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.database.DatabaseConstants;
import de.kuehweg.sqltool.database.ResultFormatter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author Michael Kühweg
 */
public class ExecutionTask extends Task<Void> {

    private final Statement statement;
    private final String sql;
    private final ExecutionGUIUpdater guiUpdater;

    public ExecutionTask(final Statement statement, final String sql,
            final ExecutionGUIUpdater guiUpdater) {
        this.statement = statement;
        this.sql = sql;
        this.guiUpdater = guiUpdater;
    }

    @Override
    protected Void call() throws Exception {
        try {
            final long timing = System.currentTimeMillis();
            final List<String> statements = StatementExtractor
                    .getStatementsFromScript(sql);
            if (UserPreferencesManager.getSharedInstance().isLimitMaxRows()) {
                statement.setMaxRows(DatabaseConstants.MAX_ROWS);
            }
            final Iterator<String> queryIterator = statements.iterator();
            while (queryIterator.hasNext() && !isCancelled()) {
                final String singleQuery = queryIterator.next();
                statement.execute(singleQuery);
                if (guiUpdater.isSilent()) {
                    continue;
                }
                final ResultFormatter resultFormatter = new ResultFormatter();
                resultFormatter.fillFromStatementResult(statement);
                // Datenbankzugriff und Aufbereitung der Daten werden zusammen
                // in die Laufzeit eingerechnet
                // Zwischenstand ausgeben
                if (guiUpdater != null) {
                    final ExecutionGUIUpdater intermediateResultUpdater =
                            new ExecutionGUIUpdater(
                            guiUpdater.getSceneToUpdate(),
                            guiUpdater.getHistoryKeeper());
                    intermediateResultUpdater
                            .setExecutionTimeInMilliseconds(System
                            .currentTimeMillis() - timing);
                    intermediateResultUpdater
                            .setResultFormatter(resultFormatter);
                    intermediateResultUpdater.setIntermediateUpdate(true);
                    intermediateResultUpdater.setSqlForHistory(singleQuery);
                    Platform.runLater(intermediateResultUpdater);
                }
            }
        } catch (final SQLException ex) {
            final String msg = ex.getLocalizedMessage() + " (SQL-State: "
                    + ex.getSQLState() + ")";
            if (guiUpdater != null) {
                guiUpdater.setErrorThatOccurred(msg);
            }
        } finally {
            if (guiUpdater != null) {
                if (guiUpdater.isSilent()) {
                    guiUpdater.setSqlForHistory(null);
                    final ResultFormatter resultFormatter =
                            new ResultFormatter();
                    resultFormatter.fillFromStatementResult(null);
                    guiUpdater.setResultFormatter(resultFormatter);
                }
                guiUpdater.setIntermediateUpdate(false);
                Platform.runLater(guiUpdater);
            }
        }
        return null;
    }
}
