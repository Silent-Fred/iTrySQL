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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Zeitlich versetztes Speichern von Änderungen an Achievements ohne explizit
 * erforderlichen Aufruf einer Speichermethode.
 *
 * @author Michael Kühweg
 */
public class DelayedAutoPersistentAchievementsSet implements PropertyChangeListener, Runnable {

	private static final int MIN_DELAY_IN_SECONDS = 1;

	private final Set<Achievement> achievements = new HashSet<>();

	private final AchievementPersister persister;

	private boolean changed;

	private int delayInSeconds;

	public DelayedAutoPersistentAchievementsSet(final AchievementPersister persister) {
		this.persister = persister;
		delayInSeconds = MIN_DELAY_IN_SECONDS;
	}

	public synchronized void registerAchievement(final Achievement achievement) {
		achievements.add(achievement);
		achievement.addPropertyChangeListener(this);
	}

	/**
	 * @return the delayInSeconds
	 */
	public int getDelayInSeconds() {
		return delayInSeconds;
	}

	/**
	 * @param delayInSeconds the delayInSeconds to set
	 */
	public void setDelayInSeconds(final int delayInSeconds) {
		this.delayInSeconds = delayInSeconds < MIN_DELAY_IN_SECONDS ? MIN_DELAY_IN_SECONDS
				: delayInSeconds;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (!changed) {
			changed = true;
			final ScheduledExecutorService executorService = Executors
					.newSingleThreadScheduledExecutor();
			executorService.schedule(this, delayInSeconds, TimeUnit.SECONDS);
			executorService.shutdown();
		}
	}

	public synchronized void persist() {
		changed = false;
		if (persister != null) {
			persister.persist(new ArrayList<>(achievements));
		}
	};

	@Override
	public void run() {
		persist();
	}
}
