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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.gamification.AchievementCounter;
import de.kuehweg.gamification.AchievementEvent;

/**
 * Test zur Absicherung der ermittelten Ränge.
 *
 * @author Michael Kühweg
 */
public class NamedRankTest {

	private RankingPoints rankingPoints;

	private Achievement[] achievements;

	@Before
	public void setUp() {
		achievements = new Achievement[100];
		rankingPoints = new RankingPoints();
		for (int i = 0; i < 100; i++) {
			final AchievementEvent event = new AchievementEvent(String.valueOf(i));
			final Achievement achievement = new Achievement(String.valueOf(i), new AchievementCounter(event, 1));
			achievements[i] = achievement;
			rankingPoints.register(achievement, 1);
		}
	}

	/**
	 * Test zur Absicherung, dass die Werte im Ranking nicht ungeprüft verändert
	 * werden. Dies könnte sonst zu ungewollt schnellen oder langsamen Sprüngen
	 * im Rangaufstieg führen.
	 */
	@Test
	public void test() {
		assertEquals(NamedRank.TODDLER, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(24);
		assertEquals(NamedRank.TODDLER, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(25);
		assertEquals(NamedRank.PUPIL, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(49);
		assertEquals(NamedRank.PUPIL, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(50);
		assertEquals(NamedRank.STUDENT, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(74);
		assertEquals(NamedRank.STUDENT, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(75);
		assertEquals(NamedRank.MASTER, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(99);
		assertEquals(NamedRank.MASTER, NamedRank.achievedRankInRankingPoints(rankingPoints));
		fireEventsForFirstNAchievements(100);
		assertEquals(NamedRank.GENIOUS, NamedRank.achievedRankInRankingPoints(rankingPoints));
	}

	private void fireEventsForFirstNAchievements(final int n) {
		for (int i = 0; i < n && i < achievements.length; i++) {
			achievements[i].event(new AchievementEvent(String.valueOf(i)), 1);
		}
	}
}
