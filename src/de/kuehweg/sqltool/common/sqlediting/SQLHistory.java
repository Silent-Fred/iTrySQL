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
package de.kuehweg.sqltool.common.sqlediting;

import de.kuehweg.sqltool.common.DialogDictionary;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.beans.property.SimpleStringProperty;

/**
 * Einzelner Eintrag in der Historie ausgeführter SQL-Anweisungen
 *
 * @author Michael Kühweg
 */
public class SQLHistory {

    private static final int DEFAULT_LENGTH_FOR_SHORT_FORM = 80;
    private static final String ELLIPSIS = "...";
    private final long timestamp;
    private final SimpleStringProperty sqlForDisplay;
    private final String originalSQL;

    /**
     * Eintrag erstellen
     *
     * @param sql
     */
    public SQLHistory(final String sql) {
        timestamp = System.currentTimeMillis();
        String oneLiner = sql.replace("\n", " ");
        oneLiner = oneLiner.replace("\t", " ");
        if (oneLiner.trim().length() > DEFAULT_LENGTH_FOR_SHORT_FORM) {
            oneLiner = oneLiner.trim().substring(0,
                    DEFAULT_LENGTH_FOR_SHORT_FORM - ELLIPSIS.length())
                    + ELLIPSIS;
        }
        sqlForDisplay = new SimpleStringProperty(oneLiner.trim());
        originalSQL = sql;
    }

    /**
     * Verkürzte Variante der ausgeführten SQL-Anweisung (für Übersicht)
     *
     * @return
     */
    public String getSqlForDisplay() {
        return sqlForDisplay.get();
    }

    /**
     * Zeitstempel, wann die SQL-Anweisung in die Historie aufgenommen wurde (das ist also
     * NICHT der exakte Ausführungszeitpunkt)
     *
     * @return
     */
    public String getTimestampFormatted() {
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timestamp);
        return MessageFormat.format(
                DialogDictionary.PATTERN_EXECUTION_TIMESTAMP.toString(),
                cal.getTime());
    }

    /**
     * Zeitstempel, wann die SQL-Anweisung in die Historie aufgenommen wurde (das ist also
     * NICHT der exakte Ausführungszeitpunkt) in Systemzeit.
     *
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Originaltext der ausgeführten SQL-Anweisung
     *
     * @return
     */
    public String getOriginalSQL() {
        return originalSQL;
    }
}
