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

import de.kuehweg.sqltool.database.execution.fake.ResultSetStubForMetaDataReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Kühweg
 */
public class ForeignKeyMetaDataReaderTest {

    public ForeignKeyMetaDataReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void readMetaData() throws SQLException {
        ForeignKeyMetaDataReader metaDataReader = new ForeignKeyMetaDataReader();
        List<ForeignKeyColumnDescription> fks = new ArrayList<>(metaDataReader.
                buildDescriptions(
                        new ResultSetStubForMetaDataReader(2)));
        assertEquals(2, fks.size());
        for (ForeignKeyColumnDescription fk : fks) {
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "FKTABLE_CAT", fk.getFkCatalog());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "FKTABLE_SCHEM", fk.getFkSchema());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "FKTABLE_NAME", fk.getFkTableName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "FK_NAME", fk.getForeignKeyName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "FKCOLUMN_NAME", fk.getFkColumnName());
            // references...
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "PKTABLE_CAT", fk.getPkCatalog());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "PKTABLE_SCHEM", fk.getPkSchema());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "PKTABLE_NAME", fk.getPkTableName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "PKCOLUMN_NAME", fk.getPkColumnName());
        }
    }

    @Test
    public void hashCodeEquals() {
        ForeignKeyColumnDescription fk1 = new ForeignKeyColumnDescription("CATALOG-F1",
                "SCHEMA-F1", "TABLE-F1", "COLUMN-F1", "FK-1", "CATALOG-P1",
                "SCHEMA-P1", "TABLE-P1", "COLUMN-P1"
        );
        ForeignKeyColumnDescription sameFk = new ForeignKeyColumnDescription("CATALOG-F1",
                "SCHEMA-F1", "TABLE-F1", "COLUMN-F1", "FK-1", "CATALOG-P1",
                "SCHEMA-P1", "TABLE-P1", "COLUMN-P1"
        );
        ForeignKeyColumnDescription differentColumnFk = new ForeignKeyColumnDescription(
                "CATALOG-F1",
                "SCHEMA-F1", "TABLE-F1", "COLUMN-F2", "FK-1", "CATALOG-P1",
                "SCHEMA-P1", "TABLE-P1", "COLUMN-P1"
        );
        ForeignKeyColumnDescription differentNameFk = new ForeignKeyColumnDescription(
                "CATALOG-F1",
                "SCHEMA-F1", "TABLE-F1", "COLUMN-F1", "FK-2", "CATALOG-P1",
                "SCHEMA-P1", "TABLE-P1", "COLUMN-P1"
        );
        ForeignKeyColumnDescription differentTableFk = new ForeignKeyColumnDescription(
                "CATALOG-F1",
                "SCHEMA-F1", "TABLE-F2", "COLUMN-F1", "FK-1", "CATALOG-P1",
                "SCHEMA-P1", "TABLE-P1", "COLUMN-P1"
        );
        ForeignKeyColumnDescription differentSchemaFk = new ForeignKeyColumnDescription(
                "CATALOG-F1",
                "SCHEMA-F2", "TABLE-F1", "COLUMN-F1", "FK-1", "CATALOG-P1",
                "SCHEMA-P1", "TABLE-P1", "COLUMN-P1"
        );
        Set<ForeignKeyColumnDescription> fks = new HashSet<>();
        fks.add(fk1);
        fks.add(sameFk);
        assertEquals(1, fks.size());
        fks.add(differentColumnFk);
        assertEquals(2, fks.size());
        fks.add(differentNameFk);
        assertEquals(3, fks.size());
        fks.add(differentTableFk);
        assertEquals(4, fks.size());
        fks.add(differentSchemaFk);
        assertEquals(5, fks.size());
    }
    
    @Test
    public void outside() {
        ForeignKeyColumnDescription fk1 = new ForeignKeyColumnDescription("CATALOG1",
                "SCHEMA1", "TABLE1", "COLUMN1", "FK-1", "CATALOG1",
                "SCHEMA1", "TABLE2", "COLUMN2"
        );
        ForeignKeyColumnDescription fk2 = new ForeignKeyColumnDescription("CATALOG1",
                "SCHEMA1", "TABLE1", "COLUMN1", "FK-1", "CATALOG1",
                "SCHEMA1", "TABLE2", "COLUMN2"
        );
        ForeignKeyColumnDescription fk3 = new ForeignKeyColumnDescription("CATALOG1",
                "SCHEMA1", "TABLE1", "COLUMN1", "FK-1", "CATALOG1",
                "SCHEMA2", "TABLE2", "COLUMN2"
        );
        assertFalse(fk1.isOutside());
        assertFalse(fk2.isOutside());
        assertTrue(fk3.isOutside());
    }
}
