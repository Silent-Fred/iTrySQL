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
package de.kuehweg.sqltool.dialog.action;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.dialog.AlertBox;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.itrysql.iTrySQLController;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.concurrent.Task;
import javafx.scene.Scene;

/**
 * @author Michael Kühweg
 */
public class ExecuteAction {

    private ExecuteAction() {
        // no instances
    }

    public static void handleExecuteAction(final Scene sceneToUpdate,
            final iTrySQLController callingController,
            final String sql) {
        handleExecuteAction(sceneToUpdate,
                callingController,
                sql, false);
    }

    public static void handleExecuteActionSilently(final Scene sceneToUpdate,
            final iTrySQLController callingController,
            final String sql) {
        handleExecuteAction(sceneToUpdate,
                callingController,
                sql, true);
    }

    private static void handleExecuteAction(final Scene sceneToUpdate,
            final iTrySQLController callingController,
            final String sql, final boolean silent) {
        if (sql == null || sql.trim().length() == 0) {
            AlertBox msg = new AlertBox(DialogDictionary.MESSAGEBOX_WARNING.
                    toString(),
                    DialogDictionary.MSG_NO_STATEMENT_TO_EXECUTE.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        } else {
            Connection connection = callingController.getConnectionHolder().
                    getConnection();
            if (connection == null) {
                AlertBox msg = new AlertBox(DialogDictionary.MESSAGEBOX_WARNING.
                        toString(),
                        DialogDictionary.MSG_NO_DB_CONNECTION.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            } else {
                ActionVisualisation.prepareSceneRunning(sceneToUpdate);
                try {
                    ExecutionGUIUpdater guiUpdater = new ExecutionGUIUpdater(
                            sceneToUpdate, callingController);
                    guiUpdater.setSilent(silent);
                    Task executionTask = new ExecutionTask(
                            callingController.getConnectionHolder().
                            getStatement(),
                            sql, guiUpdater);
                    Thread th = new Thread(executionTask);
                    th.setDaemon(true);
                    th.start();
                } catch (SQLException ex) {
                    // falls kein Statement erzeugt werden kann, landen wir schon vor der eigentlichen
                    // Ausführung in einer SQL-Exception
                    ErrorMessage msg = new ErrorMessage(
                            DialogDictionary.MESSAGEBOX_ERROR.toString(),
                            ex.getLocalizedMessage() + " (" + ex.getSQLState()
                            + ")",
                            DialogDictionary.COMMON_BUTTON_OK.toString());
                    msg.askUserFeedback();
                    ActionVisualisation.showFinished(sceneToUpdate, 0);
                }
            }
        }
    }
}