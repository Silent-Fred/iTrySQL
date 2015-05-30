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
public class SchemaDescriptionTest {

    public SchemaDescriptionTest() {
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
    public void tableTypes() {
        SchemaDescription schema = new SchemaDescription("CATALOG", "SCHEMA");

        schema.addTables(new TableDescription("CATALOG", "SCHEMA", "TABLE-1",
                "TYPE-1", "REMARK-1"));
        schema.addTables(new TableDescription("CATALOG", "SCHEMA", "TABLE-2",
                "TYPE-1", "REMARK-1"));
        schema.addTables(new TableDescription("CATALOG", "SCHEMA", "TABLE-3",
                "TYPE-2", "REMARK-1"));

        Assert.assertEquals(2, schema.getTableTypes().size());
        Assert.assertEquals(2, schema.getTablesByType("TYPE-1").size());
        Assert.assertEquals(1, schema.getTablesByType("TYPE-2").size());
    }

    @Test
    public void sorting() {
        List<SchemaDescription> schemas = new LinkedList<>();

        schemas.add(new SchemaDescription("CATALOG2", "SCHEMA1"));
        schemas.add(new SchemaDescription("CATALOG1", "SCHEMA2"));
        schemas.add(new SchemaDescription("CATALOG1", "SCHEMA1"));
        schemas.add(new SchemaDescription("CATALOG2", "SCHEMA2"));

        Collections.sort(schemas);

        Assert.assertEquals("CATALOG1", schemas.get(0).getCatalog());
        Assert.assertEquals("CATALOG1", schemas.get(1).getCatalog());
        Assert.assertEquals("CATALOG2", schemas.get(2).getCatalog());
        Assert.assertEquals("CATALOG2", schemas.get(3).getCatalog());

        Assert.assertEquals("SCHEMA1", schemas.get(0).getSchema());
        Assert.assertEquals("SCHEMA2", schemas.get(1).getSchema());
        Assert.assertEquals("SCHEMA1", schemas.get(2).getSchema());
        Assert.assertEquals("SCHEMA2", schemas.get(3).getSchema());
    }
}
