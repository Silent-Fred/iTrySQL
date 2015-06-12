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
import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.Nullability;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;
import java.sql.SQLException;
import java.util.List;
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
public class ColumnMetaDataReaderTest {

    private DatabaseDescription db;
    private CatalogDescription catalog;
    private SchemaDescription schema;
    private TableDescription table;
    private TableDescription tableUntouched;

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
        db = new DatabaseDescription("db", "product", "version");

        catalog = new CatalogDescription("TABLE_CAT");
        db.adoptOrphan(catalog);

        schema = new SchemaDescription("TABLE_SCHEM");
        catalog.adoptOrphan(schema);

        table = new TableDescription("TABLE_NAME", "TABLE", "REMARKS");
        tableUntouched = new TableDescription("TABLE1", "TABLE", "REMARKS");

        schema.adoptOrphan(table);
        schema.adoptOrphan(tableUntouched);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void readMetaData() throws SQLException {
        ColumnMetaDataReader metaDataReader = new ColumnMetaDataReader(db);
        metaDataReader.readAndAddDescriptions(new ResultSetStubForMetaDataReader(2,
                "COLUMN_NAME"));

        assertTrue(tableUntouched.getColumns().isEmpty());

        assertEquals(2, table.getColumns().size());

        int count = 1;
        for (ColumnDescription column : table.getColumns()) {
            assertTrue(isCorrectColumn(column, count++));
        }

    }

    @Test
    public void wrongWay() throws SQLException {
        DatabaseDescription wrongWayDb = new DatabaseDescription();
        ColumnMetaDataReader metaDataReader = new ColumnMetaDataReader(wrongWayDb);
        metaDataReader.readAndAddDescriptions(new ResultSetStubForMetaDataReader(2,
                "COLUMN_NAME"));

        assertEquals(1, wrongWayDb.getCatalogs().size());
        assertEquals("TABLE_CAT", wrongWayDb.getCatalogs().iterator().next().getName());

        assertEquals(1, wrongWayDb.getCatalogs().iterator().next().getSchemas().size());
        assertEquals("TABLE_SCHEM", wrongWayDb.getCatalogs().iterator().next().
                getSchemas().iterator().next().getName());

        List<TableDescription> tables = wrongWayDb.getCatalogs().iterator().next().
                getSchemas().iterator().next().getTables();
        assertEquals(1, tables.size());
        assertEquals("TABLE_NAME", tables.iterator().next().getName());

        assertEquals("UNKNOWN", tables.iterator().next().getTableType());
        assertEquals(
                "This table's description was built by accident. Some subobject was created first. The description is incomplete.",
                tables.iterator().next().getRemarks());
    }

    private boolean isCorrectColumn(ColumnDescription column, int count) {
        boolean correct = column.getRemarks().equals("REMARKS");

        // der Spaltenname wird im Stub mit fortlaufender Nummer erzeugt
        correct = correct && column.getName().equals("COLUMN_NAME" + count);

        correct = correct && column.getSize() == 0;
        correct = correct && column.getDecimalDigits() == 1;
        correct = correct && column.getType().equals("TYPE_NAME");
        correct = correct && Nullability.MAYBE == column.getNullable();
        correct = correct && column.getDefaultValue().equals("COLUMN_DEF");
        return correct;
    }
}
