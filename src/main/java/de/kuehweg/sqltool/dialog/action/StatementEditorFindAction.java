/*
 * Copyright (c) 2016, Michael Kühweg
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

import de.kuehweg.sqltool.dialog.component.editor.StatementEditor;
import de.kuehweg.sqltool.dialog.component.editor.TextAreaBasedEditor;

/**
 * @author Michael Kühweg
 */
public class StatementEditorFindAction extends FindAction {

	private FindAction findAction;

	public StatementEditorFindAction() {
		findAction = new EmptyFindAction();
	}

	public void attachToCurrentStatementEditor(final StatementEditor statementEditor) {
		if (statementEditor instanceof TextAreaBasedEditor) {
			findAction = new TextAreaFindAction(((TextAreaBasedEditor) statementEditor).getUnderlyingTextArea());
		} else {
			findAction = new EmptyFindAction();
		}
	}

	@Override
	public void find(final String searchString) {
		findAction.find(searchString);
	}

	@Override
	public void nextOccurrence(final String searchString) {
		findAction.nextOccurrence(searchString);
	}

	@Override
	public void previousOccurrence(final String searchString) {
		findAction.previousOccurrence(searchString);
	}

	@Override
	public void resetSearchPosition() {
		findAction.resetSearchPosition();
	}

}
