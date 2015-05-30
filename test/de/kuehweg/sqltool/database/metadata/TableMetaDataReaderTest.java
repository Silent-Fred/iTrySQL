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
 * Metadaten von Tabellen testen
 *
 * @author Michael Kühweg
 */
public class TableMetaDataReaderTest {

    public TableMetaDataReaderTest() {
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
        TableMetaDataReader metaDataReader = new TableMetaDataReader();
        List<TableDescription> tables = new ArrayList<>(metaDataReader.buildDescriptions(
                new ResultSetStubForMetaDataReader(2)));
        assertEquals(2, tables.size());
        for (TableDescription table : tables) {
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_CAT", table.getCatalog());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_SCHEM", table.getSchema());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_NAME", table.getTableName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_TYPE", table.getTableType());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "REMARKS", table.getRemarks());
        }
    }

    @Test
    public void hashCodeEquals() {
        TableDescription table1 = new TableDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "TYPE-1", "REMARK-1");
        TableDescription sameTable = new TableDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "TYPE-1", "REMARK-2");
        TableDescription differentTable = new TableDescription("CATALOG-1", "SCHEMA-1", "TABLE-2",
                "TYPE-1", "REMARK-1");
        TableDescription differentSchemaTable = new TableDescription("CATALOG-1", "SCHEMA-2", "TABLE-1",
                "TYPE-1", "REMARK-1");
        Set<TableDescription> tables = new HashSet<>();
        tables.add(table1);
        tables.add(sameTable);
        assertEquals(1, tables.size());
        tables.add(differentTable);
        assertEquals(2, tables.size());
        tables.add(differentSchemaTable);
        assertEquals(3, tables.size());
    }
}
