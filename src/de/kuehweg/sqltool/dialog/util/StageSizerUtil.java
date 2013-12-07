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
package de.kuehweg.sqltool.dialog.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Initiale Ausmaße des Fensters berechnen
 *
 * @author Michael Kühweg
 */
public class StageSizerUtil {

    private StageSizerUtil() {
        // util - no instances
    }

    public static Rectangle2D calculateSizeDependingOnScreenSize() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        double horizontalMargin = Math.max(16, primaryScreenBounds.getWidth()
                * 0.1);
        double verticalMargin = Math.max(16, primaryScreenBounds.getWidth()
                * 0.1);

        return new Rectangle2D(primaryScreenBounds.getMinX() + horizontalMargin
                / 2, primaryScreenBounds.getMinY() + verticalMargin / 4,
                primaryScreenBounds.getWidth() - horizontalMargin,
                primaryScreenBounds.getHeight() - verticalMargin);
    }
}
