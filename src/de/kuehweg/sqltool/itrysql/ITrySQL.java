/*
 * Copyright (c) 2013-2015, Michael Kühweg
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
import de.kuehweg.gamification.ObfuscatedAchievementXmlFilePersister;
import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.achievement.AchievementManager;
import de.kuehweg.sqltool.common.achievement.DefaultRankingPoints;
import de.kuehweg.sqltool.dialog.images.ImagePack;
import de.kuehweg.sqltool.dialog.util.StageSizerUtil;
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
		Font.loadFont(getClass().getResource("/resources/fonts/VeraMono.ttf").toExternalForm(), BASE_FONT_SIZE);
		Font.loadFont(getClass().getResource("/resources/fonts/VeraMoIt.ttf").toExternalForm(), BASE_FONT_SIZE);
		Font.loadFont(getClass().getResource("/resources/fonts/VeraMoBd.ttf").toExternalForm(), BASE_FONT_SIZE);
		Font.loadFont(getClass().getResource("/resources/fonts/VeraMoBI.ttf").toExternalForm(), BASE_FONT_SIZE);
		// spezieller Icon-Font
		Font.loadFont(getClass().getResource("/resources/fonts/iTryIcons-Roman.ttf").toExternalForm(), BASE_FONT_SIZE);
		// Font für die Achievements
		Font.loadFont(getClass().getResource("/resources/fonts/Laurel_wreath.ttf").toExternalForm(), BASE_FONT_SIZE);
	}

	/**
	 * Achievements vorbereiten, bestehenden Fortschritt einlesen.
	 */
	private void initGamification() {
		final String achievementsFilename = System.getProperty("user.home") + "/" + "SQLTutorialAchievements";
		AchievementManager.getInstance().setPersister(
				new ObfuscatedAchievementXmlFilePersister(achievementsFilename, System.getProperty("user.name")));
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
		fxmlLoader.setLocation(getClass().getResource("/resources/fxml/iTrySQL.fxml"));
		final Parent root = (Parent) fxmlLoader.load();
		root.getStylesheets().add(getClass().getResource("/resources/css/itrysql.css").toExternalForm());

		final Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		final Rectangle2D calculatedSize = StageSizerUtil.calculateSizeDependingOnScreenSize();
		primaryStage.setX(calculatedSize.getMinX());
		primaryStage.setY(calculatedSize.getMinY());
		primaryStage.setWidth(calculatedSize.getWidth());
		primaryStage.setHeight(calculatedSize.getHeight());
		primaryStage.getIcons().add(ImagePack.APP_ICON.getAsImage());
		primaryStage.setTitle(DialogDictionary.APPLICATION.toString());

		controller = (iTrySQLController) fxmlLoader.getController();
		controller.setApplicationWindow(scene.getWindow());

		primaryStage.setOnCloseRequest((final WindowEvent ev) -> {
			if (!controller.quit()) {
				ev.consume();
			}
		});
		primaryStage.show();
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
