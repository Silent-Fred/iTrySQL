/*
 * Copyright (c) 2017, Michael Kühweg
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

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.sqltool.common.achievement.RankingPoints;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Diese Klasse baut die Darstellung von Achievements als Knoten im Scene-Graph
 * auf.
 *
 * @author Michael Kühweg
 */
public class AchievementNodeBuilder {

	private static final String RESOURCE_BUNDLE_NAME = "resources/achievements/achievements";

	private static final String PREFIX_ACHIEVEMENT_NAME = "achievementName_";
	private static final String PREFIX_ACHIEVEMENT_DESCRIPTION = "achievementDescription_";
	private static final String PREFIX_ACHIEVEMENT_HINT = "achievementHint_";
	private static final String PREFIX_ACHIEVEMENT_LOGO = "achievementLogo_";

	private static final double VBOX_SPACING = 4.0;

	private ResourceBundle resourceBundle;

	private final RankingPoints rankingPoints;

	/**
	 * @param rankingPoints
	 *            Das Punktesystem, das zur Ausgabe aufbereitet wird.
	 */
	public AchievementNodeBuilder(final RankingPoints rankingPoints) {
		this.rankingPoints = rankingPoints;
	}

	public Node buildVisual() {
		resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
		final VBox box = new VBox(VBOX_SPACING);
		box.getChildren()
				.addAll(rankingPoints.getRegisteredAchievements().stream().sorted(new AchievementDefaultSortOrder())
						.map(this::derivedSingleAchievement).map(SingleAchievement::buildVisual)
						.collect(Collectors.toList()));
		return box;
	}

	private SingleAchievement derivedSingleAchievement(final Achievement achievement) {
		SingleAchievement singleAchievement;
		try {
			singleAchievement = new SingleAchievement();
			singleAchievement.setName(resourceBundle.getString(PREFIX_ACHIEVEMENT_NAME + achievement.getName()));
			singleAchievement
					.setDescription(resourceBundle.getString(PREFIX_ACHIEVEMENT_DESCRIPTION + achievement.getName()));
			singleAchievement.setHint(resourceBundle.getString(PREFIX_ACHIEVEMENT_HINT + achievement.getName()));
			singleAchievement
					.setLogoCharacter(resourceBundle.getString(PREFIX_ACHIEVEMENT_LOGO + achievement.getName()));
		} catch (final MissingResourceException mre) {
			singleAchievement = derivedSingleAchievementWithBaseTexts(achievement);
		}
		singleAchievement.setPoints(rankingPoints.pointsAchieved(achievement));
		singleAchievement.setPercentageAchieved(achievement.calculateAchievedPercentage());
		singleAchievement.setAchieved(achievement.isAchieved());

		return singleAchievement;
	}

	private SingleAchievement derivedSingleAchievementWithBaseTexts(final Achievement achievement) {
		final SingleAchievement singleAchievement = new SingleAchievement();
		singleAchievement.setName(achievement.getName());
		singleAchievement.setDescription("");
		singleAchievement.setHint("");
		singleAchievement.setLogoCharacter("");
		return singleAchievement;
	}
}
