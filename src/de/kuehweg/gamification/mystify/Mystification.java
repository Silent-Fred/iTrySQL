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
package de.kuehweg.gamification.mystify;

import de.kuehweg.gamification.encryption.BasicAchievementEncryption;

/**
 * Klasse zu Verschleierung des Inhalts der Dateien mit gespeicherten
 * Achievements.
 * <p>
 * Der gespeicherte Stand der Achievements soll:
 * <ul>
 * <li>nicht einfach mit einem Texteditor manipulierbar sein</li>
 * <li>keine offensichtliche Auskunft darüber geben, welche Leistungen noch zu
 * erbringen sind</li>
 * <li>nicht einfach von einem Anwender an einen anderen weiter gegegen werden
 * können</li>
 * <li>bei Änderungen keine offensichtliche Auskunft darüber geben, nach welchem
 * Schema die Datei von Hand manipuliert werden könnte, um einzelne oder alle
 * Achievements freizuschalten</li>
 * </ul>
 * Was hingegen <em>nicht</em> erforderlich ist: Hohe Sicherheit ;-) Wer sich
 * die Mühe macht, z.B. die Achievements in github nachzulesen und das
 * "Verschleierungsvefahren" nachstellt oder ausbaut o.ä., der soll das ruhig
 * tun - mit ähnlichem Aufwand sind die Achievements auch durch den "normalen"
 * Kurs erreichbar :-)
 * </p>
 * 
 * @author Michael Kühweg
 */
public class Mystification {

	public byte[] mystifyDataWithKey(final byte[] data, final byte[] key) throws Exception {
		byte[] zipped = Zippo.zip(data);
		return new BasicAchievementEncryption(key).encrypt(zipped);
	}

	public byte[] demystifyDataWithKey(final byte[] data, final byte[] key) throws Exception {
		byte[] decrypted = new BasicAchievementEncryption(key).decrypt(data);
		return Zippo.unzip(decrypted);
	}
}
