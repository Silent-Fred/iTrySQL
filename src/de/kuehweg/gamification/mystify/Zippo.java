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
package de.kuehweg.gamification.mystify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Michael Kühweg
 */
public final class Zippo {

	/**
	 * Utility-Klasse ohne Instances.
	 */
	private Zippo() {
	}

	public static byte[] zip(final byte[] data) throws IOException {
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream();
				final ZipOutputStream zip = new ZipOutputStream(output)) {
			zip.setMethod(ZipOutputStream.DEFLATED);
			final ZipEntry entry = new ZipEntry("achievement");

			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();

			final byte[] bytes = output.toByteArray();

			return bytes;
		}
	}

	public static byte[] unzip(final byte[] data) throws IOException {
		boolean unzippedSomething = false;
		final byte[] buffer = new byte[1024];
		try (final ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(data));
				// output mit grober Abschätzung der Länge anlegen
				final ByteArrayOutputStream out = new ByteArrayOutputStream(data.length * 4)) {
			while (zip.getNextEntry() != null) {
				int anzahl = 0;
				while ((anzahl = zip.read(buffer)) != -1) {
					// -1 wenn das Ende erreicht ist
					out.write(buffer, 0, anzahl);
				}
				unzippedSomething = true;
			}

			if (!unzippedSomething) {
				throw new ZipException("No ZipEntry in InputStream");
			}
			return out.toByteArray();
		}
	}
}
