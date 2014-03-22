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

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.UserPreferencesManager;
import java.text.MessageFormat;
import java.util.List;

/**
 * Ergebnis einer Datenbankabfrage als Text aufbereiten
 *
 * @author Michael Kühweg
 */
public class TextResultFormatter {

    private final ResultFormatter resultFormatter;

    /**
     * Im Konstruktor wird das Abfrageergebnis übergeben
     *
     * @param resultFormatter
     */
    public TextResultFormatter(final ResultFormatter resultFormatter) {
        this.resultFormatter = resultFormatter;
    }

    /**
     * Spaltenbreiten in Zeichen ermitteln (zur Ausgabe in monospaced Fonts)
     *
     * @return
     */
    private int[] calculateColumnWidths() {
        final int columnCount = resultFormatter.getHeader().length;
        final int[] width = new int[columnCount];

        // Spaltenbreiten ermitteln (Titel)
        for (int i = 0; i < columnCount; i++) {
            width[i] = resultFormatter.getHeader()[i].toString().trim()
                    .length();
        }

        // Spaltenbreiten ermitteln (Inhalt)
        for (final List<String> row : resultFormatter.getRows()) {
            int columnIndex = 0;
            for (final String column : row) {
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

    /**
     * Bereitet den Inhalt des im Konstruktor angegebenen Abfrageergebnisses
     * komplett als Text auf.
     *
     * @return
     */
    public String formatAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(MessageFormat.format(
                DialogDictionary.PATTERN_EXECUTION_TIMESTAMP_WITH_USER
                .toString(), resultFormatter.getExecutedAt(),
                resultFormatter.getExecutedBy()));
        builder.append("\n\n");

        final int width = resultFormatter.getHeader().length;
        final int[] size = calculateColumnWidths();

        // Spaltenüberschriften aufbauen
        for (int i = 0; i < width; i++) {
            builder.append(rightPad(resultFormatter.getHeader()[i].toString(),
                    " ", size[i]));
            builder.append(' ');
        }
        builder.append("\n");
        if (resultFormatter.isHeadOnly()) {
            builder.append(MessageFormat.format(
                    DialogDictionary.PATTERN_UPDATECOUNT.toString(),
                    resultFormatter.getUpdateCount()));
        } else {
            for (int i = 0; i < width; i++) {
                builder.append(rightPad("", "-", size[i]));
                builder.append(' ');
            }
            builder.append("\n");

            // Inhalte aufbauen
            for (final List<String> row : resultFormatter.getRows()) {
                int columnIndex = 0;
                for (final String column : row) {
                    builder.append(rightPad(
                            column == null ? "" : column.trim(), " ",
                            size[columnIndex]));
                    builder.append(' ');
                    columnIndex++;
                }
                builder.append("\n");
            }
            builder.append("\n");
            builder.append(MessageFormat.format(
                    DialogDictionary.PATTERN_ROWCOUNT.toString(),
                    resultFormatter.getRows().size()));
            // ist das Ergebnis eventuell abgeschnitten, wird eine Meldung
            // ausgegeben
            if (resultFormatter.getRows().size() >= DatabaseConstants.MAX_ROWS) {
                if (UserPreferencesManager.getSharedInstance().isLimitMaxRows()) {
                    builder.append("\n\n");
                    builder.append(MessageFormat.format(
                            DialogDictionary.PATTERN_MAX_ROWS.toString(),
                            resultFormatter.getRows().size()));
                }
            }
        }

        return builder.toString();
    }
}
