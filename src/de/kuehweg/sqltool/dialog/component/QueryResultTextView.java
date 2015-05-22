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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecyclePhase;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefresh;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefreshPolicy;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import java.text.MessageFormat;
import java.util.Date;
import javafx.scene.control.TextArea;

/**
 * Verlauf der SQL-Ausgaben in Textform
 *
 * @author Michael Kühweg
 */
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.INTERMEDIATE, refreshPolicy
        = ExecutionLifecycleRefreshPolicy.DELAYED)
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.ERROR)
public class QueryResultTextView implements ExecutionTracker {

    private static final int MAX_DBOUTPUT = 256 * 1024;

    private static final String TRUNCATED = "[...]";

    private String dbOutput;

    private final TextArea outputTextArea;

    public QueryResultTextView(final TextArea outputTextArea) {
        super();
        this.outputTextArea = outputTextArea;
        dbOutput = outputTextArea.getText();
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

    private Date startOfExecutionAsDate(final StatementExecutionInformation info) {
        return info != null ? new Date(info.getStartOfExecution()) : new Date();
    }

    private String executedByWithConnectionDescription(
            final StatementExecutionInformation info) {
        return info != null ? info.getExecutedBy() + "@" + info.
                getConnectionDescription() : DialogDictionary.LABEL_UNKNOWN_USER.
                toString();
    }

    /**
     * Bereitet den Inhalt eines Abfrageergebnisses als Text auf.
     *
     * @return
     */
    private String formatAsText(final StatementExecutionInformation info) {
        if (info == null) {
            return "\n";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(MessageFormat.format(
                DialogDictionary.PATTERN_EXECUTION_TIMESTAMP_WITH_USER.
                toString(), startOfExecutionAsDate(info),
                executedByWithConnectionDescription(info)));
        builder.append("\n\n");

        if (info.getStatementResult() == null) {
            builder.append(formatWithoutResultSet(info));
        } else {
            builder.append(formatWithResultSet(info));
        }
        builder.append("\n");

        return builder.toString();
    }

    private String formatWithResultSet(final StatementExecutionInformation info) {
        if (info.getStatementResult().getHeader() == null || info.
                getStatementResult().getHeader().getColumnHeaders() == null) {
            return "";
        }
        final int numberOfColumns = info.getStatementResult().getHeader().
                getColumnHeaders().length;
        final int[] size = calculateColumnWidths(info);

        final StringBuilder builder = new StringBuilder();
        // Spaltenüberschriften aufbauen
        int columnCounter = 0;
        for (String header : info.getStatementResult().getHeader().
                getColumnHeaders()) {
            builder.append(rightPad(header, " ", size[columnCounter++]));
            builder.append(' ');
        }
        builder.append("\n");

        for (int i = 0; i < numberOfColumns; i++) {
            builder.append(rightPad("", "-", size[i]));
            builder.append(' ');
        }
        builder.append("\n");

        // Inhalte aufbauen
        for (final ResultRow row : info.getStatementResult().getRows()) {
            columnCounter = 0;
            for (final String column : row.columnsAsString()) {
                builder.append(rightPad(
                        column == null ? "" : column.trim(), " ",
                        size[columnCounter++]));
                builder.append(' ');
            }
            builder.append("\n");
        }
        builder.append("\n");
        builder.append(info.getSummary());
        // ist das Ergebnis eventuell abgeschnitten, wird eine Meldung
        // ausgegeben
        if (info.isLimitMaxRowsReached()) {
            builder.append("\n\n");
            builder.append(MessageFormat.format(
                    DialogDictionary.PATTERN_MAX_ROWS.toString(),
                    info.getStatementResult().getRows().size()));
        }
        return builder.toString();
    }

    private String formatWithoutResultSet(
            final StatementExecutionInformation info) {
        return info.getSummary();
    }

    private String buildNewContent(final String currentContent, final String appendix) {
        String newContent = "";
        if (currentContent == null) {
            newContent = appendix != null ? appendix : "";
        } else if (appendix == null) {
            newContent = currentContent; // not null ist durch den ersten Zweig schon geklärt
        } else {
            if (appendix.length() >= MAX_DBOUTPUT) {
                newContent = TRUNCATED
                        + appendix.substring(appendix.length() - MAX_DBOUTPUT);
            } else if (currentContent.length() + appendix.length() >= MAX_DBOUTPUT) {
                newContent = TRUNCATED + currentContent.substring(currentContent.length()
                        - (MAX_DBOUTPUT - appendix.length())) + appendix;
            } else {
                newContent = currentContent + appendix;
            }
        }
        return newContent + "\n";
    }

    @Override
    public void beforeExecution() {
    }

    @Override
    public void intermediateUpdate(final StatementExecutionInformation executionInfo) {
        if (executionInfo != null) {
            dbOutput = buildNewContent(dbOutput, formatAsText(executionInfo));
        }
    }

    @Override
    public void afterExecution() {
    }

    @Override
    public void errorOnExecution(String message) {
        dbOutput = buildNewContent(dbOutput, message);
    }

    @Override
    public void show() {
        outputTextArea.setText(dbOutput);
        outputTextArea.positionCaret(outputTextArea.getLength());
    }

}
