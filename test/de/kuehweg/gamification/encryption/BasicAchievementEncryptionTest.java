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
package de.kuehweg.gamification.encryption;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author Michael Kühweg
 */
public class BasicAchievementEncryptionTest {

	private static final byte[] TEST_VALUE = new byte[] { 8, 1, 19, 35, 0, 24, 1, 19, 84, (byte) 0xbe, (byte) 0xc0,
			(byte) 0x01 };

	@Test
	public void testEncrypt() throws Exception {
		BasicAchievementEncryption crypt1 = new BasicAchievementEncryption(
				"ASampleKey".getBytes(Charset.forName("UTF-8")));
		BasicAchievementEncryption crypt2 = new BasicAchievementEncryption(
				"AnotherSampleKey".getBytes(Charset.forName("UTF-8")));
		assertFalse(Arrays.equals(TEST_VALUE, crypt1.encrypt(TEST_VALUE)));
		// das könnte zwar rein theoretisch in hundert kalten Wintern passieren
		// - aber falls ja, dann sollte geprüft werden, ob das wirklich ein so
		// seltener Zufall ist, oder ob die Verschlüsselung kaputt ist
		assertFalse(Arrays.equals(crypt1.encrypt(TEST_VALUE), crypt2.encrypt(TEST_VALUE)));
	}

	@Test
	public void testDecrypt() throws Exception {
		BasicAchievementEncryption crypt1 = new BasicAchievementEncryption(
				"ASampleKey".getBytes(Charset.forName("UTF-8")));
		assertTrue(Arrays.equals(TEST_VALUE, crypt1.decrypt(crypt1.encrypt(TEST_VALUE))));
	}
}