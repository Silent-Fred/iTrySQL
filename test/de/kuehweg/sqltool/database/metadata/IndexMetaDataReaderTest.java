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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Kühweg
 */
public class IndexMetaDataReaderTest {

    public IndexMetaDataReaderTest() {
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
        IndexMetaDataReader metaDataReader = new IndexMetaDataReader();
        List<IndexDescription> indices = new ArrayList<>(metaDataReader.
                buildDescriptions(
                        new ResultSetStubForMetaDataReader(2)));
        assertEquals(2, indices.size());
        for (IndexDescription idx : indices) {
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_CAT", idx.getCatalog());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_SCHEM", idx.getSchema());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_NAME", idx.getTableName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "INDEX_NAME", idx.getIndexName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "COLUMN_NAME", idx.getColumnName());
            assertTrue(idx.isNonUnique());
            assertEquals(2, idx.getOrdinalPosition());
        }
    }

    @Test
    public void hashCodeEquals() {
        IndexDescription idx1 = new IndexDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-1", "IDX-NAME-1", "COLUMN-1", 0, true);
        IndexDescription sameIndex = new IndexDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-1", "IDX-NAME-1", "COLUMN-1", 1, false);
        IndexDescription differentIndex = new IndexDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-1", "IDX-NAME-1", "COLUMN-2", 0, true);
        IndexDescription anotherDifferentIndex = new IndexDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-1", "IDX-NAME-2", "COLUMN-1", 0, true);
        IndexDescription yetAnotherDifferentIndex = new IndexDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-2", "IDX-NAME-1", "COLUMN-1", 0, true);
        IndexDescription yetAnotherWayDifferentIndex = new IndexDescription("CATALOG-1",
                "SCHEMA-2", "TABLE-1", "IDX-NAME-1", "COLUMN-1", 0, true);
        Set<IndexDescription> indices = new HashSet<>();
        indices.add(idx1);
        indices.add(sameIndex);
        assertEquals(1, indices.size());
        indices.add(differentIndex);
        assertEquals(2, indices.size());
        indices.add(anotherDifferentIndex);
        assertEquals(3, indices.size());
        indices.add(yetAnotherDifferentIndex);
        assertEquals(4, indices.size());
        indices.add(yetAnotherWayDifferentIndex);
        assertEquals(5, indices.size());
    }
}
