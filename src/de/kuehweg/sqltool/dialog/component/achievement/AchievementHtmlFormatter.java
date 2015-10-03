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

package de.kuehweg.sqltool.dialog.component.achievement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringEscapeUtils;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.common.achievement.AchievementManager;
import de.kuehweg.sqltool.common.achievement.NamedAchievement;
import de.kuehweg.sqltool.common.achievement.NamedRank;
import de.kuehweg.sqltool.common.achievement.RankingPoints;

/**
 * Bereitet den erreichten Stand der Achievements als HTML auf.
 *
 * @author Michael Kühweg
 */
public class AchievementHtmlFormatter {

	public static final String DEFAULT_RESOURCE_BUNDLE = "resources/achievements/achievement_template";

	private ResourceBundle resourceBundle;

	private RankingPoints rankingPoints;

	private final List<Achievement> achievements;

	/**
	 * HTML Formatierung für den aktuellen Fortschritt. Der Default Konstruktor
	 * verwendet Standardeinstellungen für die Vorlagen, Achievements und das
	 * Punktesystem.
	 * <p>
	 * Punktesystem: Das im {@link AchievementManager} hinterlegte Punktesystem.
	 * </p>
	 * <p>
	 * Achievements: Die im Punktesystem registrierten Achievements. In der
	 * Standardeinstellung sind das die Elemente der Enum
	 * {@link NamedAchievement}.
	 * </p>
	 * <p>
	 * ResourceBundle: resources/achievments/achievement_template.properties
	 * (bzw. lokalisierte Variante falls vorhanden)
	 * </p>
	 */
	public AchievementHtmlFormatter() {
		rankingPoints = AchievementManager.getInstance().getPointsSystem();
		// Um in der Ausgabereihenfolge unabhängig davon zu sein, was im
		// Punktesystem mit den Achievements passiert, werden sie eigenständig
		// in einer Liste gehalten, die standardmäßig
		// vorsortiert ist.
		achievements = new ArrayList<>(rankingPoints.getRegisteredAchievements());
		sortAchievements(new AchievementDefaultSortOrder());
		try {
			resourceBundle = ResourceBundle.getBundle(DEFAULT_RESOURCE_BUNDLE);
		} catch (MissingResourceException | NullPointerException e) {
			resourceBundle = null;
		}
	}

