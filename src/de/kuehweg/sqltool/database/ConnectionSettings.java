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

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Verwaltung der vom Anwender angelegten Verbindungsdaten (ConnectionSetting)
 * über Preferences. Die Verbindungsdaten stehen beim nächsten Programmstart
 * wieder zur Verfügung.
 *
 * @author Michael Kühweg
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ConnectionSettings {

	private final Set<ConnectionSetting> connectionSettings;

	public ConnectionSettings() {
		connectionSettings = new HashSet<>(20);
	}

	/**
	 * Bestehende Verbindungsdaten auslesen.
	 *
	 * @return
	 */
	@XmlElement(name = "connection")
	public Set<ConnectionSetting> getConnectionSettings() {
		return connectionSettings;
	}

	/**
	 * Fügt eine neue Verbindungsbeschreibung hinzu und liefert die entsprechend
	 * erweiterte Liste der Verbindungsdaten zurück.
	 *
	 * @param connectionSetting
	 * @return
	 */
	public Set<ConnectionSetting> addConnectionSetting(final ConnectionSetting connectionSetting) {
		if (connectionSetting != null) {
			connectionSettings.add(connectionSetting);
		}
		return getConnectionSettings();
	}

	/**
	 * Entfernt eine Verbindungsbeschreibung und liefert die entsprechend
	 * bereinigte Liste der Verbindungsdaten zurück.
	 *
	 * @param connectionSetting
	 * @return
	 */
	public Set<ConnectionSetting> removeConnectionSetting(final ConnectionSetting connectionSetting) {
		if (connectionSetting != null) {
			connectionSettings.remove(connectionSetting);
		}
		return getConnectionSettings();
	}

}
