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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

/**
 * @author Michael Kühweg
 */
public class TextAreaFindAction extends FindAction {

	private final TextArea textArea;

	private int findFrom;

	public TextAreaFindAction(final TextArea textArea) {
		super();
		this.textArea = textArea;
		findFrom = 0;
	}

	@Override
	public void find(final String searchString) {
		final String preparedSearchString = preparedSearchString(searchString);
		findFrom = 0;
		if (preparedSearchString.isEmpty()) {
			deselectAllOccurrencesInComponent();
		} else {
			findFrom = find(preparedSearchString, findFrom);
			if (findFrom >= 0) {
				selectOccurrenceInComponent(findFrom, preparedSearchString.length());
			} else {
				findFrom = 0;
				deselectAllOccurrencesInComponent();
			}
		}
	}

	@Override
	public void nextOccurrence(final String searchString) {
		if (findFrom >= 0) {
			final int current = findFrom;
			findFrom = find(searchString, findFrom + 1);
			selectOccurrenceInComponent(findFrom, searchString.length());
			if (findFrom < 0) {
				findFrom = current;
			}
		}
	}

	@Override
	public void previousOccurrence(final String searchString) {
		final int current = findFrom;
		findFrom = findBackwards(searchString, findFrom);
		selectOccurrenceInComponent(findFrom, searchString.length());
		if (findFrom < 0) {
			findFrom = current;
		}
	}

	private String preparedSearchString(final String searchString) {
		return searchString != null ? searchString.toLowerCase() : "";
	}

	private int find(final String searchString, final int findFrom) {
		return getValueSafeAndLowerCase(textArea).indexOf(searchString, findFrom);
	}

	private int findBackwards(final String searchString, final int findFrom) {
		if (findFrom > 0) {
			return getValueSafeAndLowerCase(textArea).substring(0, findFrom).lastIndexOf(searchString);
		}
		return -1;
	}

	private void selectOccurrenceInComponent(final int startAtPosition, final int lengthOfTextSelection) {
		if (startAtPosition >= 0 && lengthOfTextSelection > 0) {
			final int anchor = startAtPosition;
			final int caretPosition = startAtPosition + lengthOfTextSelection;
			if (getValueSafeAndLowerCase(textArea).length() >= caretPosition) {
				textArea.selectRange(anchor, caretPosition);
			}
		}
	}

	private void deselectAllOccurrencesInComponent() {
		textArea.deselect();
	}

	private String getValueSafeAndLowerCase(final TextArea textArea) {
		return textArea.textProperty().getValueSafe().toLowerCase();
	}

	@Override
	public void changed(final ObservableValue<? extends String> observable, final String oldValue,
			final String newValue) {
		find(observable.getValue());
	}
}
