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

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import de.kuehweg.sqltool.common.sqlediting.SQLHistoryKeeper;

/**
 * Alle "Beteiligten", um im Dialog das Ergebnis einer SQL-Anweisung zu
 * visualisieren.
 * <ul>
 * <li>TextArea für die DB-Ausgaben</li>
 * <li>SQL Verlauf</li>
 * <li>Container (als HBox) in der die Ergebnistabelle aufgebaut wird</li>
 * </ul>
 * 
 * @author Michael Kühweg
 */
public class ExecutionResultEnvironment {

	private final TextArea dbOutput;
	private final SQLHistoryKeeper historyKeeper;
	private final HBox resultTableContainer;

	public static class Builder {

		private TextArea dbOutput;
		private SQLHistoryKeeper historyKeeper;
		private HBox resultTableContainer;

		public Builder dbOutput(final TextArea dbOutput) {
			this.dbOutput = dbOutput;
			return this;
		}

		public Builder historyKeeper(final SQLHistoryKeeper historyKeeper) {
			this.historyKeeper = historyKeeper;
			return this;
		}

		public Builder resultTableContainer(final HBox resultTableContainer) {
			this.resultTableContainer = resultTableContainer;
			return this;
		}

		public ExecutionResultEnvironment build() {
			return new ExecutionResultEnvironment(this);
		}
	}

	private ExecutionResultEnvironment(final Builder builder) {
		dbOutput = builder.dbOutput;
		historyKeeper = builder.historyKeeper;
		resultTableContainer = builder.resultTableContainer;
	}

	public TextArea getDbOutput() {
		return dbOutput;
	}

	public SQLHistoryKeeper getHistoryKeeper() {
		return historyKeeper;
	}

	public HBox getResultTableContainer() {
		return resultTableContainer;
	}
}