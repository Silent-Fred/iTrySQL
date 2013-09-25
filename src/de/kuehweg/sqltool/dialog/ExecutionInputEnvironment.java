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
package de.kuehweg.sqltool.dialog;

import de.kuehweg.sqltool.common.sqlediting.SQLHistoryKeeper;
import de.kuehweg.sqltool.database.ConnectionHolder;

/**
 *
 * @author Michael Kühweg
 */
public class ExecutionInputEnvironment {

    private final boolean limitMaxRows;
    private final ConnectionHolder connectionHolder;
    private final SQLHistoryKeeper historyKeeper;

    public static class Builder {

        private boolean limitMaxRows = true;
        private ConnectionHolder connectionHolder;
        private SQLHistoryKeeper historyKeeper;

        public Builder (final ConnectionHolder connectionHolder) {
            this.connectionHolder = connectionHolder;
        }
        
        public Builder limitMaxRows(final boolean limitMaxRows) {
            this.limitMaxRows = limitMaxRows;
            return this;
        }

        public Builder historyKeeper(final SQLHistoryKeeper historyKeeper) {
            this.historyKeeper = historyKeeper;
            return this;
        }

        public ExecutionInputEnvironment build() {
            return new ExecutionInputEnvironment(this);
        }
    }

    private ExecutionInputEnvironment(final Builder builder) {
        this.limitMaxRows = builder.limitMaxRows;
        this.connectionHolder = builder.connectionHolder;
        this.historyKeeper = builder.historyKeeper;
    }

    public boolean isLimitMaxRows() {
        return limitMaxRows;
    }

    public ConnectionHolder getConnectionHolder() {
        return connectionHolder;
    }

    public SQLHistoryKeeper getHistoryKeeper() {
        return historyKeeper;
    }
}
