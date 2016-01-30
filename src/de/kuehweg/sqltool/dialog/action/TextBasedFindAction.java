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

/**
 * @author Michael Kühweg
 */
public class TextBasedFindAction extends FindAction {

	private String textContent;

	private int lastKnownFinding;

	public TextBasedFindAction() {
		super();
		resetSearchPosition();
		refreshTextContent("");
	}

	public void refreshTextContent(final String newTextContent) {
		textContent = newTextContent == null ? "" : newTextContent.toLowerCase();
	}

	@Override
	public void find(final String searchString) {
		final String preparedSearchString = preparedSearchString(searchString);
		resetSearchPosition();
		if (!preparedSearchString.isEmpty()) {
			final int foundAtPosition = find(preparedSearchString, getPositionOfLastRememberedFinding());
			if (foundAtPosition >= 0) {
				rememberPositionAsLastFinding(foundAtPosition);
			} else {
				resetSearchPosition();
			}
		}
	}

	@Override
	public void nextOccurrence(final String searchString) {
		resetSearchIfLastRememberedPositionIsNowInvalid();
		final String preparedSearchString = preparedSearchString(searchString);
		if (!preparedSearchString.isEmpty()) {
			final int foundAtPosition = find(preparedSearchString, getPositionOfLastRememberedFinding() + 1);
			if (foundAtPosition >= 0) {
				rememberPositionAsLastFinding(foundAtPosition);
			}
		}
	}

	@Override
	public void previousOccurrence(final String searchString) {
		resetSearchIfLastRememberedPositionIsNowInvalid();
		final String preparedSearchString = preparedSearchString(searchString);
		if (!preparedSearchString.isEmpty()) {
			final int foundAtPosition = findBackwards(preparedSearchString, getPositionOfLastRememberedFinding());
			if (foundAtPosition >= 0) {
				rememberPositionAsLastFinding(foundAtPosition);
			}
		}
	}

	public int getPositionOfLastRememberedFinding() {
		return lastKnownFinding;
	}

	public void resetSearchPosition() {
		lastKnownFinding = -1;
	}

	private void resetSearchIfLastRememberedPositionIsNowInvalid() {
		if (getPositionOfLastRememberedFinding() >= textContent.length()) {
			resetSearchPosition();
		}
	}

	private void rememberPositionAsLastFinding(final int positionOfCurrentFinding) {
		lastKnownFinding = positionOfCurrentFinding;
	}

	private int find(final String searchString, final int startingPosition) {
		return textContent.indexOf(searchString, startingPosition);
	}

	private int findBackwards(final String searchString, final int startingPosition) {
		final String safeAndLowerCase = textContent;
		if (startingPosition >= 0) {
			final int cutOffEverythingFrom = startingPosition < safeAndLowerCase.length() ? startingPosition
					: safeAndLowerCase.length();
			if (cutOffEverythingFrom > 0) {
				return safeAndLowerCase.substring(0, cutOffEverythingFrom).lastIndexOf(searchString);
			}
		}
		return -1;
	}

	private String preparedSearchString(final String searchString) {
		return searchString != null ? searchString.toLowerCase() : "";
	}

}
