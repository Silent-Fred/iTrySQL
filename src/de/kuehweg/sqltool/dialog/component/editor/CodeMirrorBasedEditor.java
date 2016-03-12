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
 * Editorkomponente, die mit einem in einer WebView eingebetteten CodeMirror
 * Editor arbeitet.
 *
 * @author Michael Kühweg
 */
public class CodeMirrorBasedEditor implements StatementEditor {

	public static final String RESOURCE = "/resources/codemirror/sql_editor.html";

	private enum FontSizeSteps {
		NORMAL(12), MEDIUM(14), BIG(18);
		private int equivalentFontSize;

		FontSizeSteps(final int equivalentFontSize) {
			this.equivalentFontSize = equivalentFontSize;
		}

		public int getEquivalentFontSize() {
			return equivalentFontSize;
		}

		public static FontSizeSteps normalize(final int size) {
			if (size <= NORMAL.equivalentFontSize) {
				return NORMAL;
			} else if (size >= BIG.equivalentFontSize) {
				return BIG;
			}
			return MEDIUM;
		}
	}

	private final WebView webView;

	private FontSizeSteps fontSize = FontSizeSteps.NORMAL;

	public CodeMirrorBasedEditor(final WebView webView) {
		this.webView = webView;
	}

	@Override
	public String getText() {
		return (String) webView.getEngine().executeScript("editor.getValue();");
	}

	@Override
	public String getSelectedText() {
		return (String) webView.getEngine().executeScript("editor.getSelection();");
	}

	@Override
	public CaretPosition getCaretPosition() {
		try {
			final JSObject cursorPosition = (JSObject) webView.getEngine().executeScript("editor.getCursor();");
			return new CaretPosition((Integer) cursorPosition.getMember("line"),
					(Integer) cursorPosition.getMember("ch"));
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
		final String script = "editor.getDoc().setValue('" + prepareJavaStringForCodeMirror(text) + "')";
		try {
			webView.getEngine().executeScript(script);
		} catch (final Exception e) {
		}
	}

	private String prepareJavaStringForCodeMirror(final String text) {
		// TODO eventuell über JSObject und einen Javascript-Aufruf besser zu
		// lösen (Wandlung sollte dann automatisch und richtig passieren)
		String prepared = text.replace("'", "\\'");
		prepared = prepared.replace(System.getProperty("line.separator"), "\\n");
		prepared = prepared.replace("\n", "\\n");
		prepared = prepared.replace("\r", "\\n");
		// und was ist mit \t und \f ?
		return prepared;
	}

	@Override
	public int getFontSize() {
		return fontSize.getEquivalentFontSize();
	}

	@Override
	public void setFontSize(final int size) {
		fontSize = FontSizeSteps.normalize(size);
		setFontSize(fontSize);
	}

	private void setFontSize(final FontSizeSteps step) {
		try {
			switch (step) {
			case BIG:
				webView.getEngine().executeScript("zoomBig();");
				break;
			case MEDIUM:
				webView.getEngine().executeScript("zoomMedium();");
				break;
			default:
				webView.getEngine().executeScript("zoomNormal();");
			}
		} catch (final Exception e) {
		}
	}

	@Override
	public void zoomIn() {
		switch (fontSize) {
		case MEDIUM:
			fontSize = FontSizeSteps.BIG;
			break;
		case NORMAL:
			fontSize = FontSizeSteps.MEDIUM;
			break;
		default:
			fontSize = FontSizeSteps.BIG;
		}
		setFontSize(fontSize);
	}

	@Override
	public void zoomOut() {
		switch (fontSize) {
		case BIG:
			fontSize = FontSizeSteps.MEDIUM;
			break;
		case MEDIUM:
			fontSize = FontSizeSteps.NORMAL;
			break;
		default:
			fontSize = FontSizeSteps.NORMAL;
		}
		setFontSize(fontSize);
	}

}
