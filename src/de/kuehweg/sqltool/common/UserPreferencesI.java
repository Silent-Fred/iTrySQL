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
package de.kuehweg.sqltool.common;

/**
 *
 * @author Michael Kühweg
 */
public interface UserPreferencesI {

    /**
     * Benachrichtigungston wenn Aktionen abgeschlossen sind.
     *
     * @return
     */
    ProvidedAudioClip getBeepAudioClip();

    /**
     * Lautstärke des Benachrichtigungstons wenn Aktionen abgeschlossen sind.
     *
     * @return
     */
    double getBeepVolume();

    /**
     * Ausgewählte Schriftgröße für die DB-Ausgaben
     *
     * @return
     */
    int getFontSizeDbOutput();

    /**
     * Ausgewählte Schriftgröße für die Eingabe der SQL-Anweisungen
     *
     * @return
     */
    int getFontSizeStatementInput();

    /**
     * Datenbankabfragen begrenzt?
     *
     * @return
     */
    boolean isLimitMaxRows();

    /**
     * Benachrichtgungston setzen.
     *
     * @param beepAudioClip
     */
    void setBeepAudioClip(final ProvidedAudioClip beepAudioClip);

    /**
     * Lautstärke des Benachrichtgungstons setzen.
     *
     * @param beepVolume
     */
    void setBeepVolume(final double beepVolume);

    /**
     * Schriftgröße für die DB-Ausgaben in den Voreinstellungen setzen
     *
     * @param fontSize
     */
    void setFontSizeDbOutput(final int fontSize);

    /**
     * Schriftgröße für die Eingabe der SQL-Anweisungen in den Voreinstellungen setzen
     *
     * @param fontSize
     */
    void setFontSizeStatementInput(final int fontSize);

    /**
     * Begrenzung der Ergebnismengen ein- oder ausschalten
     *
     * @param limitMaxRows
     */
    void setLimitMaxRows(final boolean limitMaxRows);

    /**
     * Standard-Benachrichtigungston wenn Aktionen abgeschlossen sind.
     *
     * @return
     */
    ProvidedAudioClip getDefaultBeepAudioClip();

    /**
     * Standard-Lautstärke des Benachrichtigungstons wenn Aktionen abgeschlossen sind.
     *
     * @return
     */
    double getDefaultBeepVolume();

    /**
     * Standard-Schriftgröße der DB-Ausgaben
     *
     * @return
     */
    int getDefaultFontSizeDbOutput();

    /**
     * Standard-Schriftgröße für die Eingabe der SQL-Anweisungen
     *
     * @return
     */
    int getDefaultFontSizeStatementInput();

    /**
     * Standardeinstellung begrenzter Datenbankabfragen
     *
     * @return
     */
    boolean isDefaultLimitMaxRows();
}
