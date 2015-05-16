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
package de.kuehweg.sqltool.common;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Dictionary als enum zur Ausgabe internationalisierbarer Dialogtexte
 * 
 * @author Michael Kühweg
 */
public enum DialogDictionary {

	APPLICATION("application"),
	ERR_LOAD_FXML("err_loadFxml"),
	ERR_UNKNOWN_COMMAND("err_unknownCommand"),
	ERR_CONNECTION_FAILURE("err_connectionFailure"),
	ERR_AUTO_COMMIT_FAILURE("err_autoCommitFailure"),
	ERR_TUTORIAL_CREATION_FAILED("err_tutorialCreationFailed"),
	ERR_FILE_OPEN_FAILED("err_fileOpenFailed"),
	ERR_FILE_SAVE_FAILED("err_fileSaveFailed"),
	ERR_CONNECTION_SETTING_SAVE_FAILED("err_connectionSettingSaveFailed"),
    ERR_HTML_EXPORT_FAILED("err_htmlExportFailed"),
    ERR_UNKNOWN_ERROR("err_unknownError"),
	MSG_NO_DB_CONNECTION("msg_noDbConnection"),
	MSG_NO_STATEMENT_TO_EXECUTE("msg_noStatementToExecute"),
	MSG_REALLY_REMOVE_CONNECTION("msg_reallyRemoveConnection"),
	MSG_REALLY_CREATE_TUTORIAL_DATA("msg_reallyCreateTutorialData"),
	MSG_SELECT_CONNECTION("msg_selectConnection"),
    MSG_REALLY_QUIT("msg_reallyQuit"),
	MESSAGEBOX_INFO("label_info"),
	MESSAGEBOX_WARNING("label_warning"),
	MESSAGEBOX_CONFIRM("label_confirm"),
	MESSAGEBOX_ERROR("label_error"),
	COMMON_BUTTON_OK("label_commonButtonOK"),
	COMMON_BUTTON_CANCEL("label_commonButtonCancel"),
	LABEL_EXECUTING("label_executing"),
	LABEL_EMPTY("label_empty"),
	LABEL_RESULT_ERROR("label_resultError"),
	LABEL_RESULT_EXECUTED("label_resultExecuted"),
	LABEL_DEFAULT_CONNECTION_IN_MEMORY("label_defaultConnectionInMemory"),
	LABEL_DEFAULT_CONNECTION_STANDALONE_USER_HOME("label_defaultConnectionStandaloneUserHome"),
	LABEL_OPEN_SCRIPT("label_openScript"),
	LABEL_SAVE_SCRIPT("label_saveScript"),
	LABEL_SAVE_OUTPUT("label_saveOutput"),
	LABEL_SAVE_OUTPUT_HTML("label_saveOutputHtml"),
	LABEL_TITLE_CONNECT("label_connectToDatabase"),
	LABEL_CONNECT("label_connect"),
	LABEL_REMOVE_CONNECTION("label_removeConnection"),
	LABEL_DB_DIRECTORY_CHOOSER("label_dbDirectoryChooser"),
	LABEL_CREATE_TUTORIAL_DATA("label_createTutorialData"),
	LABEL_EXPORT_CONNECTIONS("label_exportConnections"),
	LABEL_IMPORT_CONNECTIONS("label_importConnections"),
	LABEL_TREE_INDICES("label_treeIndices"),
	LABEL_TREE_REFERENCES("label_treeReferences"),
	LABEL_TREE_REFERENCED_BY("label_treeReferencedBy"),
    LABEL_UNKNOWN_USER("label_unknownUser"),
    LABEL_REALLY_QUIT("label_reallyQuit"),
    LABEL_NOT_REALLY_QUIT("label_notReallyQuit"),
    LABEL_APPEND_HISTORY_ITEM_TO_EDITOR("label_appendHistoryItemToEditor"),
	TOOLTIP_TUTORIAL_DATA("tooltip_tutorialData"),
	TOOLTIP_INCREASE_FONTSIZE("tooltip_increaseFontsize"),
	TOOLTIP_DECREASE_FONTSIZE("tooltip_decreaseFontsize"),
	TOOLTIP_COMMIT("tooltip_commit"),
	TOOLTIP_ROLLBACK("tooltip_rollback"),
	TOOLTIP_EXECUTE("tooltip_execute"),
	TOOLTIP_EXPORT_OUTPUT("tooltip_exportOutput"),
	TOOLTIP_CLEAR_OUTPUT("tooltip_clearOutput"),
	TOOLTIP_EXPORT_RESULT("tooltip_exportResult"),
    TOOLTIP_IN_MEMORY_DATABASE("tooltip_inMemoryDatabase"),
	PATTERN_EXECUTION_TIME("pattern_executionTime"),
	PATTERN_EXECUTION_TIMESTAMP("pattern_executionTimestamp"),
	PATTERN_EXECUTION_TIMESTAMP_WITH_USER("pattern_executionTimestampWithUser"),
	PATTERN_ROWCOUNT("pattern_rowcount"),
	PATTERN_UPDATECOUNT("pattern_updatecount"),
    PATTERN_EXECUTED_STATEMENT("pattern_executedStatement"),
	PATTERN_MAX_ROWS("pattern_maxRows"),
	PATTERN_NEW_CONNECTION_NAME("pattern_newConnectionName"),
	PATTERN_MESSAGE_IN_MEMORY_DATABASE("pattern_messageInMemoryDatabase");
	private final String key;

	private DialogDictionary(final String key) {
		this.key = key;
	}

	@Override
	public String toString() {
        try {
            return ResourceBundle.getBundle("dictionary").getString(key);
        } catch (final MissingResourceException mre) {
            return name();
        }
	}
}
