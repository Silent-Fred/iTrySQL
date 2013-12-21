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

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.sqlediting.ConnectionSetting;

/**
 * Dialog zur Auswahl einer Datenbankverbindung
 * 
 * @author Michael Kühweg
 */
public class ConnectionDialog extends Stage {

	private ConnectionSetting connectionSetting;

	public ConnectionDialog() {
		super();
		try {
			final Parent root;
			root = FXMLLoader.load(
					getClass().getResource(
							"/resources/fxml/ConnectionDialog.fxml"),
					ResourceBundle.getBundle("dictionary"));
			initStyle(StageStyle.UTILITY);
			root.getStylesheets().add(
					getClass().getResource("/resources/css/itrysql.css")
							.toExternalForm());
			setScene(new Scene(root));
			centerOnScreen();
			setResizable(false);
			initModality(Modality.APPLICATION_MODAL);
			setTitle(DialogDictionary.LABEL_TITLE_CONNECT.toString());
		} catch (final Exception ex) {
			initStyle(StageStyle.UTILITY);
			initModality(Modality.APPLICATION_MODAL);
			setScene(new Scene(
					VBoxBuilder
							.create()
							.children(
									new Text(
											DialogDictionary.APPLICATION
													.toString()),
									new Text(DialogDictionary.ERR_LOAD_FXML
											.toString())).alignment(Pos.CENTER)
							.padding(new Insets(50)).build()));
			centerOnScreen();
			setResizable(false);
			setTitle(DialogDictionary.APPLICATION.toString());
		}
	}

	protected void setConnectionSetting(
			final ConnectionSetting connectionSetting) {
		this.connectionSetting = connectionSetting;
	}

	public ConnectionSetting getConnectionSetting() {
		return connectionSetting;
	}
}
