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
package de.kuehweg.sqltool.dialog.component;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * Symbol für ein DropTarget.
 *
 * @author Michael Kühweg
 */
public class DropTargetSymbol extends Pane {

	private static final float ARC_LENGTH = 340.0f;
	private static final float START_ANGLE = 100.0f;
	private static final String COLOR = "#067db7";
	private static final double STROKE_WIDTH = 1.0;

	public DropTargetSymbol(final Pane parentPane) {
		if (parentPane != null) {
			final double baseSize = Math.min(Math.min(parentPane.getWidth(), parentPane.getHeight()), 160);
			final double bottom = parentPane.getHeight() / 2 + baseSize / 2;

			final double radius = baseSize / 3;
			final double arrowBounds = radius / 2;

			final double centerX = parentPane.getWidth() / 2 - radius;
			final double centerY = bottom - radius;

			final Arc arc = createArc(radius, centerX, centerY);
			final Group arrow = createArrow(centerX, bottom, arrowBounds, radius);

			getChildren().add(arc);
			getChildren().add(arrow);
		}
	}

	/**
	 * Kreisausschnitt für Symbol.
	 *
	 * @param radius
	 *            Radius des Kreisausschnitts.
	 * @param centerX
	 *            x-Koordinate des Kreismittelpunkts.
	 * @param centerY
	 *            y-Koordinate des Kreismittelpunkts.
	 * @return Kreisausschnitt als 2D Objekt
	 */
	private Arc createArc(final double radius, final double centerX, final double centerY) {
		final Arc arc = new Arc();
		arc.setCenterX(centerX);
		arc.setCenterY(centerY);
		arc.setRadiusX(radius);
		arc.setRadiusY(radius);
		arc.setStartAngle(START_ANGLE);
		arc.setLength(ARC_LENGTH);
		arc.setType(ArcType.OPEN);
		arc.setStrokeWidth(STROKE_WIDTH);
		arc.setStroke(Color.web(COLOR));
		arc.setFill(Color.TRANSPARENT);
		return arc;
	}

	/**
	 * Pfeil in den Kreisausschnitt des DropTarget Symbols.
	 *
	 * @param centerX
	 *            x-Koordinate des Pfeils
	 * @param bottom
	 *            Unterer Rand, von dem ausgehend die y-Koordinaten berechnet
	 *            werden
	 * @param arrowBounds
	 *            Ausdehnung der Pfeilspitze
	 * @param radius
	 *            Radius des Kreisausschnitts
	 * @return Gruppe mit den 2D Objekten, aus denen sich der Pfeil
	 *         zusammensetzt.
	 */
	private Group createArrow(final double centerX, final double bottom, final double arrowBounds,
			final double radius) {
		final Line lineVert = new Line();
		lineVert.startXProperty().set(centerX);
		lineVert.startYProperty().set(bottom - radius);
		lineVert.endXProperty().set(centerX);
		lineVert.endYProperty().set(bottom - radius * 3);
		applyCommonStrokeAndFill(lineVert);

		final Group arrow = new Group();
		arrow.getChildren().add(lineVert);
		arrow.getChildren().add(createArrowHead(centerX, bottom - radius, arrowBounds));

		return arrow;
	}

	private Group createArrowHead(final double centerX, final double centerY, final double arrowBounds) {
		final Group arrowHead = new Group();

		final Line arrowLeft = new Line();
		arrowLeft.startXProperty().set(centerX);
		arrowLeft.startYProperty().set(centerY);
		arrowLeft.endXProperty().set(centerX - arrowBounds);
		arrowLeft.endYProperty().set(centerY - arrowBounds);
		applyCommonStrokeAndFill(arrowLeft);

		final Line arrowRight = new Line();
		arrowRight.startXProperty().set(centerX);
		arrowRight.startYProperty().set(centerY);
		arrowRight.endXProperty().set(centerX + arrowBounds);
		arrowRight.endYProperty().set(centerY - arrowBounds);
		applyCommonStrokeAndFill(arrowRight);

		arrowHead.getChildren().add(arrowLeft);
		arrowHead.getChildren().add(arrowRight);

		return arrowHead;
	}

	private void applyCommonStrokeAndFill(final Shape shape) {
		shape.setStrokeWidth(STROKE_WIDTH);
		shape.setStroke(Color.web(COLOR));
		shape.setFill(Color.web(COLOR));
	}
}
