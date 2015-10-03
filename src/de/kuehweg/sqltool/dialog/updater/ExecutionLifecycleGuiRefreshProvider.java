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
package de.kuehweg.sqltool.dialog.updater;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tracker mit passend annotiertem Refresh für die verschiedenen Phasen beim
 * Ausführen einer Anweisung ermitteln.
 *
 * @author Michael Kühweg
 */
public class ExecutionLifecycleGuiRefreshProvider {

	private final Set<ExecutionTracker> trackers;
	private final Set<ExecutionTracker> pendingUpdates;

	/**
	 * @param trackers
	 *            Collection mit den zu bearbeitenden Trackern
	 */
	public ExecutionLifecycleGuiRefreshProvider(final Collection<ExecutionTracker> trackers) {
		this.trackers = new HashSet<>(trackers);
		pendingUpdates = new HashSet<>();
	}

	/**
	 * @param phase
	 *            Phase im Lifecycle der Ausführung
	 * @param policy
	 *            Art und Weise der Aktualisierung (sofort, verzögert, nie)
	 * @param tracker
	 *            Tracker
	 * @return true wenn der abgefragte Tracker zur angegebenen Phase im
	 *         Lifecycle die angegebene Policy annotiert hat
	 */
	private boolean policyOnPhase(final ExecutionLifecyclePhase phase, final ExecutionLifecycleRefreshPolicy policy,
			final ExecutionTracker tracker) {
		for (final ExecutionLifecycleRefresh refresh : tracker.getClass()
				.getAnnotationsByType(ExecutionLifecycleRefresh.class)) {
			if (refresh.phase() == phase && refresh.refreshPolicy() == policy) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param phase
	 *            Phase im Lifecycle der Ausführung
	 * @param policy
	 *            Art und Weise der Aktualisierung (sofort, verzögert, nie)
	 * @param trackers
	 *            Zu prüfende Tracker
	 * @return Set mit allen Trackern aus der übergebenen Collection, die in der
	 *         angegebenen Lifecycle Phase die angegebene Policy annotiert
	 *         haben.
	 */
	private Set<ExecutionTracker> trackersWithPolicyOnPhase(final ExecutionLifecyclePhase phase,
			final ExecutionLifecycleRefreshPolicy policy, final Collection<ExecutionTracker> trackers) {
		if (trackers == null || trackers.isEmpty()) {
			return Collections.emptySet();
		}
		final Set<ExecutionTracker> requestedRefresh = new HashSet<>(trackers.size());
		for (final ExecutionTracker tracker : trackers) {
			if (policyOnPhase(phase, policy, tracker)) {
				requestedRefresh.add(tracker);
			}
		}
		return requestedRefresh;
	}

	/**
	 * @param phase
	 *            Phase im Lifecycle der Ausführung
	 * @param trackers
	 *            Zu prüfende Tracker
	 * @return Tracker aus der übergebenen Collections, die für die angegebene
	 *         Phase als immediate annotiert sind
	 */
	private Set<ExecutionTracker> immediateTrackersForPhase(final ExecutionLifecyclePhase phase,
			final Collection<ExecutionTracker> trackers) {
		return new HashSet<>(trackersWithPolicyOnPhase(phase, ExecutionLifecycleRefreshPolicy.IMMEDIATE, trackers));
	}

	/**
	 * @param phase
	 *            Phase im Lifecycle der Ausführung
	 * @param trackers
	 *            Zu prüfende Tracker
	 * @return Tracker aus der übergebenen Collections, die für die angegebene
	 *         Phase als delayed annotiert sind
	 */
	private Set<ExecutionTracker> delayedTrackersForPhase(final ExecutionLifecyclePhase phase,
			final Collection<ExecutionTracker> trackers) {
		return new HashSet<>(trackersWithPolicyOnPhase(phase, ExecutionLifecycleRefreshPolicy.DELAYED, trackers));
	}

	/**
	 * @return Tracker, die einen Refresh der Oberfläche anstoßen sollen, bevor
	 *         die Anweisung ausgeführt wird (z.B. um den Start zu
	 *         visualisieren). Vorher verzögerte Tracker werden ebenfalls
	 *         berücksichtigt.
	 */
	public Set<ExecutionTracker> beforeExecutionGuiRefresh() {
		pendingUpdates.addAll(delayedTrackersForPhase(ExecutionLifecyclePhase.BEFORE, trackers));
		return immediateTrackersForPhase(ExecutionLifecyclePhase.BEFORE, trackers);
	}

	/**
	 * @return Tracker, die während der laufenden Ausführung einen Refresh der
	 *         Oberfläche anstoßen sollen. Vorher verzögerte Tracker werden
	 *         ebenfalls berücksichtigt.
	 *
	 */
	public Set<ExecutionTracker> intermediateExecutionGuiRefresh() {
		pendingUpdates.addAll(delayedTrackersForPhase(ExecutionLifecyclePhase.INTERMEDIATE, trackers));
		return immediateTrackersForPhase(ExecutionLifecyclePhase.INTERMEDIATE, trackers);
	}

	/**
	 * @return Tracker, die zum Abschluss der Anweisung einen Refresh der
	 *         Oberfläche anstoßen sollen. Vorher verzögerte Tracker werden
	 *         ebenfalls berücksichtigt.
	 *
	 */
	public Set<ExecutionTracker> afterExecutionGuiRefresh() {
		pendingUpdates.addAll(delayedTrackersForPhase(ExecutionLifecyclePhase.AFTER, trackers));
		return immediateTrackersForPhase(ExecutionLifecyclePhase.AFTER, trackers);
	}

	/**
	 * @return Tracker, die im Fall eines Fehlers bei der Ausführung einen
	 *         Refresh der Oberfläche anstoßen sollen. Vorher verzögerte Tracker
	 *         werden ebenfalls berücksichtigt.
	 */
	public Set<ExecutionTracker> errorExecutionGuiRefresh() {
		pendingUpdates.addAll(delayedTrackersForPhase(ExecutionLifecyclePhase.ERROR, trackers));
		return immediateTrackersForPhase(ExecutionLifecyclePhase.ERROR, trackers);
	}

	/**
	 * @return Tracker, die in vorherigen Phasen für eine verzögerte Ausgabe
	 *         vorgemerkt wurden.
	 */
	public Set<ExecutionTracker> delayedExecutionGuiRefresh() {
		final Set<ExecutionTracker> delayed = new HashSet<>(pendingUpdates);
		pendingUpdates.clear();
		return delayed;
	}
}
