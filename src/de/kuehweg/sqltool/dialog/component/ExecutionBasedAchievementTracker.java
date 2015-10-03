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

import de.kuehweg.sqltool.common.achievement.AchievementManager;
import de.kuehweg.sqltool.common.achievement.NamedAchievementEvent;
import de.kuehweg.sqltool.database.DatabaseConstants;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecyclePhase;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefresh;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;

/**
 * Auswertung der Events für Achievements, die auf der Ausführung von SQL
 * Statements beruhen.
 *
 * @author Michael Kühweg
 */
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.AFTER)
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.ERROR)
public class ExecutionBasedAchievementTracker implements ExecutionTracker {

	public static final int LARGE_RESULT_MIN_ROWS = 100;
	public static final int HUGE_RESULT_MIN_ROWS = DatabaseConstants.MAX_ROWS + 1;
	public static final int SHORT_RESULT_MAX_ROWS = 10;

	public static final int LENGTHY_STATEMENT_MIN_CHARS = 160;

	@Override
	public void beforeExecution() {
		// nothing to do
	}

	@Override
	public void intermediateUpdate(final StatementExecutionInformation executionInfo) {
		final AchievementManager achievementManager = AchievementManager.getInstance();
		achievementManager.fireEvent(NamedAchievementEvent.STATEMENT_EXECUTED.asAchievementEvent(), 1);
		eventsForStatementAppearance(executionInfo);
		eventsForKeyword(executionInfo);
		eventsForStatementGroup(executionInfo);
		eventsForResultSet(executionInfo);
	}

	private void eventsForStatementAppearance(final StatementExecutionInformation executionInformation) {
		final AchievementManager achievementManager = AchievementManager.getInstance();
		final int originalLength = executionInformation.getSql().originalStatement().trim().length();
		final int uncommentedLength = executionInformation.getSql().uncommentedStatement().trim().length();
		achievementManager.fireEvent(NamedAchievementEvent.STATEMENT_UNCOMMENTED_LENGTH.asAchievementEvent(),
				uncommentedLength);
		if (uncommentedLength >= LENGTHY_STATEMENT_MIN_CHARS) {
			achievementManager.fireEvent(NamedAchievementEvent.LENGTHY_STATEMENT.asAchievementEvent(), 1);
		}
		if (originalLength > uncommentedLength) {
			achievementManager.fireEvent(NamedAchievementEvent.STATEMENT_WITH_COMMENT.asAchievementEvent(), 1);
		}
	}

	private void eventsForKeyword(final StatementExecutionInformation executionInfo) {
		final AchievementManager achievementManager = AchievementManager.getInstance();
		final String keyword = executionInfo.getSql().firstKeyword();
		switch (keyword) {
		case "SELECT":
			achievementManager.fireEvent(NamedAchievementEvent.SELECT_STATEMENT.asAchievementEvent(), 1);
			break;
		case "ALTER":
			achievementManager.fireEvent(NamedAchievementEvent.ALTER_STATEMENT.asAchievementEvent(), 1);
			break;
		case "COMMIT":
			achievementManager.fireEvent(NamedAchievementEvent.COMMITTED.asAchievementEvent(), 1);
			break;
		case "CREATE":
			achievementManager.fireEvent(NamedAchievementEvent.CREATE_STATEMENT.asAchievementEvent(), 1);
			break;
		case "DELETE":
			achievementManager.fireEvent(NamedAchievementEvent.DELETE_STATEMENT.asAchievementEvent(), 1);
			break;
		case "DROP":
			achievementManager.fireEvent(NamedAchievementEvent.DROP_STATEMENT.asAchievementEvent(), 1);
			break;
		case "GRANT":
			achievementManager.fireEvent(NamedAchievementEvent.GRANT_STATEMENT.asAchievementEvent(), 1);
			break;
		case "INSERT":
			achievementManager.fireEvent(NamedAchievementEvent.INSERT_STATEMENT.asAchievementEvent(), 1);
			break;
		case "REVOKE":
			achievementManager.fireEvent(NamedAchievementEvent.REVOKE_STATEMENT.asAchievementEvent(), 1);
			break;
		case "ROLLBACK":
			achievementManager.fireEvent(NamedAchievementEvent.ROLLED_BACK.asAchievementEvent(), 1);
			break;
		case "UPDATE":
			achievementManager.fireEvent(NamedAchievementEvent.UPDATE_STATEMENT.asAchievementEvent(), 1);
			break;
		default:
			break;
		}
	}

	private void eventsForStatementGroup(final StatementExecutionInformation executionInfo) {
		final AchievementManager achievementManager = AchievementManager.getInstance();
		if (executionInfo.getSql().isDataControlStatement()) {
			achievementManager.fireEvent(NamedAchievementEvent.STATEMENT_GROUP_DCL.asAchievementEvent(), 1);
		}
		if (executionInfo.getSql().isDataDefinitionStatement()) {
			achievementManager.fireEvent(NamedAchievementEvent.STATEMENT_GROUP_DDL.asAchievementEvent(), 1);
		}
		if (executionInfo.getSql().isDataManipulationStatement()) {
			achievementManager.fireEvent(NamedAchievementEvent.STATEMENT_GROUP_DML.asAchievementEvent(), 1);
		}
		if (executionInfo.getSql().isTransactionControlStatement()) {
			achievementManager.fireEvent(NamedAchievementEvent.STATEMENT_GROUP_TCL.asAchievementEvent(), 1);
		}
	}

	private void eventsForResultSet(final StatementExecutionInformation executionInfo) {
		if (executionInfo.getStatementResult() != null) {
			final AchievementManager achievementManager = AchievementManager.getInstance();
			final int rowCount = executionInfo.getStatementResult().getRows().size();
			achievementManager.fireEvent(NamedAchievementEvent.EXECUTION_RESULT_ROWS.asAchievementEvent(), rowCount);
			if (rowCount >= LARGE_RESULT_MIN_ROWS) {
				achievementManager.fireEvent(NamedAchievementEvent.LARGE_RESULT.asAchievementEvent(), 1);
			}
			if (rowCount >= HUGE_RESULT_MIN_ROWS) {
				achievementManager.fireEvent(NamedAchievementEvent.HUGE_RESULT.asAchievementEvent(), 1);
			}
			if (rowCount <= SHORT_RESULT_MAX_ROWS) {
				achievementManager.fireEvent(NamedAchievementEvent.SHORT_RESULT.asAchievementEvent(), 1);
			}
			if (rowCount == 1) {
				achievementManager.fireEvent(NamedAchievementEvent.SINGLE_ROW_RESULT.asAchievementEvent(), 1);
			}
		}
	}

	@Override
	public void afterExecution() {
		final AchievementManager achievementManager = AchievementManager.getInstance();
		achievementManager.fireEvent(NamedAchievementEvent.EXECUTION_SUCCESSFUL.asAchievementEvent(), 1);
	}

	@Override
	public void errorOnExecution(final String message) {
		final AchievementManager achievementManager = AchievementManager.getInstance();
		achievementManager.fireEvent(NamedAchievementEvent.EXECUTION_ERROR.asAchievementEvent(), 1);
	}

	@Override
	public void show() {
		// wird anderweitig abgebildet
	}

}
