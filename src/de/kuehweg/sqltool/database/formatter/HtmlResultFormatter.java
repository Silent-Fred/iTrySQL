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
package de.kuehweg.sqltool.database.formatter;

import java.text.MessageFormat;
import java.util.Date;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.text.HtmlEncoder;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;

/**
 * Abfrageergebnis als HTML aufbereitet.
 *
 * @author Michael Kühweg
 */
public class HtmlResultFormatter extends ResultFormatter {

	private final HtmlEncoder htmlEncoder = new HtmlEncoder();

	/**
	 * Im Konstruktor wird das Abfrageergebnis übergeben.
	 *
	 * @param info
	 */
	public HtmlResultFormatter(final StatementExecutionInformation info) {
		super(info);
	}

	/**
	 * Formatiert einen Text als Tabellenzelle.
	 *
	 * @param value
	 *            Text wird in HTML Format gewandelt, muss also noch
	 *            "naturbelassen" sein
	 * @return
	 */
	private String formatAsTableData(final String value) {
		return new StringBuilder().append("<td>").append(htmlEncoder.encodeHtml(value)).append("</td>").toString();
	}

	/**
	 * Formatiert eine Liste von ResultRows als HTML-Tabellenzeile.
	 *
	 * @param resultRow
	 *            Liste von ResultRows, die in HTML umgewandelt werden
	 * @return
	 */
	private String formatAsTableRow(final ResultRow resultRow) {
		final StringBuilder builder = new StringBuilder("<tr>");
		for (final String column : resultRow.columnsAsString()) {
			builder.append(formatAsTableData(column));
		}
		builder.append("</tr>\n");
		return builder.toString();
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
		return new StringBuilder().append("<th>").append(htmlEncoder.encodeHtml(value)).append("</th>").toString();
	}

	/**
	 * Erzeugt den Tabellenkopf.
	 *
	 * @return Tabellenkopf als String
	 */
	private String formatTableHeader() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<thead><tr>");
		// Spaltenüberschriften aufbauen
		for (final String header : getStatementExecutionInformation().getStatementResult().getHeader()
				.getColumnHeaders()) {
			builder.append(formatAsTableHeader(header));
		}
		builder.append("</tr></thead>");
		return builder.toString();
	}

	/**
	 * Formatiert die Ergebniszeilen als Tabellenrumpf.
	 *
	 * @return Tabellenrumpf als String
	 */
	private String formatRowsAsTableBody() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<tbody>\n");
		// Inhalte aufbauen
		for (final ResultRow row : getStatementExecutionInformation().getStatementResult().getRows()) {
			builder.append(formatAsTableRow(row));
		}
		builder.append("</tbody>\n");
		return builder.toString();
	}

	/**
	 * Bereitet den kompletten Inhalt des ResultFormatters als HTML-Tabelle auf.
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

	private Date startOfExecutionAsDate() {
		return getStatementExecutionInformation() != null
				? new Date(getStatementExecutionInformation().getStartOfExecution()) : new Date();
	}

	private String executedByWithConnectionDescription() {
		final StatementExecutionInformation info = getStatementExecutionInformation();
		return info != null && info.getExecutedBy() != null && info.getConnectionDescription() != null
				? info.getExecutedBy() + "@" + info.getConnectionDescription()
				: DialogDictionary.LABEL_UNKNOWN_USER.toString();
	}

	private String formatGeneralExecutionInformation() {
		final Date when = startOfExecutionAsDate();
		final String who = executedByWithConnectionDescription();
		final String statementExecution = MessageFormat
				.format(DialogDictionary.PATTERN_EXECUTION_TIMESTAMP_WITH_USER.toString(), when, who);
		return htmlEncoder.encodeHtml(statementExecution);
	}

	private String formatEmptyResult(final ResultTemplate template) {
		template.setExecutionInformation(formatGeneralExecutionInformation());

		template.setRowCount(htmlEncoder.encodeHtml(getStatementExecutionInformation().getSummary()));

		template.setResultTable(null);
		template.setLimitedRows(null);

		return template.buildWithTemplate();
	}

	private String formatWithResult(final ResultTemplate template) {
		template.setExecutionInformation(formatGeneralExecutionInformation());
		template.setResultTable(formatResultAsTable());

		final int selectedRows = getStatementExecutionInformation().getStatementResult().getRows().size();
		final String rowCount = MessageFormat.format(DialogDictionary.PATTERN_ROWCOUNT.toString(), selectedRows);
		template.setRowCount(htmlEncoder.encodeHtml(rowCount));
		if (!getStatementExecutionInformation().isLimitMaxRowsReached()) {
			template.setLimitedRows(null);
		} else {
			final String limitedRows = MessageFormat.format(DialogDictionary.PATTERN_MAX_ROWS.toString(), selectedRows);
			template.setLimitedRows(htmlEncoder.encodeHtml(limitedRows));
		}

		return template.buildWithTemplate();
	}

	@Override
	public String format(final ResultTemplate template) {
		return getStatementExecutionInformation().getStatementResult() != null ? formatWithResult(template)
				: formatEmptyResult(template);
	}
}
