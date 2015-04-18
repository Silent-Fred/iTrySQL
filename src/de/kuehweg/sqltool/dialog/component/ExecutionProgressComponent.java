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

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * Komponente zur Anzeige des Fortschritts beim Ausführen von SQL-Anweisungen
 *
 * @author Michael Kühweg
 */
public class ExecutionProgressComponent implements
        ExecutionTracker {

    private static final double PROGRESS_RUNNING = -1;
    private static final double PROGRESS_FINISHED = 1;

    private final ProgressBar progressBar;
    private final Label executionDuration;

    private long startOfExecution;
    private long endOfExecution;

    public ExecutionProgressComponent(final ProgressBar progressBar,
            final Label executionDuration) {
        this.progressBar = progressBar;
        this.executionDuration = executionDuration;
    }

    @Override
    public void beforeExecution() {
        startOfExecution = System.currentTimeMillis();
        progressBar.setProgress(PROGRESS_RUNNING);
        executionDuration.setText(DialogDictionary.LABEL_EXECUTING.toString());
    }

    @Override
    public void intermediateUpdate(
            final List<StatementExecutionInformation> executionInfos) {
		// derzeit keine Updates während Ausführung - ist als "laufend"
		// visualisiert
    }

    @Override
    public void afterExecution() {
        endOfExecution = System.currentTimeMillis();
        progressBar.setProgress(PROGRESS_FINISHED);
        final BigDecimal executionTimeInSeconds = BigDecimal.valueOf(
                endOfExecution - startOfExecution).divide(
                        BigDecimal.valueOf(1000));
        executionDuration.setText(MessageFormat.format(
                DialogDictionary.PATTERN_EXECUTION_TIME.toString(),
                executionTimeInSeconds.toString()));
    }
}
