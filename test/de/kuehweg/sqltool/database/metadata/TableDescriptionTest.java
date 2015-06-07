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

import de.kuehweg.sqltool.database.metadata.description.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.ExportedKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.ImportedKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.IndexColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.Nullability;
import de.kuehweg.sqltool.database.metadata.description.PrimaryKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Kühweg
 */
public class TableDescriptionTest {

    public TableDescriptionTest() {
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
    public void sorting() {
        List<TableDescription> tables = new LinkedList<>();

        tables.add(new TableDescription("TABLE1", "TYPE2", "REMARK"));
        tables.add(new TableDescription("TABLE3", "TYPE1", "REMARK"));
        tables.add(new TableDescription("TABLE2", "TYPE1", "REMARK"));
        tables.add(new TableDescription("TABLE6", "TYPE1", "REMARK"));
        tables.add(new TableDescription("TABLE5", "TYPE2", "REMARK"));
        tables.add(new TableDescription("TABLE4", "TYPE1", "REMARK"));

        Collections.sort(tables);

        // nach tableName, tableType nicht relevant
        Assert.assertEquals("TABLE1", tables.get(0).getName());
        Assert.assertEquals("TABLE2", tables.get(1).getName());
        Assert.assertEquals("TABLE3", tables.get(2).getName());
        Assert.assertEquals("TABLE4", tables.get(3).getName());
        Assert.assertEquals("TABLE5", tables.get(4).getName());
        Assert.assertEquals("TABLE6", tables.get(5).getName());
    }

    @Test
    public void equalsByName() {
        TableDescription table1 = new TableDescription("TABLE1", "TYPE1", "REMARK1");
        TableDescription table1b = new TableDescription("TABLE1", "TYPE2", "REMARK1");
        TableDescription table1c = new TableDescription("TABLE1", "TYPE2", "REMARK2");
        TableDescription table2 = new TableDescription("TABLE2", "TYPE1", "REMARK1");

        Assert.assertEquals(table1, table1);
        Assert.assertEquals(table1, table1b);
        Assert.assertEquals(table1, table1c);

        Assert.assertFalse(table1.equals(table2));
    }

    @Test
    public void addColumns() {
        TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

        ColumnDescription column1 = new ColumnDescription("COLUMN1", "COLUMN-TYPE", 42, 0,
                Nullability.YES, "DEFAULT_VALUE", "COLUMN-REMARK");
        table.adoptOrphan(column1);
        Assert.assertEquals(1, table.getColumns().size());

        ColumnDescription column1b
                = new ColumnDescription("COLUMN1", "COLUMN-TYPE", 42, 0,
                        Nullability.YES, "DEFAULT_VALUE", "COLUMN-REMARK");
        table.adoptOrphan(column1b);
        Assert.assertEquals(1, table.getColumns().size());

        ColumnDescription column2 = new ColumnDescription("COLUMN2", "COLUMN-TYPE", 42, 0,
                Nullability.YES, "DEFAULT_VALUE", "COLUMN-REMARK");
        table.adoptOrphan(column2);
        Assert.assertEquals(2, table.getColumns().size());
    }

    @Test
    public void addPrimaryKeyColumns() {
        TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

        PrimaryKeyColumnDescription pk1
                = new PrimaryKeyColumnDescription("PK1", "COLUMN1");
        table.adoptOrphan(pk1);
        Assert.assertEquals(1, table.getPrimaryKeyColumns().size());

        PrimaryKeyColumnDescription pk1b = new PrimaryKeyColumnDescription("PK1",
                "COLUMN1");
        table.adoptOrphan(pk1b);
        Assert.assertEquals(1, table.getPrimaryKeyColumns().size());

        PrimaryKeyColumnDescription pk12 = new PrimaryKeyColumnDescription("PK1",
                "COLUMN2");
        table.adoptOrphan(pk12);
        Assert.assertEquals(2, table.getPrimaryKeyColumns().size());

        PrimaryKeyColumnDescription pk2
                = new PrimaryKeyColumnDescription("PK2", "COLUMN1");
        table.adoptOrphan(pk2);
        Assert.assertEquals(3, table.getPrimaryKeyColumns().size());
    }

    @Test
    public void addIndices() {
        TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

        IndexColumnDescription index1
                = new IndexColumnDescription("INDEX1", "COLUMN1", 42, false);
        table.adoptOrphan(index1);
        Assert.assertEquals(1, table.getIndices().size());

        IndexColumnDescription index1b = new IndexColumnDescription("INDEX1", "COLUMN1",
                42, false);
        table.adoptOrphan(index1b);
        Assert.assertEquals(1, table.getIndices().size());

        IndexColumnDescription index12 = new IndexColumnDescription("INDEX1", "COLUMN2",
                42, false);
        table.adoptOrphan(index12);
        Assert.assertEquals(2, table.getIndices().size());

        IndexColumnDescription index2
                = new IndexColumnDescription("INDEX2", "COLUMN1", 42, false);
        table.adoptOrphan(index2);
        Assert.assertEquals(3, table.getIndices().size());
    }

    @Test
    public void addImportedKeyColumns() {
        TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

        ImportedKeyColumnDescription fk1
                = new ImportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk1);
        Assert.assertEquals(1, table.getImportedKeyColumns().size());

        ImportedKeyColumnDescription fk1b
                = new ImportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk1b);
        Assert.assertEquals(1, table.getImportedKeyColumns().size());

        ImportedKeyColumnDescription fk12
                = new ImportedKeyColumnDescription("FK1", "FK-COLUMN2", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk12);
        Assert.assertEquals(2, table.getImportedKeyColumns().size());

        ImportedKeyColumnDescription fk2
                = new ImportedKeyColumnDescription("FK2", "FK-COLUMN1", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk2);
        Assert.assertEquals(3, table.getImportedKeyColumns().size());

        Assert.assertTrue(table.getExportedKeyColumns().isEmpty());
    }

    @Test
    public void addExportedKeyColumns() {
        TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

        ExportedKeyColumnDescription fk1
                = new ExportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk1);
        Assert.assertEquals(1, table.getExportedKeyColumns().size());

        ExportedKeyColumnDescription fk1b
                = new ExportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk1b);
        Assert.assertEquals(1, table.getExportedKeyColumns().size());

        ExportedKeyColumnDescription fk12
                = new ExportedKeyColumnDescription("FK1", "FK-COLUMN2", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk12);
        Assert.assertEquals(2, table.getExportedKeyColumns().size());

        ExportedKeyColumnDescription fk2
                = new ExportedKeyColumnDescription("FK2", "FK-COLUMN1", "PK-C", "PK-S",
                        "PK-T", "PK-COL");
        table.adoptOrphan(fk2);
        Assert.assertEquals(3, table.getExportedKeyColumns().size());

        Assert.assertTrue(table.getImportedKeyColumns().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addIllegalType() {
        TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");
        table.adoptOrphan(new SchemaDescription("SCHEMA"));
    }

}
