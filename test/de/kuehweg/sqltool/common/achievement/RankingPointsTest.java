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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.gamification.AchievementCounter;
import de.kuehweg.gamification.AchievementEvent;

/**
 * @author Michael Kühweg
 */
public class RankingPointsTest {

	private RankingPoints rankingPoints = new RankingPoints();

	private final Achievement achievement1 = createTestAchievement(1);
	private final Achievement achievement2 = createTestAchievement(2);
	private final Achievement achievement3 = createTestAchievement(3);
	private final Achievement achievement4 = createTestAchievement(4);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		rankingPoints = new RankingPoints();
		rankingPoints.register(achievement1, 1);
		rankingPoints.register(achievement2, 2);
		rankingPoints.register(achievement3, 3);
	}

	private Achievement createTestAchievement(final int number) {
		final AchievementEvent event = new AchievementEvent(String.valueOf(number));
		final Achievement achievement = new Achievement(String.valueOf(number), new AchievementCounter(event, number));
		return achievement;
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.common.achievement.RankingPoints#register(de.kuehweg.gamification.Achievement, int)}
	 * .
	 */
	@Test
	public void testRegister() {
		Assert.assertEquals(3, rankingPoints.getRegisteredAchievements().size());
		Assert.assertTrue(rankingPoints.getRegisteredAchievements().contains(achievement1));
		Assert.assertTrue(rankingPoints.getRegisteredAchievements().contains(achievement2));
		Assert.assertTrue(rankingPoints.getRegisteredAchievements().contains(achievement3));
		Assert.assertFalse(rankingPoints.getRegisteredAchievements().contains(achievement4));
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.common.achievement.RankingPoints#pointsAchieved(de.kuehweg.gamification.Achievement)}
	 * .
	 */
	@Test
	public void testPointsAchievedAchievement() {
		Assert.assertEquals(0, rankingPoints.pointsAchieved(achievement1));
		Assert.assertEquals(0, rankingPoints.pointsAchieved(achievement2));
		Assert.assertEquals(0, rankingPoints.pointsAchieved(achievement3));

		achievement1.event(new AchievementEvent("1"));
		Assert.assertEquals(1, rankingPoints.pointsAchieved(achievement1));

		// derzeit keine prozentuale Punktevergabe
		achievement2.event(new AchievementEvent("2"));
		Assert.assertEquals(0, rankingPoints.pointsAchieved(achievement2));
		achievement2.event(new AchievementEvent("2"));
		Assert.assertEquals(2, rankingPoints.pointsAchieved(achievement2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPointsAchievedAchievementException() {
		rankingPoints.pointsAchieved(achievement4);
	}

	@Test(expected = NullPointerException.class)
	public void testPointsAchievedAchievementNullPointerException() {
		rankingPoints.pointsAchieved(null);
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.common.achievement.RankingPoints#pointsAchieved()}
	 * .
	 */
	@Test
	public void testPointsAchieved() {
		Assert.assertEquals(0, rankingPoints.pointsAchieved());
		achievement2.event(new AchievementEvent("2"));
		// derzeit keine prozentuale Punktevergabe
		Assert.assertEquals(0, rankingPoints.pointsAchieved());
		achievement2.event(new AchievementEvent("2"));
		Assert.assertEquals(2, rankingPoints.pointsAchieved());
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.common.achievement.RankingPoints#maxPointsPossible()}
	 * .
	 */
	@Test
	public void testMaxPointsPossible() {
		Assert.assertEquals(6, rankingPoints.maxPointsPossible());
	}

	/**
	 * Test method for
	 * {@link de.kuehweg.sqltool.common.achievement.RankingPoints#pointsAchievableForAchievement(de.kuehweg.gamification.Achievement)}
	 * .
	 */
	@Test
	public void testPointsAchievable() {
		Assert.assertEquals(1, rankingPoints.pointsAchievableForAchievement(achievement1));
		Assert.assertEquals(2, rankingPoints.pointsAchievableForAchievement(achievement2));
		Assert.assertEquals(3, rankingPoints.pointsAchievableForAchievement(achievement3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPointsAchievableException() {
		rankingPoints.pointsAchievableForAchievement(achievement4);
	}

	@Test(expected = NullPointerException.class)
	public void testPointsAchievableNullPointerException() {
		rankingPoints.pointsAchievableForAchievement(null);
	}

}
