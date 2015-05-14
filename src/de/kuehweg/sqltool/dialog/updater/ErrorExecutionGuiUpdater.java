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

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import java.util.Collection;

/**
 * Oberfläche im Fehlerfall aufbereiten
 *
 * @author Michael Kühweg
 */
public class ErrorExecutionGuiUpdater extends AbstractExecutionGuiUpdater {

    private final String message;

    public ErrorExecutionGuiUpdater(
            final Collection<ExecutionTracker> trackers) {
        super(trackers);
        message = DialogDictionary.ERR_UNKNOWN_ERROR.toString();
    }

    public ErrorExecutionGuiUpdater(final String message,
            final Collection<ExecutionTracker> trackers) {
        super(trackers);
        this.message = message;
    }

    @Override
    public void update() {
        for (ExecutionTracker tracker : getTrackers()) {
            // Bearbeitung auf beendet setzen
            tracker.afterExecution();
        }
        // und danach eine Fehlermeldung ausgeben
        final ErrorMessage msg = new ErrorMessage(
                DialogDictionary.MESSAGEBOX_ERROR.toString(), message,
                DialogDictionary.COMMON_BUTTON_OK.toString());
        msg.askUserFeedback();
    }
}
