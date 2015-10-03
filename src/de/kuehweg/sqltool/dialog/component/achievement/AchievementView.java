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

import java.util.Observable;
import java.util.Observer;

import de.kuehweg.gamification.Achievement;
import javafx.application.Platform;
import javafx.scene.web.WebView;

/**
 * Komponente zur Anzeige des Lernfortschritts (Achievements).
 *
 * @author Michael Kühweg
 */
public class AchievementView implements Observer, Runnable {

	private final WebView view;

	private final AchievementHtmlFormatter formatter;

	/**
	 * AchievementViews existieren immer nur mit zugeordneter WebView und
	 * AchievementHtmlFormatter.
	 *
	 * @param view
	 *            {@link WebView} als Ziel für die aufbereitete Darstellung.
	 * @param formatter
	 *            {@link AchievementHtmlFormatter} zur Aufbereitung der
	 *            Lernfortschrittsanzeige.
	 */
	public AchievementView(final WebView view, final AchievementHtmlFormatter formatter) {
		this.view = view;
		this.formatter = formatter;
		addObserverToAchievements();
	}

	/**
	 * Registriert diese Komponente als Observer an den Achievements, die mit
	 * dem AchievementHtmlFormatter aufbereitet werden sollen.
	 */
	private void addObserverToAchievements() {
		if (formatter != null) {
			for (final Achievement achievement : formatter.getRankingPoints().getRegisteredAchievements()) {
				achievement.addObserver(this);
			}
		}
	}

	/**
	 * @return the view
	 */
	public WebView getView() {
		return view;
	}

	/**
	 * @return the formatter
	 */
	public AchievementHtmlFormatter getFormatter() {
		return formatter;
	}

	@Override
	public void update(final Observable o, final Object arg) {
		refresh();
	}

	/**
	 * Bereitet den Inhalt der WebView neu auf.
	 */
	public void refresh() {
		Platform.runLater(this);
	}

	@Override
	public void run() {
		if (view != null && formatter != null) {
			view.getEngine().loadContent(formatter.format());
		}
	}
}
