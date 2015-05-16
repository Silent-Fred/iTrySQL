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

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

/**
 * FontAction für eine Textkomponente aus einem Event ableiten
 *
 * @author Michael Kühweg
 */
public class FontActionFactory {

    private int whichDifference(final Node source) {
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

    private FontAction getFontActionConcreteClass(final Node source) {
        if (source != null) {
            switch (source.getId()) {
                case "toolbarZoomIn":
                case "toolbarZoomOut":
                    return new StatementInputFontAction((TextArea) source.getScene()
                            .lookup("#statementInput"));
                case "toolbarTabDbOutputZoomIn":
                case "toolbarTabDbOutputZoomOut":
                    return new DatabaseOutputFontAction((TextArea) source.getScene().
                            lookup(
                                    "#dbOutput"));
                default:
                    return null;
            }
        }
        return null;
    }

    public FontAction getFontAction(final Event event) {
        final Node source = event != null ? (Node) event.getSource() : null;
        FontAction fontAction = getFontActionConcreteClass(source);
        if (fontAction != null) {
            fontAction.setDiff(whichDifference(source));
        }
        return fontAction;
    }
}
