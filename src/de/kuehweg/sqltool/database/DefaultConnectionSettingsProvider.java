/*
 * Copyright (c) 2015-2016, Michael Kühweg
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

import de.kuehweg.sqltool.common.DialogDictionary;

/**
 * Liefert Standard-Verbindungsdaten, mit denen ohne explizite Verbindungsanlage
 * durch den Anwender gearbeitet werden kann.
 *
 * @author Michael Kühweg
 */
public final class DefaultConnectionSettingsProvider {

	/**
	 * Utility-Klasse ohne Instances.
	 */
	private DefaultConnectionSettingsProvider() {
	}

	/**
	 * Eine In-Memory Verbinung wird grundsätzlich immer angeboten.
	 *
	 * @return Verbindungsdaten für eine HSQLDB In-Memory Datenbank mit
	 *         Standardeinstellungen.
	 */
	public static ConnectionSetting getDefaultInMemoryConnection() {
		return new ConnectionSetting(DialogDictionary.LABEL_DEFAULT_CONNECTION_IN_MEMORY.toString(),
				JDBCType.HSQL_IN_MEMORY, null, "rastelli", null, null);
	}

	/**
	 * Eine dateibasierte Version im Benutzerverzeichnis.
	 *
	 * @return Verbindungsdaten für eine HSQLDB Standalone Datenbank mit
	 *         Standardeinstellungen. (im Benutzerverzeichnis des angemeldeten
	 *         Benutzers)
	 */
	public static ConnectionSetting getDefaultStandaloneUserHomeConnection() {
		return getStandaloneConnectionInDefaultDirectory(
				DialogDictionary.LABEL_DEFAULT_CONNECTION_STANDALONE_USER_HOME.toString(), "itrysql", "standard_db");
	}

	/**
	 * Eine dateibasierte Version im Benutzerverzeichnis als Vorlage für neue
	 * Verbindungen.
	 *
	 * @return Vorlage für Verbindungsdaten für eine HSQLDB Standalone Datenbank
	 */
	public static ConnectionSetting getDefaultTemplateStandaloneUserHomeConnection() {
		return getStandaloneConnectionInDefaultDirectory(DialogDictionary.PATTERN_NEW_CONNECTION_NAME.toString(),
				DialogDictionary.PATTERN_NEW_CONNECTION_FOLDER.toString(),
				DialogDictionary.PATTERN_NEW_CONNECTION_FILE.toString());
	}

	/**
	 * Baut die Verbindungsdaten für eine HSQLDB Standalone Datenbank aus den
	 * übergebenen Parametern auf. Ausgangspunkt ist dabei immer das
	 * Benutzerverzeichnis des angemeldeten Benutzers.
	 *
	 * @param name
	 *            Name der Verbindung (zur Anzeige in der Dialogauswahl)
	 * @param dbFolderInDefaultDirectory
	 *            Unterverzeichnis, das im Benutzerverzeichnis angelegt wird.
	 * @param dbName
	 *            Name der Datenbank
	 * @return Verbindungsdaten
	 */
	private static ConnectionSetting getStandaloneConnectionInDefaultDirectory(final String name,
			final String dbFolderInDefaultDirectory, final String dbName) {
		return new ConnectionSetting(name, JDBCType.HSQL_STANDALONE, defaultDirectory() + dbFolderInDefaultDirectory,
				dbName, null, null);
	}

	/**
	 * Standardverzeichnis zur Anlage von HSQLDB Standalone Datenbanken.
	 *
	 * @return Standardverzeichnis (derzeit das Benutzerverzeichnis des
	 *         angemeldeten Benutzers)
	 */
	private static String defaultDirectory() {
		return System.getProperty("user.home") + "/";
	}
}
