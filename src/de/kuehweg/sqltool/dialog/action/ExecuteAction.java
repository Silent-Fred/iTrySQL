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
package de.kuehweg.sqltool.dialog.action;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.dialog.AlertBox;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Dialogaktion: SQL-Anweisung(en) ausführen
 *
 * @author Michael Kühweg
 */
public class ExecuteAction {

    private final Collection<ExecutionTracker> trackers = new HashSet<>();

    public ExecuteAction() {
    }

    public void attach(final ExecutionTracker... trackers) {
        if (trackers != null) {
            for (ExecutionTracker tracker : trackers) {
                this.trackers.add(tracker);
            }
        }
    }

    public void detach(final ExecutionTracker... trackers) {
        if (trackers != null) {
            for (ExecutionTracker tracker : trackers) {
                this.trackers.remove(tracker);
            }
        }
    }

    /**
     * SQL ausführen und Dialog aktualisieren
     *
     * @param sql
     * @param connection
     */
    public void handleExecuteAction(final String sql,
            final Connection connection) {
        if (sql == null || sql.trim().length() == 0) {
            final AlertBox msg = new AlertBox(
                    DialogDictionary.MESSAGEBOX_WARNING.toString(),
                    DialogDictionary.MSG_NO_STATEMENT_TO_EXECUTE.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        } else {
            if (connection == null) {
                final AlertBox msg = new AlertBox(
                        DialogDictionary.MESSAGEBOX_WARNING.toString(),
                        DialogDictionary.MSG_NO_DB_CONNECTION.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            } else {
                try {
                    // die eigentliche Ausführung wird im Hintergrund gestartet
                    // und bekommt alle Informationen mit auf den Weg, um
                    // während und zum Abschluss der Ausführung die Oberfläche
                    // aktualisieren zu können.
                    final ExecutionTask executionTask = new ExecutionTask(
                            connection.createStatement(), sql);
                    executionTask.attach(trackers);
                    final Thread th = new Thread(executionTask);
                    th.setDaemon(true);
                    th.start();
                } catch (final SQLException ex) {
                    // falls kein Statement erzeugt werden kann, landen wir
                    // schon vor der eigentlichen Ausführung in einer
                    // SQL-Exception
                    final ErrorMessage msg = new ErrorMessage(
                            DialogDictionary.MESSAGEBOX_ERROR.toString(),
                            ex.getLocalizedMessage() + " (" + ex.getSQLState()
                            + ")",
                            DialogDictionary.COMMON_BUTTON_OK.toString());
                    msg.askUserFeedback();
                }
            }
        }
    }
}
