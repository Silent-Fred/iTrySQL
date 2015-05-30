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

import java.util.ArrayList;
import java.util.Collection;
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
public class MetaDataReaderTest {

    private Collection<TableDescription> tableDescriptions;

    public MetaDataReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        tableDescriptions = new LinkedList<>();
        tableDescriptions.add(new TableDescription("CATALOG-2", "SCHEMA-1", "TABLE-1",
                "TYPE-1", "REMARK-1"));
        tableDescriptions.add(new TableDescription("CATALOG-1", "SCHEMA-1", "TABLE-1",
                "TYPE-1", "REMARK-1"));
        tableDescriptions.add(new TableDescription("CATALOG-1", "SCHEMA-2", "TABLE-1",
                "TYPE-1", "REMARK-1"));
        tableDescriptions.add(new TableDescription("CATALOG-1", "SCHEMA-1", "TABLE-2",
                "TYPE-1", "REMARK-1"));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void buildSchemaDescriptions() {
        List<SchemaDescription> schemas = new ArrayList<>(new MetaDataReader().
                buildSchemaDescriptionsFromTableDescriptions(tableDescriptions));
        Collections.sort(schemas);
        Assert.assertEquals(3, schemas.size());
        Assert.assertEquals("CATALOG-1", schemas.get(0).getCatalog());
        Assert.assertEquals("CATALOG-1", schemas.get(1).getCatalog());
        Assert.assertEquals("CATALOG-2", schemas.get(2).getCatalog());

        Assert.assertEquals("SCHEMA-1", schemas.get(0).getSchema());
        Assert.assertEquals("SCHEMA-2", schemas.get(1).getSchema());
        Assert.assertEquals("SCHEMA-1", schemas.get(2).getSchema());
    }

    @Test
    public void buildCatalogDescriptions() {
        List<SchemaDescription> schemas = new ArrayList<>(new MetaDataReader().
                buildSchemaDescriptionsFromTableDescriptions(tableDescriptions));
        List<CatalogDescription> catalogs = new ArrayList<>(new MetaDataReader().
                buildCatalogDescriptionsFromSchemaDescriptions(schemas));
        Collections.sort(catalogs);
        Assert.assertEquals(2, catalogs.size());
        Assert.assertEquals("CATALOG-1", catalogs.get(0).getCatalog());
        Assert.assertEquals("CATALOG-2", catalogs.get(1).getCatalog());
    }
}
