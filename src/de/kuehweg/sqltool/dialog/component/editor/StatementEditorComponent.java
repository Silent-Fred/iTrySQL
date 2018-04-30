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

import java.io.Serializable;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.dialog.action.StatementEditorFindAction;
import de.kuehweg.sqltool.dialog.util.WebViewWithHSQLDBBugfix;
import de.kuehweg.sqltool.itrysql.ResourceLocator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author Michael Kühweg
 */
public class StatementEditorComponent implements Serializable {

	private static final long serialVersionUID = 8765875714563701049L;

	private StatementEditor statementEditor;

	private AnchorPane anchor;

	private final StatementEditorCycle statementEditorCycle = new StatementEditorCycle();

	private final StatementEditorFindAction findAction = new StatementEditorFindAction();

	public void setAnchor(final AnchorPane anchor) {
		this.anchor = anchor;
		statementEditorCycle.recycle();
		installEditorTextArea();
		findAction.attachToCurrentStatementEditor(statementEditor);
	}

	public StatementEditor getActiveStatementEditor() {
		return statementEditor;
	}

	public void toggleStatementEditor() {
		final Class<? extends StatementEditor> next = statementEditorCycle.next();
		if (next == CodeMirrorBasedEditor.class) {
			installEditorCodeMirror();
		} else if (next == AceBasedEditor.class) {
			installEditorAce();
		} else {
			installEditorTextArea();
		}
		findAction.attachToCurrentStatementEditor(statementEditor);
	}

	public StatementEditorCycle getStatementEditorCycle() {
		return statementEditorCycle;
	}

	public StatementEditorFindAction getFindAction() {
		return findAction;
	}

	/**
	 * @param node
	 *            Fixiert alle Ecken des übergebenen Knotens an der
	 *            übergeordneten AnchorPane, d.h. der Node ändert seine Größe
	 *            jeweils mit der AnchorPane.
	 */
	private void fixAnchor(final Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
	}

	/**
	 * @param resource
	 *            HTML mit Editor zur Einbettung in die WebView
	 * @return Die fertig aufgebaute und in die Anwendung eingebettete WebView,
	 *         in der der Javascript Editor läuft.
	 */
	private WebView installJavascriptEditor(final String resource) {
		final String content = statementEditor != null ? statementEditor.getText() : "";
		final WebView javascriptEditor = installWebViewForJavascriptEditor();
		installJavascriptEditorWithInitialContent(javascriptEditor, resource, content);
		webViewFixForJavascriptEditor(javascriptEditor);
		return javascriptEditor;
	}

	/**
	 * @return WebView zur Aufnahme des Editors. Die Fixierung am umgebenden
	 *         Anchor-Node wird vorbelegt.
	 */
	private WebView installWebViewForJavascriptEditor() {
		final WebView javascriptEditor = new WebView();
		anchor.getChildren().clear();
		anchor.getChildren().add(javascriptEditor);
		fixAnchor(javascriptEditor);
		return javascriptEditor;
	}

	/**
	 * @param javascriptEditor
	 *            WebView, in der der Javascript laufen soll.
	 * @param resource
	 *            HTML Ressource zur Einbettung des Editors.
	 * @param content
	 *            Initialer Inhalt des Editors.
	 */
	private void installJavascriptEditorWithInitialContent(final WebView javascriptEditor, final String resource,
			final String content) {
		final WebEngine engine = javascriptEditor.getEngine();
		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(final ObservableValue<? extends State> observable, final State oldState,
					final State newState) {
				if (newState == State.SUCCEEDED) {
					engine.getLoadWorker().stateProperty().removeListener(this);
					statementEditor.setText(content);
					statementEditor.focus();
				}
			}
		});

		final ResourceLocator locator = new ResourceLocator();
		try {
			javascriptEditor.getEngine()
					.load(locator.getExternalFormForExplodedResourceInBundle(resource).toExternalForm());
		} catch (final Exception e) {
			installEditorTextArea();
		}
	}

	/**
	 * @param javascriptEditor
	 */
	private void webViewFixForJavascriptEditor(final WebView javascriptEditor) {
		// FIXME der übliche Workaround
		javascriptEditor.setOnMouseEntered((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
		javascriptEditor.setOnMouseExited((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
	}

	/**
	 * Installiert ACE als Editor für die Eingabe der SQL-Anweisungen.
	 */
	private void installEditorAce() {
		statementEditor = new AceBasedEditor(installJavascriptEditor(AceBasedEditor.RESOURCE));
	}

	/**
	 * Installiert CodeMirror als Editor für die Eingabe der SQL-Anweisungen.
	 */
	private void installEditorCodeMirror() {
		statementEditor = new CodeMirrorBasedEditor(installJavascriptEditor(CodeMirrorBasedEditor.RESOURCE));
	}

	/**
	 * Installiert eine einfache TextArea als Editor für die Eingabe der
	 * SQL-Anweisungen.
	 */
	private void installEditorTextArea() {
		final TextArea textArea = new TextArea();
		textArea.setPromptText(DialogDictionary.PROMPT_ENTER_STATEMENT.toString());
		textArea.getStyleClass().add("itry-statement-edit");
		anchor.getChildren().clear();
		anchor.getChildren().add(textArea);
		fixAnchor(textArea);
		final String content = statementEditor != null ? statementEditor.getText() : "";
		statementEditor = new TextAreaBasedEditor(textArea);
		statementEditor.setFontSize(UserPreferencesManager.getSharedInstance().getFontSizeStatementInput());
		statementEditor.setText(content);
		statementEditor.focus();
	}

}
