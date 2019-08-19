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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.database.metadata.description.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.ExportedKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.ImportedKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.IndexColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.Nullability;
import de.kuehweg.sqltool.database.metadata.description.PrimaryKeyColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;

/**
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
		final List<TableDescription> tables = new LinkedList<>();

		tables.add(new TableDescription("TABLE1", "TYPE2", "REMARK"));
		tables.add(new TableDescription("TABLE3", "TYPE1", "REMARK"));
		tables.add(new TableDescription("TABLE2", "TYPE1", "REMARK"));
		tables.add(new TableDescription("TABLE6", "TYPE1", "REMARK"));
		tables.add(new TableDescription("TABLE5", "TYPE2", "REMARK"));
		tables.add(new TableDescription("TABLE4", "TYPE1", "REMARK"));

		Collections.sort(tables);

		// nach tableName, tableType nicht relevant
		assertEquals("TABLE1", tables.get(0).getName());
		assertEquals("TABLE2", tables.get(1).getName());
		assertEquals("TABLE3", tables.get(2).getName());
		assertEquals("TABLE4", tables.get(3).getName());
		assertEquals("TABLE5", tables.get(4).getName());
		assertEquals("TABLE6", tables.get(5).getName());
	}

	@Test
	public void equalsByName() {
		final TableDescription table1 = new TableDescription("TABLE1", "TYPE1", "REMARK1");
		final TableDescription table1b = new TableDescription("TABLE1", "TYPE2", "REMARK1");
		final TableDescription table1c = new TableDescription("TABLE1", "TYPE2", "REMARK2");
		final TableDescription table2 = new TableDescription("TABLE2", "TYPE1", "REMARK1");

		assertEquals(table1, table1);
		assertEquals(table1, table1b);
		assertEquals(table1, table1c);

		assertFalse(table1.equals(table2));
	}

	@Test
	public void addColumns() {
		final TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

		final ColumnDescription column1 = new ColumnDescription("COLUMN1", "COLUMN-TYPE", 42, 0, Nullability.YES,
				"DEFAULT_VALUE", "COLUMN-REMARK");
		table.adoptOrphan(column1);
		assertEquals(1, table.getColumns().size());

		final ColumnDescription column1b = new ColumnDescription("COLUMN1", "COLUMN-TYPE", 42, 0, Nullability.YES,
				"DEFAULT_VALUE", "COLUMN-REMARK");
		table.adoptOrphan(column1b);
		assertEquals(1, table.getColumns().size());

		final ColumnDescription column2 = new ColumnDescription("COLUMN2", "COLUMN-TYPE", 42, 0, Nullability.YES,
				"DEFAULT_VALUE", "COLUMN-REMARK");
		table.adoptOrphan(column2);
		assertEquals(2, table.getColumns().size());
	}

	@Test
	public void addPrimaryKeyColumns() {
		final TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

		final PrimaryKeyColumnDescription pk1 = new PrimaryKeyColumnDescription("PK1", "COLUMN1");
		table.adoptOrphan(pk1);
		assertEquals(1, table.getPrimaryKeyColumns().size());

		final PrimaryKeyColumnDescription pk1b = new PrimaryKeyColumnDescription("PK1", "COLUMN1");
		table.adoptOrphan(pk1b);
		assertEquals(1, table.getPrimaryKeyColumns().size());

		final PrimaryKeyColumnDescription pk12 = new PrimaryKeyColumnDescription("PK1", "COLUMN2");
		table.adoptOrphan(pk12);
		assertEquals(2, table.getPrimaryKeyColumns().size());

		final PrimaryKeyColumnDescription pk2 = new PrimaryKeyColumnDescription("PK2", "COLUMN1");
		table.adoptOrphan(pk2);
		assertEquals(3, table.getPrimaryKeyColumns().size());
	}

	@Test
	public void addIndices() {
		final TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

		final IndexColumnDescription index1 = new IndexColumnDescription("INDEX1", "COLUMN1", 42, false);
		table.adoptOrphan(index1);
		assertEquals(1, table.getIndices().size());

		final IndexColumnDescription index1b = new IndexColumnDescription("INDEX1", "COLUMN1", 42, false);
		table.adoptOrphan(index1b);
		assertEquals(1, table.getIndices().size());

		final IndexColumnDescription index12 = new IndexColumnDescription("INDEX1", "COLUMN2", 42, false);
		table.adoptOrphan(index12);
		assertEquals(2, table.getIndices().size());

		final IndexColumnDescription index2 = new IndexColumnDescription("INDEX2", "COLUMN1", 42, false);
		table.adoptOrphan(index2);
		assertEquals(3, table.getIndices().size());
	}

	@Test
	public void addImportedKeyColumns() {
		final TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

		final ImportedKeyColumnDescription fk1 = new ImportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk1);
		assertEquals(1, table.getImportedKeyColumns().size());

		final ImportedKeyColumnDescription fk1b = new ImportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk1b);
		assertEquals(1, table.getImportedKeyColumns().size());

		final ImportedKeyColumnDescription fk12 = new ImportedKeyColumnDescription("FK1", "FK-COLUMN2", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk12);
		assertEquals(2, table.getImportedKeyColumns().size());

		final ImportedKeyColumnDescription fk2 = new ImportedKeyColumnDescription("FK2", "FK-COLUMN1", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk2);
		assertEquals(3, table.getImportedKeyColumns().size());

		assertTrue(table.getExportedKeyColumns().isEmpty());
	}

	@Test
	public void addExportedKeyColumns() {
		final TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");

		final ExportedKeyColumnDescription fk1 = new ExportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk1);
		assertEquals(1, table.getExportedKeyColumns().size());

		final ExportedKeyColumnDescription fk1b = new ExportedKeyColumnDescription("FK1", "FK-COLUMN1", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk1b);
		assertEquals(1, table.getExportedKeyColumns().size());

		final ExportedKeyColumnDescription fk12 = new ExportedKeyColumnDescription("FK1", "FK-COLUMN2", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk12);
		assertEquals(2, table.getExportedKeyColumns().size());

		final ExportedKeyColumnDescription fk2 = new ExportedKeyColumnDescription("FK2", "FK-COLUMN1", "PK-C", "PK-S",
				"PK-T", "PK-COL");
		table.adoptOrphan(fk2);
		assertEquals(3, table.getExportedKeyColumns().size());

		assertTrue(table.getImportedKeyColumns().isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void addIllegalType() {
		final TableDescription table = new TableDescription("TABLE1", "TYPE1", "REMARK1");
		table.adoptOrphan(new SchemaDescription("SCHEMA"));
	}

	@Test
	public void nullSafe() {
		assertEquals("", new TableDescription(null, null, null).getName());
		assertEquals("", new TableDescription(null, null, null).getTableType());
		assertEquals("", new TableDescription(null, null, null).getRemarks());
	}

	@Test
	public void nullSafeColumns() {
		assertEquals("", new ColumnDescription(null, null, 0, 0, Nullability.MAYBE, null, null).getName());
		assertEquals("", new ColumnDescription(null, null, 0, 0, Nullability.MAYBE, null, null).getType());
		assertEquals("", new ColumnDescription(null, null, 0, 0, Nullability.MAYBE, null, null).getDefaultValue());
		assertEquals("", new ColumnDescription(null, null, 0, 0, Nullability.MAYBE, null, null).getRemarks());
	}

	@Test
	public void nullSafePrimaryKeys() {
		assertEquals("", new PrimaryKeyColumnDescription(null, null).getName());
		assertEquals("", new PrimaryKeyColumnDescription(null, null).getColumnName());
	}

	@Test
	public void nullSafeForeignKeys() {
		assertEquals("", new ImportedKeyColumnDescription(null, null, null, null, null, null).getName());
		assertEquals("", new ImportedKeyColumnDescription(null, null, null, null, null, null).getInsideColumnName());
		assertEquals("", new ImportedKeyColumnDescription(null, null, null, null, null, null).getOutsideCatalog());
		assertEquals("", new ImportedKeyColumnDescription(null, null, null, null, null, null).getOutsideSchema());
		assertEquals("", new ImportedKeyColumnDescription(null, null, null, null, null, null).getOutsideTableName());
		assertEquals("", new ImportedKeyColumnDescription(null, null, null, null, null, null).getOutsideColumnName());

		assertEquals("", new ExportedKeyColumnDescription(null, null, null, null, null, null).getName());
		assertEquals("", new ExportedKeyColumnDescription(null, null, null, null, null, null).getInsideColumnName());
		assertEquals("", new ExportedKeyColumnDescription(null, null, null, null, null, null).getOutsideCatalog());
		assertEquals("", new ExportedKeyColumnDescription(null, null, null, null, null, null).getOutsideSchema());
		assertEquals("", new ExportedKeyColumnDescription(null, null, null, null, null, null).getOutsideTableName());
		assertEquals("", new ExportedKeyColumnDescription(null, null, null, null, null, null).getOutsideColumnName());
	}

	@Test
	public void nullSafeIndices() {
		assertEquals("", new IndexColumnDescription(null, null, 0, true).getName());
		assertEquals("", new IndexColumnDescription(null, null, 0, true).getColumnName());
	}

}
