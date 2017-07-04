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

package de.kuehweg.gamification;

import java.nio.charset.Charset;

import de.kuehweg.gamification.mystify.Mystification;

/**
 * Eine dateibasierte Persistenz für als XML aufbereitete Fortschrittsdaten.
 * Diese werden jedoch nicht als Klartext abgelegt, um ein einfaches Bearbeiten
 * und Austauschen unter mehreren Anwendern zu erschweren. Erschweren, nicht
 * verhindern. Die Sicherheit der Verschlüsselung steht nicht im Vordergrund,
 * lediglich eine kleine Barriere gegen "Cheats" soll aufgebaut werden.
 *
 * @author Michael Kühweg
 */
public class ObfuscatedAchievementXmlFilePersister extends AchievementXmlFilePersister {

	private final String owner;

	/**
	 * @param filename
	 *            Name der Datei, in der der Fortschritt gespeichert wird.
	 * @param owner
	 *            Benutzername, Benutzerkennung o.ä. Die Inhalte werden an
	 *            diesen Wert gebunden (der Schlüssel zur Verschlüsselung leitet
	 *            sich aus diesem Wert ab)
	 */
	public ObfuscatedAchievementXmlFilePersister(final String filename, final String owner) {
		super(filename);
		this.owner = owner;
	}

	/**
	 * Daten werden verschleiert.
	 *
	 * @param data
	 *            Das vorbereitete XML als Byte-Array
	 * @return Die Daten, die als Byte-Array persistiert werden sollen.
	 * @throws Exception
	 */
	@Override
	protected byte[] prepareForPersist(final byte[] data) throws Exception {
		return new Mystification().mystifyDataWithKey(data, generateKeyForMystificationAndDemystification());
	}

	/**
	 * Verschleierte Daten zurückübersetzen.
	 *
	 * @param data
	 *            Die einelesenen Daten als Byte-Array
	 * @return Die Byte-Array-Repräsentation der XML Darstellung des Achievement
	 *         Fortschritts.
	 * @throws Exception
	 */
	@Override
	protected byte[] prepareAfterRead(final byte[] data) throws Exception {
		return new Mystification().demystifyDataWithKey(data, generateKeyForMystificationAndDemystification());
	}

	/**
	 * Damit gespeicherte Achievements nicht von Anwender zu Anwender
	 * weitergegeben werden können, werden sie mit dem Benutzernamen
	 * verschlüsselt.
	 *
	 * @return Schlüssel in Byte-Array-Repräsentation
	 */
	private byte[] generateKeyForMystificationAndDemystification() {
		return owner.getBytes(Charset.forName("UTF-8"));
	}

}
