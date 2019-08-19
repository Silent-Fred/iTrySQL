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
import java.util.HashSet;

import javafx.application.Platform;

/**
 * GUI passend zur Ausführung von SQL-Anweisungen aktualisieren.
 *
 * @author Michael Kühweg
 */
public class ExecutionGuiRefresh implements Runnable {

	private final Collection<ExecutionTracker> trackers;

	/**
	 * @param trackers
	 *            Collection der jeweils neu zu zeichnenden Komponenten.
	 */
	public ExecutionGuiRefresh(final Collection<ExecutionTracker> trackers) {
		this.trackers = new HashSet<>(trackers);
	}

	/**
	 * Den Inhalt aller registrierten Tracker aktualisieren.
	 */
	private void showAllAttachedTrackers() {
		trackers.stream().forEach((tracker) -> {
			tracker.show();
		});
	}

	/**
	 * Aktualisierung der Oberfläche - Einstiegspunkt für den Aufruf.
	 */
	public void show() {
		// Aktualisierung mit dem UI-Thread abstimmen
		if (trackers != null && !trackers.isEmpty()) {
			Platform.runLater(this);
		}
	}

	@Override
	// Runnable Interface implementiert, um synchron mit dem UI-Thread zu
	// arbeiten
	public void run() {
		showAllAttachedTrackers();
	}

}
