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

import de.kuehweg.sqltool.database.integration.AbstractBaseIntegration;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import java.sql.SQLException;
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
public class MetaDataReaderIntegrationTest extends AbstractBaseIntegration {

    public MetaDataReaderIntegrationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        openTestConnection();
    }

    @After
    public void tearDown() throws SQLException {
        dropPublicSchema();
        closeTestConnection();
    }

    @Test
    public void readMetaData() throws SQLException {
        DatabaseDescription db = new MetaDataReader().readMetaData(getTestConnection());
        // Standardanmeldung: 1 Catalog mit 2 System-Schemas und einem PUBLIC Schema
        Assert.assertEquals(1, db.getCatalogs().size());
        Assert.assertEquals(3, db.getCatalogs().iterator().next().getSchemas().size());
    }

    @Test
    public void createTableAndReadMetaData() throws SQLException {
        getTestConnection().createStatement().execute(
                "create table test (test numeric(10))");
        DatabaseDescription db = new MetaDataReader().readMetaData(getTestConnection());
        // Standardanmeldung: 1 Catalog mit 2 System-Schemas und einem PUBLIC Schema
        Assert.assertEquals(1, db.getCatalogs().size());
        Assert.assertEquals(3, db.getCatalogs().iterator().next().getSchemas().size());

        for (SchemaDescription schema : db.getCatalogs().iterator().next().getSchemas()) {
            if (schema.getName().equals("PUBLIC")) {
                Assert.assertEquals(1, schema.getTables().size());
                Assert.
                        assertEquals("TEST", schema.getTables().iterator().next().
                                getName());
                Assert.assertEquals("TABLE", schema.getTables().iterator().next().
                        getTableType());
            }
        }
    }

    @Test
    public void createTableAndViewAndReadMetaData() throws SQLException {
        getTestConnection().createStatement().execute(
                "create table test_table (test numeric(10))");
        getTestConnection().createStatement().execute(
                "create view test_view as select * from test_table");
        DatabaseDescription db = new MetaDataReader().readMetaData(getTestConnection());
        // Standardanmeldung: 1 Catalog mit 2 System-Schemas
        // zusätzliches Benutzerschema
        Assert.assertEquals(1, db.getCatalogs().size());
        Assert.assertEquals(3, db.getCatalogs().iterator().next().getSchemas().size());

        for (SchemaDescription schema : db.getCatalogs().iterator().next().getSchemas()) {
            if (schema.getName().equals("PUBLIC")) {
                Assert.assertEquals(2, schema.getTables().size());
                Assert.assertEquals(1, schema.getTablesByType("TABLE").size());
                Assert.assertEquals(1, schema.getTablesByType("VIEW").size());
            }
        }
    }

}
