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
public class ColumnMetaDataReaderTest {

    public ColumnMetaDataReaderTest() {
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
        ColumnMetaDataReader metaDataReader = new ColumnMetaDataReader();
        List<ColumnDescription> columns = new ArrayList<>(metaDataReader.
                buildDescriptions(
                        new ResultSetStubForMetaDataReader(2)));
        assertEquals(2, columns.size());
        for (ColumnDescription column : columns) {
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_CAT", column.getCatalog());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_SCHEM", column.getSchema());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TABLE_NAME", column.getTableName());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "REMARKS", column.getRemarks());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "COLUMN_NAME", column.getColumnName());
            assertEquals(0, column.getSize());
            assertEquals(1, column.getDecimalDigits());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "TYPE_NAME", column.getType());
            assertEquals(Nullability.MAYBE, column.getNullable());
            assertEquals(ResultSetStubForMetaDataReader.PREFIX_GET + String.class.
                    getSimpleName() + "COLUMN_DEF", column.getDefaultValue());
        }
    }

    @Test
    public void hashCodeEquals() {
        ColumnDescription col1 = new ColumnDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "COLUMN-1", "TYPE-1", 1,
                0, Nullability.YES, "DEFAULT-1", "REMARK-1");
        ColumnDescription sameColumn = new ColumnDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-1",
                "COLUMN-1", "TYPE-2", 2,
                1, Nullability.MAYBE, "DEFAULT-2", "REMARK-2");
        ColumnDescription differentColumn = new ColumnDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-1",
                "COLUMN-2", "TYPE-2", 2,
                1, Nullability.MAYBE, "DEFAULT-2", "REMARK-2");
        ColumnDescription anotherDifferentColumn = new ColumnDescription("CATALOG-1",
                "SCHEMA-1", "TABLE-2",
                "COLUMN-1", "TYPE-2", 2,
                1, Nullability.MAYBE, "DEFAULT-2", "REMARK-2");
        ColumnDescription anotherVeryDifferentColumn = new ColumnDescription("CATALOG-1",
                "SCHEMA-2", "TABLE-1",
                "COLUMN-1", "TYPE-2", 2,
                1, Nullability.MAYBE, "DEFAULT-2", "REMARK-2");
        Set<ColumnDescription> columns = new HashSet<>();
        columns.add(col1);
        columns.add(sameColumn);
        assertEquals(1, columns.size());
        columns.add(differentColumn);
        assertEquals(2, columns.size());
        columns.add(anotherDifferentColumn);
        assertEquals(3, columns.size());
        columns.add(anotherVeryDifferentColumn);
        assertEquals(4, columns.size());
    }
}
