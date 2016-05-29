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

package de.kuehweg.sqltool.common.sqlediting;

import java.util.NoSuchElementException;

/**
 * Einfache Implementierung des ScannerI-Interfaces.
 *
 * @author Michael Kühweg
 */
public class DefaultScanner implements ScannerI {

	private final char[] content;
	private int position;
	private final int maxPosition;

	private int startToken;

	/**
	 * DefaultScanner für eine String Eingabe.
	 *
	 * @param input
	 */
	public DefaultScanner(final String input) {
		content = input != null ? input.toCharArray() : new char[0];
		position = 0;
		maxPosition = content.length - 1;
		startToken = 0;
	}

	/**
	 * @see de.kuehweg.sqltool.common.sqlediting.ScannerI#read()
	 */
	@Override
	public char read() throws NoSuchElementException {
		if (hasMoreElements()) {
			return content[position++];
		}
		throw new NoSuchElementException();
	}

	/**
	 * @see de.kuehweg.sqltool.common.sqlediting.ScannerI#lookahead()
	 */
	@Override
	public char lookahead() throws NoSuchElementException {
		if (hasMoreElements()) {
			return content[position];
		}
		throw new NoSuchElementException();
	}

	/**
	 * @see de.kuehweg.sqltool.common.sqlediting.ScannerI#hasMoreElements()
	 */
	@Override
	public boolean hasMoreElements() {
		return position <= maxPosition;
	}

	/**
	 * @see de.kuehweg.sqltool.common.sqlediting.ScannerI#pushback()
	 */
	@Override
	public void pushback() {
		pushback(1);
	}

	/**
	 * @see de.kuehweg.sqltool.common.sqlediting.ScannerI#pushback(int)
	 */
	@Override
	public void pushback(final int n) {
		position -= n;
		if (position < 0) {
			position = 0;
		}
	}

	/**
	 * @see de.kuehweg.sqltool.common.sqlediting.ScannerI#startToken()
	 */
	@Override
	public void startToken() {
		startToken = position;
	}

	/**
	 * @see de.kuehweg.sqltool.common.sqlediting.ScannerI#currentToken()
	 */
	@Override
	public char[] currentToken() {
		if (position - startToken > 0) {
			final char[] token = new char[position - startToken];
			System.arraycopy(content, startToken, token, 0, position - startToken);
			return token;
		}
		return null;
	}

}
