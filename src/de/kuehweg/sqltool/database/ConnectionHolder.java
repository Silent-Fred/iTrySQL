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
package de.kuehweg.sqltool.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.sqlediting.ConnectionSetting;
import de.kuehweg.sqltool.dialog.ErrorMessage;

/**
 * Hält die Verbindung zur Datenbank
 * 
 * @author Michael Kühweg
 */
public class ConnectionHolder {

	private Connection connection;
	private final BooleanProperty connectedProperty;

	public ConnectionHolder() {
		connectedProperty = new SimpleBooleanProperty(false);
	}

	/**
	 * Baut die "richtige" Verbindung auf, abgeleitet aus den
	 * "benutzertauglichen" Verbindungsdaten
	 * 
	 * @param connection
	 */
	private void connect(final Connection connection) {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (final SQLException ex) {
			}
		}
		this.connection = connection;
	}

	/**
	 * Baut die Verbindung auf Basis der übergebenen Verbindungsdaten auf
	 * 
	 * @param connectionSetting
	 */
	public void connect(final ConnectionSetting connectionSetting) {
		Connection newConnection = null;
		try {
			Class.forName(connectionSetting.getType().getDriverClass())
					.newInstance();
			final Properties properties = new Properties();
			properties.setProperty(
					"user",
					connectionSetting.getUser() != null ? connectionSetting
							.getUser() : "");
			properties.setProperty(
					"password",
					connectionSetting.getPassword() != null ? connectionSetting
							.getPassword() : "");
			properties.setProperty("hsqldb.tx",
					DatabaseConstants.DEFAULT_TRANSACTION_CONTROL);
			newConnection = DriverManager.getConnection(
					connectionSetting.getUrl(), properties);
			connect(newConnection);
			connectedProperty.set(true);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | SQLException ex) {
			final ErrorMessage msg = new ErrorMessage(
					DialogDictionary.MESSAGEBOX_ERROR.toString(),
					DialogDictionary.ERR_CONNECTION_FAILURE.toString(),
					DialogDictionary.COMMON_BUTTON_OK.toString());
			msg.askUserFeedback();
		}
	}

	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				connectedProperty.set(false);
			} catch (final SQLException ex) {
			}
			connection = null;
		}
	}

	public BooleanProperty connectedProperty() {
		return connectedProperty;
	}

	public Connection getConnection() {
		return connection;
	}

	public Statement getStatement() throws SQLException {
		return connection.createStatement();
	}
}
