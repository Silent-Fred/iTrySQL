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
import java.util.Collection;
import java.util.List;

/**
 * GUI-Updater für einer reguläre Ausführung von Anweisungen in der Oberfläche
 *
 * @author Michael Kühweg
 */
public class StandardLifecycleGuiUpdaterProvider implements ExecutionLifecycleGuiUpdaterProvider {

    @Override
    public AbstractExecutionGuiUpdater beforeExecutionGuiUpdater(
            Collection<ExecutionTracker> trackers) {
        return new BeforeExecutionGuiUpdater(trackers);
    }

    @Override
    public AbstractExecutionGuiUpdater intermediateExecutionGuiUpdater(
            List<StatementExecutionInformation> executionInfos,
            Collection<ExecutionTracker> trackers) {
        return new IntermediateExecutionGuiUpdater(executionInfos, trackers);
    }

    @Override
    public AbstractExecutionGuiUpdater afterExecutionGuiUpdater(
            Collection<ExecutionTracker> trackers) {
        return new AfterExecutionGuiUpdater(trackers);
    }

    @Override
    public AbstractExecutionGuiUpdater errorExecutionGuiUpdater(String message,
            Collection<ExecutionTracker> trackers) {
        return new ErrorExecutionGuiUpdater(message, trackers);
    }

    @Override
    public AbstractExecutionGuiUpdater errorExecutionGuiUpdater(
            Collection<ExecutionTracker> trackers) {
        return new ErrorExecutionGuiUpdater(trackers);
    }

}
