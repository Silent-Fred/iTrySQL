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

package de.kuehweg.sqltool.dialog.util;

import java.util.ArrayList;
import java.util.Collection;

import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Unter MacOS tritt in manchen Versionen (JavaFX und/oder MacOS) das Problem
 * auf, dass bei Größenänderungen des Fensters das Icon nicht mehr richtig
 * angezeigt wird. Diese Utility-Klasse kann dazu benutzt werden, einen
 * Workaround an eine Stage zu binden.
 *
 * @author Michael Kühweg
 */
public final class WindowIconRepaintIssueOnResizeFix {

	/**
	 * Utility-Klasse ohne Instances.
	 */
	private WindowIconRepaintIssueOnResizeFix() {
	}

	/**
	 * @param stage
	 *            Der übergebenen Stage werden ChangeListener hinzugefügt, die
	 *            bei Größenänderungen des Fensters das Window-Icon neu setzen.
	 */
	public static void fix(final Stage stage) {
		stage.widthProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			final Collection<Image> images = new ArrayList<>(stage.getIcons());
			stage.getIcons().clear();
			stage.getIcons().addAll(images);
		});
		stage.heightProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			final Collection<Image> images = new ArrayList<>(stage.getIcons());
			stage.getIcons().clear();
			stage.getIcons().addAll(images);
		});
	}
}
