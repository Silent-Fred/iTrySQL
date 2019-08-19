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

import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * Editorkomponente, die mit einem in einer WebView eingebetteten ACE Editor
 * arbeitet.
 *
 * @author Michael Kühweg
 */
public class AceBasedEditor implements StatementEditor {

	private static final long serialVersionUID = 2399100995033444144L;

	public static final String RESOURCE = "ace/sql_editor.html";

	public static final int MIN_FONT_SIZE = 9;
	public static final int MAX_FONT_SIZE = 32;

	private static final int DEFAULT_FONT_SIZE = 12;

	private final WebView webView;

	private int fontSize = DEFAULT_FONT_SIZE;

	public AceBasedEditor(final WebView webView) {
		this.webView = webView;
	}

	@Override
	public String getText() {
		return (String) webView.getEngine().executeScript("editor.getValue();");
	}

	@Override
	public String getSelectedText() {
		return (String) webView.getEngine().executeScript("editor.session.getTextRange(editor.getSelectionRange());");
	}

	@Override
	public CaretPosition getCaretPosition() {
		try {
			final JSObject cursorPosition = (JSObject) webView.getEngine().executeScript("editor.getCursorPosition();");
			return new CaretPosition((Integer) cursorPosition.getMember("row"),
					(Integer) cursorPosition.getMember("column"));
		} catch (final Exception e) {
			return new CaretPosition();
		}
	}

	@Override
	public int getCaretPositionAsIndex() {
		return getCaretPosition().appliedOnString(getText());
	}

	@Override
	public void setText(final String text) {
		final String script = "editor.setValue('" + prepareJavaStringForCodeMirror(text) + "', -1)";
		webView.getEngine().executeScript(script);
	}

	private String prepareJavaStringForCodeMirror(final String text) {
		String prepared = text.replace("'", "\\'");
		prepared = prepared.replace(System.getProperty("line.separator"), "\\n");
		prepared = prepared.replace("\n", "\\n");
		prepared = prepared.replace("\r", "\\n");
		// und was ist mit \t und \f ?
		return prepared;
	}

	@Override
	public void focus() {
		try {
			webView.getEngine().executeScript("editor.focus();");
		} catch (final Exception e) {
		}
	}

	@Override
	public int getFontSize() {
		return fontSize;
	}

	@Override
	public void setFontSize(final int size) {
		fontSize = size;
		final String script = "editor.setFontSize(" + getFontSize() + ")";
		try {
			webView.getEngine().executeScript(script);
		} catch (final Exception e) {
		}
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
