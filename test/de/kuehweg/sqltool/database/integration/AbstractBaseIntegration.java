/*
 * Copyright (c) 2015, Michael Kühweg
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
package de.kuehweg.sqltool.database.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import de.kuehweg.sqltool.database.DatabaseConstants;
import de.kuehweg.sqltool.database.JDBCType;

/**
 * Basisklasse für Integrationstests mit der HSQLDB-Datenbank.
 * 
 * @author Michael Kühweg
 */
public abstract class AbstractBaseIntegration {

	private Connection connection;

	protected Connection getTestConnection() {
		return connection;
	}

	protected void openTestConnection() throws SQLException {
		final Properties properties = new Properties();
		properties.setProperty("user", "JOHN_DOE");
		properties.setProperty("password", "secret");
		properties.setProperty("shutdown", "true");
		// Transaktionsmanagements wie in der Applikation verwenden
		properties.setProperty(DatabaseConstants.HSQLDB_PROPERTY_TX_CONTROL,
				DatabaseConstants.DEFAULT_TRANSACTION_CONTROL);
		properties.setProperty("hsqldb.files_readonly", "true");
		connection = DriverManager.getConnection(JDBCType.HSQL_IN_MEMORY.getUrlPrefix() + "mem", properties);
	}

	protected void closeTestConnection() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	protected void dropSchema(final String schema) throws SQLException {
		if (connection != null) {
			connection.createStatement().execute("DROP SCHEMA " + schema + " CASCADE");
		}
	}

	protected void dropPublicSchema() throws SQLException {
		dropSchema("PUBLIC");
	}

}
