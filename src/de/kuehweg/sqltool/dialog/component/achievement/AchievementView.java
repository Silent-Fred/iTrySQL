/*
 * Copyright (c) 2015-2017, Michael Kühweg
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

package de.kuehweg.sqltool.dialog.component.achievement;

import java.util.Observable;
import java.util.Observer;

import de.kuehweg.sqltool.common.achievement.RankingPoints;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Komponente zur Anzeige des Lernfortschritts (Achievements).
 *
 * @author Michael Kühweg
 */
public class AchievementView implements Observer, Runnable {

	private final RankingPoints ranking;
	private final Pane rankPane;
	private final Pane achievementsPane;

	public AchievementView(final RankingPoints ranking, final Pane rankPane, final Pane achievementsPane) {
		this.ranking = ranking;
		this.rankPane = rankPane;
		this.achievementsPane = achievementsPane;
		addObserverToAchievements();
	}

	private void addObserverToAchievements() {
		ranking.getRegisteredAchievements().stream().forEach(a -> a.addObserver(this));
	}

	@Override
	public void update(final Observable o, final Object arg) {
		refresh();
	}

	public void refresh() {
		Platform.runLater(this);
	}

	@Override
	public void run() {
		rankPane.getChildren().clear();
		final Node rank = new RankNodeBuilder(ranking).buildVisual();
		AnchorPane.setTopAnchor(rank, 0.0);
		AnchorPane.setLeftAnchor(rank, 0.0);
		AnchorPane.setRightAnchor(rank, 0.0);
		rankPane.getChildren().add(rank);

		achievementsPane.getChildren().clear();
		final Node achievements = new AchievementNodeBuilder(ranking).buildVisual();
		AnchorPane.setTopAnchor(achievements, 0.0);
		AnchorPane.setLeftAnchor(achievements, 0.0);
		AnchorPane.setRightAnchor(achievements, 0.0);
		achievementsPane.getChildren().add(achievements);
	}
}
