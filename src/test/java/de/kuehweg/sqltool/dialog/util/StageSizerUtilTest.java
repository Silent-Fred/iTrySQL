/*
 * Copyright (c) 2016, Michael Kühweg
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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.geometry.Rectangle2D;

/**
 * @author Michael Kühweg
 */
public class StageSizerUtilTest {

	@Test
	public void testMinimumMargins() {
		final Rectangle2D bigEnough = new Rectangle2D(0, 0, StageSizerUtil.MIN_HORIZONTAL_MARGIN_ABSOLUTE * 2 + 3,
				StageSizerUtil.MIN_VERTICAL_MARGIN_ABSOLUTE * 2 + 3);

		Rectangle2D calculatedRectangle = StageSizerUtil.calculateSizeDependingOnScreenSize(bigEnough);
		assertTrue(calculatedRectangle.getWidth() > StageSizerUtil.MIN_HORIZONTAL_MARGIN_ABSOLUTE);
		assertTrue(calculatedRectangle.getHeight() > StageSizerUtil.MIN_VERTICAL_MARGIN_ABSOLUTE);

		final Rectangle2D tooSmall = new Rectangle2D(0, 0, StageSizerUtil.MIN_HORIZONTAL_MARGIN_ABSOLUTE - 1,
				StageSizerUtil.MIN_VERTICAL_MARGIN_ABSOLUTE - 1);
		calculatedRectangle = StageSizerUtil.calculateSizeDependingOnScreenSize(tooSmall);
		assertTrue(calculatedRectangle.getWidth() < StageSizerUtil.MIN_HORIZONTAL_MARGIN_ABSOLUTE);
		assertTrue(calculatedRectangle.getHeight() < StageSizerUtil.MIN_VERTICAL_MARGIN_ABSOLUTE);
	}

}
