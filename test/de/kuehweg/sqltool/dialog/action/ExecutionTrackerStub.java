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
package de.kuehweg.sqltool.dialog.action;

import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecyclePhase;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefresh;
import de.kuehweg.sqltool.dialog.updater.ExecutionLifecycleRefreshPolicy;
import de.kuehweg.sqltool.dialog.updater.ExecutionTracker;

/**
 * Stub für Tests mit ExecutionTracker. Ohne Refresh, wird daher nicht an den
 * UI-Thread verwiesen.
 *
 * @author Michael Kühweg
 */
@ExecutionLifecycleRefresh(phase = ExecutionLifecyclePhase.ERROR, refreshPolicy = ExecutionLifecycleRefreshPolicy.NONE)
public class ExecutionTrackerStub implements ExecutionTracker {

	private int beforeExecutionCalls;
	private int intermediateUpdateCalls;
	private int afterExecutionCalls;
	private int errorOnExecutionCalls;
	private int showCalls;

	private String message;

	@Override
	public void beforeExecution() {
		beforeExecutionCalls++;
	}

	@Override
	public void intermediateUpdate(final StatementExecutionInformation executionInfo) {
		intermediateUpdateCalls++;
	}

	@Override
	public void afterExecution() {
		afterExecutionCalls++;
	}

	@Override
	public void errorOnExecution(final String message) {
		this.message = message;
		errorOnExecutionCalls++;
	}

	@Override
	public void show() {
		showCalls++;
	}

	public int getBeforeExecutionCalls() {
		return beforeExecutionCalls;
	}

	public void setBeforeExecutionCalls(final int beforeExecutionCalls) {
		this.beforeExecutionCalls = beforeExecutionCalls;
	}

	public int getIntermediateUpdateCalls() {
		return intermediateUpdateCalls;
	}

	public void setIntermediateUpdateCalls(final int intermediateUpdateCalls) {
		this.intermediateUpdateCalls = intermediateUpdateCalls;
	}

	public int getAfterExecutionCalls() {
		return afterExecutionCalls;
	}

	public void setAfterExecutionCalls(final int afterExecutionCalls) {
		this.afterExecutionCalls = afterExecutionCalls;
	}

	public int getErrorOnExecutionCalls() {
		return errorOnExecutionCalls;
	}

	public void setErrorOnExecutionCalls(final int errorOnExecutionCalls) {
		this.errorOnExecutionCalls = errorOnExecutionCalls;
	}

	public int getShowCalls() {
		return showCalls;
	}

	public void setShowCalls(final int showCalls) {
		this.showCalls = showCalls;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

}
