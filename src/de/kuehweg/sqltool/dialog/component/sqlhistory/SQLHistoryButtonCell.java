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
package de.kuehweg.sqltool.dialog.component.sqlhistory;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.sqlediting.SQLHistory;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextArea;

/**
 * Button in TableView des SQL-Verlaufs
 *
 * @author Michael Kühweg
 */
public class SQLHistoryButtonCell extends TableCell<SQLHistory, String> {

    private final Button button;

    private SQLHistory historyItem;

    public SQLHistoryButtonCell(final TextArea appendTo) {
        super();
        button = new Button(
                DialogDictionary.LABEL_APPEND_HISTORY_ITEM_TO_EDITOR.
                toString());
        button.setOnAction((ActionEvent event) -> {
            if (appendTo != null && historyItem != null) {
                appendTo.appendText("\n" + historyItem.getOriginalSQL());
            }
        });
    }

    @Override
    protected void updateItem(String text, boolean empty) {
        if (!empty) {
            setGraphic(button);
            historyItem = (SQLHistory) tableRowProperty().getValue().
                    getItem();
        } else {
            setGraphic(null);
            historyItem = null;
        }
    }

}
