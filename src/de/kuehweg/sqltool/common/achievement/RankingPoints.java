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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.gamification.PointsByAchievement;

/**
 * Punktesystem für die Fortschrittskontrolle.
 *
 * @author Michael Kühweg
 */
public class RankingPoints {

	private final List<PointsByAchievement> pointsByAchievement = new ArrayList<>();

	/**
	 * @param achievement
	 *            Achievement
	 * @param points
	 *            Punkte beim Erreichen des Achievements
	 */
	public void register(final Achievement achievement, final int points) {
		pointsByAchievement.add(new PointsByAchievement(achievement, points));
	}

	/**
	 * @return Collection mit den registrieren Achievements
	 */
	public Collection<Achievement> getRegisteredAchievements() {
		final Collection<Achievement> registeredAchievements = new ArrayList<>();
		for (final PointsByAchievement entry : pointsByAchievement) {
			registeredAchievements.add(entry.getAchievement());
		}
		return registeredAchievements;
	}

	/**
	 * Liefert das registrierte Tupel aus Achievement und Punktzahl für ein
	 * übergebenes Achievement.
	 *
	 * @param achievement
	 *            Achievement
	 * @return Tupel (Achievement, Punktzahl) als {@link PointsByAchievement}
	 * @throws IllegalArgumentException
	 *             Nicht registrierte Achivements werfen eine
	 *             {@link IllegalArgumentException}
	 * @throws NullPointerException
	 *             Beim Aufruf mit null als Achievement
	 */
	private PointsByAchievement getRegisteredPointsByAchievement(final Achievement achievement)
			throws IllegalArgumentException, NullPointerException {
		for (final PointsByAchievement entry : pointsByAchievement) {
			if (achievement.equals(entry.getAchievement())) {
				return entry;
			}
		}
		throw new IllegalArgumentException("Unregistered achievement: " + achievement.getName());
	}

	/**
	 * Bisher erreichte Punktzahl für ein einzelnes Achievement im Punktesystem
	 * berechnen.
	 *
	 * @param achievement
	 *            Achievement
	 * @return Aktuell erreichte Punktzahl für das Achievement
	 * @throws IllegalArgumentException
	 *             Nicht registrierte Achivements werfen eine
	 *             {@link IllegalArgumentException}
	 * @throws NullPointerException
	 *             Beim Aufruf mit null als Achievement
	 */
	public int pointsAchieved(final Achievement achievement) throws IllegalArgumentException, NullPointerException {
		int pointsAchieved = 0;
		final PointsByAchievement points = getRegisteredPointsByAchievement(achievement);
		pointsAchieved = points.calculateCurrentlyAchievedPoints();
		return pointsAchieved;
	}

	/**
	 * @return Summe der bislang erreichten Punkte über alle registrierten
	 *         Achievements
	 */
	public int pointsAchieved() {
		int pointsAchieved = 0;
		for (final PointsByAchievement points : pointsByAchievement) {
			pointsAchieved += points.calculateCurrentlyAchievedPoints();
		}
		return pointsAchieved;
	}

	/**
	 * @return Anzahl der insgesamt erreichbaren Punkte in diesem Punktesystem
	 */
	public int maxPointsPossible() {
		int max = 0;
		for (final PointsByAchievement points : pointsByAchievement) {
			max += points.getPoints();
		}
		return max;
	}

	/**
	 * @param achievement
	 *            Achievement - muss registriert sein
	 * @return Punkte, die es für das Erreichen des Achievements gibt
	 * @throws IllegalArgumentException
	 *             Nicht registrierte Achievements werfen eine
	 *             {@link IllegalArgumentException}
	 * @throws NullPointerException
	 *             Beim Aufruf mit null als Achievement
	 */
	public int pointsAchievableForAchievement(final Achievement achievement)
			throws IllegalArgumentException, NullPointerException {
		final PointsByAchievement entry = getRegisteredPointsByAchievement(achievement);
		return entry.getPoints();
	}
}