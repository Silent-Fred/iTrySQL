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

import java.sql.Connection;
import java.sql.SQLException;

import javafx.concurrent.Task;
import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.dialog.AlertBox;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.environment.ExecutionInputEnvironment;
import de.kuehweg.sqltool.dialog.environment.ExecutionProgressEnvironment;
import de.kuehweg.sqltool.dialog.environment.ExecutionResultEnvironment;

/**
 * @author Michael Kühweg
 */
public class ExecuteAction {

	private final ExecutionInputEnvironment input;
	private final ExecutionProgressEnvironment progress;
	private final ExecutionResultEnvironment result;

	public ExecuteAction(final ExecutionInputEnvironment input,
			final ExecutionProgressEnvironment progress,
			final ExecutionResultEnvironment result) {
		this.input = input;
		this.progress = progress;
		this.result = result;
	}

	public void handleExecuteAction(final String sql) {
		handleExecuteAction(sql, false);
	}

	public void handleExecuteActionSilently(final String sql) {
		handleExecuteAction(sql, true);
	}

	private void handleExecuteAction(final String sql, final boolean silent) {
		if (sql == null || sql.trim().length() == 0) {
			final AlertBox msg = new AlertBox(
					DialogDictionary.MESSAGEBOX_WARNING.toString(),
					DialogDictionary.MSG_NO_STATEMENT_TO_EXECUTE.toString(),
					DialogDictionary.COMMON_BUTTON_OK.toString());
			msg.askUserFeedback();
		} else {
			final Connection connection = input.getConnectionHolder() != null ? input
					.getConnectionHolder().getConnection() : null;
			if (connection == null) {
				final AlertBox msg = new AlertBox(
						DialogDictionary.MESSAGEBOX_WARNING.toString(),
						DialogDictionary.MSG_NO_DB_CONNECTION.toString(),
						DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();
			} else {
				ActionVisualisation.prepareSceneRunning(progress
						.getProgressIndicator().getScene());
				try {
					final ExecutionGUIUpdater guiUpdater = new ExecutionGUIUpdater(
							progress.getProgressIndicator().getScene(),
							result.getHistoryKeeper());
					guiUpdater.setSilent(silent);
					final Task<Void> executionTask = new ExecutionTask(input
							.getConnectionHolder().getStatement(), sql,
							guiUpdater);
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
					ActionVisualisation.showFinished(progress
							.getProgressIndicator().getScene(), 0);
				}
			}
		}
	}
}