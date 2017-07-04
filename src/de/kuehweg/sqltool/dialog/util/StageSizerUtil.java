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
 * Initiale Ausmaße des Fensters berechnen.
 *
 * @author Michael Kühweg
 */
public final class StageSizerUtil {

	public static final int TOP_FRACTION = 4;

	public static final int MIN_HORIZONTAL_MARGIN_ABSOLUTE = 16;
	public static final int MIN_VERTICAL_MARGIN_ABSOLUTE = 16;

	public static final double MIN_HORIZONTAL_MARGIN_RELATIVE = 0.025;
	public static final double MIN_VERTICAL_MARGIN_RELATIVE = 0.025;

	private StageSizerUtil() {
		// util - no instances
	}

	/**
	 * Initiale Größe des Anwendungsfensters auf Basis der verfügbaren
	 * Bildschirmgröße berechnen.
	 *
	 * @return Rechteck mit den berechneten Abmessungen
	 */
	public static Rectangle2D calculateSizeDependingOnScreenSize() {
		return calculateSizeDependingOnScreenSize(Screen.getPrimary().getVisualBounds());
	}

	public static Rectangle2D calculateSizeDependingOnScreenSize(final Rectangle2D bounds) {

		final double horizontalMargin = calculateHorizontalMargin(bounds);
		final double verticalMargin = calculateVerticalMargin(bounds);

		return new Rectangle2D(bounds.getMinX() + horizontalMargin / 2,
				bounds.getMinY() + verticalMargin / TOP_FRACTION, bounds.getWidth() - horizontalMargin,
				bounds.getHeight() - verticalMargin);
	}

	private static double calculateHorizontalMargin(final Rectangle2D bounds) {
		final double preferredMargin = Math.max(MIN_HORIZONTAL_MARGIN_ABSOLUTE,
				bounds.getWidth() * MIN_HORIZONTAL_MARGIN_RELATIVE);
		if (preferredMargin * 2 >= bounds.getWidth()) {
			return Math.min(MIN_HORIZONTAL_MARGIN_ABSOLUTE, bounds.getWidth() * MIN_HORIZONTAL_MARGIN_RELATIVE);
		}
		return preferredMargin;
	}

	private static double calculateVerticalMargin(final Rectangle2D bounds) {
		final double preferredMargin = Math.max(MIN_VERTICAL_MARGIN_ABSOLUTE,
				bounds.getHeight() * MIN_VERTICAL_MARGIN_RELATIVE);
		if (preferredMargin * 2 >= bounds.getWidth()) {
			return Math.min(MIN_VERTICAL_MARGIN_ABSOLUTE, bounds.getHeight() * MIN_VERTICAL_MARGIN_RELATIVE);
		}
		return preferredMargin;
	}
}
