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
package de.kuehweg.sqltool.database.formatter;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Textaufbereitung für Ergebnisse einer Anweisung. Allgemeiner Hinweis: Alle Bestandteile
 * werden ohne Zeilenwechsel am Ende erzeugt, so dass je nach Template die Bestandteile
 * z.T. auch in einer Zeile kombiniert werden könnten.
 *
 * @author Michael Kühweg
 */
public class TextResultFormatter extends ResultFormatter {

    public TextResultFormatter(StatementExecutionInformation statementExecutionInformation) {
        super(statementExecutionInformation);
    }

    /**
     * Spaltenbreiten in Zeichen ermitteln (zur Ausgabe in monospaced Fonts)
     *
     * @return
     */
    private int[] calculateColumnWidths(final StatementExecutionInformation info) {
        final int[] width = new int[info.getStatementResult().getHeader().
                getColumnHeaders().length];

        // Spaltenbreiten ermitteln (Titel)
        int columnIndex = 0;
        for (final String header : info.getStatementResult().getHeader().
                getColumnHeaders()) {
            width[columnIndex++] = header.trim().length();
        }

        // Spaltenbreiten ermitteln (Inhalt)
        for (final ResultRow row : info.getStatementResult().getRows()) {
            columnIndex = 0;
            for (final String column : row.columnsAsString()) {
                final int length = column.trim().length();
                if (length > width[columnIndex]) {
                    width[columnIndex] = length;
                }
                columnIndex++;
            }
        }
        return width;
    }

    /**
     * String rechts mit dem Füllzeichen bis zur angegebenen Länge auffüllen
     *
     * @param str Ausgangstext
     * @param paddingString Fülltext / Füllzeichen
     * @param paddingLength Länge bis zu der aufgefüllt wird
     * @return Aufgefüllter Text
     */
    private String rightPad(final String str, final String paddingString,
            final int paddingLength) {
        final StringBuilder builder = new StringBuilder(str);
        while (builder.length() <= paddingLength) {
            builder.append(paddingString);
        }
        return builder.substring(0, paddingLength);
    }

    private Date startOfExecutionAsDate() {
        return getStatementExecutionInformation() != null ? new Date(
                getStatementExecutionInformation().getStartOfExecution()) : new Date();
    }

    private String executedByWithConnectionDescription() {
        final StatementExecutionInformation info = getStatementExecutionInformation();
        return info != null ? info.getExecutedBy() + "@" + info.
                getConnectionDescription() : DialogDictionary.LABEL_UNKNOWN_USER.
                toString();
    }

    private String formatResultAsTable() {
        final StatementExecutionInformation info = getStatementExecutionInformation();
        if (info.getStatementResult().getHeader() == null || info.
                getStatementResult().getHeader().getColumnHeaders() == null) {
            return "";
        }
        final int[] columnWidths = calculateColumnWidths(info);

        StringBuilder builder = new StringBuilder(formatHeader(columnWidths));
        return builder.append(formatRows(columnWidths)).toString();
    }

    /**
     * Ergebniszeilen aufbereiten
     *
     * @param columnWidths Array mit den Spaltenbreiten, auf die die jeweiligen Spalten
     * aufgefüllt werden
     * @return
     */
    private String formatRows(final int[] columnWidths) {
        // Inhalte aufbauen
        final StatementExecutionInformation info = getStatementExecutionInformation();
        final StringBuilder builder = new StringBuilder();
        for (final ResultRow row : info.getStatementResult().getRows()) {
            // ACHTUNG: Hier immer VOR einer Zeile einen Zeilenwechsel.
            // Das ist OK, da alle Bestandteile, also auch der Header, ohne Zeilenwechsel
            // am Ende erzeugt werden.
            builder.append("\n");
            int columnCounter = 0;
            for (final String column : row.columnsAsString()) {
                if (columnCounter > 0) {
                    builder.append(' ');
                }
                builder.append(rightPad(column == null ? "" : column.trim(), " ",
                        columnWidths[columnCounter++]));
            }
        }
        return builder.toString();
    }

    /**
     * Spaltenüberschriften erzeugen
     *
     * @param columnWidths Array mit den Spaltenbreiten, auf die die jeweiligen Spalten
     * aufgefüllt werden
     * @return
     */
    private String formatHeader(final int[] columnWidths) {
        final StringBuilder titleBuilder = new StringBuilder();
        final StringBuilder underlineBuilder = new StringBuilder();
        int columnCounter = 0;
        for (String header : getStatementExecutionInformation().getStatementResult().
                getHeader().getColumnHeaders()) {
            if (columnCounter > 0) {
                titleBuilder.append(' ');
                underlineBuilder.append(' ');
            }
            titleBuilder.append(rightPad(header, " ", columnWidths[columnCounter]));
            underlineBuilder.append(rightPad("", "-", columnWidths[columnCounter]));
            columnCounter++;
        }
        return new StringBuilder(titleBuilder.toString()).append("\n").append(
                underlineBuilder).toString();
    }

    private String formatGeneralExecutionInformation() {
        final Date when = startOfExecutionAsDate();
        final String who = executedByWithConnectionDescription();
        final String statementExecution = MessageFormat.format(
                DialogDictionary.PATTERN_EXECUTION_TIMESTAMP_WITH_USER
                .toString(), when, who);
        return statementExecution;
    }

    private String formatEmptyResult(ResultTemplate template) {
        template.setExecutionInformation(formatGeneralExecutionInformation());
        template.setRowCount(getStatementExecutionInformation().getSummary());

        template.setResultTable(null);
        template.setLimitedRows(null);

        return template.buildWithTemplate();
    }

    private String formatWithResult(final ResultTemplate template) {
        template.setExecutionInformation(formatGeneralExecutionInformation());
        template.setResultTable(formatResultAsTable());

        int selectedRows = getStatementExecutionInformation().getStatementResult().
                getRows().size();
        final String rowCount = MessageFormat.format(
                DialogDictionary.PATTERN_ROWCOUNT.toString(),
                selectedRows);
        template.setRowCount(rowCount);
        if (!getStatementExecutionInformation().isLimitMaxRowsReached()) {
            template.setLimitedRows(null);
        } else {
            final String limitedRows = MessageFormat.format(
                    DialogDictionary.PATTERN_MAX_ROWS.toString(), selectedRows);
            template.setLimitedRows(limitedRows);
        }

        return template.buildWithTemplate();
    }

    @Override
    public String format(ResultTemplate template) {
        return getStatementExecutionInformation().getStatementResult() != null ? formatWithResult(
                template) : formatEmptyResult(template);
    }

}
