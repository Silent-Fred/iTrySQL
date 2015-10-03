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
package de.kuehweg.sqltool.common.achievement;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.gamification.AchievementCounter;

/**
 * Alle in der Anwendung verwendeten Achievements.
 *
 * @author Michael Kühweg
 */
public enum NamedAchievement {

	FRIEND_OF_THE_APPLICATION(
			new AchievementCounter(NamedAchievementEvent.CONNECTION_ESTABLISHED.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.EXPORTED_RESULT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.FONT_SIZE_CHANGED.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.READ_ABOUT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.SCRIPT_SAVED.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.SCRIPT_EXECUTED.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.TUTORIAL_BUILT.asAchievementEvent(), 1)),

	ALL_FOUR_WINDS(new AchievementCounter(NamedAchievementEvent.STATEMENT_GROUP_DML.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.STATEMENT_GROUP_DDL.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.STATEMENT_GROUP_TCL.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.STATEMENT_GROUP_DCL.asAchievementEvent(), 1)),

	GETTING_PRACTICE(new AchievementCounter(NamedAchievementEvent.EXECUTION_SUCCESSFUL.asAchievementEvent(), 50)),

	ROW_ROW_ROW_YOUR_BOAT(
			new AchievementCounter(NamedAchievementEvent.EXECUTION_RESULT_ROWS.asAchievementEvent(), 2500)),

	ADVENTUROUS(new AchievementCounter(NamedAchievementEvent.SELECT_STATEMENT.asAchievementEvent(), 10),
			new AchievementCounter(NamedAchievementEvent.LENGTHY_STATEMENT.asAchievementEvent(), 1)),

	UNIMPRESSED_BY_DIFFICULTY(new AchievementCounter(NamedAchievementEvent.LENGTHY_STATEMENT.asAchievementEvent(), 5)),

	TAKE_RISKS_AND_ACCEPT_FAILURE(
			new AchievementCounter(NamedAchievementEvent.EXECUTION_ERROR.asAchievementEvent(), 10)),

	THE_WALKING_DEADLOCK(new AchievementCounter(NamedAchievementEvent.EXECUTION_DEADLOCK.asAchievementEvent(), 1)),

	BACHELOR_OF_MANIPULATION(new AchievementCounter(NamedAchievementEvent.SELECT_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.INSERT_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.UPDATE_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.DELETE_STATEMENT.asAchievementEvent(), 1)),

	MASTER_OF_MANIPULATION(new AchievementCounter(NamedAchievementEvent.SELECT_STATEMENT.asAchievementEvent(), 10),
			new AchievementCounter(NamedAchievementEvent.INSERT_STATEMENT.asAchievementEvent(), 2),
			new AchievementCounter(NamedAchievementEvent.UPDATE_STATEMENT.asAchievementEvent(), 2),
			new AchievementCounter(NamedAchievementEvent.DELETE_STATEMENT.asAchievementEvent(), 2)),

	ALL_THAT_YOU_SHOULD_DO(new AchievementCounter(NamedAchievementEvent.SELECT_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.INSERT_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.UPDATE_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.DELETE_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.GRANT_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.REVOKE_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.CREATE_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.ALTER_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.DROP_STATEMENT.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.COMMITTED.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.ROLLED_BACK.asAchievementEvent(), 1)),

	WORKED_WITH_TRANSACTIONS(new AchievementCounter(NamedAchievementEvent.COMMITTED.asAchievementEvent(), 1),
			new AchievementCounter(NamedAchievementEvent.ROLLED_BACK.asAchievementEvent(), 1)),

	BE_THE_ONE_FOR_ME(new AchievementCounter(NamedAchievementEvent.SINGLE_ROW_RESULT.asAchievementEvent(), 10)),

	RATHER_SPECIFIC(new AchievementCounter(NamedAchievementEvent.SHORT_RESULT.asAchievementEvent(), 10)),

	THE_MORE_THE_MERRIER(new AchievementCounter(NamedAchievementEvent.LARGE_RESULT.asAchievementEvent(), 5)),

	YOU_REALLY_EXAGGERATE(new AchievementCounter(NamedAchievementEvent.HUGE_RESULT.asAchievementEvent(), 1)),

	MR_CHATTY(new AchievementCounter(NamedAchievementEvent.STATEMENT_WITH_COMMENT.asAchievementEvent(), 1)),

	LONG_STORY(new AchievementCounter(NamedAchievementEvent.STATEMENT_UNCOMMENTED_LENGTH.asAchievementEvent(), 2048)),

	THE_UNACHIEVABLE_ACHIEVEMENT(
			new AchievementCounter(NamedAchievementEvent.THE_NEVER_HAPPENING_EVENT.asAchievementEvent(), 1));

	private Achievement achievement;

	NamedAchievement(final AchievementCounter... achievementCounters) {
		achievement = new Achievement(name(), achievementCounters);
	}

	public Achievement asAchievement() {
		return achievement;
	}
}
