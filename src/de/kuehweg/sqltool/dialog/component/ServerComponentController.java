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

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.hsqldb.server.ServerAcl;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.sqlediting.ConnectionSetting;
import de.kuehweg.sqltool.common.sqlediting.ConnectionSettings;
import de.kuehweg.sqltool.database.ServerManager;
import de.kuehweg.sqltool.dialog.ErrorMessage;

/**
 * Steuerung der Serverkomponente (Dialog)
 * 
 * @author Michael Kühweg
 */
public class ServerComponentController {

	public static class Builder {

		private ComboBox<ConnectionSetting> serverConnectionSelection;
		private Button startButton;
		private Button shutdownButton;
		private TextField serverAlias;

		public Builder() {
		}

		public Builder serverConnectionSelection(
				final ComboBox<ConnectionSetting> serverConnectionSelection) {
			this.serverConnectionSelection = serverConnectionSelection;
			return this;
		}

		public Builder startButton(final Button startButton) {
			this.startButton = startButton;
			return this;
		}

		public Builder shutdownButton(final Button shutdownButton) {
			this.shutdownButton = shutdownButton;
			return this;
		}

		public Builder serverAlias(final TextField serverAlias) {
			this.serverAlias = serverAlias;
			return this;
		}

		public ServerComponentController build() {
			return new ServerComponentController(this);
		}
	}

	private final ComboBox<ConnectionSetting> serverConnectionSelection;
	private final Button startButton;
	private final Button shutdownButton;
	private final TextField serverAlias;

	private ServerComponentController(final Builder builder) {
		serverConnectionSelection = builder.serverConnectionSelection;
		startButton = builder.startButton;
		shutdownButton = builder.shutdownButton;
		serverAlias = builder.serverAlias;
		fillServerConnectionSettings();
		controlServerStatusButtonStates();
	}

	public void changeServerConnection() {
		final ConnectionSetting connectionSetting = serverConnectionSelection
				.getValue();
		serverAlias.setText(connectionSetting != null ? connectionSetting
				.getDbName() : null);
	}

	public void startServer() {
		try {
			final ConnectionSetting connectionSetting = serverConnectionSelection
					.getValue();
			ServerManager.getSharedInstance().startServer(connectionSetting,
					serverAlias.getText());
		} catch (IOException | ServerAcl.AclFormatException
				| IllegalArgumentException ex) {
			final ErrorMessage msg = new ErrorMessage(
					DialogDictionary.MESSAGEBOX_ERROR.toString(),
					DialogDictionary.ERR_SERVER_START_FAILED.toString(),
					DialogDictionary.COMMON_BUTTON_OK.toString());
			msg.askUserFeedback();
		}
	}

	public void shutdownServer() {
		try {
			ServerManager.getSharedInstance().shutdownServer();
		} catch (final Throwable ex) {
			final ErrorMessage msg = new ErrorMessage(
					DialogDictionary.MESSAGEBOX_ERROR.toString(),
					DialogDictionary.ERR_SERVER_START_FAILED.toString(),
					DialogDictionary.COMMON_BUTTON_OK.toString());
			msg.askUserFeedback();
		}
	}

	public void refreshServerConnectionSettings() {
		fillServerConnectionSettings();
	}

	private void fillServerConnectionSettings() {
		final ConnectionSettings settings = new ConnectionSettings();
		final ObservableList<ConnectionSetting> serverSettings = FXCollections
				.observableArrayList();
		for (final ConnectionSetting connectionSetting : settings
				.getConnectionSettings()) {
			if (connectionSetting.getType().isPossibleServer()) {
				serverSettings.add(connectionSetting);
			}
		}
		serverConnectionSelection.setItems(serverSettings);
	}

	private void controlServerStatusButtonStates() {
		startButton.disableProperty().set(false);
		shutdownButton.disableProperty().set(true);
		Task<Void> serverStatusTask;
		serverStatusTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				while (!isCancelled()) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							final boolean running = ServerManager
									.getSharedInstance().isRunning();
							startButton.disableProperty().set(running);
							shutdownButton.disableProperty().set(!running);
						}
					});
					Thread.sleep(1000);
				}
				return null;
			}
		};
		final Thread th = new Thread(serverStatusTask);
		th.setDaemon(true);
		th.start();
	}
}
