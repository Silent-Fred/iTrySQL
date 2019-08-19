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
package de.kuehweg.sqltool.dialog;

import de.kuehweg.sqltool.resources.ImagePack;
import javafx.scene.control.Alert.AlertType;

/**
 * Spezialisierter Dialog für Rückfragen mit der Möglichkeit, mehrere Buttons
 * anzuzeigen.
 *
 * @author Michael Kühweg
 */
public class ConfirmDialog extends CommonDialog {

	/**
	 * @param title
	 *            Dialogtitel
	 * @param message
	 *            Nachricht an den Anwender
	 * @param buttonTexts
	 *            Buttons in der Reihenfolge von rechts nach links, wobei rechts
	 *            (d.h. der erste angegebene Button) als Default vorbelegt ist
	 */
	public ConfirmDialog(final String title, final String message, final String... buttonTexts) {
		super(title, message, buttonTexts);
		specialize(AlertType.CONFIRMATION, ImagePack.MSG_QUESTION);
	}

}
