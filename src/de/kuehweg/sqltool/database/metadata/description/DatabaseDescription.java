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
package de.kuehweg.sqltool.database.metadata.description;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.kuehweg.sqltool.common.DialogDictionary;

/**
 * Beschreibung der Metadaten einer Datenbank. Die Datenbank enthält catalogs.
 *
 * @author Michael Kühweg
 */
public class DatabaseDescription extends DatabaseObjectDescription {

	private final String dbProductName;
	private final String dbProductVersion;
	private final Set<CatalogDescription> catalogs;

	public DatabaseDescription() {
		super(DialogDictionary.MSG_NO_DB_CONNECTION_FOR_SCHEMA_TREE_VIEW
				.toString());
		dbProductName = "";
		dbProductVersion = "";
		catalogs = new HashSet<>();
	}

	public DatabaseDescription(final String name, final String dbProductName,
			final String dbProductVersion) {
		super(name);
		this.dbProductName = dbProductName == null ? "" : dbProductName;
		this.dbProductVersion = dbProductVersion == null ? ""
				: dbProductVersion;
		catalogs = new HashSet<>();
	}

	public String getDbProductName() {
		return dbProductName;
	}

	public String getDbProductVersion() {
		return dbProductVersion;
	}

	public List<CatalogDescription> getCatalogs() {
		final List<CatalogDescription> result = new ArrayList<>(catalogs);
		Collections.sort(result);
		return result;
	}

	private void addCatalogs(final CatalogDescription... cats) {
		for (final CatalogDescription catalog : cats) {
			catalogs.add(catalog);
		}
	}

	@Override
	protected void appendChild(final DatabaseObjectDescription child) {
		if (CatalogDescription.class.isAssignableFrom(child.getClass())) {
			addCatalogs((CatalogDescription) child);
		} else {
			super.appendChild(child);
		}
	}
}
