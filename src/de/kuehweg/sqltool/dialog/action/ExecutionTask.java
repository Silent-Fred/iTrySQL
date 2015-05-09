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

import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.common.sqlediting.StatementString;
import de.kuehweg.sqltool.database.execution.StatementExecution;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.updater.AbstractExecutionGuiUpdater;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import de.kuehweg.sqltool.dialog.updater.GuiUpdaterProviderI;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    private final GuiUpdaterProviderI guiUpdaterProvider;
    private final Collection<ExecutionTracker> trackers;
    private int maxRows;
    private long lastTimeUIWasUpdated;

    public ExecutionTask(final Statement statement, final String sql,
            final GuiUpdaterProviderI guiUpdaterProvider) {
        this.statement = statement;
        this.sql = sql;
        this.guiUpdaterProvider = guiUpdaterProvider;
        this.trackers = new HashSet<>();
    }

    public void setLimitMaxRows(final int maxRows) {
        this.maxRows = maxRows;
    }

    public void attach(final ExecutionTracker... trackers) {
        if (trackers != null) {
            for (ExecutionTracker tracker : trackers) {
                this.trackers.add(tracker);
            }
        }
    }

    public void attach(final Collection<ExecutionTracker> trackers) {
        if (trackers != null) {
            this.trackers.addAll(trackers);
        }
    }

    private void beforeExecution() {
        if (guiUpdaterProvider != null) {
            AbstractExecutionGuiUpdater updater = guiUpdaterProvider.
                    beforeExecutionGuiUpdater(trackers);
            if (updater != null) {
                updater.show();
            }
        }
    }

    private void intermediateUpdate(
            final List<StatementExecutionInformation> executionInfos) {
        if (guiUpdaterProvider != null) {
            AbstractExecutionGuiUpdater updater = guiUpdaterProvider.
                    intermediateExecutionGuiUpdater(executionInfos, trackers);
            if (updater != null) {
                updater.show();
            }
        }
    }

    private void afterExecution() {
        if (guiUpdaterProvider != null) {
            AbstractExecutionGuiUpdater updater = guiUpdaterProvider.
                    afterExecutionGuiUpdater(trackers);
            if (updater != null) {
                updater.show();
            }
        }
    }

    private void intermediateUpdateInIntervals(
            final List<StatementExecutionInformation> executionInfos) {
        // zu häufige Updates der Oberfläche bei vielen, kurzen Anweisungen
        // blockieren die UI. Daher nur in regelmäßigen Abständen aktualisieren.
        if (System.currentTimeMillis() - lastTimeUIWasUpdated
                > UI_UPDATE_INTERVAL_MILLISECONDS) {
            intermediateUpdate(executionInfos);
            executionInfos.clear();
            lastTimeUIWasUpdated = System.currentTimeMillis();
        }
    }

    private void errorUpdate(final String message) {
        if (guiUpdaterProvider != null) {
            final AbstractExecutionGuiUpdater updater
                    = guiUpdaterProvider.errorExecutionGuiUpdater(message, trackers);
            if (updater != null) {
                updater.show();
            }
        }
    }

    @Override
    protected Void call() throws Exception {
        try {
            final List<StatementString> statements = new StatementExtractor()
                    .getStatementsFromScript(sql);
            statement.setMaxRows(maxRows);
            beforeExecution();
            List<StatementExecutionInformation> executionInfos
                    = new ArrayList<>();
            final Iterator<StatementString> queryIterator = statements.
                    iterator();
            while (queryIterator.hasNext() && !isCancelled()) {
                final StatementString singleQuery = queryIterator.next();

                if (!singleQuery.isEmpty()) {
                    executionInfos.add(new StatementExecution(singleQuery).
                            execute(statement));
                }
                intermediateUpdateInIntervals(executionInfos);
            }
            // falls noch Zwischenupdates ausstehen sollten, diese abarbeiten
            intermediateUpdate(executionInfos);
            // dann abschließen
            afterExecution();
        } catch (final SQLException ex) {
            errorUpdate(
                    ex.getLocalizedMessage() + " (SQL-State: "
                    + ex.getSQLState() + ")");
        }
        return null;
    }

}
