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
package de.kuehweg.sqltool.dialog.component.sqlhistory;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.kuehweg.sqltool.common.DialogDictionary;
import javafx.beans.property.SimpleStringProperty;

/**
 * Einzelner Eintrag in der Historie ausgeführter SQL-Anweisungen.
 *
 * @author Michael Kühweg
 */
public class SqlHistoryEntry {

	private static final int DEFAULT_LENGTH_FOR_SHORT_FORM = 80;
	private static final String ELLIPSIS = "...";

	private final long timestamp;
	private final SimpleStringProperty sqlForDisplay;
	private final String originalSQL;

	/**
	 * @param sql
	 *            SQL Anweisung, für die ein Eintrag im Verlauf erstellt werden
	 *            soll.
	 */
	public SqlHistoryEntry(final String sql) {
		timestamp = System.currentTimeMillis();
		sqlForDisplay = new SimpleStringProperty(prepareSqlForDisplay(sql));
		originalSQL = sql;
	}

	/**
	 * Aufbereitung für die verkürzte, einzeilige Anzeige der ausgeführten
	 * Anweisung.
	 *
	 * @param sql
	 *            Originaltext der Anweisung
	 * @return Einzeiliger Text, auf die maximale Länge
	 *         {@link SqlHistoryEntry#DEFAULT_LENGTH_FOR_SHORT_FORM} gekürzt.
	 */
	private String prepareSqlForDisplay(final String sql) {
		String oneLiner = sql.replace("\n", " ");
		oneLiner = oneLiner.replace("\t", " ");
		if (oneLiner.trim().length() > DEFAULT_LENGTH_FOR_SHORT_FORM) {
			oneLiner = oneLiner.trim().substring(0, DEFAULT_LENGTH_FOR_SHORT_FORM - ELLIPSIS.length()) + ELLIPSIS;
		}
		return oneLiner.trim();
	}

	/**
	 * Verkürzte Variante der ausgeführten SQL-Anweisung (für Übersicht).
	 *
	 * @return Die Kurzform der Anweisung.
	 */
	public String getSqlForDisplay() {
		return sqlForDisplay.get();
	}

	/**
	 * Zeitstempel, wann die SQL-Anweisung in die Historie aufgenommen wurde.
	 * (das ist also NICHT der exakte Ausführungszeitpunkt)
	 *
	 * @return Timestamp als String aufbereitet.
	 */
	public String getTimestampFormatted() {
		final Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timestamp);
		return MessageFormat.format(DialogDictionary.PATTERN_EXECUTION_TIMESTAMP.toString(), cal.getTime());
	}

	/**
	 * Zeitstempel, wann die SQL-Anweisung in die Historie aufgenommen wurde.
	 * (das ist also NICHT der exakte Ausführungszeitpunkt)
	 *
	 * @return Zeitstempel als long Wert.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return Originaltext der SQL Anweisung
	 */
	public String getOriginalSQL() {
		return originalSQL;
	}
}
