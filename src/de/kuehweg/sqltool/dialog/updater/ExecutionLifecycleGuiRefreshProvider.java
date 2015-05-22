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
 * Tracker mit passend annotiertem Refresh für die verschiedenen Phasen beim Ausführen
 * einer Anweisung ermitteln
 *
 * @author Michael Kühweg
 */
public class ExecutionLifecycleGuiRefreshProvider {

    private final Set<ExecutionTracker> trackers;
    private final Set<ExecutionTracker> pendingUpdates;

    public ExecutionLifecycleGuiRefreshProvider(
            final Collection<ExecutionTracker> trackers) {
        this.trackers = new HashSet<>(trackers);
        this.pendingUpdates = new HashSet<>();
    }

    private boolean policyOnPhase(final ExecutionLifecyclePhase phase,
            final ExecutionLifecycleRefreshPolicy policy, final ExecutionTracker tracker) {
        for (ExecutionLifecycleRefresh refresh : tracker.getClass().getAnnotationsByType(
                ExecutionLifecycleRefresh.class)) {
            if (refresh.phase() == phase && refresh.refreshPolicy() == policy) {
                return true;
            }
        }
        return false;
    }

    private Set<ExecutionTracker> trackersWithPolicyOnPhase(
            final ExecutionLifecycleRefreshPolicy policy,
            final ExecutionLifecyclePhase phase,
            final Collection<ExecutionTracker> trackers) {
        if (trackers == null || trackers.isEmpty()) {
            return Collections.emptySet();
        }
        Set<ExecutionTracker> requestedRefresh = new HashSet<>(trackers.size());
        for (ExecutionTracker tracker : trackers) {
            if (policyOnPhase(phase, policy, tracker)) {
                requestedRefresh.add(tracker);
            }
        }
        return requestedRefresh;
    }

    private Set<ExecutionTracker> immediateTrackersForPhase(ExecutionLifecyclePhase phase,
            final Collection<ExecutionTracker> trackers) {
        return new HashSet<>(trackersWithPolicyOnPhase(
                ExecutionLifecycleRefreshPolicy.IMMEDIATE,
                phase, trackers));
    }

    private Set<ExecutionTracker> delayedTrackersForPhase(ExecutionLifecyclePhase phase,
            final Collection<ExecutionTracker> trackers) {
        return new HashSet<>(trackersWithPolicyOnPhase(
                ExecutionLifecycleRefreshPolicy.DELAYED,
                phase, trackers));
    }

    public Set<ExecutionTracker> beforeExecutionGuiRefresh(
            final Collection<ExecutionTracker> trackers) {
        pendingUpdates.addAll(delayedTrackersForPhase(ExecutionLifecyclePhase.BEFORE,
                trackers));
        return immediateTrackersForPhase(ExecutionLifecyclePhase.BEFORE, trackers);
    }

    public Set<ExecutionTracker> intermediateExecutionGuiRefresh(
            final Collection<ExecutionTracker> trackers) {
        pendingUpdates.addAll(
                delayedTrackersForPhase(ExecutionLifecyclePhase.INTERMEDIATE, trackers));
        return immediateTrackersForPhase(ExecutionLifecyclePhase.INTERMEDIATE, trackers);
    }

    public Set<ExecutionTracker> afterExecutionGuiRefresh(
            final Collection<ExecutionTracker> trackers) {
        pendingUpdates.addAll(
                delayedTrackersForPhase(ExecutionLifecyclePhase.AFTER, trackers));
        return immediateTrackersForPhase(ExecutionLifecyclePhase.AFTER, trackers);
    }

    public Set<ExecutionTracker> errorExecutionGuiRefresh(
            final Collection<ExecutionTracker> trackers) {
        pendingUpdates.addAll(
                delayedTrackersForPhase(ExecutionLifecyclePhase.ERROR, trackers));
        return immediateTrackersForPhase(ExecutionLifecyclePhase.ERROR, trackers);
    }

    public Set<ExecutionTracker> delayedExecutionGuiRefresh(
            final Collection<ExecutionTracker> trackers) {
        Set<ExecutionTracker> delayed = new HashSet<>(pendingUpdates);
        pendingUpdates.clear();
        return delayed;
    }
}
