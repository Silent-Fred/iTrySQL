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

package de.kuehweg.sqltool.dialog.component.editor;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

import de.kuehweg.sqltool.itrysql.ResourceLocator;

/**
 * @author Michael Kühweg
 */
public class StatementEditorCycle implements Serializable {

	private static final long serialVersionUID = -8885095183930281907L;

	private final LinkedList<Class<? extends StatementEditor>> statementEditorClasses = new LinkedList<>();

	private int cycle = 0;

	public StatementEditorCycle() {
		if (isPlainTextAvailable()) {
			statementEditorClasses.add(TextAreaBasedEditor.class);
		}
		if (isCodemirrorAvailable()) {
			statementEditorClasses.add(CodeMirrorBasedEditor.class);
		}
		if (isAceAvailable()) {
			statementEditorClasses.add(AceBasedEditor.class);
		}
	}

	public void recycle() {
		cycle = 0;
	}

	public Class<? extends StatementEditor> next() {
		cycle = (cycle + 1) % statementEditorClasses.size();
		return statementEditorClasses.get(cycle);
	}

	public int numberOfOptions() {
		return statementEditorClasses.size();
	}

	private boolean isPlainTextAvailable() {
		return true;
	}

	private boolean isCodemirrorAvailable() {
		final ResourceLocator locator = new ResourceLocator();
		try {
			return new File(locator.getExternalFormForExplodedResourceInBundle(CodeMirrorBasedEditor.RESOURCE).toURI()
					.getPath()).exists();
		} catch (final Exception e) {
			return false;
		}
	}

	private boolean isAceAvailable() {
		final ResourceLocator locator = new ResourceLocator();
		try {
			return new File(
					locator.getExternalFormForExplodedResourceInBundle(AceBasedEditor.RESOURCE).toURI().getPath())
							.exists();
		} catch (final Exception e) {
			return false;
		}
	}
}
