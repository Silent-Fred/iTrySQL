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

import java.util.ResourceBundle;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.ConnectionSetting;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Dialog zur Auswahl einer Datenbankverbindung.
 *
 * @author Michael Kühweg
 */
public class ConnectionDialog extends Stage {

	private ConnectionSetting connectionSetting;

	/**
	 * Konstruktor für den Verbindungsdialog.
	 *
	 * @param owner
	 *            Fenster von dem aus der Dialog geöffnet wurde. Wenn null, dann
	 *            wird der Verbindungsdialog als Top Level Fenster geöffnet. Im
	 *            Fullscreen Modus ist das nicht schön :-(
	 */
	public ConnectionDialog(final Window owner) {
		Scene scene;
		try {
			final Parent root;
			root = FXMLLoader.load(getClass().getResource("/fxml/ConnectionDialog.fxml"),
					ResourceBundle.getBundle("dictionary"));
			root.getStylesheets().add(getClass().getResource("/css/itrysql.css").toExternalForm());
			scene = new Scene(root);
		} catch (final Exception ex) {
			scene = FallbackSceneFactory.createNewInstance();
		}
		setScene(scene);
		initStyle(StageStyle.DECORATED);
		initModality(Modality.WINDOW_MODAL);
		initOwner(owner);

		centerOnScreen();
		setResizable(false);
		setTitle(DialogDictionary.LABEL_TITLE_CONNECT.toString());
	}

	/**
	 * Verbindungsdaten im Dialog setzen.
	 *
	 * @param connectionSetting
	 *            Verbindungsdaten
	 */
	protected void setConnectionSetting(final ConnectionSetting connectionSetting) {
		this.connectionSetting = connectionSetting;
	}

	/**
	 * @return Die im Dialog ausgewählten Verbindungsdaten
	 */
	public ConnectionSetting getConnectionSetting() {
		return connectionSetting;
	}
}
