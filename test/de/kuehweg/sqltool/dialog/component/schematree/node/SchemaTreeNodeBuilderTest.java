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
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
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
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        Assert.assertEquals("db", root.getTitle());
        Assert.assertEquals(SchemaTreeNodeType.DATABASE, root.getType());
        Assert.assertEquals("product version", root.getChildren().get(0).getTitle());
        Assert.assertEquals(SchemaTreeNodeType.PLAIN, root.getChildren().get(0).getType());
    }

    @Test
    public void basicCatalog() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog2 = new CatalogDescription("catalog2");
        CatalogDescription catalog1 = new CatalogDescription("catalog1");

        db.adoptOrphan(catalog2);
        db.adoptOrphan(catalog1);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        Assert.assertEquals(3, root.getChildren().size());

        Assert.assertEquals(SchemaTreeNodeType.PLAIN, root.getChildren().get(0).getType());

        SchemaTreeNode accessCatalog1 = root.getChildren().get(1);
        SchemaTreeNode accessCatalog2 = root.getChildren().get(2);

        Assert.assertEquals(SchemaTreeNodeType.CATALOG, accessCatalog1.getType());
        Assert.assertEquals(catalog1.getName(), accessCatalog1.getTitle());
        Assert.assertEquals(SchemaTreeNodeType.CATALOG, accessCatalog2.getType());
        Assert.assertEquals(catalog2.getName(), accessCatalog2.getTitle());
    }

    @Test
    public void basicSchema() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog2 = new CatalogDescription("catalog2");
        CatalogDescription catalog1 = new CatalogDescription("catalog1");

        SchemaDescription schema = new SchemaDescription("schema");

        catalog1.adoptOrphan(schema);
        db.adoptOrphan(catalog2);
        db.adoptOrphan(catalog1);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessCatalog1 = root.getChildren().get(1);
        SchemaTreeNode accessCatalog2 = root.getChildren().get(2);

        SchemaTreeNode accessSchema = accessCatalog1.getChildren().get(0);

        Assert.assertEquals(SchemaTreeNodeType.SCHEMA, accessSchema.getType());
        Assert.assertEquals(schema.getName(), accessSchema.getTitle());
        Assert.assertTrue(accessCatalog2.getChildren().isEmpty());
    }

    @Test
    public void basicTableTypes() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        TableDescription view = new TableDescription("view", "VIEW", null);
        TableDescription view2 = new TableDescription("view2", "VIEW", null);

        schema.adoptOrphan(view);
        schema.adoptOrphan(view2);
        schema.adoptOrphan(table);
        catalog.adoptOrphan(schema);
        db.adoptOrphan(catalog);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessSchema = root.getChildren().get(1).getChildren().get(0);

        Assert.assertEquals(2, accessSchema.getChildren().size());
        Assert.assertEquals(SchemaTreeNodeType.TABLE_TYPE, accessSchema.getChildren().get(
                0).getType());
        Assert.assertEquals("TABLE", accessSchema.getChildren().get(0).getTitle());
        Assert.assertEquals(SchemaTreeNodeType.TABLE_TYPE, accessSchema.getChildren().get(
                1).getType());
        Assert.assertEquals("VIEW", accessSchema.getChildren().get(1).getTitle());
        Assert.assertEquals(1, accessSchema.getChildren().get(0).getChildren().size());
        Assert.assertEquals(2, accessSchema.getChildren().get(1).getChildren().size());
    }

    @Test
    public void table() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        TableDescription view1 = new TableDescription("view1", "VIEW", null);
        TableDescription view2 = new TableDescription("view2", "VIEW", null);

        schema.adoptOrphan(view1);
        schema.adoptOrphan(view2);
        schema.adoptOrphan(table);
        catalog.adoptOrphan(schema);
        db.adoptOrphan(catalog);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessSchema = root.getChildren().get(1).getChildren().get(0);
        SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
        SchemaTreeNode accessViewType = accessSchema.getChildren().get(1);

        SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
        SchemaTreeNode accessView1 = accessViewType.getChildren().get(0);
        SchemaTreeNode accessView2 = accessViewType.getChildren().get(1);

        Assert.assertEquals(SchemaTreeNodeType.TABLE, accessTable.getType());
        Assert.assertEquals(SchemaTreeNodeType.TABLE, accessView1.getType());
        Assert.assertEquals(SchemaTreeNodeType.TABLE, accessView2.getType());
        Assert.assertEquals(1, accessTableType.getChildren().size());
        Assert.assertEquals(2, accessViewType.getChildren().size());

        Assert.assertEquals(SchemaTreeNodeType.PLAIN, accessTable.getChildren().get(0).
                getType());
        Assert.assertEquals("remarks", accessTable.getChildren().get(0).getTitle());
        Assert.assertTrue(accessView1.getChildren().isEmpty());
        Assert.assertTrue(accessView2.getChildren().isEmpty());
    }

    @Test
    public void columns() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2,
                Nullability.NO, null, "remarks1");
        ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0,
                Nullability.YES, null, null);
        ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0,
                Nullability.YES, "default3", null);
        PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

        table.adoptOrphan(column1);
        table.adoptOrphan(column2);
        table.adoptOrphan(column3);
        table.adoptOrphan(pk);
        schema.adoptOrphan(table);
        catalog.adoptOrphan(schema);
        db.adoptOrphan(catalog);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessSchema = root.getChildren().get(1).getChildren().get(0);
        SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
        SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
        SchemaTreeNode accessColumn1 = accessTable.getChildren().get(1);
        SchemaTreeNode accessColumn2 = accessTable.getChildren().get(2);
        SchemaTreeNode accessColumn3 = accessTable.getChildren().get(3);

        Assert.assertEquals(SchemaTreeNodeType.PLAIN, accessTable.getChildren().get(0).
                getType());
        Assert.assertEquals("remarks", accessTable.getChildren().get(0).getTitle());
        Assert.
                assertEquals(SchemaTreeNodeType.PRIMARY_KEY_COLUMN, accessColumn1.
                        getType());
        Assert.assertEquals("column1", accessColumn1.getTitle());
        Assert.assertEquals(SchemaTreeNodeType.COLUMN, accessColumn2.getType());
        Assert.assertEquals("column2", accessColumn2.getTitle());

        Assert.assertEquals(2, accessColumn1.getChildren().size());
        Assert.assertEquals("remarks1", accessColumn1.getChildren().get(0).getTitle());
        Assert.assertEquals("type1(42,2)", accessColumn1.getChildren().get(1).getTitle());

        Assert.assertEquals(2, accessColumn2.getChildren().size());
        Assert.assertEquals("type2(42)", accessColumn2.getChildren().get(0).getTitle());
        Assert.assertEquals("NULLABLE", accessColumn2.getChildren().get(1).getTitle());

        Assert.assertEquals(3, accessColumn3.getChildren().size());
        Assert.assertEquals("type3", accessColumn3.getChildren().get(0).getTitle());
        Assert.assertEquals("NULLABLE", accessColumn2.getChildren().get(1).getTitle());
        Assert.assertEquals("DEFAULT: default3", accessColumn3.getChildren().get(2).
                getTitle());

    }

    @Test
    public void importedKeysOutsideSchema() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2,
                Nullability.NO, null, "remarks1");
        ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0,
                Nullability.YES, null, null);
        ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0,
                Nullability.YES, "default3", null);
        PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

        ImportedKeyColumnDescription importedKey1 = new ImportedKeyColumnDescription("fk",
                "column2", "to_catalog", "to_schema", "to_table", "to_column1");
        ImportedKeyColumnDescription importedKey2 = new ImportedKeyColumnDescription("fk",
                "column3", "to_catalog", "to_schema", "to_table", "to_column2");

        table.adoptOrphan(column1);
        table.adoptOrphan(column2);
        table.adoptOrphan(column3);
        table.adoptOrphan(pk);
        table.adoptOrphan(importedKey1);
        table.adoptOrphan(importedKey2);
        schema.adoptOrphan(table);
        catalog.adoptOrphan(schema);
        db.adoptOrphan(catalog);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessCatalog = root.getChildren().get(1);
        SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
        SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
        SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
        SchemaTreeNode accessImportedKeys = accessTable.getChildren().get(4);
        SchemaTreeNode accessImportedKey = accessImportedKeys.getChildren().get(0);

        Assert.
                assertEquals(SchemaTreeNodeType.IMPORTED_KEYS, accessImportedKeys.
                        getType());
        Assert.assertEquals(1, accessImportedKeys.getChildren().size());

        Assert.assertEquals(SchemaTreeNodeType.IMPORTED_KEY, accessImportedKey.getType());
        Assert.assertEquals("fk", accessImportedKey.getTitle());
        Assert.assertEquals(2, accessImportedKey.getChildren().size());

        Assert.assertEquals("column2 -> to_catalog.to_schema.to_table.to_column1",
                accessImportedKey.getChildren().get(0).getTitle());
        Assert.assertEquals("column3 -> to_catalog.to_schema.to_table.to_column2",
                accessImportedKey.getChildren().get(1).getTitle());
    }

    @Test
    public void importedKeysInsideSchema() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2,
                Nullability.NO, null, "remarks1");
        ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0,
                Nullability.YES, null, null);
        ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0,
                Nullability.YES, "default3", null);
        PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

        ImportedKeyColumnDescription importedKey1 = new ImportedKeyColumnDescription("fk",
                "column2", "catalog", "schema", "to_table", "to_column1");
        ImportedKeyColumnDescription importedKey2 = new ImportedKeyColumnDescription("fk",
                "column3", "catalog", "schema", "to_table", "to_column2");

        table.adoptOrphan(column1);
        table.adoptOrphan(column2);
        table.adoptOrphan(column3);
        table.adoptOrphan(pk);
        table.adoptOrphan(importedKey1);
        table.adoptOrphan(importedKey2);
        schema.adoptOrphan(table);
        catalog.adoptOrphan(schema);
        db.adoptOrphan(catalog);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessCatalog = root.getChildren().get(1);
        SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
        SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
        SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
        SchemaTreeNode accessImportedKeys = accessTable.getChildren().get(4);
        SchemaTreeNode accessImportedKey = accessImportedKeys.getChildren().get(0);

        Assert.
                assertEquals(SchemaTreeNodeType.IMPORTED_KEYS, accessImportedKeys.
                        getType());
        Assert.assertEquals(1, accessImportedKeys.getChildren().size());

        Assert.assertEquals(SchemaTreeNodeType.IMPORTED_KEY, accessImportedKey.getType());
        Assert.assertEquals("fk", accessImportedKey.getTitle());
        Assert.assertEquals(2, accessImportedKey.getChildren().size());

        Assert.assertEquals("column2 -> to_table.to_column1",
                accessImportedKey.getChildren().get(0).getTitle());
        Assert.assertEquals("column3 -> to_table.to_column2",
                accessImportedKey.getChildren().get(1).getTitle());
    }

    @Test
    public void exportedKeysOutsideSchema() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2,
                Nullability.NO, null, "remarks1");
        ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0,
                Nullability.YES, null, null);
        ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0,
                Nullability.YES, "default3", null);
        PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

        ExportedKeyColumnDescription exportedKey1 = new ExportedKeyColumnDescription("fk",
                "column2", "from_catalog", "from_schema", "from_table", "from_column1");
        ExportedKeyColumnDescription exportedKey2 = new ExportedKeyColumnDescription("fk",
                "column3", "from_catalog", "from_schema", "from_table", "from_column2");

        table.adoptOrphan(column1);
        table.adoptOrphan(column2);
        table.adoptOrphan(column3);
        table.adoptOrphan(pk);
        table.adoptOrphan(exportedKey1);
        table.adoptOrphan(exportedKey2);
        schema.adoptOrphan(table);
        catalog.adoptOrphan(schema);
        db.adoptOrphan(catalog);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessCatalog = root.getChildren().get(1);
        SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
        SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
        SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
        SchemaTreeNode accessExportedKeys = accessTable.getChildren().get(4);
        SchemaTreeNode accessExportedKey = accessExportedKeys.getChildren().get(0);

        Assert.
                assertEquals(SchemaTreeNodeType.EXPORTED_KEYS, accessExportedKeys.
                        getType());
        Assert.assertEquals(1, accessExportedKeys.getChildren().size());

        Assert.assertEquals(SchemaTreeNodeType.EXPORTED_KEY, accessExportedKey.getType());
        Assert.assertEquals("fk", accessExportedKey.getTitle());
        Assert.assertEquals(2, accessExportedKey.getChildren().size());

        Assert.assertEquals("column2 <- from_catalog.from_schema.from_table.from_column1",
                accessExportedKey.getChildren().get(0).getTitle());
        Assert.assertEquals("column3 <- from_catalog.from_schema.from_table.from_column2",
                accessExportedKey.getChildren().get(1).getTitle());
    }

    @Test
    public void exportedKeysInsideSchema() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2,
                Nullability.NO, null, "remarks1");
        ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0,
                Nullability.YES, null, null);
        ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0,
                Nullability.YES, "default3", null);
        PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

        ExportedKeyColumnDescription exportedKey1 = new ExportedKeyColumnDescription("fk",
                "column2", "catalog", "schema", "from_table", "from_column1");
        ExportedKeyColumnDescription exportedKey2 = new ExportedKeyColumnDescription("fk",
                "column3", "catalog", "schema", "from_table", "from_column2");

        table.adoptOrphan(column1);
        table.adoptOrphan(column2);
        table.adoptOrphan(column3);
        table.adoptOrphan(pk);
        table.adoptOrphan(exportedKey1);
        table.adoptOrphan(exportedKey2);
        schema.adoptOrphan(table);
        catalog.adoptOrphan(schema);
        db.adoptOrphan(catalog);

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessCatalog = root.getChildren().get(1);
        SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
        SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
        SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
        SchemaTreeNode accessExportedKeys = accessTable.getChildren().get(4);
        SchemaTreeNode accessExportedKey = accessExportedKeys.getChildren().get(0);

        Assert.
                assertEquals(SchemaTreeNodeType.EXPORTED_KEYS, accessExportedKeys.
                        getType());
        Assert.assertEquals(1, accessExportedKeys.getChildren().size());

        Assert.assertEquals(SchemaTreeNodeType.EXPORTED_KEY, accessExportedKey.getType());
        Assert.assertEquals("fk", accessExportedKey.getTitle());
        Assert.assertEquals(2, accessExportedKey.getChildren().size());

        Assert.assertEquals("column2 <- from_table.from_column1",
                accessExportedKey.getChildren().get(0).getTitle());
        Assert.assertEquals("column3 <- from_table.from_column2",
                accessExportedKey.getChildren().get(1).getTitle());
    }

    @Test
    public void indices() {
        DatabaseDescription db = new DatabaseDescription("db", "product", "version");

        CatalogDescription catalog = new CatalogDescription("catalog");
        SchemaDescription schema = new SchemaDescription("schema");
        TableDescription table = new TableDescription("table", "TABLE", "remarks");
        ColumnDescription column1 = new ColumnDescription("column1", "type1", 42, 2,
                Nullability.NO, null, "remarks1");
        ColumnDescription column2 = new ColumnDescription("column2", "type2", 42, 0,
                Nullability.YES, null, null);
        ColumnDescription column3 = new ColumnDescription("column3", "type3", 0, 0,
                Nullability.YES, "default3", null);
        PrimaryKeyColumnDescription pk = new PrimaryKeyColumnDescription("pk", "column1");

        IndexColumnDescription indexNonUnique = new IndexColumnDescription("idx1",
                "column1", 1, true);
        IndexColumnDescription indexUnique1
                = new IndexColumnDescription("idx2", "column2", 1, false);
        IndexColumnDescription indexUnique2
                = new IndexColumnDescription("idx2", "column3", 2, false);

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

        SchemaTreeNode root = new SchemaTreeNodeBuilder(db).getRootOfPopulatedTree();

        SchemaTreeNode accessCatalog = root.getChildren().get(1);
        SchemaTreeNode accessSchema = accessCatalog.getChildren().get(0);
        SchemaTreeNode accessTableType = accessSchema.getChildren().get(0);
        SchemaTreeNode accessTable = accessTableType.getChildren().get(0);
        SchemaTreeNode accessIndices = accessTable.getChildren().get(4);
        SchemaTreeNode accessIndexNonUnique = accessIndices.getChildren().get(0);
        SchemaTreeNode accessIndexUnique = accessIndices.getChildren().get(1);

        Assert.assertEquals(SchemaTreeNodeType.INDICES, accessIndices.getType());
        Assert.assertEquals(SchemaTreeNodeType.INDEX, accessIndexNonUnique.getType());
        Assert.assertEquals(SchemaTreeNodeType.INDEX, accessIndexUnique.getType());

        Assert.assertEquals(2, accessIndices.getChildren().size());

        Assert.assertEquals("idx1 (NON UNIQUE)", accessIndexNonUnique.getTitle());
        Assert.assertEquals("idx2", accessIndexUnique.getTitle());

        Assert.assertEquals(1, accessIndexNonUnique.getChildren().size());
        Assert.assertEquals(2, accessIndexUnique.getChildren().size());

        Assert.assertEquals(SchemaTreeNodeType.INDEX_COLUMN, accessIndexNonUnique.
                getChildren().get(0).getType());
        Assert.assertEquals(SchemaTreeNodeType.INDEX_COLUMN, accessIndexUnique.
                getChildren().
                get(0).getType());
        Assert.assertEquals(SchemaTreeNodeType.INDEX_COLUMN, accessIndexUnique.
                getChildren().
                get(1).getType());
    }

}
