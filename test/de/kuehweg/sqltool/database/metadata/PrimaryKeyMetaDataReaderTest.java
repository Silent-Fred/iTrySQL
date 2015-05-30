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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Kühweg
 */
public class PrimaryKeyMetaDataReaderTest {

    public PrimaryKeyMetaDataReaderTest() {
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
        PrimaryKeyMetaDataReader metaDataReader = new PrimaryKeyMetaDataReader();
        List<PrimaryKeyColumnDescription> pks = new ArrayList<>(metaDataReader.
                buildDescriptions(
                        new ResultSetStubForMetaDataReader(2)));
        assertEquals(2, pks.size());
        for (PrimaryKeyColumnDescription pk : pks) {
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_CAT", pk.getCatalog());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_SCHEM", pk.getSchema());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_NAME", pk.getTableName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "PK_NAME", pk.getPrimaryKeyName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "COLUMN_NAME", pk.getColumnName());
        }
    }

    @Test
    public void hashCodeEquals() {
        PrimaryKeyColumnDescription pk1 = new PrimaryKeyColumnDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "PK-NAME-1", "COLUMN-1");
        PrimaryKeyColumnDescription samePk = new PrimaryKeyColumnDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "PK-NAME-1", "COLUMN-1");
        PrimaryKeyColumnDescription differentPk = new PrimaryKeyColumnDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "PK-NAME-1", "COLUMN-2");
        PrimaryKeyColumnDescription anotherDifferentPk = new PrimaryKeyColumnDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "PK-NAME-2", "COLUMN-1");
        PrimaryKeyColumnDescription yetAnotherDifferentPk = new PrimaryKeyColumnDescription("CATALOG-1", "SCHEMA-1", "TABLE-2",
                "PK-NAME-1", "COLUMN-1");
        PrimaryKeyColumnDescription yetAnotherWayDifferentPk = new PrimaryKeyColumnDescription("CATALOG-1", "SCHEMA-2", "TABLE-1",
                "PK-NAME-1", "COLUMN-1");
        Set<PrimaryKeyColumnDescription> pks = new HashSet<>();
        pks.add(pk1);
        pks.add(samePk);
        assertEquals(1, pks.size());
        pks.add(differentPk);
        assertEquals(2, pks.size());
        pks.add(anotherDifferentPk);
        assertEquals(3, pks.size());
        pks.add(yetAnotherDifferentPk);
        assertEquals(4, pks.size());
        pks.add(yetAnotherWayDifferentPk);
        assertEquals(5, pks.size());
    }
}
