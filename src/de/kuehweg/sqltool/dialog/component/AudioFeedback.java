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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.ProvidedAudioClip;
import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecyclePhase;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefresh;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;

/**
 * Akustische Rückmeldung zu SQL-Anweisungen.
 *
 * @author Michael Kühweg
 */
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.AFTER)
public class AudioFeedback implements ExecutionTracker {

	@Override
	public void beforeExecution() {
		// kein Audio Feedback vor der Ausführung
	}

	@Override
	public void intermediateUpdate(final StatementExecutionInformation executionInfo) {
		// kein Audio Feedback während der Ausführung
	}

	@Override
	public void afterExecution() {
		// kein inhaltlicher Update, show() - naja... in dem Fall wenig
		// passender Name -
		// gibt den Ton aus
	}

	@Override
	public void errorOnExecution(final String message) {
		// derzeit kein Ton bei Fehler vorgesehen
	}

	@Override
	public void show() {
		final ProvidedAudioClip audioClip = UserPreferencesManager.getSharedInstance().getBeepAudioClip();
		if (audioClip != null) {
			final double volume = UserPreferencesManager.getSharedInstance().getBeepVolume();
			if (volume > 0.0) {
				audioClip.play(volume);
			}
		}
	}

}
