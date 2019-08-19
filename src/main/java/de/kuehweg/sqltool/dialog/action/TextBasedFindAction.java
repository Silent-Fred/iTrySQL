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

import java.util.function.BiFunction;

/**
 * @author Michael Kühweg
 */
public class TextBasedFindAction extends FindAction {

	private String textContentNullSafeAndLowerCase;

	private int lastKnownFinding;

	public TextBasedFindAction() {
		resetSearchPosition();
		refreshTextContent("");
	}

	public void refreshTextContent(final String newTextContent) {
		textContentNullSafeAndLowerCase = newTextContent == null ? "" : newTextContent.toLowerCase();
	}

	@Override
	public void find(final String searchString) {
		resetSearchPosition();
		nextOccurrence(searchString);
	}

	@Override
	public void nextOccurrence(final String searchString) {
		findOccurrence(searchString, getPositionOfLastRememberedFinding() + 1,
				(what, where) -> findForward(what, where));
	}

	@Override
	public void previousOccurrence(final String searchString) {
		findOccurrence(searchString, getPositionOfLastRememberedFinding(), (what, where) -> findBackwards(what, where));
	}

	private void findOccurrence(final String searchString, final int positionToStartFrom,
			final BiFunction<String, Integer, Integer> finder) {
		resetSearchIfLastRememberedPositionIsNowInvalid();
		final String preparedSearchString = preparedSearchString(searchString);
		if (!preparedSearchString.isEmpty()) {
			final int foundAtPosition = finder.apply(preparedSearchString, positionToStartFrom);
			if (foundAtPosition >= 0) {
				rememberPositionAsLastFinding(foundAtPosition);
			}
		}
	}

	public int getPositionOfLastRememberedFinding() {
		return lastKnownFinding;
	}

	@Override
	public void resetSearchPosition() {
		lastKnownFinding = -1;
	}

	private void resetSearchIfLastRememberedPositionIsNowInvalid() {
		if (getPositionOfLastRememberedFinding() >= textContentNullSafeAndLowerCase.length()) {
			resetSearchPosition();
		}
	}

	private void rememberPositionAsLastFinding(final int positionOfCurrentFinding) {
		lastKnownFinding = positionOfCurrentFinding;
	}

	private int findForward(final String searchString, final int startingPosition) {
		return textContentNullSafeAndLowerCase.indexOf(searchString, startingPosition);
	}

	private int findBackwards(final String searchString, final int startingPosition) {
		if (startingPosition >= 0) {
			final int cutOffEverythingFrom = startingPosition < textContentNullSafeAndLowerCase.length()
					? startingPosition : textContentNullSafeAndLowerCase.length();
			if (cutOffEverythingFrom > 0) {
				return textContentNullSafeAndLowerCase.substring(0, cutOffEverythingFrom).lastIndexOf(searchString);
			}
		}
		return -1;
	}

}
