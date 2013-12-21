/*
 * Copyright (c) 2013, Michael Kühweg
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
package de.kuehweg.sqltool.dialog.environment;

import de.kuehweg.sqltool.database.ConnectionHolder;

/**
 * Sammlung aller "Beteiligten", die eine Ausführung einer SQL-Anweisung
 * beeinflussen.
 * <ul>
 * <li>Flag, ob die Ergebnismenge begrenzt werden soll</li>
 * <li>ConnectionHolder für die Datenbankverbindung</li>
 * </ul>
 * 
 * @author Michael Kühweg
 */
public class ExecutionInputEnvironment {

	private final boolean limitMaxRows;
	private final ConnectionHolder connectionHolder;

	/**
	 * Builder-Pattern
	 */
	public static class Builder {

		private boolean limitMaxRows = true;
		private final ConnectionHolder connectionHolder;

		public Builder(final ConnectionHolder connectionHolder) {
			this.connectionHolder = connectionHolder;
		}

		public Builder limitMaxRows(final boolean limitMaxRows) {
			this.limitMaxRows = limitMaxRows;
			return this;
		}

		public ExecutionInputEnvironment build() {
			return new ExecutionInputEnvironment(this);
		}
	}

	private ExecutionInputEnvironment(final Builder builder) {
		limitMaxRows = builder.limitMaxRows;
		connectionHolder = builder.connectionHolder;
	}

	public boolean isLimitMaxRows() {
		return limitMaxRows;
	}

	public ConnectionHolder getConnectionHolder() {
		return connectionHolder;
	}

}
