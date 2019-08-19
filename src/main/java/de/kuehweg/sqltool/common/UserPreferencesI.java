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
	 * @return Benachrichtigungston wenn Aktionen abgeschlossen sind.
	 */
	ProvidedAudioClip getBeepAudioClip();

	/**
	 * @return Lautstärke des Benachrichtigungstons wenn Aktionen abgeschlossen
	 *         sind.
	 */
	double getBeepVolume();

	/**
	 * @return Präferierte Schriftgröße für die DB-Ausgaben.
	 */
	int getFontSizeDbOutput();

	/**
	 * @return Präferierte Schriftgröße für die Eingabe der SQL-Anweisungen.
	 */
	int getFontSizeStatementInput();

	/**
	 * @return Datenbankabfragen begrenzt?
	 */
	boolean isLimitMaxRows();

	/**
	 * @param beepAudioClip
	 *            Benachrichtgungston (nach Abschluss einer Aktion)
	 */
	void setBeepAudioClip(ProvidedAudioClip beepAudioClip);

	/**
	 * @param beepVolume
	 *            Lautstärke des Benachrichtgungstons. (aus: 0.0 - volle
	 *            Lautstärke: 1.0)
	 */
	void setBeepVolume(double beepVolume);

	/**
	 * Schriftgröße für die DB-Ausgaben in den Voreinstellungen setzen.
	 *
	 * @param fontSize
	 *            Schriftgröße
	 */
	void setFontSizeDbOutput(int fontSize);

	/**
	 * Schriftgröße für die Eingabe der SQL-Anweisungen in den Voreinstellungen
	 * setzen.
	 *
	 * @param fontSize
	 *            Schriftgröße
	 */
	void setFontSizeStatementInput(int fontSize);

	/**
	 * Begrenzung der Ergebnismengen ein- oder ausschalten.
	 *
	 * @param limitMaxRows
	 *            Wenn true, sollen die Datenbankabfragen nur eine begrenzte
	 *            Anzahl Ergebniszeilen liefern. Diese Einstellung hat noch
	 *            keinen Einfluss auf die tatsächliche Verarbeitung, sie ist
	 *            lediglich die vom Benutzer gewünschte Einstellung und muss
	 *            entsprechend in der Verarbeitungslogik ausgewertet werden.
	 */
	void setLimitMaxRows(boolean limitMaxRows);

	/**
	 * @return Standard-Benachrichtigungston wenn Aktionen abgeschlossen sind.
	 *         (wenn noch keine Benutzerpräferenz hinterlegt ist)
	 */
	ProvidedAudioClip getDefaultBeepAudioClip();

	/**
	 * @return Standard-Lautstärke des Benachrichtigungstons wenn Aktionen
	 *         abgeschlossen sind. (wenn noch keine Benutzerpräferenz hinterlegt
	 *         ist)
	 */
	double getDefaultBeepVolume();

	/**
	 * @return Standard-Schriftgröße der DB-Ausgaben. (wenn noch keine
	 *         Benutzerpräferenz hinterlegt ist)
	 */
	int getDefaultFontSizeDbOutput();

	/**
	 * @return Standard-Schriftgröße für die Eingabe der SQL-Anweisungen. (wenn
	 *         noch keine Benutzerpräferenz hinterlegt ist)
	 */
	int getDefaultFontSizeStatementInput();

	/**
	 * @return Standardeinstellung begrenzter Datenbankabfragen. (wenn noch
	 *         keine Benutzerpräferenz hinterlegt ist)
	 */
	boolean isDefaultLimitMaxRows();
}
