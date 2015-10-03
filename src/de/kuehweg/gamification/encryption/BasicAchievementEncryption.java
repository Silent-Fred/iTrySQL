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

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Michael Kühweg
 */
public class BasicAchievementEncryption {

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	private final Key key;
	private final IvParameterSpec ivParameterSpec;

	public BasicAchievementEncryption(final byte[] keyValue) {
		try {
			key = generateKey(keyValue);
			ivParameterSpec = generateIvParameterSpec();
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
	}

	public byte[] encrypt(final byte[] data) throws Exception {
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
		final byte[] encrypted = cipher.doFinal(data);
		return Base64.getEncoder().encode(encrypted);
	}

	public byte[] decrypt(final byte[] encryptedData) throws Exception {
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
		final byte[] decoded = Base64.getDecoder().decode(encryptedData);
		final byte[] decodedAndDecrypted = cipher.doFinal(decoded);
		return decodedAndDecrypted;
	}

	private Key generateKey(final byte[] keyValue) throws NoSuchAlgorithmException {
		final MessageDigest md = MessageDigest.getInstance("MD5");
		final Key key = new SecretKeySpec(md.digest(keyValue), "AES");
		return key;
	}

	private IvParameterSpec generateIvParameterSpec() {
		return new IvParameterSpec(new byte[] { 0x08, 0x01, 0x19, 0x35, (byte) 0xab, (byte) 0xad, (byte) 0xba,
				(byte) 0xbe, 0x24, 0x01, 0x19, (byte) 0x84, (byte) 0xba, (byte) 0xdc, 0x0d, (byte) 0xe5 });
	}

}
