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

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;

/**
 * Abbildung der vom Anwender steuerbaren Einstellungen, die beim nächsten
 * Programmstart wiederhergestellt werden.
 *
 * @author Michael Kühweg
 */
public class UserPreferences implements UserPreferencesI {

	// Keys der Preferences als Konstanten definiert
	private static final String LIMIT_MAX_ROWS = "limitMaxRows";
	private static final String FONT_SIZE_STATEMENT_INPUT = "fontSize.statementInput";
	private static final String FONT_SIZE_DB_OUTPUT = "fontSize.dbOutput";
	private static final String BEEP_AUDIO_CLIP = "beepAudioClip";
	private static final String BEEP_VOLUME = "beepVolume";
	// Preferences mit Defaultwerten
	private static final int DEFAULT_FONT_SIZE_STATEMENT_INPUT = 12;
	private static final int DEFAULT_FONT_SIZE_DB_OUTPUT = 10;
	private boolean limitMaxRows = isDefaultLimitMaxRows();
	private int fontSizeStatementInput = getDefaultFontSizeStatementInput();
	private int fontSizeDbOutput = getDefaultFontSizeDbOutput();
	private double beepVolume = getDefaultBeepVolume();
	private ProvidedAudioClip beepAudioClip = getDefaultBeepAudioClip();

	private final Preferences preferences;

	public UserPreferences() {
		preferences = Preferences.userNodeForPackage(getClass()).node(getClass().getSimpleName());
		initialize();
		addChangeListener();
	}

	private void initialize() {
		limitMaxRows = preferences.getBoolean(LIMIT_MAX_ROWS, isDefaultLimitMaxRows());
		fontSizeStatementInput = preferences.getInt(FONT_SIZE_STATEMENT_INPUT, getDefaultFontSizeStatementInput());
		fontSizeDbOutput = preferences.getInt(FONT_SIZE_DB_OUTPUT, getDefaultFontSizeDbOutput());
		beepVolume = preferences.getDouble(BEEP_VOLUME, getDefaultBeepVolume());
		try {
			final String beepAudioClipName = preferences.get(BEEP_AUDIO_CLIP, getDefaultBeepAudioClip().name());
			beepAudioClip = ProvidedAudioClip.valueOf(beepAudioClipName);
		} catch (final IllegalArgumentException ex) {
			beepAudioClip = getDefaultBeepAudioClip();
		}
	}

	private void addChangeListener() {
		preferences.addPreferenceChangeListener((final PreferenceChangeEvent evt) -> {
			try {
				evt.getNode().flush();
			} catch (final BackingStoreException ex) {
				Logger.getLogger(UserPreferences.class.getName()).log(Level.WARNING, evt.getKey(), ex);
			}
		});
	}

	/**
	 * Datenbankabfragen begrenzt?
	 *
	 * @return
	 */
	@Override
	public boolean isLimitMaxRows() {
		return limitMaxRows;
	}

	/**
	 * Begrenzung der Ergebnismengen ein- oder ausschalten.
	 *
	 * @param limitMaxRows
	 */
	@Override
	public void setLimitMaxRows(final boolean limitMaxRows) {
		this.limitMaxRows = limitMaxRows;
		preferences.putBoolean(LIMIT_MAX_ROWS, limitMaxRows);
	}

	/**
	 * Ausgewählte Schriftgröße für die Eingabe der SQL-Anweisungen.
	 *
	 * @return
	 */
	@Override
	public int getFontSizeStatementInput() {
		return fontSizeStatementInput;
	}

	/**
	 * Schriftgröße für die Eingabe der SQL-Anweisungen in den Voreinstellungen
	 * setzen.
	 *
	 * @param fontSize
	 */
	@Override
	public void setFontSizeStatementInput(final int fontSize) {
		fontSizeStatementInput = fontSize;
		preferences.putInt(FONT_SIZE_STATEMENT_INPUT, fontSize);
	}

	/**
	 * Ausgewählte Schriftgröße für die DB-Ausgaben.
	 *
	 * @return
	 */
	@Override
	public int getFontSizeDbOutput() {
		return fontSizeDbOutput;
	}

	/**
	 * Schriftgröße für die DB-Ausgaben in den Voreinstellungen setzen.
	 *
	 * @param fontSize
	 */
	@Override
	public void setFontSizeDbOutput(final int fontSize) {
		fontSizeDbOutput = fontSize;
		preferences.putInt(FONT_SIZE_DB_OUTPUT, fontSize);
	}

	/**
	 * Lautstärke des Benachrichtigungstons wenn Aktionen abgeschlossen sind.
	 *
	 * @return
	 */
	@Override
	public double getBeepVolume() {
		return beepVolume;
	}

	/**
	 * Lautstärke des Benachrichtgungstons setzen.
	 *
	 * @param beepVolume
	 */
	@Override
	public void setBeepVolume(final double beepVolume) {
		this.beepVolume = beepVolume;
		preferences.putDouble(BEEP_VOLUME, beepVolume);
	}

	/**
	 * Benachrichtigungston wenn Aktionen abgeschlossen sind.
	 *
	 * @return
	 */
	@Override
	public ProvidedAudioClip getBeepAudioClip() {
		return beepAudioClip;
	}

	/**
	 * Benachrichtgungston setzen.
	 *
	 * @param beepAudioClip
	 */
	@Override
	public void setBeepAudioClip(final ProvidedAudioClip beepAudioClip) {
		this.beepAudioClip = beepAudioClip;
		preferences.put(BEEP_AUDIO_CLIP, beepAudioClip.name());
	}

	@Override
	public ProvidedAudioClip getDefaultBeepAudioClip() {
		return ProvidedAudioClip.BEEP;
	}

	@Override
	public double getDefaultBeepVolume() {
		return 0.0;
	}

	@Override
	public int getDefaultFontSizeDbOutput() {
		return DEFAULT_FONT_SIZE_DB_OUTPUT;
	}

	@Override
	public int getDefaultFontSizeStatementInput() {
		return DEFAULT_FONT_SIZE_STATEMENT_INPUT;
	}

	@Override
	public boolean isDefaultLimitMaxRows() {
		return true;
	}

}
