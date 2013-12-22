/*
 * Copyright (c) 2013, Michael Kühweg
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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Drag and Drop für SQL-Skripte
 * 
 * @author Michael Kühweg
 */
public class SourceFileDropTargetUtil {

	private static final String DROP_TARGET_SYMBOL_ID = "removeMeOnDragExited";

	private SourceFileDropTargetUtil() {
		// Util
	}

	private static void setOnDragOver(final Pane containerPane) {

		containerPane.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Dragboard dragboard = event.getDragboard();
				if (event.getGestureSource() != containerPane
						&& dragboard.hasFiles()
						&& dragboard.getFiles().size() == 1) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});
	}

	private static void setOnDragEntered(final Pane containerPane,
			final TextArea statementInput) {
		containerPane.setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Dragboard dragboard = event.getDragboard();
				if (event.getGestureSource() != containerPane
						&& dragboard.hasFiles()
						&& dragboard.getFiles().size() == 1) {
					final Pane dropTargetSymbol = new DropTargetSymbol(
							containerPane);
					dropTargetSymbol.setId(DROP_TARGET_SYMBOL_ID);
					containerPane.getChildren().add(dropTargetSymbol);
					statementInput.setOpacity(0.5);
					// Blur-Effekt kann im Moment noch nicht in FX-CSS angegeben
					// werden
					final BoxBlur bb = new BoxBlur();
					bb.setWidth(5);
					bb.setHeight(5);
					bb.setIterations(3);
					statementInput.setEffect(bb);
				}
				event.consume();
			}
		});
	}

	private static void setOnDragExited(final Pane containerPane,
			final TextArea statementInput) {
		containerPane.setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Node child = containerPane.lookup("#"
						+ DROP_TARGET_SYMBOL_ID);
				if (child != null) {
					containerPane.getChildren().remove(child);
				}
				statementInput.setOpacity(1.0);
				statementInput.setEffect(null);
				event.consume();
			}
		});
	}

	private static void setOnDragDropped(final Pane containerPane,
			final TextArea statementInput) {
		containerPane.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Dragboard dragboard = event.getDragboard();
				boolean success = false;
				if (dragboard.hasFiles() && dragboard.getFiles().size() == 1) {
					try {
						final String script = FileUtil.readFile(
								dragboard.getFiles().get(0).toURI().toURL());
						statementInput.setText(script);
						success = true;
					} catch (final IOException ex) {
						final ErrorMessage msg = new ErrorMessage(
								DialogDictionary.MESSAGEBOX_ERROR.toString(),
								DialogDictionary.ERR_FILE_OPEN_FAILED
										.toString(),
								DialogDictionary.COMMON_BUTTON_OK.toString());
						msg.askUserFeedback();
					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
	}

	/**
	 * Macht den angegebenen Container zu einem DropTarget für SQL-Skripte und
	 * setzt die TextArea als Ziel für den Skriptinhalt
	 * 
	 * @param containerPane
	 *            DropTarget
	 * @param statementInput
	 *            Ziel für Skript
	 */
	public static void transformIntoSourceFileDropTarget(
			final Pane containerPane, final TextArea statementInput) {

		setOnDragOver(containerPane);

		setOnDragEntered(containerPane, statementInput);

		setOnDragExited(containerPane, statementInput);

		setOnDragDropped(containerPane, statementInput);

	}
}
