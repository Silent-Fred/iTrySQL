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
package de.kuehweg.sqltool.dialog.component;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;

/**
 * Symbol für ein DropTarget
 * 
 * @author Michael Kühweg
 */
public class DropTargetSymbol extends Pane {

    private static final String COLOR = "#067db7";
    private static final double STROKE_WIDTH = 1.0;
    
	public DropTargetSymbol(final Pane parentPane) {
		super();
		if (parentPane != null) {
			final double baseSize = Math.min(
					Math.min(parentPane.getWidth(), parentPane.getHeight()),
					160);

			final double radius = baseSize / 3;
			final double arrowBounds = radius / 2;

			final double centerX = parentPane.getWidth() / 2 - radius;
			final double bottom = parentPane.getHeight() / 2 + baseSize / 2;

			final Arc arc = new Arc();
			arc.setCenterX(centerX);
			arc.setCenterY(bottom - radius);
			arc.setRadiusX(radius);
			arc.setRadiusY(radius);
			arc.setStartAngle(100.0f);
			arc.setLength(340.0f);
			arc.setType(ArcType.OPEN);
			arc.setStrokeWidth(STROKE_WIDTH);
			arc.setStroke(Color.web(COLOR));
			arc.setFill(Color.TRANSPARENT);

			final Line lineVert = new Line();
			lineVert.startXProperty().set(centerX);
			lineVert.startYProperty().set(bottom - radius);
			lineVert.endXProperty().set(centerX);
			lineVert.endYProperty().set(bottom - radius * 3);
			lineVert.setStrokeWidth(STROKE_WIDTH);
			lineVert.setStroke(Color.web(COLOR));
			lineVert.setFill(Color.web(COLOR));

			final Line arrowLeft = new Line();
			arrowLeft.startXProperty().set(centerX);
			arrowLeft.startYProperty().set(bottom - radius);
			arrowLeft.endXProperty().set(centerX - arrowBounds);
			arrowLeft.endYProperty().set(bottom - radius - arrowBounds);
			arrowLeft.setStrokeWidth(STROKE_WIDTH);
			arrowLeft.setStroke(Color.web(COLOR));
			arrowLeft.setFill(Color.web(COLOR));

			final Line arrowRight = new Line();
			arrowRight.startXProperty().set(centerX);
			arrowRight.startYProperty().set(bottom - radius);
			arrowRight.endXProperty().set(centerX + arrowBounds);
			arrowRight.endYProperty().set(bottom - radius - arrowBounds);
			arrowRight.setStrokeWidth(STROKE_WIDTH);
			arrowRight.setStroke(Color.web(COLOR));
			arrowRight.setFill(Color.web(COLOR));

			getChildren().add(arc);
			getChildren().add(lineVert);
			getChildren().add(arrowLeft);
			getChildren().add(arrowRight);
		}
	}
}
