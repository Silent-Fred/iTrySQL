/*
 * Copyright (c) 2019, Michael Kühweg
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Michael Kühweg
 */
public class AchievementUserPreferencesPersister implements AchievementPersister {

	@Override
	public void persist(final Collection<Achievement> achievements) {
		final Preferences preferences = gamificationPreferences();
		for (final AchievementCounter counter : countersFrom(achievements)) {
			if (counter.getCounter() > preferences.getInt(counter.getEvent().getUniqueId(), 0)) {
				preferences.putInt(counter.getEvent().getUniqueId(), counter.getCounter());
			}
		}
		try {
			preferences.flush();
		} catch (final BackingStoreException e) {
			// unschön, aber derzeit sind Achievements nicht "mission critical",
			// ein einfacher Eintrag im Log reicht deshalb aus
			Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
		}
	}

	@Override
	public Collection<AchievementCounter> read() {
		final Preferences preferences = gamificationPreferences();
		final Collection<AchievementCounter> counters = new LinkedList<>();
		try {
			for (final String key : preferences.keys()) {
				counters.add(new AchievementCounter(new AchievementEvent(key), preferences.getInt(key, 0)));
			}
		} catch (final BackingStoreException e) {
			// unschön, aber derzeit sind Achievements nicht "mission critical",
			// ein einfacher Eintrag im Log reicht
			Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
			return Collections.emptyList();
		}
		return counters;
	}

	private Preferences gamificationPreferences() {
		return Preferences.userNodeForPackage(getClass()).node(getClass().getSimpleName());
	}

	private Collection<AchievementCounter> countersFrom(final Collection<Achievement> achievements) {
		final Collection<AchievementCounter> counters = new ArrayList<>();
		achievements.stream().forEach(achievement -> counters.addAll(achievement.achieved()));
		return counters;
	}
}
