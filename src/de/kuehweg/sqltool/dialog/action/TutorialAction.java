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
import de.kuehweg.sqltool.dialog.ConfirmDialog;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Michael Kühweg
 */
public class TutorialAction {

    public TutorialAction() {
    }

    public void createTutorial(final ExecuteAction executeAction) {
        final ConfirmDialog confirm = new ConfirmDialog(
                DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
                DialogDictionary.MSG_REALLY_CREATE_TUTORIAL_DATA.toString(),
                DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString(),
                DialogDictionary.COMMON_BUTTON_CANCEL.toString());
        if (DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString().equals(
                confirm.askUserFeedback())) {
            try {
                final InputStream tutorialStream = getClass()
                        .getResourceAsStream("/resources/sql/tutorial.sql");
                final StringBuffer b;
                try (InputStreamReader reader = new InputStreamReader(
                        tutorialStream, "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(
                        reader)) {
                    b = new StringBuffer();
                    String s = null;
                    while ((s = bufferedReader.readLine()) != null) {
                        b.append(s);
                        b.append('\n');
                    }
                }
                final String tutorialSql = b.toString();
                executeAction.handleExecuteActionSilently(tutorialSql);
            } catch (final IOException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_TUTORIAL_CREATION_FAILED + " ("
                        + ex.getLocalizedMessage() + ")",
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
        }
    }
}
