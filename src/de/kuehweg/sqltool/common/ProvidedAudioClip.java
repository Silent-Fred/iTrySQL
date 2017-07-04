/*
 * Copyright (c) 2014, Michael Kühweg
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

import javafx.scene.media.AudioClip;

/**
 * AudioClips, die die Anwendung selbst mitbringt.
 *
 * @author Michael Kühweg
 */
public enum ProvidedAudioClip {

	ACOUSTIC("Acoustic", "acoustic.mp3"),
	BEEP("Beep", "beep.mp3"),
	BELL("Bell", "bell.mp3"),
	BONGO("Bongo", "bongo.mp3"),
	ELECTRIC("Electric", "electric.mp3"),
	PLING("Pling", "pling.mp3"),
	PLONG("Plong", "plong.mp3"),
	PLOP("Plop", "plop.mp3"),
	SHIMMER("Shimmer", "shimmer.mp3"),
	STEELSTRING("Steel Strings", "steelstring.mp3"),
	WHIRLY("Whirly", "whirly.mp3");

	private static final String PATH_TO_AUDIO_RESOURCES = "/resources/audio/";
	private final String uiName;
	private final String resource;

	ProvidedAudioClip(final String uiName, final String resource) {
		this.uiName = uiName;
		this.resource = resource;
	}

	public void play() {
		new AudioClip(this.getClass().getResource(PATH_TO_AUDIO_RESOURCES + resource).toExternalForm()).play();
	}

	public void play(final double volume) {
		if (volume > 0.0) {
			new AudioClip(this.getClass().getResource(PATH_TO_AUDIO_RESOURCES + resource).toExternalForm())
					.play(volume);
		}
	}

	@Override
	public String toString() {
		return uiName;
	}
}
