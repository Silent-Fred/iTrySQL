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
package de.kuehweg.sqltool.database;

import de.kuehweg.sqltool.common.exception.DatabaseConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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
		if (this.connection != null && this.connection != connection) {
			disconnect();
		}
		this.connection = connection;
    }

    /**
     * Baut die Verbindung auf Basis der übergebenen Verbindungsdaten auf
     *
     * @param connectionSetting
     * @throws de.kuehweg.sqltool.common.exception.DatabaseConnectionException
     */
    public void connect(final ConnectionSetting connectionSetting) throws DatabaseConnectionException {
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
            // in den Schulungsunterlagen wird das Verhalten des DBMS
            // auf Grundlage eines bestimmten Transaktionsmanagements
            // beschrieben - dieses wird daher explizit gesetzt
            properties.setProperty(
                    DatabaseConstants.HSQLDB_PROPERTY_TX_CONTROL,
                    DatabaseConstants.DEFAULT_TRANSACTION_CONTROL);
            if (connectionSetting.getType() == JDBCType.HSQL_STANDALONE) {
				// TODO von den beiden folgenden Properties wird eigentlich nur
                // eines benötigt. Bei Gelegenheit mal ein bisschen genauer
                // untersuchen, welcher Ansatz der bessere ist.

                // als Schulungsclient wird mit kleinen Datenbanken gearbeitet, den
                // automatischen Checkpoint daher recht niedrig ansetzen
                properties.setProperty(
                        DatabaseConstants.HSQLDB_PROPERTY_LOG_SIZE_MB,
                        DatabaseConstants.DEFAULT_LOG_SIZE_MB);
                // der automatische Shutdown ist zwar für "echte" DB-Clients
                // nicht wirklich empfehlenswert, aber für die Zwecke der Schulung
                // eigentlich genau das richtige Verhalten
                properties.setProperty(
                        DatabaseConstants.HSQLDB_PROPERTY_SHUTDOWN,
                        DatabaseConstants.DEFAULT_SHUTDOWN_STANDALONE_DB);
            }
            connect(DriverManager.getConnection(connectionSetting.getUrl(),
                    properties));
            connectedProperty.set(true);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            connectedProperty.set(false);
            throw new DatabaseConnectionException(ex);
        }
    }

    public void disconnect() {
        if (connection != null) {
            new DatabaseDisconnector(connection).disconnect();
            connectedProperty.set(false);
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
