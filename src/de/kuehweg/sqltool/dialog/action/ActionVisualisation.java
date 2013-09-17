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
package de.kuehweg.sqltool.dialog.action;

import de.kuehweg.sqltool.common.DialogDictionary;
import java.math.BigDecimal;
import java.text.MessageFormat;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 *
 * @author Michael Kühweg
 */
public class ActionVisualisation {

    private ActionVisualisation() {
        // utility class
    }

    public static void prepareSceneRunning(final Scene scene) {
        ProgressIndicator progressIndicator = (ProgressIndicator) scene.lookup("#executionProgressIndicator");
        if (progressIndicator != null) {
            progressIndicator.setProgress(-1);
        }
        Label executionTime = (Label) scene.lookup("#executionTime");
        if (executionTime != null) {
            executionTime.setText(DialogDictionary.LABEL_EXECUTING.toString());
        }
    }

    public static void showFinished(final Scene scene, final long executionTimeInMilliseconds) {
        ProgressIndicator progressIndicator = (ProgressIndicator) scene.lookup("#executionProgressIndicator");
        if (progressIndicator != null) {
            progressIndicator.setProgress(1);
        }
        Label executionTime = (Label) scene.lookup("#executionTime");
        if (executionTime != null) {
            BigDecimal executionTimeInSeconds = BigDecimal.valueOf(executionTimeInMilliseconds)
                    .divide(BigDecimal.valueOf(1000));
            executionTime.setText(MessageFormat.format(DialogDictionary.PATTERN_EXECUTION_TIME.toString(),
                    executionTimeInSeconds.toString()));
        }
    }
}
