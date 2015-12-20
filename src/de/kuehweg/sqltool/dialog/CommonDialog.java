/*
 * Copyright (c) 2013-2015, Michael Kühweg
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
package de.kuehweg.sqltool.dialog;

import java.util.Optional;

import de.kuehweg.sqltool.dialog.images.ImagePack;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;

/**
 * Allgemeine Dialogboxen für Hinweismeldungen.
 *
 * @author Michael Kühweg
 */
public abstract class CommonDialog {

	private final Alert alert;

	public CommonDialog(final String title, final String message, final String... buttonTexts) {
		super();
		alert = new Alert(AlertType.NONE);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.getButtonTypes().clear();
		appendFirstButtonAsDefault(buttonTexts);
		appendButtonsBetweenFirstAndLast(buttonTexts);
		appendLastButtonAsCancelIfPresent(buttonTexts);
	}

	public void specialize(final AlertType alertType, final ImagePack icon) {
		alert.setAlertType(alertType);
		alert.setGraphic(new ImageView(icon.getAsImage()));
	}

	/**
	 * Dialog anzeigen und auf Anwenderaktion warten.
	 *
	 * @return Buttontext des angeklickten Buttons, wie er beim Aufbau mit
	 *         addDialogButtons() angegeben wurde
	 */
	public String askUserFeedback() {
		return findUserFeedback(alert.showAndWait());
	}

	private void appendFirstButtonAsDefault(final String... buttonTexts) {
		alert.getButtonTypes().add(new ButtonType(buttonTexts[0], ButtonData.OK_DONE));
	}

	private void appendLastButtonAsCancelIfPresent(final String... buttonTexts) {
		if (buttonTexts.length > 1) {
			alert.getButtonTypes().add(new ButtonType(buttonTexts[buttonTexts.length - 1], ButtonData.CANCEL_CLOSE));
		}
	}

	private void appendButtonsBetweenFirstAndLast(final String... buttonTexts) {
		boolean dropFirst = true;
		String delayedToDropLast = null;
		for (final String buttonText : buttonTexts) {
			if (!dropFirst) {
				if (delayedToDropLast != null) {
					alert.getButtonTypes().add(new ButtonType(delayedToDropLast));
				}
				delayedToDropLast = buttonText;
			}
			dropFirst = false;
		}
	}

	/**
	 * Wandelt den ausgewählten Button von seiner ButtonType Darstellung zurück
	 * in den Text.
	 *
	 * @param selectedButtonType
	 *            Ausgewählter Button
	 * @return Buttontext, der vom Aufrufer mitgegeben wurde.
	 */
	private String findUserFeedback(final Optional<ButtonType> selectedButtonType) {
		if (selectedButtonType.isPresent()) {
			for (final ButtonType buttonType : alert.getButtonTypes()) {
				if (buttonType == selectedButtonType.get()) {
					return buttonType.getText();
				}
			}
		}
		return null;
	}

}
