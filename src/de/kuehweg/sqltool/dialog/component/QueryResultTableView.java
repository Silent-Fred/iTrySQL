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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.ResultHeader;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.database.execution.StatementResult;
import de.kuehweg.sqltool.database.formatter.HtmlResultFormatter;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

/**
 * Baustein für Ergebnistabelle.
 *
 * @author Michael Kühweg
 */
public class QueryResultTableView implements UpdateableOnStatementExecution {

    public static final String RESULT_TABLE_ID
            = "useMeToFetchTheResultFormatter";

    private final HBox parentContainer;
    private TableView<ObservableList<String>> tableView;
    private StatementExecutionInformation infoToView;

    public QueryResultTableView(final HBox parentContainer) {
        super();
        this.parentContainer = parentContainer;
    }

    public String toHtml() {
        if (infoToView == null) {
            return "";
        }
        return new HtmlResultFormatter(infoToView).formatAsHtml();
    }

    private void buildViewWithoutResultSet(
            final StatementExecutionInformation info) {
        if (info != null && info.getStatementResult() == null) {
            tableView.getColumns().clear();
            tableView.getItems().clear();
            final TableColumn<ObservableList<String>, String> col
                    = new TableColumn<>(
                            DialogDictionary.LABEL_RESULT_EXECUTED.toString());
            col.setMinWidth(200);
            col.setCellValueFactory(
                    new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(
                                final CellDataFeatures<ObservableList<String>, String> param) {
                                    return new SimpleStringProperty(param.
                                            getValue()
                                            .get(0).toString());
                                }
                    });
            tableView.getColumns().add(col);

            final ObservableList<ObservableList<String>> content = FXCollections
                    .observableArrayList();
            final ObservableList<String> row = FXCollections
                    .observableArrayList();
            row.add(info.getSummary());
            content.add(row);
            tableView.setItems(content);
        }
    }

    private void buildViewWithResultSet(final StatementExecutionInformation info) {
        if (info != null && info.getStatementResult() != null) {
            tableView.getColumns().clear();
            tableView.getItems().clear();
            int i = 0;
            for (final String head : info.getStatementResult().getHeader().
                    getColumnHeaders()) {
                final TableColumn<ObservableList<String>, String> col
                        = new TableColumn<>(
                                head);
                col.setMinWidth(100);
                final int accessIndex = i++;
                col.setCellValueFactory(
                        new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(
                                    final CellDataFeatures<ObservableList<String>, String> param) {
                                        return new SimpleStringProperty(param.
                                                getValue()
                                                .get(accessIndex).toString());
                                    }
                        });
                tableView.getColumns().add(col);
            }
            final ObservableList<ObservableList<String>> content = FXCollections
                    .observableArrayList();
            for (final ResultRow resultRow : info.getStatementResult().getRows()) {
                final ObservableList<String> row = FXCollections
                        .observableArrayList();
                for (final String column : resultRow.columnsAsString()) {
                    row.add(column);
                }
                content.add(row);
            }
            tableView.setItems(content);
        }
    }

    private void buildView(final StatementExecutionInformation info) {
        tableView = new TableView<>();
        tableView.setId(RESULT_TABLE_ID);
        HBox.setHgrow(tableView, Priority.ALWAYS);

        if (info != null) {
            if (info.getStatementResult() != null) {
                buildViewWithResultSet(info);
            } else {
                buildViewWithoutResultSet(info);
            }
        }
        parentContainer.getChildren().clear();
        parentContainer.getChildren().add(tableView);
    }

    @Override
    public void beforeExecution() {
        StatementExecutionInformation info = new StatementExecutionInformation();
        info.setStatementResult(new StatementResult());
        info.getStatementResult().setHeader(new ResultHeader(
                DialogDictionary.LABEL_EXECUTING.
                toString()));
        buildView(info);
        // eventuellen alten Inhalt verwerfen
        infoToView = null;
    }

    @Override
    public void intermediateUpdate(
            final List<StatementExecutionInformation> executionInfos) {
        if (executionInfos != null && !executionInfos.isEmpty()) {
            infoToView = executionInfos.get(executionInfos.size() - 1);
        }
    }

    @Override
    public void afterExecution() {
        if (infoToView == null) {
            infoToView = new StatementExecutionInformation();
            infoToView.setStatementResult(new StatementResult());
            infoToView.getStatementResult().setHeader(new ResultHeader(
                    DialogDictionary.LABEL_RESULT_EXECUTED.
                    toString()));
        }
        buildView(infoToView);
    }
}
