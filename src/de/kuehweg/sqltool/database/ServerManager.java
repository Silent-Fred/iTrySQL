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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;

import de.kuehweg.sqltool.common.sqlediting.ConnectionSetting;

/**
 * Datenbankserver verwalten
 * 
 * @author Michael Kühweg
 */
public class ServerManager {

	private static ServerManager sharedInstance;

	static {
		sharedInstance = new ServerManager();
	}
	private Server server;

	private ServerManager() {
		// kein Aufruf von außerhalb
	}

	public static ServerManager getSharedInstance() {
		return sharedInstance;
	}

	/**
	 * Startet den Server auf Basis der angegebenen Verbindungsdaten
	 * 
	 * @param connectionSetting
	 *            Verbindungsdaten
	 * @param alias
	 *            Alias unter dem die Datenbank im Server gestartet wird
	 * @throws IOException
	 * @throws ServerAcl.AclFormatException
	 * @throws IllegalArgumentException
	 */
	public void startServer(final ConnectionSetting connectionSetting,
			final String alias) throws IOException,
			ServerAcl.AclFormatException, IllegalArgumentException {
		try {
			// eventuell noch laufende andere Server beenden
			shutdownServer();
		} catch (final Throwable ex) {
		}
		if (connectionSetting == null
				|| !connectionSetting.getType().isPossibleServer()) {
			// die angegebene Verbindung muss als Servergrundlage geeignet sein
			throw new IllegalArgumentException(
					"Illegal: HSQL-Server based on type "
							+ (connectionSetting == null ? "<null>"
									: connectionSetting.getType()));
		}
		// die Datenbank wird immer als database.0 im HSQLDB-Server gestartet
		final HsqlProperties properties = new HsqlProperties();
		properties.setProperty("server.database.0",
				connectionSetting.getUrlWithoutTypePrefix());
		String dbname = alias != null && alias.trim().length() > 0 ? alias
				: connectionSetting.getDbName();
		try {
			dbname = URLEncoder.encode(dbname, "UTF-8");
		} catch (final UnsupportedEncodingException ex) {
			dbname = "johndoe";
		}
		properties.setProperty("server.dbname.0", dbname);
		// server.silent true - default
		// server.trace false - default
		// server.address - default
		// server.tls false - default
		// server.daemon false - default
		// server.remote_open false - default
		properties.setProperty("hsqldb.tx",
				DatabaseConstants.DEFAULT_TRANSACTION_CONTROL);
		server = new Server();
		server.setProperties(properties);
		server.start();
	}

	/**
	 * Server anhalten.
	 * 
	 * @throws Throwable
	 */
	public void shutdownServer() throws Throwable {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Prüfen, ob derzeit ein Server aktiv ist
	 * 
	 * @return
	 */
	public boolean isRunning() {
		if (server == null) {
			return false;
		}
		try {
			server.checkRunning(true);
		} catch (final Exception e) {
			return false;
		}
		return true;
	}
}
