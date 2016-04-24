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

/**
 * In JavaFX Versionen zwischen 1.8.0_40 und 1.8.0_72 (je ausschließlich)
 * funktionieren im fertigen Application Bundle in WebViews keine Ressourcen,
 * die im Bundle mit ausgeliefert werden.
 *
 * @author Michael Kühweg
 */
public class WebViewBundledResourceErrorDetection {

	private static final String VERSION_PROPERTY = "java.version";

	private static final int JAVA_1_0 = 10;
	private static final int JAVA_8 = 18;

	private final int majorVersion;
	private final int updateVersion;

	public WebViewBundledResourceErrorDetection() {
		super();
		majorVersion = readMajorVersion();
		updateVersion = readUpdateVersion();
	}

	private int readMajorVersion() {
		try {
			final String version = System.getProperty(VERSION_PROPERTY);
			int pos = version.indexOf('.');
			pos = version.indexOf('.', pos + 1);
			final String major = version.substring(0, pos).replace(".", "");
			return Integer.parseInt(major);
		} catch (final Exception wtfWentWrong) {
			return JAVA_1_0;
		}
	}

	private int readUpdateVersion() {
		final String version = System.getProperty(VERSION_PROPERTY);
		final int pos = version.lastIndexOf('_');
		if (pos < 0) {
			return 0;
		}
		final String update = version.substring(pos + 1, version.length());
		try {
			return Integer.parseInt(update);
		} catch (final NumberFormatException wtfWentWrong) {
			return 0;
		}
	}

	public boolean runningOnJavaVersionWithRenderingDeficiencies() {
		return majorVersion == JAVA_8 && updateVersion > 40 && updateVersion < 72;
	}

	public boolean runningOnJavaVersionWithBundledJavascriptDeficiencies() {
		// FIXME Wird laut Bugtracking erst in Java 9 behoben sein - da bislang
		// aber ungetestet, wird hier davon ausgegangen, dass es halt überhaupt
		// nicht funktioniert.
		return true;
	}

}
