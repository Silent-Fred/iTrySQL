/*
 * Copyright (c) 2015, Michael Kühweg
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
package de.kuehweg.sqltool.dialog.component.schematree;

import de.kuehweg.sqltool.dialog.component.schematree.node.SchemaTreeNode;

/**
 * Styles für Knoten in der Strukturansicht.
 *
 * @author Michael Kühweg
 */
public class SchemaTreeStyleFinder {

	public String styleClass(final SchemaTreeNode node) {
		switch (node.getType()) {
		case DATABASE:
			return SchemaTreeConstants.STYLE_DATABASE;
		case CATALOG:
			return SchemaTreeConstants.STYLE_USER;
		case SCHEMA:
			return SchemaTreeConstants.STYLE_USER;
		case TABLE_TYPE:
		case TABLE:
			return SchemaTreeConstants.STYLE_TABLE;
		case COLUMN:
			return SchemaTreeConstants.STYLE_COLUMN;
		case PRIMARY_KEY_COLUMN:
			return SchemaTreeConstants.STYLE_PRIMARY_KEY;
		case IMPORTED_KEYS:
		case IMPORTED_KEY:
		case IMPORTED_KEY_COLUMN:
			return SchemaTreeConstants.STYLE_REFERENCES;
		case EXPORTED_KEYS:
		case EXPORTED_KEY:
		case EXPORTED_KEY_COLUMN:
			return SchemaTreeConstants.STYLE_FOREIGN_KEY_CONSTRAINT;
		case INDICES:
		case INDEX:
		case INDEX_COLUMN:
			return SchemaTreeConstants.STYLE_INDEX;
		default:
			return "";
		}
	}

}
