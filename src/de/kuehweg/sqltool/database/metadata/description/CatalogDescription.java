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
package de.kuehweg.sqltool.database.metadata.description;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Beschreibung der catalog Metadaten. Ein catalog enthält Schemata.
 *
 * @author Michael Kühweg
 */
public class CatalogDescription extends DatabaseObjectDescription {

	private final Set<SchemaDescription> schemas;

	public CatalogDescription(final String catalog) {
		super(catalog);
		schemas = new HashSet<>();
	}

	/**
	 * Schemas im Catalog, sortiert nach Schemaname.
	 *
	 * @return
	 */
	public List<SchemaDescription> getSchemas() {
		final List<SchemaDescription> result = new ArrayList<>(schemas);
		Collections.sort(result);
		return result;
	}

	private void addSchemas(final SchemaDescription... schemas) {
		for (final SchemaDescription schema : schemas) {
			this.schemas.add(schema);
		}
	}

	@Override
	protected void appendChild(final DatabaseObjectDescription child) {
		if (SchemaDescription.class.isAssignableFrom(child.getClass())) {
			addSchemas((SchemaDescription) child);
		} else {
			super.appendChild(child);
		}
	}
}
