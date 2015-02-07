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
package de.kuehweg.sqltool.database.formatter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.kuehweg.sqltool.common.DialogDictionary;

/**
 * Ergebnisse einer Datenbankabfrage mit grundlegenden
 * Aufbereitungsmöglichkeiten
 * 
 * @author Michael Kühweg
 */
public class ResultFormatter {

	private static final String NULL_STR = "[null]";
	private Object[] header;
	private List<Object[]> rows;
	private boolean initialised;
	private boolean headOnly;
	private String executedBy;
	private Date executedAt;
	private int updateCount;

	private void setHeader(final Object[] headRow) {
		header = Arrays.copyOf(headRow, headRow.length);
	}

	private void addRow(final Object[] row) {
		rows.add(Arrays.copyOf(row, row.length));
	}

	/**
	 * ResultFormatter mit dem Ergebnis einer SQL-Anweisung füllen.
	 * 
	 * @param statement
	 *            Das auszuwertende Statement. Es muss vor dem Aufruf dieser
	 *            Methode bereits ausgeführt worden sein.
	 * @throws SQLException
	 */
	public void fillFromStatementResult(final Statement statement)
			throws SQLException {
		initialised = true;
		setHeader(new String[] { DialogDictionary.LABEL_RESULT_EXECUTED
				.toString() });
		rows = new LinkedList<>();
		executedAt = new Date();
		if (statement == null) {
			return;
		}
		ResultSet resultSet = null;
		try {
			executedBy = statement.getConnection().getMetaData().getUserName()
					+ "@" + statement.getConnection().getMetaData().getURL();
			resultSet = statement.getResultSet();
			updateCount = statement.getUpdateCount();
			if (resultSet == null) {
				headOnly = true;
			} else {
				headOnly = false;
				final ResultSetMetaData metaData = resultSet.getMetaData();
				final int col = metaData.getColumnCount();
				final Object[] headRow = new Object[col];
				for (int i = 1; i <= col; i++) {
					headRow[i - 1] = metaData.getColumnLabel(i);
				}
				setHeader(headRow);
				final Object[] row = new Object[col];
				while (resultSet.next()) {
					for (int i = 1; i <= col; i++) {
						row[i - 1] = resultSet.getObject(i);
						if (resultSet.wasNull()) {
							row[i - 1] = NULL_STR;
						}
					}
					addRow(row);
				}
			}
		} catch (final SQLException ex) {
			setHeader(new String[] { DialogDictionary.LABEL_RESULT_ERROR
					.toString() });
			rows.clear();
		}
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (final SQLException ex) {
			setHeader(new String[] { DialogDictionary.LABEL_RESULT_ERROR
					.toString() });
			addRow(new String[] { DialogDictionary.LABEL_EMPTY.toString() });
		}
	}

	/**
	 * Nicht alle Statements liefern eine echte Ergebnismenge zurück (z.B. DDL)
	 * 
	 * @return
	 */
	public boolean isHeadOnly() {
		return headOnly;
	}

	/**
	 * Zeitstempel, wann die Auswertung des ResultSet durchgeführt wurde (das
	 * ist also NICHT der Zeitpunkt der Ausführung der SQL-Anweisung).
	 * 
	 * @return
	 */
	public Date getExecutedAt() {
		return executedAt != null ? executedAt : new Date();
	}

	/**
	 * Hinweise, wer / welche Datenbankverbindung die SQL-Anweisung ausgeführt
	 * hat.
	 * 
	 * @return
	 */
	public String getExecutedBy() {
		return executedBy != null ? executedBy : "unknown";
	}

	/**
	 * Kopfzeile der Ergebnistabelle als String-Array
	 * 
	 * @return
	 */
	public String[] getHeader() {
		if (!initialised) {
			setHeader(new String[] { DialogDictionary.LABEL_RESULT_ERROR
					.toString() });
			addRow(new String[] { DialogDictionary.LABEL_EMPTY.toString() });
		}
		final String[] result = new String[header.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = header[i].toString();
		}
		return result;
	}

	/**
	 * Ergebniszeilen als Liste (Zeilen) von String-Listen (Spalten).
	 * 
	 * @return
	 */
	public List<List<String>> getRows() {
		if (!initialised) {
			setHeader(new String[] { DialogDictionary.LABEL_RESULT_ERROR
					.toString() });
			addRow(new String[] { DialogDictionary.LABEL_EMPTY.toString() });
		}
		final List<List<String>> result = new ArrayList<>(rows.size());
		if (!headOnly) {
			for (final Object[] row : rows) {
				final List<String> oneRow = new ArrayList<>(header.length);
				for (final Object col : row) {
					oneRow.add(col != null ? col.toString() : "");
				}
				result.add(oneRow);
			}
		}
		return result;
	}

	/**
	 * Update Count analog der JDBC-Rückgabe, d.h. Anzahl der manipulierten
	 * Zeilen oder -1, falls ein ResultSet erzeugt wurde oder keine weiteren
	 * Ergebnisse vorhanden sind
	 * 
	 * @return
	 */
	public int getUpdateCount() {
		return updateCount;
	}
}
