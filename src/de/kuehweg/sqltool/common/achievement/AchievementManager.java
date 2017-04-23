/*
 * Copyright (c) 2015-2017, Michael Kühweg
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

import java.util.Arrays;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.gamification.AchievementEvent;
import de.kuehweg.gamification.AchievementPersister;
import de.kuehweg.gamification.DelayedAutoPersistentAchievementsSet;

/**
 * Zentrale Anlaufstelle, um die Achievements anzusprechen und zu persistieren.
 * Als Singleton implementiert, damit einfach von unterschiedlichsten Stellen
 * darauf zugegriffen werden kann.
 * <p>
 * Basis für die Achievements sind die Elemente der NamedAchievement
 * Enumeration.
 * <p>
 * Der aktuelle Fortschritt wird automatisch gespeichert, eine explizite
 * Speicherung ist daher in der Regel nicht erforderlich. Da das Speichern im
 * Hintergrund leicht zeitversetz erfolgt, sollte jedoch bei Programmende ein
 * expliziter Aufruf erfolgen.
 *
 * @author Michael Kühweg
 */
public final class AchievementManager {

	private static final AchievementManager INSTANCE;

	static {
		INSTANCE = new AchievementManager();
	}

	private final Achievement[] achievements = new Achievement[NamedAchievement.values().length];

	private DelayedAutoPersistentAchievementsSet autoPersistence;

	private AchievementPersister persister;

	private RankingPoints pointsSystem;

	/**
	 * Singleton, wird nicht von außerhalb instanziiert.
	 */
	private AchievementManager() {
		resetAllAchievements();
		pointsSystem = new DefaultRankingPoints();
	}

	/**
	 * @return Die für die gesamte Applikation gültige Instanz der Achievements.
	 */
	public static AchievementManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Setzt alle Achievements auf ihren Ursprungszustand zurück. Ein
	 * gespeicherter Fortschritt muss immer auf diesen Ursprungszustand
	 * angewendet werden, um den aktuellen Stand der Achievements zu berechnen.
	 */
	public void resetAllAchievements() {
		int arrayIndex = 0;
		for (final NamedAchievement namedAchievement : NamedAchievement.values()) {
			achievements[arrayIndex++] = namedAchievement.asAchievement();
		}
	}

	/**
	 * Ereignis mit Gewichtung / Anzahl an alle Achievements weiterreichen.
	 *
	 * @param event
	 *            Ereignis, das aufgetreten ist
	 * @param count
	 *            Anzahl, Gewichtung o.ä. Wert, der von einem Achievement im
	 *            Zusammenhang mit diesem Ereignis verarbeitet wird
	 */
	public void fireEvent(final AchievementEvent event, final int count) {
		for (final Achievement achievement : achievements) {
			achievement.fire(event, count);
		}
	}

	/**
	 * @return Den aktuell eingestellten AchievementPersister.
	 */
	public AchievementPersister getPersister() {
		return persister;
	}

	/**
	 * Setzt einen AchievementPersister und bestimmt damit die Art der
	 * Speicherung des Fortschritts (z.B. als XML Datei, verschlüsselte XML
	 * Datei, evtl. über Preferences,...).
	 *
	 * @param persister
	 *            Der zu verwendende {@link AchievementPersister}
	 */
	public void setPersister(final AchievementPersister persister) {
		this.persister = persister;
		registerForAutoPersist();
	}

	/**
	 * Meldet den AchievementManager zur automatischen Speicherung mittels des
	 * eingetragenen {@link AchievementPersister} an.
	 */
	private void registerForAutoPersist() {
		autoPersistence = new DelayedAutoPersistentAchievementsSet(persister);
		for (final Achievement achievement : achievements) {
			autoPersistence.registerAchievement(achievement);
		}
	}

	/**
	 * Änderungen an Achievements werden automatisch gespeichert. Wegen des
	 * leichten Zeitverzugs beim automatischen Speichern kann mit dieser Methode
	 * aber auch ein Speichern explizt angestoßen werden (z.B. bei
	 * Programmende).
	 */
	public void flush() {
		if (persister != null) {
			persister.persist(Arrays.asList(achievements));
		}
	}

	/**
	 * @return Aktuelles Punktesystem
	 */
	public RankingPoints getPointsSystem() {
		return pointsSystem;
	}

	/**
	 * Setzt das zu verwendende Punktesystem. Standardeinstellung beim Aufbau
	 * des Singletons sind die DefaultRankingPoints. Diese Einstellung kann hier
	 * bei Bedarf, z.B. für Unit-Tests, geändert werden.
	 *
	 * @param pointsSystem
	 */
	public void setPointsSystem(final RankingPoints pointsSystem) {
		this.pointsSystem = pointsSystem;
	}

	/**
	 * @return Anzahl der bisher erreichten Punkte
	 */
	public int getPointsAchieved() {
		if (pointsSystem == null) {
			return 0;
		}
		return pointsSystem.pointsAchieved();
	}
}
