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

/**
 * Verknüpfung von Achievements mit zugeordneter Punktzahl.
 *
 * @author Michael Kühweg
 */
public class PointsByAchievement {

	private final Achievement achievement;

	private final int points;

	/**
	 * @param achievement
	 *            Achievement
	 * @param points
	 *            Punkte für den Anwender, wenn das Achievement komplett erfüllt
	 *            ist
	 */
	public PointsByAchievement(final Achievement achievement, final int points) {
		this.achievement = achievement;
		this.points = points;
	}

	/**
	 * @return Achievement
	 */
	public Achievement getAchievement() {
		return achievement;
	}

	/**
	 * @return Punkte für das Erreichen des Achievements
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Berechnet die derzeit gültige Anzahl Punkte für das Achievment.
	 *
	 * @return Bei erfülltem Achievement volle Punktzahl wie im Konstruktor
	 *         angegeben, sonst Null Punkte.
	 */
	public int calculateCurrentlyAchievedPoints() {
		return achievement.isAchieved() ? points : 0;
	}
}
