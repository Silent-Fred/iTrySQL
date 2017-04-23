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

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Komponente zur Darstellung eines einzelnen Achievements.
 *
 * @author Michael Kühweg
 */
public class SingleAchievement {

	private static final int LABEL_LOGO_FIXED_WIDTH = 64;

	private static final double HBOX_SPACING = 0.0;
	private static final double VBOX_LABEL_SPACING = 2.0;
	private static final double DESCRIPTION_LABEL_MAX_WIDTH = 333.0;

	private boolean achieved;
	private String name;
	private String description;
	private String hint;
	private String logoCharacter;
	private int points;
	private double percentageAchieved;

	/**
	 * Baut einen Knoten für den Scene-Graph auf, der die visuelle Darstellung
	 * des einzelnen Achievements repräsentiert.
	 *
	 * @return Knoten für den Scene-Graph.
	 */
	public Node buildVisual() {
		final VBox nameAndDescription = new VBox(VBOX_LABEL_SPACING, nodeForName(), nodeForDescription());
		final HBox box = new HBox(HBOX_SPACING, nodeForLogo(), nameAndDescription);
		// Punkte bringen im Moment keinen Mehrwert, daher nicht angezeigt
		nameAndDescription.setAlignment(Pos.BOTTOM_LEFT);
		box.setAlignment(Pos.BOTTOM_LEFT);
		return box;
	}

	public boolean isAchieved() {
		return achieved;
	}

	public void setAchieved(final boolean achieved) {
		this.achieved = achieved;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(final String hint) {
		this.hint = hint;
	}

	public String getLogoCharacter() {
		return logoCharacter;
	}

	public void setLogoCharacter(final String logoCharacter) {
		this.logoCharacter = logoCharacter;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(final int points) {
		this.points = points;
	}

	public double getPercentageAchieved() {
		return percentageAchieved;
	}

	public void setPercentageAchieved(final double percentageAchieved) {
		this.percentageAchieved = percentageAchieved;
	}

	private Node nodeForName() {
		final Label label = new Label(name);
		label.getStyleClass().add("itrysql-achievement-label-name");
		return label;
	}

	private Node nodeForDescription() {
		final Label label = new Label(achieved ? description : hint);
		label.setWrapText(true);
		label.setMaxWidth(DESCRIPTION_LABEL_MAX_WIDTH);
		label.getStyleClass().add("itrysql-achievement-label-description");
		return label;
	}

	private Node nodeForLogo() {
		return achieved ? nodeForAchieved() : nodeForPercentage();
	}

	private Node nodeForAchieved() {
		final Label label = new Label(logoCharacter);
		label.getStyleClass().add("itrysql-achievement-logo");
		label.setPrefWidth(LABEL_LOGO_FIXED_WIDTH);
		label.setMinWidth(LABEL_LOGO_FIXED_WIDTH);
		label.setMaxWidth(LABEL_LOGO_FIXED_WIDTH);
		return label;
	}

	private Node nodeForPercentage() {
		final ProgressIndicator progress = new ProgressIndicator(percentageAchieved);
		progress.getStyleClass().add("itrysql-achievement-progress");
		progress.setPrefWidth(LABEL_LOGO_FIXED_WIDTH);
		progress.setMinWidth(LABEL_LOGO_FIXED_WIDTH);
		progress.setMaxWidth(LABEL_LOGO_FIXED_WIDTH);
		return progress;
	}
}
