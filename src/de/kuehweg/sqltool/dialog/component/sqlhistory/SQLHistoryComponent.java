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
package de.kuehweg.sqltool.dialog.component.sqlhistory;

import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import de.kuehweg.sqltool.common.sqlediting.SQLHistory;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.control.TableView;

/**
 * Verlauf der ausgeführten SQL-Anweisungen
 *
 * @author Michael Kühweg
 */
public class SQLHistoryComponent implements ExecutionTracker {

    private static final int MAX_HISTORY_ENTRIES = 100;

    private final TableView<SQLHistory> sqlHistory;

    private final List<StatementExecutionInformation> statementBacklog
            = new LinkedList<>();

    public SQLHistoryComponent(final TableView<SQLHistory> sqlHistory) {
        super();
        this.sqlHistory = sqlHistory;
    }

    @Override
    public void beforeExecution() {
        statementBacklog.clear();
    }

    @Override
    public void intermediateUpdate(
            final List<StatementExecutionInformation> executionInfos) {
        statementBacklog.addAll(executionInfos);
    }

    @Override
    public void afterExecution() {
        final List<SQLHistory> historyEntriesFromBacklog = new LinkedList<>();
        for (StatementExecutionInformation info : statementBacklog) {
            if (info.getSql() != null) {
                historyEntriesFromBacklog.add(0, new SQLHistory(info.getSql().
                        uncommentedStatement()));
            }
        }
        sqlHistory.getItems().addAll(0, historyEntriesFromBacklog);
        if (sqlHistory.getItems().size() > MAX_HISTORY_ENTRIES) {
            sqlHistory.getItems().remove(MAX_HISTORY_ENTRIES, sqlHistory.
                    getItems().size());
        }
        statementBacklog.clear();
    }

}
