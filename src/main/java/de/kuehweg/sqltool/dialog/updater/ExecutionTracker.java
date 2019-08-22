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

import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;

/**
 * Schnittstelle für Oberflächenbestandteile, deren Zustand sich ändert, wenn
 * eine SQL-Anweisung ausgeführt wird. Während einzelner Phasen der Ausführung
 * kann der innere Zustand des Trackers aktualisiert werden, die UI zu einem
 * späteren Zeitpunkt.
 *
 * @author Michael Kühweg
 */
public interface ExecutionTracker {

	/**
	 * Wird vor dem Beginn der Ausführung aufgerufen.
	 */
	void beforeExecution();

	/**
	 * Wird nach jeder einzelnen Anweisung aufgerufen.
	 *
	 * @param executionInfo Das Ergebnis der zuletzt ausgeführen SQL Anweisung
	 */
	void intermediateUpdate(StatementExecutionInformation executionInfo);

	/**
	 * Wird nach Abschluss aller SQL Anweisungen aufgerufen.
	 */
	void afterExecution();

	/**
	 * Wird aufgerufen, wenn ein Fehler bei der Ausführung aufgetreten ist.
	 *
	 * @param message Fehlermeldung, z.B. SQL-State
	 */
	void errorOnExecution(String message);

	/**
	 * Aktualisierung der UI.
	 */
	void show();
}
