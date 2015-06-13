/*
 * Copyright (c) 2013-2015, Michael Kühweg
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
package de.kuehweg.sqltool.dialog.action;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.concurrent.Task;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.common.sqlediting.StatementString;
import de.kuehweg.sqltool.database.execution.StatementExecution;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.updater.ExecutionGuiRefresh;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleGuiRefreshProvider;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;

/**
 * Task zur Ausführung von SQL-Anweisungen und Aktualisierung der Oberfläche
 *
 * @author Michael Kühweg
 */
public class ExecutionTask extends Task<Void> {

	// Anzahl Millisekunden zwischen den verzögerten Refreshes
	private static final long REFRESH_DELAY = 500;

	private final Statement statement;
	private final String sql;
	private final Collection<ExecutionTracker> trackers;
	private int maxRows;

	public ExecutionTask(final Statement statement, final String sql) {
		this.statement = statement;
		this.sql = sql;
		trackers = new HashSet<>();
	}

	public void setMaxRows(final int maxRows) {
		this.maxRows = maxRows;
	}

	public void attach(final ExecutionTracker... trackers) {
		if (trackers != null) {
			for (final ExecutionTracker tracker : trackers) {
				this.trackers.add(tracker);
			}
		}
	}

	public void attach(final Collection<ExecutionTracker> trackers) {
		if (trackers != null) {
			this.trackers.addAll(trackers);
		}
	}

	private void beforeExecution() {
		for (final ExecutionTracker tracker : trackers) {
			tracker.beforeExecution();
		}
	}

	private void intermediateUpdate(
			final StatementExecutionInformation executionInfo) {
		for (final ExecutionTracker tracker : trackers) {
			tracker.intermediateUpdate(executionInfo);
		}
	}

	private void afterExecution() {
		for (final ExecutionTracker tracker : trackers) {
			tracker.afterExecution();
		}
	}

	private void errorUpdate(final String message) {
		for (final ExecutionTracker tracker : trackers) {
			tracker.errorOnExecution(message);
		}
	}

	@Override
	protected Void call() throws Exception {
		final ExecutionLifecycleGuiRefreshProvider lifecycleRefresh = new ExecutionLifecycleGuiRefreshProvider(
				trackers);
		try {
			final List<StatementString> statements = new StatementExtractor()
					.getStatementsFromScript(sql);
			statement.setMaxRows(maxRows);

			// before execution
			beforeExecution();
			refreshBeforePhase(lifecycleRefresh);

			// during execution
			long lastDelayedRefresh = System.currentTimeMillis();
			final Iterator<StatementString> queryIterator = statements
					.iterator();
			while (queryIterator.hasNext() && !isCancelled()) {
				final StatementString singleQuery = queryIterator.next();
				if (!singleQuery.isEmpty()) {
					intermediateUpdate(new StatementExecution(singleQuery)
							.execute(statement));
					// UI immediate
					refresh(lifecycleRefresh.intermediateExecutionGuiRefresh());
					// UI delayed
					if (System.currentTimeMillis() - lastDelayedRefresh > REFRESH_DELAY) {
						lastDelayedRefresh = System.currentTimeMillis();
						refresh(lifecycleRefresh.delayedExecutionGuiRefresh());
					}
				}
			}
			// eventuell noch pending refreshes
			refresh(lifecycleRefresh.delayedExecutionGuiRefresh());

			// dann abschließen
			afterExecution();
			refreshAfterPhase(lifecycleRefresh);
		} catch (final SQLException ex) {
			final String message = ex.getLocalizedMessage() + " (SQL-State: "
					+ ex.getSQLState() + ")";
			errorUpdate(message);
			refreshErrorPhase(lifecycleRefresh);
		} finally {
			statement.close();
		}
		return null;
	}

	private void refresh(final Collection<ExecutionTracker> trackersForRefresh) {
		if (trackersForRefresh != null && !trackersForRefresh.isEmpty()) {
			new ExecutionGuiRefresh(trackersForRefresh).show();
		}
	}

	private void refreshErrorPhase(
			final ExecutionLifecycleGuiRefreshProvider lifecycleRefresh) {
		Set<ExecutionTracker> trackersForRefresh;
		trackersForRefresh = lifecycleRefresh.errorExecutionGuiRefresh();
		trackersForRefresh
				.addAll(lifecycleRefresh.delayedExecutionGuiRefresh());
		refresh(trackersForRefresh);
	}

	private void refreshAfterPhase(
			final ExecutionLifecycleGuiRefreshProvider lifecycleRefresh) {
		Set<ExecutionTracker> trackersForRefresh;
		// UI am Ende der AFTER Phase komplett refreshed
		trackersForRefresh = lifecycleRefresh.afterExecutionGuiRefresh();
		trackersForRefresh
				.addAll(lifecycleRefresh.delayedExecutionGuiRefresh());
		refresh(trackersForRefresh);
	}

	private void refreshBeforePhase(
			final ExecutionLifecycleGuiRefreshProvider lifecycleRefresh) {
		Set<ExecutionTracker> trackersForRefresh;
		// UI am Ende der BEFORE Phase komplett refreshed
		trackersForRefresh = lifecycleRefresh.beforeExecutionGuiRefresh();
		trackersForRefresh
				.addAll(lifecycleRefresh.delayedExecutionGuiRefresh());
		refresh(trackersForRefresh);
	}

}
