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
package de.kuehweg.sqltool.database.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.kuehweg.sqltool.database.metadata.description.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.Nullability;

/**
 * Metadaten für Tabellenspalten aufbereiten.
 *
 * @author Michael Kühweg
 */
public class ColumnMetaDataReader extends AbstractMetaDataReader {

	public ColumnMetaDataReader(final DatabaseDescription root) {
		super(root);
	}

	@Override
	protected void readAndAddDescription(final ResultSet column) throws SQLException {
		findParent(column.getString("TABLE_CAT"), column.getString("TABLE_SCHEM"), column.getString("TABLE_NAME"))
				.adoptOrphan(new ColumnDescription(column.getString("COLUMN_NAME"), column.getString("TYPE_NAME"),
						column.getInt("COLUMN_SIZE"), column.getInt("DECIMAL_DIGITS"),
						nullabilityOnColumn(column.getString("IS_NULLABLE")), column.getString("COLUMN_DEF"),
						column.getString("REMARKS")));
	}

	private Nullability nullabilityOnColumn(final String nullableMeta) {
		Nullability nullabilityToUse;
		switch (nullableMeta) {
		case "YES":
			nullabilityToUse = Nullability.YES;
			break;
		case "NO":
			nullabilityToUse = Nullability.NO;
			break;
		default:
			nullabilityToUse = Nullability.MAYBE;
		}
		return nullabilityToUse;
	}

}
