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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Abbildung der vom Anwender steuerbaren Einstellungen, die beim nächsten
 * Programmstart wiederhergestellt werden.
 * 
 * @author Michael Kühweg
 */
public class UserPreferences {

	private boolean limitMaxRows = true;
	private int fontSize = 12;
	private final Preferences preferences;
	private boolean initialized;

	public UserPreferences() {
		preferences = Preferences.userNodeForPackage(getClass()).node(
				getClass().getSimpleName());
	}

	private void initialize() {
		if (!initialized) {
			limitMaxRows = preferences.getBoolean("limitMaxRows", true);
			fontSize = preferences.getInt("fontSize", 12);
			initialized = true;
		}
	}

	/**
	 * Datenbankabfragen begrenzt?
	 * 
	 * @return
	 */
	public boolean isLimitMaxRows() {
		initialize();
		return limitMaxRows;
	}

	/**
	 * Begrenzung der Ergebnismengen ein- oder ausschalten
	 * 
	 * @param limitMaxRows
	 */
	public void setLimitMaxRows(final boolean limitMaxRows) {
		initialize();
		if (this.limitMaxRows != limitMaxRows) {
			this.limitMaxRows = limitMaxRows;
			preferences.putBoolean("limitMaxRows", limitMaxRows);
			try {
				preferences.flush();
			} catch (final BackingStoreException e) {
			}
		}
	}

	/**
	 * Ausgewählte Schriftgröße
	 * 
	 * @return
	 */
	public int getFontSize() {
		initialize();
		return fontSize;
	}

	/**
	 * Schriftgröße in den Voreinstellungen setzen
	 * 
	 * @param fontSize
	 */
	public void setFontSize(final int fontSize) {
		initialize();
		if (this.fontSize != fontSize) {
			this.fontSize = fontSize;
			preferences.putInt("fontSize", fontSize);
			try {
				preferences.flush();
			} catch (final BackingStoreException e) {
			}
		}
	}
}
