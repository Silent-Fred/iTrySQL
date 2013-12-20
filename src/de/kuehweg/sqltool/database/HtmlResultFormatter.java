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
package de.kuehweg.sqltool.database;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.common.UserPreferencesManager;

/**
 * Abfrageergebnis als HTML aufbereitet
 * 
 * @author Michael Kühweg
 */
public class HtmlResultFormatter {

	private final ResultFormatter resultFormatter;

	/**
	 * Im Konstruktor wird das Abfrageergebnis übergeben
	 * 
	 * @param resultFormatter
	 */
	public HtmlResultFormatter(final ResultFormatter resultFormatter) {
		this.resultFormatter = resultFormatter;
	}

	/**
	 * Formatiert einen Text als Tabellenüberschrift.
	 * 
	 * @param value
	 *            Text wird in HTML Format gewandelt, muss also noch
	 *            "naturbelassen" sein
	 * @return
	 */
	private String formatAsTableHeader(final String value) {
		return new StringBuilder().append("<th>")
				.append(StringEscapeUtils.escapeHtml4(value)).append("</th>")
				.toString();
	}

	/**
	 * Formatiert einen Text als Tabellenzelle
	 * 
	 * @param value
	 *            Text wird in HTML Format gewandelt, muss also noch
	 *            "naturbelassen" sein
	 * @return
	 */
	private String formatAsTableData(final String value) {
		return new StringBuilder().append("<td>")
				.append(StringEscapeUtils.escapeHtml4(value)).append("</td>")
				.toString();
	}

	/**
	 * Formatiert eine Liste von Texten als Tabellenzeile
	 * 
	 * @param columns
	 *            Liste von Texten, die in HTML umgewandelt werden
	 * @return
	 */
	private String formatAsTableRow(final List<String> columns) {
		final StringBuilder builder = new StringBuilder("<tr>");
		for (final String column : columns) {
			builder.append(formatAsTableData(column));
		}
		builder.append("</tr>\n");
		return builder.toString();
	}

	/**
	 * Erzeugt den Tabellenkopf
	 * 
	 * @return Tabellenkopf als String
	 */
	private String formatTableHeader() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<thead><tr>");
		// Spaltenüberschriften aufbauen
		for (final String header : resultFormatter.getHeader()) {
			builder.append(formatAsTableHeader(header));
		}
		builder.append("</tr></thead>");
		return builder.toString();
	}

	/**
	 * Formatiert die Zeilen aus dem ResultFormatter als Tabellenrumpf
	 * 
	 * @return Tabellenrumpf als String
	 */
	private String formatRowsAsTableBody() {
		final StringBuilder builder = new StringBuilder();
		if (!resultFormatter.isHeadOnly()) {
			builder.append("<tbody>\n");
			// Inhalte aufbauen
			for (final List<String> row : resultFormatter.getRows()) {
				builder.append(formatAsTableRow(row));
			}
			builder.append("</tbody>\n");
		}
		return builder.toString();
	}

	/**
	 * Bereitet den kompletten Inhalt des ResultFormatters als HTML-Tabelle auf
	 * 
	 * @return
	 */
	private String formatResultAsTable() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<table>\n");
		builder.append(formatTableHeader());
		builder.append(formatRowsAsTableBody());
		builder.append("</table>\n");
		return builder.toString();
	}

	/**
	 * Liefert den Inhalt des im Konstruktor angegebenen ResultFormatters in
	 * HTML aufbereitet zurück
	 * 
	 * @return
	 * @throws IOException
	 */
	public String formatAsHtml() throws IOException {
		final String statementExecution = MessageFormat.format(
				DialogDictionary.PATTERN_EXECUTION_TIMESTAMP_WITH_USER
						.toString(), resultFormatter.getExecutedAt(),
				resultFormatter.getExecutedBy());

		final String resultTable = formatResultAsTable();

		String rowCount = "";
		String limitedRows = "";
		if (!resultFormatter.isHeadOnly()) {
			rowCount = MessageFormat.format(
					DialogDictionary.PATTERN_ROWCOUNT.toString(),
					resultFormatter.getRows().size());
			// ist das Ergebnis eventuell abgeschnitten, wird eine Meldung
			// ausgegeben
			if (resultFormatter.getRows().size() >= DatabaseConstants.MAX_ROWS) {
				if (UserPreferencesManager.getSharedInstance().isLimitMaxRows()) {
					limitedRows = MessageFormat.format(
							DialogDictionary.PATTERN_MAX_ROWS.toString(),
							resultFormatter.getRows().size());
				}
			}
		}
		String template = FileUtil
				.readResourceFile("/resources/html/exporttemplate.html");
		template = template.replace("statement execution goes here",
				statementExecution);
		template = template.replace("result table goes here", resultTable);
		template = template.replace("row count goes here", rowCount);
		template = template.replace("limit rows goes here", limitedRows);
		return template;
	}
}
