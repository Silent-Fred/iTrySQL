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
 * Testimplementierung der UserPreferences
 *
 * @author Michael Kühweg
 */
public class UserPreferencesStub implements UserPreferencesI {

    // Preferences mit Defaultwerten
    private boolean limitMaxRows = isDefaultLimitMaxRows();
    private int fontSizeStatementInput = getDefaultFontSizeStatementInput();
    private int fontSizeDbOutput = getDefaultFontSizeDbOutput();
    private double beepVolume = getDefaultBeepVolume();
    private ProvidedAudioClip beepAudioClip = getDefaultBeepAudioClip();

    @Override
    public boolean isLimitMaxRows() {
        return limitMaxRows;
    }

    @Override
    public void setLimitMaxRows(final boolean limitMaxRows) {
        this.limitMaxRows = limitMaxRows;
    }

    @Override
    public int getFontSizeStatementInput() {
        return fontSizeStatementInput;
    }

    @Override
    public void setFontSizeStatementInput(final int fontSize) {
        fontSizeStatementInput = fontSize;
    }

    @Override
    public int getFontSizeDbOutput() {
        return fontSizeDbOutput;
    }

    @Override
    public void setFontSizeDbOutput(final int fontSize) {
        fontSizeDbOutput = fontSize;
    }

    @Override
    public double getBeepVolume() {
        return beepVolume;
    }

    @Override
    public void setBeepVolume(final double beepVolume) {
        this.beepVolume = beepVolume;
    }

    @Override
    public ProvidedAudioClip getBeepAudioClip() {
        return beepAudioClip;
    }

    @Override
    public void setBeepAudioClip(final ProvidedAudioClip beepAudioClip) {
        this.beepAudioClip = beepAudioClip;
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
        return 12;
    }

    @Override
    public int getDefaultFontSizeStatementInput() {
        return 12;
    }

    @Override
    public boolean isDefaultLimitMaxRows() {
        return true;
    }
}
