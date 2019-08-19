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

import java.util.function.Consumer;

import javafx.scene.control.TextArea;

/**
 * @author Michael Kühweg
 */
public class TextAreaFindAction extends TextBasedFindAction {

	private final TextArea textArea;

	public TextAreaFindAction(final TextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public void find(final String searchString) {
		resetSearchPosition();
		deselectAllOccurrencesInComponent();
		refreshTextContent(getValueSafe());
		final String preparedSearchString = preparedSearchString(searchString);
		super.find(preparedSearchString);
		if (getPositionOfLastRememberedFinding() >= 0) {
			selectOccurrenceInComponent(getPositionOfLastRememberedFinding(), preparedSearchString.length());
		} else {
			resetSearchPosition();
		}
	}

	@Override
	public void nextOccurrence(final String searchString) {
		findOccurrence(searchString, (what) -> super.nextOccurrence(what));
	}

	@Override
	public void previousOccurrence(final String searchString) {
		findOccurrence(searchString, (what) -> super.previousOccurrence(what));
	}

	private void findOccurrence(final String searchString, final Consumer<String> finder) {
		deselectAllOccurrencesInComponent();
		refreshTextContent(getValueSafe());
		final String preparedSearchString = preparedSearchString(searchString);
		finder.accept(preparedSearchString);
		if (getPositionOfLastRememberedFinding() >= 0) {
			selectOccurrenceInComponent(getPositionOfLastRememberedFinding(), preparedSearchString.length());
		}
	}

	private void selectOccurrenceInComponent(final int startAtPosition, final int lengthOfTextSelection) {
		if (startAtPosition >= 0 && lengthOfTextSelection > 0) {
			final int anchor = startAtPosition;
			final int caretPosition = startAtPosition + lengthOfTextSelection;
			if (getValueSafe().length() >= caretPosition) {
				textArea.selectRange(anchor, caretPosition);
			}
		}
	}

	private void deselectAllOccurrencesInComponent() {
		textArea.deselect();
	}

	private String getValueSafe() {
		return textArea.textProperty().getValueSafe();
	}
}
