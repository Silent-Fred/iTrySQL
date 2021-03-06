/*
 * Copyright (c) 2014, Michael Kühweg
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
 * Bugfix für die in der Kombination von HSQLDB mit WebViews auftretenden
 * Probleme, dass wegen des von HSQLDB umgestellten Log-Levels die WebViews in
 * einen Debug-Modus verfallen.
 *
 * @author Michael Kühweg
 */
public final class WebViewWithHSQLDBBugfix {

	private WebViewWithHSQLDBBugfix() {
		// utility class
	}

	/**
	 * Workaround / Fix für die Kollission zwischen JavaFX und HSQLDB.
	 */
	public static void fix() {
		// scheint mit JavaFX 11+ und HSQLDB 2.5 nicht mehr aufzutreten
		// Logger.getLogger("com.sun.javafx.webkit.prism.WCGraphicsPrismContext").setLevel(Level.OFF);
	}
}
