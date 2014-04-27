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

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.ProvidedAudioClip;
import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.database.formatter.ResultFormatter;
import de.kuehweg.sqltool.database.formatter.TextResultFormatter;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.component.QueryResultTableView;
import de.kuehweg.sqltool.dialog.environment.ExecutionProgressEnvironment;
import de.kuehweg.sqltool.dialog.environment.ExecutionResultEnvironment;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Benutzeroberfläche zum Abschluss einer SQL-Anweisung aktualisieren
 *
 * @author Michael Kühweg
 */
public class ExecutionGUIUpdater implements Runnable {

    public static final String RESULT_TABLE_ID =
            "useMeToFetchTheResultFormatter";
    private static final int MAX_DBOUTPUT = 1024 * 1024;
    private final ExecutionProgressEnvironment progress;
    private final ExecutionResultEnvironment result;
    private final long startTime;
    private long executionTimeInMilliseconds;
    private String errorThatOccurred;
    private ResultFormatter resultFormatter;
    private boolean intermediateUpdate;
    private String sqlForHistory;

    public ExecutionGUIUpdater(final ExecutionProgressEnvironment progress,
            final ExecutionResultEnvironment result) {
        this.progress = progress;
        this.result = result;
        startTime = System.currentTimeMillis();
    }

    public ExecutionProgressEnvironment getProgress() {
        return progress;
    }

    public ExecutionResultEnvironment getResult() {
        return result;
    }

    @Override
    public void run() {
        updateScene();
    }

    public void setExecutionTimeInMilliseconds(
            final long executionTimeInMilliseconds) {
        this.executionTimeInMilliseconds = executionTimeInMilliseconds;
    }

    public void setErrorThatOccurred(final String errorThatOccurred) {
        this.errorThatOccurred = errorThatOccurred;
    }

    public void setResultFormatter(final ResultFormatter resultFormatter) {
        this.resultFormatter = resultFormatter;
    }

    /**
     * Zeigt an, ob es sich um ein Zwischenupdate der Oberfläche handelt und die
     * Ausführung danach noch weiter geht, oder ob die Ausführung beendet ist.
     *
     * @param intermediateUpdate true - Zwischenupdate / false - Abschluss
     */
    public void setIntermediateUpdate(final boolean intermediateUpdate) {
        this.intermediateUpdate = intermediateUpdate;
    }

    public void setSqlForHistory(final String sqlForHistory) {
        this.sqlForHistory = sqlForHistory;
    }

    private void updateScene() {
        if (executionTimeInMilliseconds == 0) {
            executionTimeInMilliseconds = System.currentTimeMillis()
                    - startTime;
        }
        if (intermediateUpdate) {
            // Zwischenupdate - es geht weiter
            progress.prepareSceneRunning();
        } else {
            // Abschluss markieren
            progress.showFinished(executionTimeInMilliseconds);
        }
        if (errorThatOccurred == null) {
            result.getHistoryKeeper().addExecutedSQLToHistory(sqlForHistory);
            if (resultFormatter != null) {
                final TextArea dbOutput = result.getDbOutput();
                if (dbOutput != null) {
                    final String currentResult = new TextResultFormatter(
                            resultFormatter).formatAsText();
                    if (dbOutput.getText().length() + currentResult.length()
                            < MAX_DBOUTPUT) {
                        dbOutput.appendText(currentResult);
                    } else {
                        int howMuchFromOld = MAX_DBOUTPUT
                                - currentResult.length();
                        howMuchFromOld = howMuchFromOld < 0 ? 0
                                : howMuchFromOld;
                        int startInOld = dbOutput.getText().length()
                                - howMuchFromOld - 1;
                        startInOld = startInOld < 0 ? 0 : startInOld;
                        final String old = dbOutput.getText(startInOld,
                                dbOutput.getText().length());
                        dbOutput.setText(old + currentResult);
                    }
                    dbOutput.appendText("\n");
                }
                buildTableViewFromResultFormatter();
            }
            result.focusResult();
            finalBeep();
        } else {
            final ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    errorThatOccurred,
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
    }

    private void buildTableViewFromResultFormatter() {
        final HBox resultTableContainer = result.getResultTableContainer();
        resultTableContainer.getChildren().clear();
        final QueryResultTableView dynamicTableView = new QueryResultTableView(
                resultFormatter);
        dynamicTableView.setId(RESULT_TABLE_ID);
        dynamicTableView.buildTableView();
        resultTableContainer.getChildren().add(dynamicTableView);
        HBox.setHgrow(dynamicTableView, Priority.ALWAYS);
    }

    private void finalBeep() {
        if (!intermediateUpdate) {
            ProvidedAudioClip audioClip = UserPreferencesManager.
                    getSharedInstance().getBeepAudioClip();
            if (audioClip != null) {
                double volume = UserPreferencesManager.getSharedInstance().
                        getBeepVolume();
                if (volume > 0.0) {
                    audioClip.play(volume);
                }
            }
        }
    }
}
