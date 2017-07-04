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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Dateioperationen.
 *
 * @author Michael Kühweg
 */
public final class FileUtil {

	/**
	 *
	 */
	private static final String FILENAME_EXTENSION_SEPARATOR = ".";
	private static final String CHARSET_UTF_8 = "UTF-8";

	private FileUtil() {
		// no instances
	}

	/**
	 * Liest die Daten aus einer URL ein und gibt sie als String zurück.
	 * Voraussetzung sind daher Textdateien (wird jedoch nicht verifiziert).
	 * Convenience für das Nachlesen von Vorlagedateien, Snippets,...
	 *
	 * @param url
	 *            URL der zu ladenden Datei / Ressource
	 * @return Den Dateiinhalt / die Resource als String
	 * @throws URISyntaxException
	 *             Bei fehlerhafter Angabe der URL.
	 * @throws IOException
	 *             Wenn Fehler beim Zugriff auf die Datei/Ressource auftreten.
	 */
	public static String readFile(final URL url) throws URISyntaxException, IOException {
		try (final InputStream inputStream = url.openStream()) {
			final StringBuffer b;
			final InputStreamReader reader = new InputStreamReader(inputStream, CHARSET_UTF_8);
			try (BufferedReader bufferedReader = new BufferedReader(reader)) {
				b = new StringBuffer();
				String s;
				while ((s = bufferedReader.readLine()) != null) {
					b.append(s);
					b.append('\n');
				}
			}
			return b.toString();
		}
	}

	/**
	 * Text in die Datei mit dem angegebenen Dateinamen ausgeben.
	 *
	 * @param file
	 *            Dateiname
	 * @param text
	 *            Textinhalt der ausgegeben werden soll
	 * @throws IOException
	 *             In manchen Java-Versionen in Kombinationen mit manchen
	 *             Begriebssystemen gab es in der Vergangenheit die
	 *             unterschiedlichsten Probleme :-( Falls das je nach
	 *             Konstellation wieder auftaucht, wird die dann auftretende
	 *             Exception als "normales" IO-Problem verpackt.
	 */
	public static void writeFile(final URL file, final String text) throws IOException {
		Path path = null;
		try {
			path = Paths.get(file.toURI());
		} catch (final Exception ex) {
			// wenn Java mal wieder nicht mit der konkreten Plattform klarkommt
			// (derzeit keine Sonderbehandlung, wird als "normales" Problem
			// verpackt)
			throw new IOException(ex);
		}
		Files.write(path, text.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	/**
	 * Datei lesen, die als Ressource im Paket eingebettet ist (z.B. vorgegebene
	 * Initialisierungsskripte für die Datenbankinhalte). Es wird erwartet, dass
	 * die Datei Text enthält.
	 *
	 * @param resourceName
	 *            Name der Datei innerhalb des Pakets (Pfadangabe erforderlich)
	 * @return Inhalt der Datei als String
	 * @throws IOException
	 *             Falls die Ressourcendatei nicht gelesen werden kann.
	 */
	public static String readResourceFile(final String resourceName) throws IOException {
		final InputStream tutorialStream = FileUtil.class.getResourceAsStream(resourceName);
		final StringBuffer b;
		final InputStreamReader reader = new InputStreamReader(tutorialStream, CHARSET_UTF_8);
		try (BufferedReader bufferedReader = new BufferedReader(reader)) {
			b = new StringBuffer();
			String s = null;
			while ((s = bufferedReader.readLine()) != null) {
				b.append(s);
				b.append('\n');
			}
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
	public static URL enforceExtension(final URL filename, final String ext) {
		URL filenameWithExtension = null;
		final String safeAndTrimmedExt = ext != null ? ext.trim() : "";
		if (nothingToDoToEnforceExtension(filename, safeAndTrimmedExt)) {
			filenameWithExtension = filename;
		} else {
			try {
				filenameWithExtension = new URL(
						filename.toExternalForm() + (safeAndTrimmedExt.startsWith(FILENAME_EXTENSION_SEPARATOR) ? ""
								: FILENAME_EXTENSION_SEPARATOR) + safeAndTrimmedExt);
			} catch (final MalformedURLException ex) {
				filenameWithExtension = filename;
			}
		}
		return filenameWithExtension;
	}

	private static boolean nothingToDoToEnforceExtension(final URL filename, final String ext) {
		return filename.getFile().toLowerCase().endsWith(FILENAME_EXTENSION_SEPARATOR + ext.trim().toLowerCase());
	}

	/**
	 * Konvertierung in URI für ein File. Falls der Dateiname schon als URI
	 * vorliegt, wird nicht doppelt gewandelt (würde bei file.toURI()
	 * passieren).
	 *
	 * @param file
	 * @return URI der Datei
	 */
	public static URI convertToURI(final File file) {
		URI uri;
		try {
			uri = new URI("file:" + file.getAbsolutePath());
		} catch (final URISyntaxException ex) {
			uri = file.toURI();
		}
		return uri;
	}
}
