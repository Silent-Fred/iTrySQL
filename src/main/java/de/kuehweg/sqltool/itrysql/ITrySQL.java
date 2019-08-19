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
package de.kuehweg.sqltool.itrysql;

import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.kuehweg.gamification.AchievementCounter;
import de.kuehweg.gamification.AchievementPersister;
import de.kuehweg.gamification.AchievementUserPreferencesPersister;
import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.achievement.AchievementManager;
import de.kuehweg.sqltool.common.achievement.DefaultRankingPoints;
import de.kuehweg.sqltool.dialog.util.StageSizerUtil;
import de.kuehweg.sqltool.dialog.util.WindowIconRepaintIssueOnResizeFix;
import de.kuehweg.sqltool.resources.FontLibrary;
import de.kuehweg.sqltool.resources.ImagePack;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Zentrale Klasse der "iTry SQL" Applikation.
 *
 * @author Michael Kühweg
 */
public class ITrySQL extends Application {

	private static final int BASE_FONT_SIZE = 12;

	private iTrySQLController controller;

	@Override
	public final void init() throws Exception {
		super.init();
		initFonts();
		initGamification();
	}

	/**
	 * Initialisierung der speziellen Fonts, die in der Anwendung verwendet
	 * werden.
	 */
	private void initFonts() {
		for (final FontLibrary font : FontLibrary.values()) {
			Font.loadFont(getClass().getResource(font.resourceLocation()).toExternalForm(), BASE_FONT_SIZE);
		}
	}

	/**
	 * Achievements vorbereiten, bestehenden Fortschritt einlesen.
	 */
	private void initGamification() {
		AchievementManager.getInstance().setPersister(new AchievementUserPreferencesPersister());
		applyProgress();
		AchievementManager.getInstance().setPointsSystem(new DefaultRankingPoints());
	}

	/**
	 * Bisher erreichten Fortschritt bei den Achievements einlesen und auf den
	 * aktuellen Stand des AchievementManagers anwenden. In der Regel sollte
	 * dazu der AchievementManager vorher auf den Ausgangszustand zurückgesetzt
	 * worden sein.
	 */
	private void applyProgress() {
		final AchievementManager achievementManager = AchievementManager.getInstance();
		final Collection<AchievementCounter> achievedSoFar = achievementManager.getPersister().read();
		// die jetzt auf den Ausgangszustand angewandten Änderungen sollen nicht
		// persistiert werden, da sie ja genau zu dem Stand führen, der gerade
		// eben gelesen wurde.
		final AchievementPersister persister = achievementManager.getPersister();
		achievementManager.setPersister(null);
		// vom Ausgangszustand ausgehend den erreichten Fortschritt anwenden
		achievementManager.resetAllAchievements();
		for (final AchievementCounter counter : achievedSoFar) {
			achievementManager.fireEvent(counter.getEvent(), counter.getCounter());
		}
		// Änderungen ab jetzt wieder persistieren
		achievementManager.setPersister(persister);
	}

	@Override
	public final void start(final Stage primaryStage) throws Exception {
		final FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(ResourceBundle.getBundle("dictionary"));
		fxmlLoader.setLocation(getClass().getResource("/fxml/iTrySQL.fxml"));
		final Parent root = (Parent) fxmlLoader.load();
		root.getStylesheets().add(getClass().getResource("/css/itrysql.css").toExternalForm());

		final Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		adjustPositionAndSize(primaryStage);
		primaryStage.getIcons().addAll(ImagePack.APP_ICON.getAsImage(), ImagePack.APP_ICON_256x256.getAsImage(),
				ImagePack.APP_ICON_128x128.getAsImage(), ImagePack.APP_ICON_64x64.getAsImage(),
				ImagePack.APP_ICON_32x32.getAsImage(), ImagePack.APP_ICON_16x16.getAsImage());

		primaryStage.setTitle(DialogDictionary.APPLICATION.toString());

		controller = (iTrySQLController) fxmlLoader.getController();
		controller.setApplicationWindow(scene.getWindow());

		primaryStage.setOnCloseRequest((final WindowEvent ev) -> {
			if (!controller.quit()) {
				ev.consume();
			}
		});

		// FIXME
		WindowIconRepaintIssueOnResizeFix.fix(primaryStage);

		primaryStage.show();
	}

	/**
	 * @param primaryStage
	 */
	private void adjustPositionAndSize(final Stage primaryStage) {
		final Rectangle2D calculatedSize = StageSizerUtil.calculateSizeDependingOnScreenSize();
		primaryStage.setX(calculatedSize.getMinX());
		primaryStage.setY(calculatedSize.getMinY());
		primaryStage.setWidth(calculatedSize.getWidth());
		primaryStage.setHeight(calculatedSize.getHeight());
	}

	@Override
	public final void stop() throws Exception {
		try {
			if (controller != null) {
				controller.stop();
			}
		} catch (final Throwable ex) {
			Logger.getLogger(ITrySQL.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String[] args) {
		launch(args);
	}
}
