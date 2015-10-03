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

import java.net.URL;
import java.util.ResourceBundle;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.ConnectionSetting;
import de.kuehweg.sqltool.database.ManagedConnectionSettings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller für den Verbindunsgdialog.
 *
 * @author Michael Kühweg
 */
public class ConnectionDialogController implements Initializable {

	@FXML
	private Button cancel;
	@FXML
	private Button connect;
	@FXML
	private ComboBox<ConnectionSetting> connectionSettings;
	@FXML
	private PasswordField password;
	@FXML
	private TextField user;

	/**
	 * Benutzer bricht ohne Verbindungsaufbau ab.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	public void cancel(final ActionEvent event) {
		final Node node = (Node) event.getSource();
		final Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
	}

	/**
	 * Wechsel der ausgewählten Verbindung in der DropDown-Liste.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	public void changeConnection(final ActionEvent event) {
		user.setText(connectionSettings.getValue().getUser());
	}

	/**
	 * Aufbau einer Datenbankverbindung auf Basis der im Dialog ausgewählten
	 * Verbindungsdaten.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	public void connect(final ActionEvent event) {
		final ConnectionSetting selectedSetting = connectionSettings.getValue();
		if (selectedSetting == null) {
			final InfoBox info = new InfoBox(DialogDictionary.LABEL_CONNECT.toString(),
					DialogDictionary.MSG_SELECT_CONNECTION.toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
			info.askUserFeedback();
		} else {
			final ConnectionSetting connectionSetting = new ConnectionSetting(selectedSetting.getName(),
					selectedSetting.getType(), selectedSetting.getDbPath(), selectedSetting.getDbName(), user.getText(),
					password.getText());
			final Node node = (Node) event.getSource();
			final ConnectionDialog stage = (ConnectionDialog) node.getScene().getWindow();
			stage.setConnectionSetting(connectionSetting);
			stage.close();
		}
	}

	@Override
	public void initialize(final URL fxmlFileLocation, final ResourceBundle resources) {
		assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'ConnectionDialog.fxml'.";
		assert connect != null : "fx:id=\"connect\" was not injected: check your FXML file 'ConnectionDialog.fxml'.";
		assert connectionSettings != null : "fx:id=\"connectionSettings\" was not injected: check your FXML file 'ConnectionDialog.fxml'.";
		assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'ConnectionDialog.fxml'.";
		assert user != null : "fx:id=\"user\" was not injected: check your FXML file 'ConnectionDialog.fxml'.";

		final ManagedConnectionSettings settings = new ManagedConnectionSettings();
		connectionSettings.setItems(FXCollections.observableArrayList(settings.getConnectionSettings()));
	}
}
