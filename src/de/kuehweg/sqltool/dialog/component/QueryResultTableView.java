/*
 * Copyright (c) 2013-2016, Michael Kühweg
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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.ResultHeader;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.database.execution.StatementResult;
import de.kuehweg.sqltool.database.formatter.DefaultHtmlResultTemplate;
import de.kuehweg.sqltool.database.formatter.HtmlResultFormatter;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecyclePhase;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefresh;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;

/**
 * Baustein für Ergebnistabelle.
 *
 * @author Michael Kühweg
 */
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.BEFORE)
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.AFTER)
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.ERROR)
public class QueryResultTableView implements ExecutionTracker {

	public static final String RESULT_TABLE_ID = "useMeToFetchTheResultFormatter";

	private static final int MIN_COLUMN_WIDTH_MULTIPLE_COLUMNS = 111;

	private static final int MIN_COLUMN_WIDTH_SINGLE_COLUMN = 256;

	private static final int MAX_ROWS_IN_VIEW = 5000;

	private final Tooltip maxRowsTooltip;

	private final TableView<ObservableList<String>> tableView;
	private StatementExecutionInformation infoToView;
	private String errorMessage;

	public QueryResultTableView(final TableView<ObservableList<String>> tableView) {
		this.tableView = tableView;
		maxRowsTooltip = new Tooltip(
				MessageFormat.format(DialogDictionary.PATTERN_MAX_ROWS_IN_TABLE_VIEW.toString(), MAX_ROWS_IN_VIEW));
	}

	public String toHtml() {
		if (infoToView == null) {
			return "";
		}
		return new HtmlResultFormatter(infoToView).format(new DefaultHtmlResultTemplate());
	}

	private ObservableList<TableColumn<ObservableList<String>, String>>
			buildTableViewHeaderWithColumnNameList(final List<String> columnHeaders) {
		final ObservableList<TableColumn<ObservableList<String>, String>> header = FXCollections.observableArrayList();
		int i = 0;
		for (final String head : columnHeaders) {
			final TableColumn<ObservableList<String>, String> col = new TableColumn<>(head);
			col.setMinWidth(
					columnHeaders.size() == 1 ? MIN_COLUMN_WIDTH_SINGLE_COLUMN : MIN_COLUMN_WIDTH_MULTIPLE_COLUMNS);
			final int accessIndex = i++;
			col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(accessIndex).toString()));
			header.add(col);
		}
		return header;
	}

	private ObservableList<ObservableList<String>>
			buildContentWithoutResultSet(final StatementExecutionInformation info) {
		final ObservableList<ObservableList<String>> content = FXCollections.observableArrayList();
		if (info != null && info.getStatementResult() == null) {
			final ObservableList<String> row = FXCollections.observableArrayList();
			row.add(info.getSummary());
			content.add(row);
		}
		return content;
	}

	private ObservableList<ObservableList<String>> buildContentWithResultSet(final StatementExecutionInformation info) {
		final ObservableList<ObservableList<String>> content = FXCollections.observableArrayList();
		if (info != null && info.getStatementResult() != null) {
			final int upperBound = Math.min(info.getStatementResult().getRows().size(), MAX_ROWS_IN_VIEW);
			if (upperBound == MAX_ROWS_IN_VIEW) {
				Tooltip.install(tableView, maxRowsTooltip);
			}
			for (final ResultRow resultRow : info.getStatementResult().getRows().subList(0, upperBound)) {
				final ObservableList<String> row = FXCollections.observableArrayList();
				for (final String column : resultRow.columnsAsString()) {
					row.add(column);
				}
				content.add(row);
			}
		}
		return content;
	}

	private ObservableList<ObservableList<String>> buildContentWithErrorMessage(final String errorMessage) {
		final ObservableList<ObservableList<String>> content = FXCollections.observableArrayList();
		final ObservableList<String> row = FXCollections.observableArrayList();
		row.add(errorMessage);
		content.add(row);
		return content;
	}

	private void prepareBuildView() {
		tableView.getColumns().clear();
		tableView.getItems().clear();
		tableView.setTableMenuButtonVisible(false);
		Tooltip.uninstall(tableView, maxRowsTooltip);
	}

	private void buildView(final StatementExecutionInformation info) {
		prepareBuildView();
		if (info != null) {
			if (info.getStatementResult() != null) {
				tableView.getColumns().addAll(buildTableViewHeaderWithColumnNameList(
						Arrays.asList(info.getStatementResult().getHeader().getColumnHeaders())));
				tableView.setItems(buildContentWithResultSet(info));
				tableView.setTableMenuButtonVisible(true);
			} else {
				tableView.getColumns().addAll(buildTableViewHeaderWithColumnNameList(
						Collections.singletonList(DialogDictionary.LABEL_RESULT_EXECUTED.toString())));
				tableView.setItems(buildContentWithoutResultSet(info));
			}
		}
	}

	private void buildError(final String errorMessage) {
		prepareBuildView();
		tableView.getColumns().addAll(buildTableViewHeaderWithColumnNameList(
				Collections.singletonList(DialogDictionary.LABEL_EXECUTION_ERROR.toString())));
		tableView.setItems(buildContentWithErrorMessage(errorMessage));
	}

	@Override
	public void beforeExecution() {
		final StatementExecutionInformation info = new StatementExecutionInformation();
		info.setStatementResult(new StatementResult());
		info.getStatementResult().setHeader(new ResultHeader(DialogDictionary.LABEL_EXECUTING.toString()));
		// eventuellen alten Inhalt verwerfen
		infoToView = null;
		errorMessage = null;
	}

	@Override
	public void intermediateUpdate(final StatementExecutionInformation executionInfo) {
		if (executionInfo != null) {
			infoToView = executionInfo;
		}
		errorMessage = null;
	}

	@Override
	public void afterExecution() {
		if (infoToView == null) {
			infoToView = new StatementExecutionInformation();
			infoToView.setStatementResult(new StatementResult());
			infoToView.getStatementResult()
					.setHeader(new ResultHeader(DialogDictionary.LABEL_RESULT_EXECUTED.toString()));
		}
		errorMessage = null;
	}

	@Override
	public void errorOnExecution(final String message) {
		infoToView = null;
		errorMessage = message;
	}

	@Override
	public void show() {
		if (errorMessage != null) {
			buildError(errorMessage);
		} else {
			buildView(infoToView);
		}
	}
}