	/**
	 * @return the resourceBundle
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * Setzt ein ResourceBundle zur Verwendung bei der Ausgabeaufbereitung. Die
	 * erforderlichen Einträge sind im Default ResourceBundle dokumentiert.
	 *
	 * @param resourceBundle
	 *            the resourceBundle to set
	 */
	public void setResourceBundle(final ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	/**
	 * @return the rankingPoints
	 */
	public RankingPoints getRankingPoints() {
		return rankingPoints;
	}

	/**
	 * @param rankingPoints
	 *            the rankingPoints to set
	 */
	public void setRankingPoints(final RankingPoints rankingPoints) {
		this.rankingPoints = rankingPoints;
	}

	/**
	 * @param resourceKey
	 *            Key für einen Eintrag im ResourceBundle.
	 * @return Den Eintrag aus dem ResourceBundle - falls nicht vorhanden, den
	 *         übergebenen resourceKey, HTML escaped.
	 */
	private String getStringFromResourceBundleDefaultsToKey(final String resourceKey) {
		String value = "";
		try {
			value = resourceBundle.getString(resourceKey);
		} catch (final MissingResourceException | NullPointerException e) {
			value = StringEscapeUtils.escapeHtml4(resourceKey);
		}
		return value;
	}

	/**
	 * Lädt Ressourcen (in Form von Textschnipseln) die im ResourceBundle unter
	 * dem angegebenen Schlüssel als Verweis auf eine Textressource angegeben
	 * sind.
	 *
	 * @param resourceKey
	 *            Schlüssel der nachzuladenden Ressource
	 * @return Inhalt der unter dem angegebenen Schlüssel verknüpften
	 *         Textressource
	 */
	private String loadResource(final String resourceKey) {
		final String resourcePath = getStringFromResourceBundleDefaultsToKey(resourceKey);
		final URL resource = getClass().getResource(resourcePath);
		String resourceContent;
		try {
			resourceContent = FileUtil.readFile(resource);
		} catch (NullPointerException | URISyntaxException | IOException e1) {
			resourceContent = "resource not available (" + resourceKey + " -> " + resourcePath + ")";
		}
		return resourceContent;
	}

	/**
	 * @param rank
	 *            Rang
	 * @return HTML Schnipsel zur Darstellung des Rangs (mit Bezeichnung und
	 *         Logo)
	 */
	private String formatRank(final NamedRank rank) {
		final String rankTemplate = loadResource("rankTemplate");
		final String rankName = getStringFromResourceBundleDefaultsToKey("rankName" + rank.name());
		final String rankLogo = loadResource("rankLogo" + rank.name());
		try {
			return MessageFormat.format(rankTemplate, rankName, rankLogo);
		} catch (final IllegalArgumentException e) {
			return StringEscapeUtils.escapeHtml4(rank.name());
		}
	}

	/**
	 * @param achievement
	 *            Achievement
	 * @return HTML Schnipsel zur Darstellung eines Achievements (mit
	 *         Bezeichnung, Beschreibung, Logo und Punktzahl)
	 */
	private String formatAchievement(final Achievement achievement) {
		final String templateKey = achievement.isAchieved() ? "achievementTemplateAchieved"
				: "achievementTemplateUnachieved";
		final String achievementTemplate = loadResource(templateKey);
		final String achievementName = getStringFromResourceBundleDefaultsToKey(
				"achievementName" + achievement.getName());
		final String achievementDescription = getStringFromResourceBundleDefaultsToKey(
				"achievementDescription" + achievement.getName());
		final String achievementHint = getStringFromResourceBundleDefaultsToKey(
				"achievementHint" + achievement.getName());
		final String logoKey = achievement.isAchieved() ? "achievementLogoAchieved" : "achievementLogoUnachieved";
		final String achievementLogo = getStringFromResourceBundleDefaultsToKey(logoKey + achievement.getName());
		final String achievementPoints = String.valueOf(rankingPoints.pointsAchieved(achievement));
		final String achievementPercentageAchieved = String
				.valueOf(Math.round(achievement.calculateAchievedPercentage() * 100));
		try {
			return MessageFormat.format(achievementTemplate, achievementName, achievementDescription, achievementHint,
					achievementLogo, achievementPoints, achievementPercentageAchieved);
		} catch (final IllegalArgumentException e) {
			return StringEscapeUtils.escapeHtml4(achievement.getName());
		}
	}

	/**
	 * @return HTML Schnipsel zur Darstellung aller NamedAchievements
	 */
	private String formatAchievements() {
		final StringBuilder formatted = new StringBuilder();
		for (final Achievement achievement : achievements) {
			formatted.append(formatAchievement(achievement));
		}
		return formatted.toString();
	}

	/**
	 * @return Komplette HTML Darstellung, z.B. zur Verwendung in einer WebView.
	 */
	public String format() {
		final String template = loadResource("gamificationTemplate");
		final String formattedRank = formatRank(
				NamedRank.achievedRankInRankingPoints(AchievementManager.getInstance().getPointsSystem()));
		final String formattedAchievements = formatAchievements();
		try {
			return MessageFormat.format(template, formattedRank, formattedAchievements);
		} catch (final IllegalArgumentException e) {
			// TODO eigentlich ist das ja echt ziemlich unwahrscheinlich. Reicht
			// dann eine solche testweise, lapidare Meldung?
			return StringEscapeUtils.escapeHtml4("Oops.");
		}
	}

	/**
	 * Sortiert die interne Liste der Achievements mit dem übergebenen
	 * Comparator. Die Reihenfolge der Achievements in der formatierten Ausgabe
	 * entspricht der Reihenfolge in der internen Liste.
	 *
	 * @param comparator
	 */
	public void sortAchievements(final Comparator<Achievement> comparator) {
		achievements.sort(comparator);
	}
}
