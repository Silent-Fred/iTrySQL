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
package de.kuehweg.sqltool.dialog.action;

import de.kuehweg.sqltool.common.UserPreferencesI;
import de.kuehweg.sqltool.dialog.base.FontResizer;

/**
 * Schriftgröße ändern
 *
 * @author Michael Kühweg
 */
public abstract class FontAction {

    public static final int MIN_FONT_SIZE = 9;
    public static final int MAX_FONT_SIZE = 32;

    private int diff;

    private FontResizer alternativeFontResizer;

    private UserPreferencesI userPreferences;

    public int getDiff() {
        return diff;
    }

    public void setDiff(final int diff) {
        this.diff = diff;
    }

    public UserPreferencesI getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(final UserPreferencesI userPreferences) {
        this.userPreferences = userPreferences;
    }

    /**
     * Das Setzen der Schriftgröße übernimmt ein FontResizer.
     *
     * @param fontResizer
     */
    public void setAlternativeFontResizer(final FontResizer fontResizer) {
        this.alternativeFontResizer = fontResizer;
    }

    /**
     * Standardverfahren zum Ändern der Schriftgröße in der Komponente. Wird verwendet,
     * wenn kein alternatives Verfahren via setAlternativeFontResizer() angegeben wird
     *
     * @return
     */
    public abstract FontResizer getDefaultFontResizer();

    /**
     * Implementierung zum Speichern der zuletzt eingestellten Schriftgröße in
     * Benutzereinstellungen.
     *
     * @param size
     */
    public abstract void storeToPreferences(final int size);

    /**
     * Fontmanipulationen behandeln
     */
    public void handleFontAction() {
        final int targetFontSize = modifyFontSize();
        if (getUserPreferences() != null) {
            storeToPreferences(targetFontSize);
        }
    }

    /**
     * Schriftgröße der definierten Komponente ändern - gesetzte Schriftgröße wird als
     * Ergebnis geliefert
     *
     * @return
     */
    private int modifyFontSize() {
        FontResizer resizer
                = alternativeFontResizer != null ? alternativeFontResizer : getDefaultFontResizer();
        int targetSize = resizer.getFontSize() + diff;
        targetSize = targetSize < MIN_FONT_SIZE ? MIN_FONT_SIZE : targetSize;
        targetSize = targetSize > MAX_FONT_SIZE ? MAX_FONT_SIZE : targetSize;
        resizer.setFontSize(targetSize);
        return targetSize;
    }

}
