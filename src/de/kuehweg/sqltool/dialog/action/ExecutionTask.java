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
package de.kuehweg.sqltool.dialog.action;

import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.common.sqlediting.StatementString;
import de.kuehweg.sqltool.database.DatabaseConstants;
import de.kuehweg.sqltool.database.execution.StatementExecution;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.component.UpdateableOnStatementExecution;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Task zur Ausführung von SQL-Anweisungen und Aktualisierung der Oberfläche
 *
 * @author Michael Kühweg
 */
public class ExecutionTask extends Task<Void> {

    private final static long UI_UPDATE_INTERVAL_MILLISECONDS = 500;

    private final Statement statement;
    private final String sql;
    private final Collection<UpdateableOnStatementExecution> updateableComponents;
    private long lastTimeUIWasUpdated;

    public ExecutionTask(final Statement statement, final String sql) {
        this.statement = statement;
        this.sql = sql;
        this.updateableComponents = new HashSet<>();
    }

    public void addUpdateableComponents(
            final UpdateableOnStatementExecution... updateables) {
        if (updateables != null) {
            for (UpdateableOnStatementExecution updateable : updateables) {
                updateableComponents.add(updateable);
            }
        }
    }

    public void addUpdateableComponents(
            final Collection<UpdateableOnStatementExecution> updateables) {
        if (updateables != null) {
            updateableComponents.addAll(updateables);
        }
    }

    private void beforeExecution() {
        final BeforeExecutionGuiUpdater updater
                = new BeforeExecutionGuiUpdater(updateableComponents);
        Platform.runLater(updater);
    }

    private void intermediateUpdate(
            final List<StatementExecutionInformation> executionInfos) {
        final IntermediateExecutionGuiUpdater updater
                = new IntermediateExecutionGuiUpdater(executionInfos,
                        updateableComponents);
        Platform.runLater(updater);
    }

    private void afterExecution() {
        final AfterExecutionGuiUpdater updater
                = new AfterExecutionGuiUpdater(updateableComponents);
        Platform.runLater(updater);
    }

    @Override
    protected Void call() throws Exception {
        try {
            final List<StatementString> statements = new StatementExtractor()
                    .getStatementsFromScript(sql);
            if (UserPreferencesManager.getSharedInstance().isLimitMaxRows()) {
                statement.setMaxRows(DatabaseConstants.MAX_ROWS);
            }
            beforeExecution();
            List<StatementExecutionInformation> executionInfos
                    = new ArrayList<>();
            final Iterator<StatementString> queryIterator = statements.
                    iterator();
            while (queryIterator.hasNext() && !isCancelled()) {
                final StatementString singleQuery = queryIterator.next();

                executionInfos.add(new StatementExecution(singleQuery).execute(
                        statement));

                if (System.currentTimeMillis() - lastTimeUIWasUpdated
                        > UI_UPDATE_INTERVAL_MILLISECONDS) {
                    intermediateUpdate(executionInfos);
                    executionInfos.clear();
                    lastTimeUIWasUpdated = System.currentTimeMillis();
                }
            }
            // falls noch Zwischenupdates ausstehen sollten, diese erst abarbeiten
            intermediateUpdate(executionInfos);
            afterExecution();
        } catch (final SQLException ex) {
            final String msg = ex.getLocalizedMessage() + " (SQL-State: "
                    + ex.getSQLState() + ")";
            final ErrorExecutionGuiUpdater updater
                    = new ErrorExecutionGuiUpdater(updateableComponents);
            updater.setErrormessage(msg);
            Platform.runLater(updater);
        }
        return null;
    }

}
