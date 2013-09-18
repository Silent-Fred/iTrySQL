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
import de.kuehweg.sqltool.common.sqlediting.SQLHistoryKeeper;
import de.kuehweg.sqltool.database.ResultFormatter;
import de.kuehweg.sqltool.database.TextResultFormatter;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

/**
 *
 * @author Michael Kühweg
 */
public class ExecutionGUIUpdater implements Runnable {

    private static final int MAX_DBOUTPUT = 1024 * 1024;
    private Scene sceneToUpdate;
    private final SQLHistoryKeeper historyKeeper;
    private final long startTime;
    private long executionTimeInMilliseconds;
    private String errorThatOccurred;
    private ResultFormatter resultFormatter;
    private boolean intermediateUpdate;
    private String sqlForHistory;
    private boolean silent;

    public ExecutionGUIUpdater(final Scene sceneToUpdate, final SQLHistoryKeeper historyKeeper) {
        this.sceneToUpdate = sceneToUpdate;
        this.historyKeeper = historyKeeper;
        startTime = System.currentTimeMillis();
    }

    public Scene getSceneToUpdate() {
        return sceneToUpdate;
    }

    public SQLHistoryKeeper getHistoryKeeper() {
        return historyKeeper;
    }

    @Override
    public void run() {
        updateScene();
    }

    public void setExecutionTimeInMilliseconds(final long executionTimeInMilliseconds) {
        this.executionTimeInMilliseconds = executionTimeInMilliseconds;
    }

    public void setErrorThatOccurred(final String errorThatOccurred) {
        this.errorThatOccurred = errorThatOccurred;
    }

    public void setResultFormatter(final ResultFormatter resultFormatter) {
        this.resultFormatter = resultFormatter;
    }

    public void setIntermediateUpdate(final boolean intermediateUpdate) {
        this.intermediateUpdate = intermediateUpdate;
    }

    public void setSqlForHistory(final String sqlForHistory) {
        this.sqlForHistory = sqlForHistory;
    }

    public void setSilent(final boolean silent) {
        this.silent = silent;
    }
    
    public boolean isSilent() {
        return silent;
    }
    
    private void updateScene() {
        if (executionTimeInMilliseconds == 0) {
            executionTimeInMilliseconds = System.currentTimeMillis() - startTime;
        }
        if (intermediateUpdate) {
            ActionVisualisation.prepareSceneRunning(sceneToUpdate);
        } else {
            ActionVisualisation.showFinished(sceneToUpdate, executionTimeInMilliseconds);
        }
        if (errorThatOccurred == null) {
            historyKeeper.addExecutedSQLToHistory(sqlForHistory);
            if (resultFormatter != null) {
                TextArea dbOutput = (TextArea) sceneToUpdate.lookup("#dbOutput");
                if (dbOutput != null) {
                    String currentResult = MessageFormat.format(DialogDictionary.PATTERN_EXECUTION_TIMESTAMP.toString(), new Date())
                            + "\n\n"
                            + new TextResultFormatter(resultFormatter).formatAsText();
                    if (dbOutput.getText().length() + currentResult.length() < MAX_DBOUTPUT) {
                        dbOutput.appendText(currentResult);
                    } else {
                        int howMuchFromOld = MAX_DBOUTPUT - currentResult.length();
                        howMuchFromOld = howMuchFromOld < 0 ? 0 : howMuchFromOld;
                        int startInOld = dbOutput.getText().length() - howMuchFromOld - 1;
                        startInOld = startInOld < 0 ? 0 : startInOld;
                        String old = dbOutput.getText(startInOld, dbOutput.getText().length());
                        dbOutput.setText(old + currentResult);
                    }
                    dbOutput.appendText("\n");
                }
                buildTableViewFromResultFormatter();
            }
        } else {
            ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    errorThatOccurred,
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
    }

    private void buildTableViewFromResultFormatter() {
        HBox resultTableContainer = (HBox) sceneToUpdate.lookup("#resultTableContainer");
        resultTableContainer.getChildren().clear();
        TableView<ObservableList> dynamicTableView = new TableView<>();
        int i = 0;
        for (final String head : resultFormatter.getHeader()) {
            TableColumn col = new TableColumn(head);
            col.setMinWidth(100);
            final int accessIndex = i++;
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(accessIndex).toString());
                }
            });
            dynamicTableView.getColumns().add(col);
        }
        ObservableList<ObservableList> content = FXCollections.observableArrayList();
        for (final List<String> rowFromFormatter : resultFormatter.getRows()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (String column : rowFromFormatter) {
                row.add(column);
            }
            content.add(row);
        }

        dynamicTableView.setItems(content);

        resultTableContainer.getChildren()
                .add(dynamicTableView);
        HBox.setHgrow(dynamicTableView, Priority.ALWAYS);
    }
}
