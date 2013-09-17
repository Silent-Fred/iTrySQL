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

import de.kuehweg.sqltool.common.UserPreferencesManager;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * @author Michael Kühweg
 */
public class FontAction {

    private static final double MIN_FONT_SIZE = 12;
    private static final double MAX_FONT_SIZE = 32;

    private FontAction() {
        // utility class - no instances desired
    }

    private static void modifyFontSize(final TextArea textArea, double diff) {
        if (textArea != null) {
            Text text = ((Text) textArea.lookup(".text"));
            if (text != null) {
                double fontSize = ((Text) textArea.lookup(".text")).getFont().getSize();
                fontSize += diff;
                fontSize = fontSize < MIN_FONT_SIZE ? MIN_FONT_SIZE : fontSize;
                fontSize = fontSize > MAX_FONT_SIZE ? MAX_FONT_SIZE : fontSize;
                textArea.setStyle("-fx-font-size: " + (fontSize + diff) + ";");
                UserPreferencesManager.getSharedInstance().setFontSize((int)Math.round(fontSize));
            }
        }
    }

    private static void increaseFontSize(final TextArea textArea) {
        modifyFontSize(textArea, +1);
    }

    private static void decreaseFontSize(final TextArea textArea) {
        modifyFontSize(textArea, -1);
    }

    public static void handleFontAction(Event event) {
        Node source = event != null ? (Node) event.getSource() : null;
        if (source != null) {
            TextArea statementInput = (TextArea) source.getScene().lookup("#statementInput");
            TextArea dbOutput = (TextArea) source.getScene().lookup("#dbOutput");
            if (statementInput != null) {
                switch (source.getId()) {
                    case "toolbarZoomIn":
                        increaseFontSize(statementInput);
                        increaseFontSize(dbOutput);
                        break;
                    case "toolbarZoomOut":
                        decreaseFontSize(statementInput);
                        decreaseFontSize(dbOutput);
                        break;
                }
            }
        }
    }

}
