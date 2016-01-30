/*
 * Copyright (c) 2015-2016, Michael Kühweg
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael Kühweg
 */
public class Achievement extends Observable {

	private final String name;

	private final AchievementCounter[] requirements;

	private final Map<AchievementEvent, AchievementCounter> eventsWithCurrentCountdown;

	/**
	 * @param name
	 *            Name des Achievements. Wird z.B. verwendet, um das Achievement
	 *            benannt speichern und wieder laden zu können. Soll das
	 *            Achievement also persistent sein (können), dann muss der Name
	 *            eindeutig vergeben werden.
	 * @param requirements
	 *            Jedes Achievement muss mit den relevanten AchievmentEvents
	 *            inklusive der erforderlichen Anzahl initialisiert werden.
	 */
	public Achievement(final String name, final AchievementCounter... requirements) {
		this.name = name;
		// Initialzustand merken, um prozentuale Anteile berechnen zu können
		this.requirements = Arrays.copyOf(requirements, requirements.length);
		eventsWithCurrentCountdown = new ConcurrentHashMap<>(requirements.length * 2 + 100);
		for (final AchievementCounter requirement : requirements) {
			eventsWithCurrentCountdown.put(requirement.getEvent(),
					new AchievementCounter(requirement.getEvent(), requirement.getCounter()));
		}
	}

	/**
	 * @return Name des Achievements
	 */
	public String getName() {
		return name;
	}

	/**
	 * Registriert das Auftreten eines Ereignisses.
	 *
	 * @param event
	 *            Art des Ereignisses. Für das Achievement irrelevante
	 *            Ereignisse werden ignoriert. (somit kann ein Event problemlos
	 *            an alle Achievements übergeben werden)
	 */
	public void event(final AchievementEvent event) {
		event(event, 1);
	}

	/**
	 * Registriert das n-fache Auftreten eines Ereignisses.
	 *
	 * @param event
	 *            Art des Ereignisses. Für das Achievement irrelevante
	 *            Ereignisse werden ignoriert. (somit kann ein Event problemlos
	 *            an alle Achievements übergeben werden)
	 * @param count
	 *            Anzahl des Auftretens des Events, dessen Umfang o.ä. Beispiel:
	 *            Achievement zählt Anzahl Millisekunden durchgeführter
	 *            Berechnungen; dann kann z.B. eine Berechnung mit 25ms durch
	 *            einen Aufruf mit count = 25 registriert werden
	 */
	public void event(final AchievementEvent event, final int count) {
		final AchievementCounter countdown = eventsWithCurrentCountdown.get(event);
		if (countdown != null && countdown.getCounter() > 0) {
			setChanged();
			notifyObservers();
			synchronized (countdown) {
				countdown.setCounter(countdown.getCounter() > count ? countdown.getCounter() - count : 0);
			}
		}
	}

	/**
	 * Berechnet den Grad der Zielerreichung.
	 *
	 * @return Angabe der prozentualen Zielerreichung, z.B. 0.25 bei 25%
	 *         Zielerreichung
	 */
	public double calculateAchievedPercentage() {
		double required = 0.0;
		double remains = 0.0;
		for (final AchievementCounter requirement : requirements) {
			required += requirement.getCounter();
		}
		for (final AchievementCounter currentCountdown : eventsWithCurrentCountdown.values()) {
			remains += currentCountdown.getCounter();
		}
		return (required - remains) / required;
	}

	/**
	 * Zielerreichung abfragen.
	 *
	 * @return true wenn alle für das Achievement erforderlichen
	 *         AchievementEvents bereits oft genug ausgelöst wurden, wenn also
	 *         der Countdown bei allen zum Achievement gehörenden
	 *         AchievementEvents auf 0 ist.
	 */
	public boolean isAchieved() {
		for (final AchievementCounter countdown : eventsWithCurrentCountdown.values()) {
			if (countdown != null && countdown.getCounter() > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Liefert die für das Achievment relevanten AchievementEvents zusammen mit
	 * der bereits erreichten Anzahl Events.
	 *
	 * @return Liefert die aktuellen Zählerstände der bereits erreichten Events.
	 */
	public Set<AchievementCounter> achieved() {
		final Set<AchievementCounter> achieved = new HashSet<>();
		for (final AchievementCounter requirement : requirements) {
			final AchievementCounter countdown = eventsWithCurrentCountdown.get(requirement.getEvent());
			// folgendes sollte eigentlich immer der Fall sein
			if (countdown != null) {
				achieved.add(new AchievementCounter(requirement.getEvent(),
						requirement.getCounter() - countdown.getCounter()));
			}
		}
		return achieved;
	}

	/**
	 * Liefert die für das Achievement relevanten AchievementEvents zusammen mit
	 * der noch verbleibenden Anzahl erforderlicher Events bis zur
	 * Zielerreichung.
	 *
	 * @return Liefert die aktuellen Zählerstände der noch fehlenden Events.
	 */
	public Set<AchievementCounter> remains() {
		final Set<AchievementCounter> remains = new HashSet<>();
		for (final AchievementCounter requirement : requirements) {
			final AchievementCounter countdown = eventsWithCurrentCountdown.get(requirement.getEvent());
			// folgendes sollte eigentlich immer der Fall sein
			if (countdown != null) {
				remains.add(new AchievementCounter(requirement.getEvent(), countdown.getCounter()));
			}
		}
		return remains;
	}
}
