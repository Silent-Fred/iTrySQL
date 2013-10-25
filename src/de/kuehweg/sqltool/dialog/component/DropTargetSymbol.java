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
package de.kuehweg.sqltool.dialog.component;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;

/**
 *
 * @author Michael Kühweg
 */
public class DropTargetSymbol extends Pane {

    public DropTargetSymbol(final Pane parentPane) {
        super();
        if (parentPane != null) {
            double baseSize = Math.min(Math.min(parentPane.getWidth(),
                    parentPane.getHeight()), 160);

            double radius = baseSize / 3;
            double arrowBounds = radius / 2;

            double centerX = parentPane.getWidth() / 2 - radius;
            double bottom = parentPane.getHeight() / 2 + baseSize / 2;

            Arc arc = new Arc();
            arc.setCenterX(centerX);
            arc.setCenterY(bottom - radius);
            arc.setRadiusX(radius);
            arc.setRadiusY(radius);
            arc.setStartAngle(100.0f);
            arc.setLength(340.0f);
            arc.setType(ArcType.OPEN);
            arc.setStrokeWidth(1.0);
            arc.setStroke(Color.web("#0071bb"));
            arc.setFill(Color.TRANSPARENT);

            Line lineVert = new Line();
            lineVert.startXProperty().set(centerX);
            lineVert.startYProperty().set(bottom - radius);
            lineVert.endXProperty().set(centerX);
            lineVert.endYProperty().set(bottom - radius * 3);
            lineVert.setStrokeWidth(1.0);
            lineVert.setStroke(Color.web("#0071bb"));
            lineVert.setFill(Color.web("#0071bb"));

            Line arrowLeft = new Line();
            arrowLeft.startXProperty().set(centerX);
            arrowLeft.startYProperty().set(bottom - radius);
            arrowLeft.endXProperty().set(centerX - arrowBounds);
            arrowLeft.endYProperty().set(bottom - radius - arrowBounds);
            arrowLeft.setStrokeWidth(1.0);
            arrowLeft.setStroke(Color.web("#0071bb"));
            arrowLeft.setFill(Color.web("#0071bb"));

            Line arrowRight = new Line();
            arrowRight.startXProperty().set(centerX);
            arrowRight.startYProperty().set(bottom - radius);
            arrowRight.endXProperty().set(centerX + arrowBounds);
            arrowRight.endYProperty().set(bottom - radius - arrowBounds);
            arrowRight.setStrokeWidth(1.0);
            arrowRight.setStroke(Color.web("#0071bb"));
            arrowRight.setFill(Color.web("#0071bb"));

            getChildren().add(arc);
            getChildren().add(lineVert);
            getChildren().add(arrowLeft);
            getChildren().add(arrowRight);
        }
    }
}
