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
 * Schriftgröße ändern
 *
 * @author Michael Kühweg
 */
public class FontAction {

    public static final int MIN_FONT_SIZE = 12;
    public static final int MAX_FONT_SIZE = 32;

    /**
     * Schriftgröße einer TextArea ändern - gesetzte Schriftgröße wird als Ergebnis
     * geliefert
     *
     * @param textArea
     * @param diff
     * @return
     */
    private int modifyFontSize(final TextArea textArea, final int diff) {
        if (textArea != null) {
            final Text text = (Text) textArea.lookup(".text");
            if (text != null) {
                double fontSize = ((Text) textArea.lookup(".text")).getFont()
                        .getSize();
                fontSize += diff;
                fontSize = fontSize < MIN_FONT_SIZE ? MIN_FONT_SIZE : fontSize;
                fontSize = fontSize > MAX_FONT_SIZE ? MAX_FONT_SIZE : fontSize;
                textArea.setStyle("-fx-font-size: " + (fontSize + diff) + ";");
                return (int) Math.round(fontSize);
            }
        }
        return MIN_FONT_SIZE;
    }

    private int whichDifference(final Event event) {
        final Node source = event != null ? (Node) event.getSource() : null;
        if (source != null) {
            switch (source.getId()) {
                case "toolbarZoomIn":
                case "toolbarTabDbOutputZoomIn":
                    return 1;
                case "toolbarZoomOut":
                case "toolbarTabDbOutputZoomOut":
                    return -1;
                default:
                    return 0;
            }
        }
        return 0;
    }

    private TextArea whichTextArea(final Event event) {
        final Node source = event != null ? (Node) event.getSource() : null;
        if (source != null) {
            final TextArea dbOutput = (TextArea) source.getScene().lookup(
                    "#dbOutput");
            switch (source.getId()) {
                case "toolbarZoomIn":
                case "toolbarZoomOut":
                    return (TextArea) source.getScene()
                            .lookup("#statementInput");
                case "toolbarTabDbOutputZoomIn":
                case "toolbarTabDbOutputZoomOut":
                    return (TextArea) source.getScene().lookup(
                            "#dbOutput");
                default:
                    return null;
            }
        }
        return null;
    }

    private void changeVisualsAndPreferences(final Event event) {
        final Node source = event != null ? (Node) event.getSource() : null;
        if (source != null) {
            final TextArea whichTextArea = whichTextArea(event);
            final int whichDifference = whichDifference(event);
            if (whichTextArea != null && whichDifference != 0) {
                switch (source.getId()) {
                    case "toolbarZoomIn":
                    case "toolbarZoomOut":
                        UserPreferencesManager.getSharedInstance().
                                setFontSizeStatementInput(
                                        modifyFontSize(whichTextArea, whichDifference));
                        break;
                    case "toolbarTabDbOutputZoomIn":
                    case "toolbarTabDbOutputZoomOut":
                        UserPreferencesManager.getSharedInstance().setFontSizeDbOutput(
                                modifyFontSize(whichTextArea, whichDifference));
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Fontmanipulationen behandeln
     *
     * @param event
     */
    public void handleFontAction(final Event event) {
        changeVisualsAndPreferences(event);
    }
}
