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
package de.kuehweg.sqltool.dialog.updater;

import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * GUI passend zur Ausführung von SQL-Anweisungen aktualisieren
 *
 * @author Michael Kühweg
 */
public abstract class AbstractExecutionGuiUpdater implements Runnable {

    private final List<StatementExecutionInformation> statementExecutionInformations
            = new ArrayList<>();

    private final Collection<ExecutionTracker> trackers
            = new HashSet<>();

    public AbstractExecutionGuiUpdater(
            final Collection<ExecutionTracker> trackers) {
        super();
        if (trackers != null) {
            this.trackers.addAll(trackers);
        }
    }

    public AbstractExecutionGuiUpdater(
            final List<StatementExecutionInformation> executionInfos,
            final Collection<ExecutionTracker> trackers) {
        this(trackers);
        statementExecutionInformations.addAll(executionInfos);
    }

    public List<StatementExecutionInformation> getStatementExecutionInformations() {
        return statementExecutionInformations;
    }

    protected Collection<ExecutionTracker> getTrackers() {
        return trackers;
    }

    public abstract void update();

    @Override
    public void run() {
        update();
    }

}
