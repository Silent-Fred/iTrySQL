/*
 * Copyright (c) 2016, Michael Kühweg
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

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

/**
 * Suche in Tabellenkomponenten.
 *
 * @author Michael Kühweg
 */
public class TableViewFindAction extends FindAction {

	private final TableView<ObservableList<String>> tableView;

	private ObservableList<String> lastFoundRow;

	/**
	 * @param tableView
	 *            Unterstützt derzeit nur den für Abfrageergebnisse verwendeten
	 *            Tabellentyp.
	 */
	public TableViewFindAction(final TableView<ObservableList<String>> tableView) {
		this.tableView = tableView;
	}

	@Override
	public void find(final String searchString) {
		resetSearchPosition();
		tableView.getSelectionModel().clearSelection();
		nextOccurrence(searchString);
	}

	private boolean rowMatchesString(final ObservableList<String> row, final String preparedSearchString) {
		for (final String column : row) {
			if (column != null && column.toLowerCase().contains(preparedSearchString)) {
				return true;
			}
		}
		return false;
	}

	private ObservableList<String> findRowBeforeLastFinding(final String preparedSearchString,
			final ObservableList<String> lastFinding, final SortedList<ObservableList<String>> tableRows) {
		ObservableList<String> finding = null;
		for (final ObservableList<String> row : tableRows) {
			if (row == lastFinding) {
				break;
			} else if (rowMatchesString(row, preparedSearchString)) {
				finding = row;
			}
		}
		return finding;
	}

	private ObservableList<String> findRowAfterLastFinding(final String preparedSearchString,
			final ObservableList<String> lastFinding, final SortedList<ObservableList<String>> tableRows) {
		ObservableList<String> finding = null;
		boolean passed = lastFinding == null;
		for (final ObservableList<String> row : tableRows) {
			if (passed && rowMatchesString(row, preparedSearchString)) {
				finding = row;
				break;
			}
			passed = passed || row == lastFinding;
		}
		return finding;
	}

	@Override
	public void nextOccurrence(final String searchString) {
		final ObservableList<String> finding = findRowAfterLastFinding(preparedSearchString(searchString),
				getRowOfLastRememberedFinding(), sortedTableItems());
		rememberAndSelectFinding(finding);
	}

	@Override
	public void previousOccurrence(final String searchString) {
		final ObservableList<String> finding = findRowBeforeLastFinding(preparedSearchString(searchString),
				getRowOfLastRememberedFinding(), sortedTableItems());
		rememberAndSelectFinding(finding);
	}

	/**
	 * @param finding
	 *            Die Fundstelle, die sich die FindAction als letzten Treffer
	 *            merken soll und die in der TableView selektiert wird.
	 */
	private void rememberAndSelectFinding(final ObservableList<String> finding) {
		if (finding != null) {
			tableView.getSelectionModel().clearSelection();
			tableView.getSelectionModel().select(finding);
			tableView.scrollTo(finding);
			rememberRowAsLastFinding(finding);
		}
	}

	/**
	 * @return Inhalt der TableView als sortierte Liste gemäß den aktuell in der
	 *         TableView eingestellten Sortierkriterien.
	 */
	private SortedList<ObservableList<String>> sortedTableItems() {
		final SortedList<ObservableList<String>> sorted = new SortedList<>(tableView.getItems());
		sorted.setComparator(tableView.getComparator());
		return sorted;
	}

	@Override
	public void resetSearchPosition() {
		lastFoundRow = null;
	}

	private void rememberRowAsLastFinding(final ObservableList<String> rowOfCurrentFinding) {
		lastFoundRow = rowOfCurrentFinding;
	}

	private ObservableList<String> getRowOfLastRememberedFinding() {
		return lastFoundRow;
	}

}
