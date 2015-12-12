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
package de.kuehweg.sqltool.dialog.component.schematree.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.ColumnDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
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
public class SchemaTreeNodeBuilderTest {

	DatabaseDescription db;

	public SchemaTreeNodeBuilderTest() {
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
	public void basicDatabase() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		assertEquals("db", root.getTitle());
		assertEquals(SchemaTreeNodeType.DATABASE, root.getType());
		assertEquals("product version", root.getChildren().get(0).getTitle());
		assertEquals(SchemaTreeNodeType.PLAIN, root.getChildren().get(0).getType());
	}

	@Test
	public void basicCatalog() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog2 = new CatalogDescription("catalog2");
		final CatalogDescription catalog1 = new CatalogDescription("catalog1");

		db.adoptOrphan(catalog2);
		db.adoptOrphan(catalog1);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		assertEquals(3, root.getChildren().size());

		assertEquals(SchemaTreeNodeType.PLAIN, root.getChildren().get(0).getType());

		final SchemaTreeNode accessCatalog1 = root.getChildren().get(1);
		final SchemaTreeNode accessCatalog2 = root.getChildren().get(2);

		assertEquals(SchemaTreeNodeType.CATALOG, accessCatalog1.getType());
		assertEquals(catalog1.getName(), accessCatalog1.getTitle());
		assertEquals(SchemaTreeNodeType.CATALOG, accessCatalog2.getType());
		assertEquals(catalog2.getName(), accessCatalog2.getTitle());
	}

	@Test
	public void basicSchema() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog2 = new CatalogDescription("catalog2");
		final CatalogDescription catalog1 = new CatalogDescription("catalog1");

		final SchemaDescription schema = new SchemaDescription("schema");

		catalog1.adoptOrphan(schema);
		db.adoptOrphan(catalog2);
		db.adoptOrphan(catalog1);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessCatalog1 = root.getChildren().get(1);
		final SchemaTreeNode accessCatalog2 = root.getChildren().get(2);

		final SchemaTreeNode accessSchema = accessCatalog1.getChildren().get(0);

		assertEquals(SchemaTreeNodeType.SCHEMA, accessSchema.getType());
		assertEquals(schema.getName(), accessSchema.getTitle());
		assertTrue(accessCatalog2.getChildren().isEmpty());
	}

	@Test
	public void basicTableTypes() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final TableDescription view = new TableDescription("view", "VIEW", null);
		final TableDescription view2 = new TableDescription("view2", "VIEW", null);

		schema.adoptOrphan(view);
		schema.adoptOrphan(view2);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessSchema = root.getChildren().get(1).getChildren().get(0);

		assertEquals(2, accessSchema.getChildren().size());
		assertEquals(SchemaTreeNodeType.TABLE_TYPE, accessSchema.getChildren().get(0).getType());
		assertEquals("TABLE", accessSchema.getChildren().get(0).getTitle());
		assertEquals(SchemaTreeNodeType.TABLE_TYPE, accessSchema.getChildren().get(1).getType());
		assertEquals("VIEW", accessSchema.getChildren().get(1).getTitle());
		assertEquals(1, accessSchema.getChildren().get(0).getChildren().size());
		assertEquals(2, accessSchema.getChildren().get(1).getChildren().size());
	}

	@Test
	public void table() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final TableDescription view1 = new TableDescription("view1", "VIEW", null);
		final TableDescription view2 = new TableDescription("view2", "VIEW", null);

		schema.adoptOrphan(view1);
		schema.adoptOrphan(view2);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessSchema = root.getChildren().get(1).getChildren().get(0);
		final SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
		final SchemaTreeNode accessViewType = accessSchema.getChildren().get(1);

		final SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
		final SchemaTreeNode accessView1 = accessViewType.getChildren().get(0);
		final SchemaTreeNode accessView2 = accessViewType.getChildren().get(1);

		assertEquals(SchemaTreeNodeType.TABLE, accessTable.getType());
		assertEquals(SchemaTreeNodeType.TABLE, accessView1.getType());
		assertEquals(SchemaTreeNodeType.TABLE, accessView2.getType());
		assertEquals(1, accessTableType.getChildren().size());
		assertEquals(2, accessViewType.getChildren().size());

		assertEquals(SchemaTreeNodeType.PLAIN, accessTable.getChildren().get(0).getType());
		assertEquals("remarks", accessTable.getChildren().get(0).getTitle());
		assertTrue(accessView1.getChildren().isEmpty());
		assertTrue(accessView2.getChildren().isEmpty());
	}

	@Test
	public void columns() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2, Nullability.NO, null,
				"remarks1");
		final ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0, Nullability.YES, null, null);
		final ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0, Nullability.YES, "default3",
				null);
		final PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

		table.adoptOrphan(column1);
		table.adoptOrphan(column2);
		table.adoptOrphan(column3);
		table.adoptOrphan(pk);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessSchema = root.getChildren().get(1).getChildren().get(0);
		final SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
		final SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
		final SchemaTreeNode accessColumn1 = accessTable.getChildren().get(1);
		final SchemaTreeNode accessColumn2 = accessTable.getChildren().get(2);
		final SchemaTreeNode accessColumn3 = accessTable.getChildren().get(3);

		assertEquals(SchemaTreeNodeType.PLAIN, accessTable.getChildren().get(0).getType());
		assertEquals("remarks", accessTable.getChildren().get(0).getTitle());
		assertEquals(SchemaTreeNodeType.PRIMARY_KEY_COLUMN, accessColumn1.getType());
		assertEquals("column1", accessColumn1.getTitle());
		assertEquals(SchemaTreeNodeType.COLUMN, accessColumn2.getType());
		assertEquals("column2", accessColumn2.getTitle());

		assertEquals(2, accessColumn1.getChildren().size());
		assertEquals("remarks1", accessColumn1.getChildren().get(0).getTitle());
		assertEquals("type1(42,2)", accessColumn1.getChildren().get(1).getTitle());

		assertEquals(2, accessColumn2.getChildren().size());
		assertEquals("type2(42)", accessColumn2.getChildren().get(0).getTitle());
		assertEquals("NULLABLE", accessColumn2.getChildren().get(1).getTitle());

		assertEquals(3, accessColumn3.getChildren().size());
		assertEquals("type3", accessColumn3.getChildren().get(0).getTitle());
		assertEquals("NULLABLE", accessColumn2.getChildren().get(1).getTitle());
		assertEquals("DEFAULT: default3", accessColumn3.getChildren().get(2).getTitle());

	}

	@Test
	public void importedKeysOutsideSchema() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2, Nullability.NO, null,
				"remarks1");
		final ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0, Nullability.YES, null, null);
		final ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0, Nullability.YES, "default3",
				null);
		final PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

		final ImportedKeyColumnDescription importedKey1 = new ImportedKeyColumnDescription("fk", "column2",
				"to_catalog", "to_schema", "to_table", "to_column1");
		final ImportedKeyColumnDescription importedKey2 = new ImportedKeyColumnDescription("fk", "column3",
				"to_catalog", "to_schema", "to_table", "to_column2");

		table.adoptOrphan(column1);
		table.adoptOrphan(column2);
		table.adoptOrphan(column3);
		table.adoptOrphan(pk);
		table.adoptOrphan(importedKey1);
		table.adoptOrphan(importedKey2);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessCatalog = root.getChildren().get(1);
		final SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
		final SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
		final SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
		final SchemaTreeNode accessImportedKeys = accessTable.getChildren().get(4);
		final SchemaTreeNode accessImportedKey = accessImportedKeys.getChildren().get(0);

		assertEquals(SchemaTreeNodeType.IMPORTED_KEYS, accessImportedKeys.getType());
		assertEquals(1, accessImportedKeys.getChildren().size());

		assertEquals(SchemaTreeNodeType.IMPORTED_KEY, accessImportedKey.getType());
		assertEquals("fk", accessImportedKey.getTitle());
		assertEquals(2, accessImportedKey.getChildren().size());

		assertEquals("column2 -> to_catalog.to_schema.to_table.to_column1",
				accessImportedKey.getChildren().get(0).getTitle());
		assertEquals("column3 -> to_catalog.to_schema.to_table.to_column2",
				accessImportedKey.getChildren().get(1).getTitle());
	}

	@Test
	public void importedKeysInsideSchema() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2, Nullability.NO, null,
				"remarks1");
		final ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0, Nullability.YES, null, null);
		final ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0, Nullability.YES, "default3",
				null);
		final PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

		final ImportedKeyColumnDescription importedKey1 = new ImportedKeyColumnDescription("fk", "column2", "catalog",
				"schema", "to_table", "to_column1");
		final ImportedKeyColumnDescription importedKey2 = new ImportedKeyColumnDescription("fk", "column3", "catalog",
				"schema", "to_table", "to_column2");

		table.adoptOrphan(column1);
		table.adoptOrphan(column2);
		table.adoptOrphan(column3);
		table.adoptOrphan(pk);
		table.adoptOrphan(importedKey1);
		table.adoptOrphan(importedKey2);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessCatalog = root.getChildren().get(1);
		final SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
		final SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
		final SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
		final SchemaTreeNode accessImportedKeys = accessTable.getChildren().get(4);
		final SchemaTreeNode accessImportedKey = accessImportedKeys.getChildren().get(0);

		assertEquals(SchemaTreeNodeType.IMPORTED_KEYS, accessImportedKeys.getType());
		assertEquals(1, accessImportedKeys.getChildren().size());

		assertEquals(SchemaTreeNodeType.IMPORTED_KEY, accessImportedKey.getType());
		assertEquals("fk", accessImportedKey.getTitle());
		assertEquals(2, accessImportedKey.getChildren().size());

		assertEquals("column2 -> to_table.to_column1", accessImportedKey.getChildren().get(0).getTitle());
		assertEquals("column3 -> to_table.to_column2", accessImportedKey.getChildren().get(1).getTitle());
	}

	@Test
	public void exportedKeysOutsideSchema() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2, Nullability.NO, null,
				"remarks1");
		final ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0, Nullability.YES, null, null);
		final ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0, Nullability.YES, "default3",
				null);
		final PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

		final ExportedKeyColumnDescription exportedKey1 = new ExportedKeyColumnDescription("fk", "column2",
				"from_catalog", "from_schema", "from_table", "from_column1");
		final ExportedKeyColumnDescription exportedKey2 = new ExportedKeyColumnDescription("fk", "column3",
				"from_catalog", "from_schema", "from_table", "from_column2");

		table.adoptOrphan(column1);
		table.adoptOrphan(column2);
		table.adoptOrphan(column3);
		table.adoptOrphan(pk);
		table.adoptOrphan(exportedKey1);
		table.adoptOrphan(exportedKey2);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessCatalog = root.getChildren().get(1);
		final SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
		final SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
		final SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
		final SchemaTreeNode accessExportedKeys = accessTable.getChildren().get(4);
		final SchemaTreeNode accessExportedKey = accessExportedKeys.getChildren().get(0);

		assertEquals(SchemaTreeNodeType.EXPORTED_KEYS, accessExportedKeys.getType());
		assertEquals(1, accessExportedKeys.getChildren().size());

		assertEquals(SchemaTreeNodeType.EXPORTED_KEY, accessExportedKey.getType());
		assertEquals("fk", accessExportedKey.getTitle());
		assertEquals(2, accessExportedKey.getChildren().size());

		assertEquals("column2 <- from_catalog.from_schema.from_table.from_column1",
				accessExportedKey.getChildren().get(0).getTitle());
		assertEquals("column3 <- from_catalog.from_schema.from_table.from_column2",
				accessExportedKey.getChildren().get(1).getTitle());
	}

	@Test
	public void exportedKeysInsideSchema() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2, Nullability.NO, null,
				"remarks1");
		final ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0, Nullability.YES, null, null);
		final ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0, Nullability.YES, "default3",
				null);
		final PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

		final ExportedKeyColumnDescription exportedKey1 = new ExportedKeyColumnDescription("fk", "column2", "catalog",
				"schema", "from_table", "from_column1");
		final ExportedKeyColumnDescription exportedKey2 = new ExportedKeyColumnDescription("fk", "column3", "catalog",
				"schema", "from_table", "from_column2");

		table.adoptOrphan(column1);
		table.adoptOrphan(column2);
		table.adoptOrphan(column3);
		table.adoptOrphan(pk);
		table.adoptOrphan(exportedKey1);
		table.adoptOrphan(exportedKey2);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessCatalog = root.getChildren().get(1);
		final SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
		final SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
		final SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
		final SchemaTreeNode accessExportedKeys = accessTable.getChildren().get(4);
		final SchemaTreeNode accessExportedKey = accessExportedKeys.getChildren().get(0);

		assertEquals(SchemaTreeNodeType.EXPORTED_KEYS, accessExportedKeys.getType());
		assertEquals(1, accessExportedKeys.getChildren().size());

		assertEquals(SchemaTreeNodeType.EXPORTED_KEY, accessExportedKey.getType());
		assertEquals("fk", accessExportedKey.getTitle());
		assertEquals(2, accessExportedKey.getChildren().size());

		assertEquals("column2 <- from_table.from_column1", accessExportedKey.getChildren().get(0).getTitle());
		assertEquals("column3 <- from_table.from_column2", accessExportedKey.getChildren().get(1).getTitle());
	}

	@Test
	public void indices() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("catalog");
		final SchemaDescription schema = new SchemaDescription("schema");
		final TableDescription table = new TableDescription("table", "TABLE", "remarks");
		final ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2, Nullability.NO, null,
				"remarks1");
		final ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0, Nullability.YES, null, null);
		final ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0, Nullability.YES, "default3",
				null);
		final PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

		final IndexColumnDescription indexNonUnique = new IndexColumnDescription("idx1", "column1", 1, true);
		final IndexColumnDescription indexUnique1 = new IndexColumnDescription("idx2", "column2", 1, false);
		final IndexColumnDescription indexUnique2 = new IndexColumnDescription("idx2", "column3", 2, false);

		table.adoptOrphan(column1);
		table.adoptOrphan(column2);
		table.adoptOrphan(column3);
		table.adoptOrphan(pk);
		table.adoptOrphan(indexNonUnique);
		table.adoptOrphan(indexUnique1);
		table.adoptOrphan(indexUnique2);
		schema.adoptOrphan(table);
		catalog.adoptOrphan(schema);
		db.adoptOrphan(catalog);

		final SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

		final SchemaTreeNode accessCatalog = root.getChildren().get(1);
		final SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
		final SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
		final SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
		final SchemaTreeNode accessIndices = accessTable.getChildren().get(4);
		final SchemaTreeNode accessIndexNonUnique = accessIndices.getChildren().get(0);
		final SchemaTreeNode accessIndexUnique = accessIndices.getChildren().get(1);

		assertEquals(SchemaTreeNodeType.INDICES, accessIndices.getType());
		assertEquals(SchemaTreeNodeType.INDEX, accessIndexNonUnique.getType());
		assertEquals(SchemaTreeNodeType.INDEX, accessIndexUnique.getType());

		assertEquals(2, accessIndices.getChildren().size());

		assertEquals("idx1 (NON UNIQUE)", accessIndexNonUnique.getTitle());
		assertEquals("idx2", accessIndexUnique.getTitle());

		assertEquals(1, accessIndexNonUnique.getChildren().size());
		assertEquals(2, accessIndexUnique.getChildren().size());

		assertEquals(SchemaTreeNodeType.INDEX_COLUMN, accessIndexNonUnique.getChildren().get(0).getType());
		assertEquals(SchemaTreeNodeType.INDEX_COLUMN, accessIndexUnique.getChildren().get(0).getType());
		assertEquals(SchemaTreeNodeType.INDEX_COLUMN, accessIndexUnique.getChildren().get(1).getType());
	}

}
