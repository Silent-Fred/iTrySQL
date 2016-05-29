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
package de.kuehweg.sqltool.itrysql;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.dialog.images.ImagePack;
import de.kuehweg.sqltool.dialog.util.StageSizerUtil;
import de.kuehweg.sqltool.dialog.util.WindowIconRepaintIssueOnResizeFix;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Weitere Fenster der Applikation (wenn nicht als primary stage geöffnet).
 *
 * @author Michael Kühweg
 */
public class ITrySQLStage extends Stage {

	private static final double X_OFFSET = 48;
	private static final double Y_OFFSET = 32;

	/**
	 * @param callerStage
	 *            Aufrufende Stage
	 */
	public ITrySQLStage(final Stage callerStage) {
		this(callerStage, DialogDictionary.APPLICATION.toString());
	}

	/**
	 * @param callerStage
	 *            Aufrufende Stage
	 * @param title
	 *            Fenstertitel
	 */
	public ITrySQLStage(final Stage callerStage, final String title) {
		try {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setResources(ResourceBundle.getBundle("dictionary"));
			fxmlLoader.setLocation(getClass().getResource("/resources/fxml/iTrySQL.fxml"));
			final Parent root = (Parent) fxmlLoader.load();
			root.getStylesheets().add(getClass().getResource("/resources/css/itrysql.css").toExternalForm());

			final Scene scene = new Scene(root);
			setScene(scene);
			getIcons().addAll(ImagePack.APP_ICON.getAsImage(), ImagePack.APP_ICON_256x256.getAsImage(),
					ImagePack.APP_ICON_128x128.getAsImage(), ImagePack.APP_ICON_64x64.getAsImage(),
					ImagePack.APP_ICON_32x32.getAsImage(), ImagePack.APP_ICON_16x16.getAsImage());
			setTitle(title);
			adjustPositionAndSize(callerStage);
			final iTrySQLController controller = fxmlLoader.getController();
			setOnHiding(controller);
			setOnCloseRequest(controller);
			controller.setApplicationWindow(scene.getWindow());

			// FIXME
			WindowIconRepaintIssueOnResizeFix.fix(callerStage);

		} catch (final IOException ex) {
			Logger.getLogger(ITrySQLStage.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * @param callerStage
	 */
	private void adjustPositionAndSize(final Stage callerStage) {
		if (callerStage != null) {
			setX(callerStage.getX() + X_OFFSET);
			setY(callerStage.getY() + Y_OFFSET);
			setWidth(callerStage.getWidth());
			setHeight(callerStage.getHeight());
		} else {
			final Rectangle2D calculatedSize = StageSizerUtil.calculateSizeDependingOnScreenSize();
			setX(calculatedSize.getMinX());
			setY(calculatedSize.getMinY());
			setWidth(calculatedSize.getWidth());
			setHeight(calculatedSize.getHeight());
		}
	}
}
