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

import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.database.formatter.DefaultTextResultTemplate;
import de.kuehweg.sqltool.database.formatter.ResultTemplate;
import de.kuehweg.sqltool.database.formatter.TextResultFormatter;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecyclePhase;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefresh;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefreshPolicy;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import javafx.scene.control.TextArea;

/**
 * Verlauf der SQL-Ausgaben in Textform
 *
 * @author Michael Kühweg
 */
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.INTERMEDIATE, refreshPolicy
        = ExecutionLifecycleRefreshPolicy.DELAYED)
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.ERROR)
public class QueryResultTextView implements ExecutionTracker {

    private static final int MAX_DBOUTPUT_LENGTH = 256 * 1024;

    private static final String TRUNCATED = "[...]";

    private final ResultTemplate resultTemplate;

    private String dbOutput;

    private final TextArea outputTextArea;

    public QueryResultTextView(final TextArea outputTextArea) {
        super();
        this.outputTextArea = outputTextArea;
        dbOutput = outputTextArea.getText();
        resultTemplate = new DefaultTextResultTemplate();
    }

    private String buildNewContent(final String currentContent, final String appendix) {
        String newContent = "";
        if (currentContent == null) {
            newContent = appendix != null ? appendix : "";
        } else if (appendix == null) {
            newContent = currentContent; // not null ist durch den ersten Zweig schon geklärt
        } else {
            if (appendix.length() >= MAX_DBOUTPUT_LENGTH) {
                newContent = TRUNCATED
                        + appendix.substring(appendix.length() - MAX_DBOUTPUT_LENGTH);
            } else if (currentContent.length() + appendix.length() >= MAX_DBOUTPUT_LENGTH) {
                newContent = TRUNCATED + currentContent.substring(currentContent.length()
                        - (MAX_DBOUTPUT_LENGTH - appendix.length())) + appendix;
            } else {
                newContent = currentContent + appendix;
            }
        }
        return newContent + "\n";
    }

    @Override
    public void beforeExecution() {
    }

    @Override
    public void intermediateUpdate(final StatementExecutionInformation executionInfo) {
        if (executionInfo != null) {
            dbOutput = buildNewContent(dbOutput, new TextResultFormatter(executionInfo).
                    format(resultTemplate));
        }
    }

    @Override
    public void afterExecution() {
    }

    @Override
    public void errorOnExecution(String message) {
        dbOutput = buildNewContent(dbOutput, message);
    }

    @Override
    public void show() {
        outputTextArea.setText(dbOutput);
        outputTextArea.positionCaret(outputTextArea.getLength());
    }

}
