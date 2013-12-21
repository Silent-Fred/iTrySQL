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
package de.kuehweg.sqltool.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Dateioperationen
 * 
 * @author Michael Kühweg
 */
public class FileUtil {

	private FileUtil() {
		// no instances
	}

	/**
	 * Textdatei mit dem angegebenen Dateinamen einlesen und als String
	 * zurückliefern
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(final String file) throws IOException {
		final Path path = FileSystems.getDefault().getPath(file);
		final byte[] filearray = Files.readAllBytes(path);
		return new String(filearray);
	}

	/**
	 * Text in die Datei mit dem angegebenen Dateinamen ausgeben.
	 * 
	 * @param file
	 *            Dateiname
	 * @param text
	 *            Textinhalt der ausgegeben werden soll
	 * @throws IOException
	 */
	public static void writeFile(final String file, final String text)
			throws IOException {
		final Path path = FileSystems.getDefault().getPath(file);
		Files.write(path, text.getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
	}

	/**
	 * Datei lesen, die als Ressource im Paket eingebettet ist (z.B. vorgegebene
	 * Initialisierungsskripte für die Datenbankinhalte)
	 * 
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	public static String readResourceFile(final String resourceName)
			throws IOException {
		final InputStream tutorialStream = FileUtil.class
				.getResourceAsStream(resourceName);
		final StringBuffer b;
		final InputStreamReader reader = new InputStreamReader(tutorialStream,
				"UTF-8");
		final BufferedReader bufferedReader = new BufferedReader(reader);
		b = new StringBuffer();
		String s = null;
		while ((s = bufferedReader.readLine()) != null) {
			b.append(s);
			b.append('\n');
		}
		return b.toString();
	}

	/**
	 * Dateinamenserweiterung an einen Dateinamen anhängen, sofern nicht bereits
	 * vorhanden.
	 * 
	 * @param filename
	 *            Bisheriger Dateiname
	 * @param ext
	 *            erforderliche Erweiterung (z.B. html, sql o.ä.)
	 * @return Dateiname mit Erweiterung
	 */
	public static String enforceExtension(final String filename,
			final String ext) {
		if (ext == null) {
			return filename;
		}
		final String trimmedExt = ext.trim();
		final String trimmedFilename = filename.trim();
		if (trimmedFilename.toLowerCase().endsWith(
				"." + trimmedExt.toLowerCase())) {
			return trimmedFilename;
		}
		return trimmedFilename + (trimmedExt.startsWith(".") ? "" : ".") + ext;
	}
}
