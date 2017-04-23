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

import de.kuehweg.sqltool.common.achievement.NamedRank;
import de.kuehweg.sqltool.common.achievement.RankingPoints;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Aufbereitung des aktuellen Rangs zur Darstellung im Scene-Graph.
 *
 * @author Michael Kühweg
 */
public class RankNodeBuilder {

	private static final String RESOURCE_BUNDLE_NAME = "resources/achievements/achievements";

	private static final String PREFIX_RANK_NAME = "rankName_";
	private static final String PREFIX_RANK_LOGO = "rankLogo_";

	private static final String DEFAULT_LOGO = "/resources/achievements/logo/Default.png";

	private static final double GRAPHIC_TEXT_GAP_SIZE = 4.0;

	private static final int LOGO_WIDTH = 96;
	private static final int LOGO_HEIGHT = 96;

	private ResourceBundle resourceBundle;

	private final RankingPoints rankingPoints;

	/**
	 * @param rankingPoints
	 *            Das Punktesystem, das zur Ausgabe aufbereitet wird.
	 */
	public RankNodeBuilder(final RankingPoints rankingPoints) {
		this.rankingPoints = rankingPoints;
	}

	public Node buildVisual() {
		resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
		return nodeForNameAndLogo();
	}

	private Node nodeForNameAndLogo() {
		final VBox box = new VBox(GRAPHIC_TEXT_GAP_SIZE, nodeForLogo(), nodeForName());
		box.setAlignment(Pos.CENTER);
		return box;
	}

	private Node nodeForName() {
		final NamedRank rank = NamedRank.achievedRankInRankingPoints(rankingPoints);
		String name;
		try {
			name = resourceBundle.getString(PREFIX_RANK_NAME + rank.name());
		} catch (final MissingResourceException mre) {
			name = rank.name();
		}
		final Label label = new Label(name);
		label.getStyleClass().add("itry-rank-label");
		return label;
	}

	private Node nodeForLogo() {
		// Logos können Text enthalten, daher Zugriff über Pfad aus einem
		// ResourceBundle
		final NamedRank rank = NamedRank.achievedRankInRankingPoints(rankingPoints);
		String imagePath;
		try {
			imagePath = resourceBundle.getString(PREFIX_RANK_LOGO + rank.name());
		} catch (final MissingResourceException mre) {
			imagePath = DEFAULT_LOGO;
		}
		final ImageView imageView = new ImageView();
		// setSmooth wird momentan anscheinend komplett ignoriert, sieht dann
		// alles "verwaschen" aus. Daher der Umweg über ein "zu großes" Bild,
		// das verkleinert angezeigt wird.
		imageView.setSmooth(false);
		imageView.setFitHeight(LOGO_HEIGHT);
		imageView.setFitWidth(LOGO_WIDTH);
		imageView.setImage(new Image(this.getClass().getResourceAsStream(imagePath)));
		return imageView;
	}
}
