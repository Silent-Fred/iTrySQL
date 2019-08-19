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

package de.kuehweg.sqltool.dialog.component.achievement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.gamification.AchievementCounter;
import de.kuehweg.gamification.AchievementEvent;
import de.kuehweg.sqltool.common.achievement.NamedAchievement;

/**
 * Standardsortierung für Achievements testen.
 *
 * @author Michael Kühweg
 */
public class AchievementDefaultSortOrderTest {

	@Test
	public void testCompareNamedAchievements() {
		assertTrue(new AchievementDefaultSortOrder().compare(NamedAchievement.FRIEND_OF_THE_APPLICATION.asAchievement(),
				NamedAchievement.ADVENTUROUS.asAchievement()) < 0);
		assertEquals(0, new AchievementDefaultSortOrder().compare(NamedAchievement.ADVENTUROUS.asAchievement(),
				NamedAchievement.ADVENTUROUS.asAchievement()));
		assertTrue(new AchievementDefaultSortOrder().compare(NamedAchievement.BACHELOR_OF_MANIPULATION.asAchievement(),
				NamedAchievement.THE_WALKING_DEADLOCK.asAchievement()) > 0);
	}

	@Test
	public void testCompareAchievementsUsingKnownNames() {
		final AchievementCounter counter = new AchievementCounter(new AchievementEvent("A"), 1);
		final Achievement achievement1 = new Achievement("ADVENTUROUS", counter);
		final Achievement achievement2 = new Achievement("FRIEND_OF_THE_APPLICATION", counter);
		assertTrue(new AchievementDefaultSortOrder().compare(achievement2, achievement1) < 0);
		assertEquals(0,
				new AchievementDefaultSortOrder().compare(NamedAchievement.ADVENTUROUS.asAchievement(), achievement1));
		assertEquals(0, new AchievementDefaultSortOrder()
				.compare(NamedAchievement.FRIEND_OF_THE_APPLICATION.asAchievement(), achievement2));
		assertTrue(new AchievementDefaultSortOrder().compare(achievement1, achievement2) > 0);
		assertTrue(new AchievementDefaultSortOrder().compare(achievement1,
				NamedAchievement.FRIEND_OF_THE_APPLICATION.asAchievement()) > 0);
	}

	@Test
	public void testCompareAchievementsBasicNames() {
		final AchievementCounter counter1 = new AchievementCounter(new AchievementEvent("A"), 1);
		final AchievementCounter counter2 = new AchievementCounter(new AchievementEvent("B"), 1);
		final Achievement achievement1 = new Achievement("A", counter1);
		final Achievement achievement2 = new Achievement("B", counter1);
		final Achievement achievement3 = new Achievement("A", counter2);
		assertTrue(new AchievementDefaultSortOrder().compare(achievement1, achievement2) < 0);
		assertEquals(0, new AchievementDefaultSortOrder().compare(achievement1, achievement3));
		assertTrue(new AchievementDefaultSortOrder().compare(achievement2, achievement1) > 0);
	}

	@Test
	public void testCompareAchievementsMixedNames() {
		final AchievementCounter counter = new AchievementCounter(new AchievementEvent("A"), 1);
		final Achievement achievement1 = new Achievement("A", counter);
		final Achievement achievement2 = new Achievement("B", counter);
		assertTrue(new AchievementDefaultSortOrder().compare(achievement1,
				NamedAchievement.FRIEND_OF_THE_APPLICATION.asAchievement()) < 0);
		assertTrue(new AchievementDefaultSortOrder().compare(achievement2,
				NamedAchievement.ADVENTUROUS.asAchievement()) > 0);
	}
}
