/*
 * Copyright (c) 2016, Michael Kühweg
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

package de.kuehweg.sqltool.dialog.component.editor;

/**
 * @author Michael Kühweg
 */
public class CaretPosition {

	private int line;

	private int character;

	public CaretPosition() {
		this(0, 0);
	}

	public CaretPosition(final int line, final int column) {
		super();
		this.line = line;
		character = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(final int line) {
		this.line = line;
	}

	public int getCharacter() {
		return character;
	}

	public void setCharacter(final int character) {
		this.character = character;
	}

	public int appliedOnString(final String text) {
		int position = 0;
		int countdownLines = line;
		int countdownCharacter = character;
		for (final char ch : text.toCharArray()) {
			if (countdownLines > 0) {
				if ('\n' == ch) {
					countdownLines--;
				}
			} else {
				if (countdownCharacter > 0) {
					countdownCharacter--;
				} else {
					return position;
				}
			}
			position++;
		}
		return position;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[line: " + line + ", ch: " + character + "]";
	}
}
