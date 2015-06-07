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

import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.ImportedKeyColumnDescription;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Metadaten der Foreign Key Constraints aufbereiten
 *
 * @author Michael Kühweg
 */
public class ImportedKeyMetaDataReader extends AbstractMetaDataReader {

    public ImportedKeyMetaDataReader(DatabaseDescription root) {
        super(root);
    }

    @Override
    protected void readAndAddDescription(ResultSet foreignKeyConstraint) throws SQLException {
        findParent(foreignKeyConstraint.getString("FKTABLE_CAT"),
                foreignKeyConstraint.getString("FKTABLE_SCHEM"),
                foreignKeyConstraint.getString("FKTABLE_NAME")).adoptOrphan(
                        new ImportedKeyColumnDescription(
                                foreignKeyConstraint.getString("FK_NAME"),
                                foreignKeyConstraint.getString("FKCOLUMN_NAME"),
                                foreignKeyConstraint.getString("PKTABLE_CAT"),
                                foreignKeyConstraint.getString("PKTABLE_SCHEM"),
                                foreignKeyConstraint.getString("PKTABLE_NAME"),
                                foreignKeyConstraint.getString("PKCOLUMN_NAME")));
    }

}
