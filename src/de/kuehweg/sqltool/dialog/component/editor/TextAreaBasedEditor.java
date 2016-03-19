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

package de.kuehweg.sqltool.dialog.component.editor;

import de.kuehweg.sqltool.dialog.base.FontSizeZoomable;
import de.kuehweg.sqltool.dialog.base.TextAreaZoomable;
import javafx.scene.control.TextArea;

/**
 * Einfache Standardimplementierung für einen Code-Editor auf Basis einer
 * {@link TextArea}.
 *
 * @author Michael Kühweg
 */
public class TextAreaBasedEditor implements StatementEditor {

	private final TextArea textArea;

	private final FontSizeZoomable zoomable;

	public TextAreaBasedEditor(final TextArea textArea) {
		super();
		this.textArea = textArea;
		zoomable = new TextAreaZoomable(textArea);
	}

	@Override
	public String getText() {
		return textArea.getText();
	}

	@Override
	public String getSelectedText() {
		return textArea.getSelectedText();
	}

	@Override
	public CaretPosition getCaretPosition() {
		// FIXME not yet implemented
		return null;
	}

	@Override
	public int getCaretPositionAsIndex() {
		return textArea.getCaretPosition();
	}

	@Override
	public void setText(final String text) {
		textArea.setText(text);
	}

	@Override
	public void focus() {
		textArea.requestFocus();
	}

	@Override
	public void zoomIn() {
		zoomable.zoomIn();
	}

	@Override
	public void zoomOut() {
		zoomable.zoomOut();
	}

	@Override
	public int getFontSize() {
		return zoomable.getFontSize();
	}

	@Override
	public void setFontSize(final int size) {
		zoomable.setFontSize(size);
	}

}
