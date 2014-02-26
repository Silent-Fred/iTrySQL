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

    // Keys der Preferences als Konstanten definiert
    private static final String LIMIT_MAX_ROWS = "limitMaxRows";
    private static final String FONT_SIZE_STATEMENT_INPUT =
            "fontSize.statementInput";
    private static final String FONT_SIZE_DB_OUTPUT = "fontSize.dbOutput";
    private static final String BEEP_AUDIO_CLIP = "beepAudioClip";
    private static final String BEEP_VOLUME = "beepVolume";
    // Preferences mit Defaultwerten
    private boolean limitMaxRows = true;
    private int fontSizeStatementInput = 12;
    private int fontSizeDbOutput = 12;
    private double beepVolume = 0.0;
    private ProvidedAudioClip beepAudioClip = ProvidedAudioClip.BEEP;
    private final Preferences preferences;
    private boolean initialized;

    public UserPreferences() {
        preferences = Preferences.userNodeForPackage(getClass()).node(
                getClass().getSimpleName());
    }

    private void initialize() {
        if (!initialized) {
            limitMaxRows = preferences.getBoolean(LIMIT_MAX_ROWS, true);
            fontSizeStatementInput = preferences.getInt(
                    FONT_SIZE_STATEMENT_INPUT, 12);
            fontSizeDbOutput = preferences.getInt(FONT_SIZE_DB_OUTPUT, 12);
            beepVolume = preferences.getDouble(BEEP_VOLUME, 0.0);
            try {
                String beepAudioClipName = preferences.
                        get(BEEP_AUDIO_CLIP, "");
                beepAudioClip = ProvidedAudioClip.valueOf(beepAudioClipName);
            } catch (IllegalArgumentException ex) {
                beepAudioClip = ProvidedAudioClip.BEEP;
            }
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
            preferences.putBoolean(LIMIT_MAX_ROWS, limitMaxRows);
            try {
                preferences.flush();
            } catch (final BackingStoreException e) {
            }
        }
    }

    /**
     * Ausgewählte Schriftgröße für die Eingabe der SQL-Anweisungen
     *
     * @return
     */
    public int getFontSizeStatementInput() {
        initialize();
        return fontSizeStatementInput;
    }

    /**
     * Schriftgröße für die Eingabe der SQL-Anweisungen in den Voreinstellungen
     * setzen
     *
     * @param fontSize
     */
    public void setFontSizeStatementInput(final int fontSize) {
        initialize();
        if (this.fontSizeStatementInput != fontSize) {
            this.fontSizeStatementInput = fontSize;
            preferences.putInt(FONT_SIZE_STATEMENT_INPUT, fontSize);
            try {
                preferences.flush();
            } catch (final BackingStoreException e) {
            }
        }
    }

    /**
     * Ausgewählte Schriftgröße für die DB-Ausgaben
     *
     * @return
     */
    public int getFontSizeDbOutput() {
        initialize();
        return fontSizeDbOutput;
    }

    /**
     * Schriftgröße für die DB-Ausgaben in den Voreinstellungen setzen
     *
     * @param fontSize
     */
    public void setFontSizeDbOutput(final int fontSize) {
        initialize();
        if (this.fontSizeDbOutput != fontSize) {
            this.fontSizeDbOutput = fontSize;
            preferences.putInt(FONT_SIZE_DB_OUTPUT, fontSize);
            try {
                preferences.flush();
            } catch (final BackingStoreException e) {
            }
        }
    }

    /**
     * Lautstärke des Benachrichtigungstons wenn Aktionen abgeschlossen sind.
     *
     * @return
     */
    public double getBeepVolume() {
        initialize();
        return beepVolume;
    }

    /**
     * Lautstärke des Benachrichtgungstons setzen.
     *
     * @param beepVolume
     */
    public void setBeepVolume(double beepVolume) {
        initialize();
        if (this.beepVolume != beepVolume) {
            this.beepVolume = beepVolume;
            preferences.putDouble(BEEP_VOLUME, beepVolume);
            try {
                preferences.flush();
            } catch (final BackingStoreException e) {
            }
        }
    }

    /**
     * Benachrichtigungston wenn Aktionen abgeschlossen sind.
     *
     * @return
     */
    public ProvidedAudioClip getBeepAudioClip() {
        initialize();
        return beepAudioClip;
    }

    /**
     * Benachrichtgungston setzen.
     *
     * @param beepAudioClip
     */
    public void setBeepAudioClip(ProvidedAudioClip beepAudioClip) {
        initialize();
        if (this.beepAudioClip != beepAudioClip) {
            this.beepAudioClip = beepAudioClip;
            preferences.put(BEEP_AUDIO_CLIP, beepAudioClip.name());
            try {
                preferences.flush();
            } catch (final BackingStoreException e) {
            }
        }
    }
}
