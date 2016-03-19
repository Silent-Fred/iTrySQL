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

package de.kuehweg.sqltool.dialog.base;

import javafx.scene.control.TextArea;

/**
 * @author Michael Kühweg
 */
public class TextAreaZoomable implements FontSizeZoomable {

	public static final int MIN_FONT_SIZE = 9;
	public static final int MAX_FONT_SIZE = 32;

	private static final int DEFAULT_FONT_SIZE = 12;

	private final TextArea textArea;

	public TextAreaZoomable(final TextArea textArea) {
		this.textArea = textArea;
	}

	public int getFontSize() {
		return textArea != null ? (int) Math.round(textArea.getFont().getSize()) : DEFAULT_FONT_SIZE;
	}

	public void setFontSize(final int size) {
		textArea.setStyle("-fx-font-size: " + size + ";");
	}

	private int zoom(final int diff) {
		int targetSize = getFontSize() + diff;
		targetSize = targetSize < MIN_FONT_SIZE ? MIN_FONT_SIZE : targetSize;
		targetSize = targetSize > MAX_FONT_SIZE ? MAX_FONT_SIZE : targetSize;
		return targetSize;
	}

	@Override
	public void zoomIn() {
		setFontSize(zoom(1));
	}

	@Override
	public void zoomOut() {
		setFontSize(zoom(-1));
	}

}
