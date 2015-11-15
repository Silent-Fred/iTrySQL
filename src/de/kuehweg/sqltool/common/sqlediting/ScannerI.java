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

package de.kuehweg.sqltool.common.sqlediting;

import java.util.NoSuchElementException;

/**
 * Schnittstelle für Scanner.
 *
 * @author Michael Kühweg
 */
public interface ScannerI {

	/**
	 * Liest das nächste Zeichen aus dem Eingabestrom.
	 *
	 * @return Nächstes Zeichen aus dem Eingabestrom.
	 * @throws NoSuchElementException
	 *             wenn über das Ende des Eingabestroms gelesen wird.
	 */
	char read() throws NoSuchElementException;

	/**
	 * Gibt das nächste Zeichen aus dem Eingabestrom zurück OHNE die Position im
	 * Eingabestrom zu verändern.
	 *
	 * @return Nächstes Zeichen aus dem Eingabestrom.
	 * @throws NoSuchElementException
	 *             wenn über das Ende des Eingabestroms gelesen wird.
	 */
	char lookahead() throws NoSuchElementException;

	/**
	 * Setzt den Eingabestrom um ein Zeichen zurück.
	 */
	void pushback();

	/**
	 * Setzt den Eingabestrom um <em>i</em> Zeichen zurück.
	 *
	 * @param n
	 *            Anzahl der Zeichen, um die der Eingabestrom zurückgesetzt
	 *            wird.
	 */
	void pushback(int n);

	/**
	 * Prüft, ob das Ende des Eingabestroms erreicht ist.
	 *
	 * @return true wenn noch weitere Zeichen mit {@link ScannerI#read()}
	 *         gelesen werden können, ohne dass eine
	 *         {@link NoSuchElementException} geworfen wird.
	 */
	boolean hasMoreElements();

	/**
	 * Teilt dem Scanner mit, dass ab der aktuellen Position ein neues Token
	 * beginnt.
	 */
	void startToken();

	/**
	 * Liefert den gelesenen Inhalt seit dem zuletzt mit
	 * {@link ScannerI#startToken()} markierten Anfang eines Tokens.
	 *
	 * @return Gelesene Zeichen ab der Anfangsposition des Tokens.
	 */
	char[] currentToken();

}
