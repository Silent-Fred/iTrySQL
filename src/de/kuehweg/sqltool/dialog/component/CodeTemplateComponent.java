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

package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.sqlediting.CodeHelp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 *
 * @author Michael Kühweg
 */
public class CodeTemplateComponent {

    private final ListView<CodeHelp> templates;
    private final TextArea targetForTemplate;
    private final TextArea targetForSyntax;
    
    public CodeTemplateComponent(final ListView<CodeHelp> templates, final TextArea targetForTemplate, final TextArea targetForSyntax) {
        this.templates = templates;
        this.targetForTemplate = targetForTemplate;
        this.targetForSyntax = targetForSyntax;
    }

    public void fillCodeTemplates() {
        final ObservableList<CodeHelp> templateListViewItems = FXCollections
                .observableArrayList();
        for (final CodeHelp template : CodeHelp.values()) {
            templateListViewItems.add(template);
        }
        templates.setItems(templateListViewItems);
        templates.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<CodeHelp>() {
            @Override
            public void changed(
                    final ObservableValue<? extends CodeHelp> ov,
                    final CodeHelp oldValue, final CodeHelp newValue) {
                final StringBuilder statementTemplate = new StringBuilder();
                if (newValue.getSyntaxDescription() != null
                        && newValue.getSyntaxDescription().trim()
                        .length() > 0) {
                    statementTemplate
                            .append("-- ")
                            .append(
                            DialogDictionary.LABEL_SEE_SYNTAX_IN_DBOUTPUT
                            .toString()).append("\n");
                    targetForSyntax.appendText("\n"
                            + newValue.getSyntaxDescription() + "\n");
                }
                statementTemplate.append(newValue.getCodeTemplate());
                targetForTemplate.insertText(
                        targetForTemplate.getCaretPosition(),
                        statementTemplate.toString());
            }
        });
    }

}
