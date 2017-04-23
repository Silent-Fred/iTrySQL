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
package de.kuehweg.gamification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

/**
 * @author Michael Kühweg
 */
public class AchievementTest {

	@Test
	public void equality() {
		// derzeit kein inhaltlicher Vergleich
		final AchievementEvent eventA = new AchievementEvent("A");
		final AchievementEvent eventB = new AchievementEvent("B");
		final Achievement achievement1 = new Achievement("A", new AchievementCounter(eventA, 10),
				new AchievementCounter(eventB, 20));
		final Achievement achievement2 = new Achievement("A", new AchievementCounter(eventA, 10),
				new AchievementCounter(eventB, 20));
		assertNotEquals(achievement1, achievement2);
	}

	@Test
	public void basicCountdown() {
		final AchievementEvent eventA = new AchievementEvent("A");
		final AchievementEvent eventB = new AchievementEvent("B");

		final Achievement achievement = new Achievement("A", new AchievementCounter(eventA, 10),
				new AchievementCounter(eventB, 20));
		assertEquals(10, remains(achievement, eventA));
		assertEquals(0, achieved(achievement, eventA));
		assertEquals(20, remains(achievement, eventB));
		assertEquals(0, achieved(achievement, eventB));

		achievement.fire(eventA);
		assertEquals(9, remains(achievement, eventA));
		assertEquals(1, achieved(achievement, eventA));
		assertEquals(20, remains(achievement, eventB));
		assertEquals(0, achieved(achievement, eventB));
	}

	@Test
	public void basicCountdownBigStep() {
		final AchievementEvent eventA = new AchievementEvent("A");
		final AchievementEvent eventB = new AchievementEvent("B");

		final Achievement achievement = new Achievement("A", new AchievementCounter(eventA, 10),
				new AchievementCounter(eventB, 20));
		assertEquals(10, remains(achievement, eventA));
		assertEquals(0, achieved(achievement, eventA));
		assertEquals(20, remains(achievement, eventB));
		assertEquals(0, achieved(achievement, eventB));

		achievement.fire(eventB, 5);
		assertEquals(10, remains(achievement, eventA));
		assertEquals(0, achieved(achievement, eventA));
		assertEquals(15, remains(achievement, eventB));
		assertEquals(5, achieved(achievement, eventB));
	}

	@Test
	public void basicCountdownIrrelevantEvent() {
		final AchievementEvent eventA = new AchievementEvent("A");
		final AchievementEvent eventB = new AchievementEvent("B");
		final AchievementEvent eventC = new AchievementEvent("C");

		final Achievement achievement = new Achievement("A", new AchievementCounter(eventA, 10),
				new AchievementCounter(eventB, 20));
		assertEquals(10, remains(achievement, eventA));
		assertEquals(0, achieved(achievement, eventA));
		assertEquals(20, remains(achievement, eventB));
		assertEquals(0, achieved(achievement, eventB));

		achievement.fire(eventC);
		assertEquals(10, remains(achievement, eventA));
		assertEquals(0, achieved(achievement, eventA));
		assertEquals(20, remains(achievement, eventB));
		assertEquals(0, achieved(achievement, eventB));

		assertEquals(2, achievement.achieved().size());
	}

	@Test
	public void countdownStopsAtZero() {
		final AchievementEvent eventA = new AchievementEvent("A");
		final AchievementEvent eventB = new AchievementEvent("B");

		final Achievement achievement = new Achievement("A", new AchievementCounter(eventA, 10),
				new AchievementCounter(eventB, 20));

		achievement.fire(eventA, 5);
		assertEquals(5, remains(achievement, eventA));
		assertEquals(5, achieved(achievement, eventA));

		// Null wird direkt erreicht
		achievement.fire(eventA, 5);
		assertEquals(0, remains(achievement, eventA));
		assertEquals(10, achieved(achievement, eventA));

		achievement.fire(eventA, 5);
		assertEquals(0, remains(achievement, eventA));
		assertEquals(10, achieved(achievement, eventA));

		// Null wird direkt übersprungen
		achievement.fire(eventB, 25);
		assertEquals(0, remains(achievement, eventB));
		assertEquals(20, achieved(achievement, eventB));
	}

	@Test
	public void achievedYesOrNo() {
		final AchievementEvent eventA = new AchievementEvent("A");
		final AchievementEvent eventB = new AchievementEvent("B");

		final Achievement achievement = new Achievement("A", new AchievementCounter(eventA, 10),
				new AchievementCounter(eventB, 20));

		achievement.fire(eventA, 5);
		assertFalse(achievement.isAchieved());

		achievement.fire(eventA, 5);
		assertFalse(achievement.isAchieved());

		achievement.fire(eventB, 19);
		assertFalse(achievement.isAchieved());

		achievement.fire(eventB, 1);
		assertTrue(achievement.isAchieved());
	}

	@Test
	public void percentage() {
		final AchievementEvent eventA = new AchievementEvent("A");
		final AchievementEvent eventB = new AchievementEvent("B");

		final Achievement achievement = new Achievement("A", new AchievementCounter(eventA, 20),
				new AchievementCounter(eventB, 80));

		achievement.fire(eventA, 10);
		assertEquals(10, Math.round(achievement.calculateAchievedPercentage() * 100));

		achievement.fire(eventB, 10);
		assertEquals(20, Math.round(achievement.calculateAchievedPercentage() * 100));

		achievement.fire(eventA, 10);
		assertEquals(30, Math.round(achievement.calculateAchievedPercentage() * 100));

		// A geht unter Null - d.h. bleibt bei Null
		achievement.fire(eventA, 10);
		assertEquals(30, Math.round(achievement.calculateAchievedPercentage() * 100));

		achievement.fire(eventB, 69);
		assertEquals(99, Math.round(achievement.calculateAchievedPercentage() * 100));

		achievement.fire(eventB, 10);
		assertEquals(100, Math.round(achievement.calculateAchievedPercentage() * 100));
	}

	private int counterForEvent(final Collection<AchievementCounter> counter, final AchievementEvent event) {
		for (final AchievementCounter countdown : counter) {
			if (countdown.getEvent().equals(event)) {
				return countdown.getCounter();
			}
		}
		throw new IllegalArgumentException();
	}

	private int remains(final Achievement achievement, final AchievementEvent event) {
		return counterForEvent(achievement.remains(), event);
	}

	private int achieved(final Achievement achievement, final AchievementEvent event) {
		return counterForEvent(achievement.achieved(), event);
	}
}
