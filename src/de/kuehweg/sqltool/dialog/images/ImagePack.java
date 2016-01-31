/*
 * Copyright (c) 2013-2016, Michael Kühweg
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
package de.kuehweg.sqltool.dialog.images;

import javafx.scene.image.Image;

/**
 * Bilder, Icons,... ansprechbar machen.
 *
 * @author Michael Kühweg
 */
public enum ImagePack {

	APP_ICON("AppIcon.png"), APP_ICON_256x256("AppIcon256.png"), APP_ICON_128x128("AppIcon128.png"), APP_ICON_64x64(
			"AppIcon64.png"), APP_ICON_32x32("AppIcon32.png"), APP_ICON_16x16("AppIcon16.png"), MSG_ERROR(
					"msg_error.png"), MSG_INFO("msg_info.png"), MSG_QUESTION("msg_question.png"), MSG_WARNING(
							"msg_warning.png");

	private static final String RESOURCE_PATH = "/resources/images/";
	private final String imageName;

	private ImagePack(final String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return Enum-Element als fertiges Image-Objekt
	 */
	public Image getAsImage() {
		return new Image(this.getClass().getResourceAsStream(RESOURCE_PATH + imageName));
	}
}
